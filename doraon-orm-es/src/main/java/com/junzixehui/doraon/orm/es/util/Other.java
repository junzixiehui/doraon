package com.junzixehui.doraon.orm.es.util;

import com.junzixiehui.doraon.design.pattern.chain.Command;
import com.junzixiehui.doraon.design.pattern.chain.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * pointer 前移
 * <p/>
 * Created by  on 16/2/17.
 */
public class Other implements Command {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public boolean execute(Context context) throws Exception {
        int pointer = (Integer) context.get(SqlContextConst.POINTER);
        String[] words = (String[]) context.get(SqlContextConst.WORLDS);
        context.put(SqlContextConst.POINTER, ++pointer);
        if (pointer < words.length) {
            context.put(SqlContextConst.CURRENT_WOLD, words[pointer]);
        }
        return PROCESSING_COMPLETE;
    }
}
