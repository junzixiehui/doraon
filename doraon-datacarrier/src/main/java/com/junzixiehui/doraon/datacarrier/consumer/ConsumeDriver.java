/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.junzixiehui.doraon.datacarrier.consumer;

import com.junzixiehui.doraon.datacarrier.buffer.*;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Pool of consumers <p> Created by wusheng on 2016/10/25.
 */
public class ConsumeDriver<T> implements IDriver {
    private boolean running;
    private ConsumerThread[] consumerThreads;
    private Channels<T> channels;
    private ReentrantLock lock;

    public ConsumeDriver(String name, Channels<T> channels, Class<? extends IConsumer<T>> consumerClass, int num,
        long consumeCycle) {
        this(channels, num);
        for (int i = 0; i < num; i++) {
            consumerThreads[i] = new ConsumerThread("DataCarrier." + name + ".Consumser." + i + ".Thread", getNewConsumerInstance(consumerClass), consumeCycle);
            consumerThreads[i].setDaemon(true);
        }
    }

    public ConsumeDriver(String name, Channels<T> channels, IConsumer<T> prototype, int num, long consumeCycle) {
        this(channels, num);
        prototype.init();
        for (int i = 0; i < num; i++) {
            consumerThreads[i] = new ConsumerThread("DataCarrier." + name + ".Consumser." + i + ".Thread", prototype, consumeCycle);
            consumerThreads[i].setDaemon(true);
        }

    }

    private ConsumeDriver(Channels<T> channels, int num) {
        running = false;
        this.channels = channels;
        consumerThreads = new ConsumerThread[num];
        lock = new ReentrantLock();
    }

    private IConsumer<T> getNewConsumerInstance(Class<? extends IConsumer<T>> consumerClass) {
        try {
            IConsumer<T> inst = consumerClass.newInstance();
            inst.init();
            return inst;
        } catch (InstantiationException e) {
            throw new ConsumerCannotBeCreatedException(e);
        } catch (IllegalAccessException e) {
            throw new ConsumerCannotBeCreatedException(e);
        }
    }

    @Override
    public void begin(Channels channels) {
        if (running) {
            return;
        }
        try {
            lock.lock();
            this.allocateBuffer2Thread();
            for (ConsumerThread consumerThread : consumerThreads) {
                consumerThread.start();
            }
            running = true;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean isRunning(Channels channels) {
        return running;
    }

    private void allocateBuffer2Thread() {
        int channelSize = this.channels.getChannelSize();
        if (channelSize < consumerThreads.length) {
            /**
             * if consumerThreads.length > channelSize
             * each channel will be process by several consumers.
             */
            ArrayList<Integer>[] threadAllocation = new ArrayList[channelSize];
            for (int threadIndex = 0; threadIndex < consumerThreads.length; threadIndex++) {
                int index = threadIndex % channelSize;
                if (threadAllocation[index] == null) {
                    threadAllocation[index] = new ArrayList<Integer>();
                }
                threadAllocation[index].add(threadIndex);
            }

            for (int channelIndex = 0; channelIndex < channelSize; channelIndex++) {
                ArrayList<Integer> threadAllocationPerChannel = threadAllocation[channelIndex];
                Buffer<T> channel = this.channels.getBuffer(channelIndex);
                int bufferSize = channel.getBufferSize();
                int step = bufferSize / threadAllocationPerChannel.size();
                for (int i = 0; i < threadAllocationPerChannel.size(); i++) {
                    int threadIndex = threadAllocationPerChannel.get(i);
                    int start = i * step;
                    int end = i == threadAllocationPerChannel.size() - 1 ? bufferSize : (i + 1) * step;
                    consumerThreads[threadIndex].addDataSource(channel, start, end);
                }
            }
        } else {
            /**
             * if consumerThreads.length < channelSize
             * each consumer will process several channels.
             *
             * if consumerThreads.length == channelSize
             * each consumer will process one channel.
             */
            for (int channelIndex = 0; channelIndex < channelSize; channelIndex++) {
                int consumerIndex = channelIndex % consumerThreads.length;
                consumerThreads[consumerIndex].addDataSource(channels.getBuffer(channelIndex));
            }
        }

    }

    @Override
    public void close(Channels channels) {
        try {
            lock.lock();
            this.running = false;
            for (ConsumerThread consumerThread : consumerThreads) {
                consumerThread.shutdown();
            }
        } finally {
            lock.unlock();
        }
    }
}
