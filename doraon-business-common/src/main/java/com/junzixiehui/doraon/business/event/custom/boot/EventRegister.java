/*
 * Copyright 2017 Alibaba.com All right reserved. This software is the
 * confidential and proprietary information of Alibaba.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Alibaba.com.
 */
package com.junzixiehui.doraon.business.event.custom.boot;


import com.junzixiehui.doraon.business.common.constant.DoraonConstant;
import com.junzixiehui.doraon.business.event.Event;
import com.junzixiehui.doraon.business.event.custom.event.EventHandlerI;
import com.junzixiehui.doraon.business.event.custom.event.EventHub;
import com.junzixiehui.doraon.util.exception.ServiceException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;

/**
 * EventRegister
 *
 * @author shawnzhan.zxy
 * @date 2017/11/20
 */
@Component
public class EventRegister {

    @Resource
    private EventHub eventHub;

    private Class<? extends Event> getEventFromExecutor(Class<?> eventExecutorClz) {
        Method[] methods = eventExecutorClz.getDeclaredMethods();
        for (Method method : methods) {
            if (isExecuteMethod(method)){
                return checkAndGetEventParamType(method);
            }
        }
        throw new ServiceException("Event param in " + eventExecutorClz + " " + DoraonConstant.EXE_METHOD
                                 + "() is not detected");
    }

    public void doRegistration(EventHandlerI eventHandler){
        Class<? extends Event> eventClz = getEventFromExecutor(eventHandler.getClass());
        eventHub.register(eventClz, eventHandler);
    }

    private boolean isExecuteMethod(Method method){
        return DoraonConstant.EXE_METHOD.equals(method.getName()) && !method.isBridge();
    }

    private Class checkAndGetEventParamType(Method method){
        Class<?>[] exeParams = method.getParameterTypes();
        if (exeParams.length == 0){
            throw new ServiceException("Execute method in "+method.getDeclaringClass()+" should at least have one parameter");
        }
        if(!Event.class.isAssignableFrom(exeParams[0]) ){
            throw new ServiceException("Execute method in "+method.getDeclaringClass()+" should be the subClass of Event");
        }
        return exeParams[0];
    }
}
