package com.junzixiehui.doraon.design.pattern.chain;

/**
 * <p>Description: </p>
 * @author: by qulibin
 * @date: 2020/12/1  16:29
 * @version: 1.0
 */
public interface Chain {

	void addCommand(Command var1);

	boolean execute(Context var1) throws Exception;
}
