package com.junzixiehui.doraon.design.pattern.chain;

/**
 * <p>Description: </p>
 * @author: by qulibin
 * @date: 2020/12/1  16:28
 * @version: 1.0
 */
public interface Command {
	boolean CONTINUE_PROCESSING = false;
	boolean PROCESSING_COMPLETE = true;

	boolean execute(Context var1) throws Exception;
}
