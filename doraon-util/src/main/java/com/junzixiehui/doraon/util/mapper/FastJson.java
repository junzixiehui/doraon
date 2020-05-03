package com.junzixiehui.doraon.util.mapper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.base.Preconditions;

import java.util.List;

/**
 * <p>Description: </p>
 *
 * @author: by qulibin
 * @date: 2018/5/12  17:46
 * @version: 1.0
 */
public final class FastJson {
    private static SerializerFeature[] WRITE_NULL_VALUE_SERIALIZER_FEATURES;

    private FastJson() {
    }

    public static <T> T jsonStr2Object(String jsonStr, Class<T> valueType) {
        Preconditions.checkNotNull(jsonStr);

        try {
            return JSON.parseObject(jsonStr, valueType);
        } catch (Exception var3) {
            throw new JsonException("FastJson.jsonStr2Object error.", var3);
        }
    }

    public static String object2JsonStr(Object obj) {
        Preconditions.checkNotNull(obj);

        try {
            return JSON.toJSONString(obj);
        } catch (Exception var2) {
            throw new JsonException("FastJson.object2JsonStr error.", var2);
        }
    }

    public static String object2JsonStrForIgnore(Object obj, List<String> ignoreFieldNameList) {
        Preconditions.checkNotNull(obj);

        try {
            return JSON.toJSONString(obj, new FastJson.IgnoreFieldNameFilter(ignoreFieldNameList), new SerializerFeature[0]);
        } catch (Exception var3) {
            throw new JsonException("FastJson.object2JsonStr error, ignoreFieldNameList:" + ignoreFieldNameList, var3);
        }
    }

    public static String object2JsonStrForInclude(Object obj, List<String> includeFieldNameList) {
        Preconditions.checkNotNull(obj);

        try {
            return JSON.toJSONString(obj, new FastJson.IncludeFieldNameFilter(includeFieldNameList), new SerializerFeature[0]);
        } catch (Exception var3) {
            throw new JsonException("FastJson.object2JsonStrForInclude error, includeFieldNameList:" + includeFieldNameList, var3);
        }
    }

    public static String object2JsonStrUseNullValue(Object obj) {
        Preconditions.checkNotNull(obj);

        try {
            return JSON.toJSONString(obj, WRITE_NULL_VALUE_SERIALIZER_FEATURES);
        } catch (Exception var2) {
            throw new JsonException("FastJson.object2JsonStrUseNullValue error.", var2);
        }
    }

    public static String object2JsonStrForIgnoreUseNullValue(Object obj, List<String> ignoreFieldNameList) {
        Preconditions.checkNotNull(obj);

        try {
            return JSON.toJSONString(obj, new FastJson.IgnoreFieldNameFilter(ignoreFieldNameList), WRITE_NULL_VALUE_SERIALIZER_FEATURES);
        } catch (Exception var3) {
            throw new JsonException("FastJson.object2JsonStrForIgnoreUseNullValue error, ignoreFieldNameList:" + ignoreFieldNameList, var3);
        }
    }

    public static String object2JsonStrForIncludeUseNullValue(Object obj, List<String> includeFieldNameList) {
        Preconditions.checkNotNull(obj);

        try {
            return JSON.toJSONString(obj, new FastJson.IncludeFieldNameFilter(includeFieldNameList), WRITE_NULL_VALUE_SERIALIZER_FEATURES);
        } catch (Exception var3) {
            throw new JsonException("FastJson.object2JsonStrForIncludeUseNullValue error, includeFieldNameList:" + includeFieldNameList, var3);
        }
    }

    static {
        WRITE_NULL_VALUE_SERIALIZER_FEATURES = new SerializerFeature[]{SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.WriteNullNumberAsZero, SerializerFeature.WriteNullListAsEmpty, SerializerFeature.WriteNullBooleanAsFalse};
    }

    private static class IncludeFieldNameFilter implements PropertyFilter {
        private List<String> includeFieldNameList;

        public IncludeFieldNameFilter(List<String> includeFieldNameList) {
            this.includeFieldNameList = includeFieldNameList;
        }

        public boolean apply(Object source, String name, Object value) {
            return this.includeFieldNameList.contains(name);
        }
    }

    private static class IgnoreFieldNameFilter implements PropertyFilter {
        private List<String> ignoreFieldNameList;

        public IgnoreFieldNameFilter(List<String> ignoreFieldNameList) {
            this.ignoreFieldNameList = ignoreFieldNameList;
        }

        public boolean apply(Object source, String name, Object value) {
            return !this.ignoreFieldNameList.contains(name);
        }
    }
}
