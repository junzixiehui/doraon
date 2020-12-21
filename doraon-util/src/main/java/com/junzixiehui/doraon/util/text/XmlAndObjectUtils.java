package com.junzixiehui.doraon.util.text;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.lang.reflect.Field;
import java.util.Iterator;

/**
 * 微信XML转对象工具类
 */
public class XmlAndObjectUtils {

	/**
	 * java.lang.Object类
	 */
	public static final String JAVA_LANG_OBJECT = "java.lang.Object";
	/**
	 * lang类型
	 */
	public static final String LONG = "long";
    /**
     * 私有构造
     */
    private XmlAndObjectUtils() {
    }

    /**
     * 组装XML节点信息
     *
     * @param object 待组装实体
     * @param superclass          父类
     * @param rootElement         根节点
     * @return Element
     * @throws IllegalAccessException 异常
     */
    private static Element getSuperClassValue(Object object, Class<?> superclass, Element rootElement) throws IllegalAccessException {
        if (JAVA_LANG_OBJECT.equalsIgnoreCase(superclass.getName())) {
            // 父类是Object了 BaseMessageAndEventRequestAndResponse类型
            return rootElement;
        } else {
            Field[] declaredFields = superclass.getDeclaredFields();
            for (int i = 0; i < declaredFields.length; i++) {
                Field declaredField = declaredFields[i];
                declaredField.setAccessible(true);
                String name = declaredField.getName();
                String valueTypeName = declaredField.getType().getName();
                if (LONG.equalsIgnoreCase(valueTypeName)) {
                    Element nodeElement = rootElement.addElement(name);
                    long aLong = declaredField.getLong(object);
                    nodeElement.addCDATA(String.valueOf(aLong));
                } else {
                    Object obj = declaredField.get(object);
                    if (obj == null) {
                        continue;
                    }
                    Element nodeElement = rootElement.addElement(name);
                    nodeElement.addCDATA(String.valueOf(obj));
                }
            }
            return getSuperClassValue(object, superclass.getSuperclass(), rootElement);
        }
    }

    /**
     * Object对象转符合微信要求的xml字符串
     *
     * @param obj 返回消息
     * @return 符合微信要求的xml字符串
     */
    public static String objectToxml(Object obj) throws IllegalAccessException {
        if (obj == null) {
            return null;
        }
        /*创建一个document*/
        Document documentResponse = DocumentHelper.createDocument();
        // 根节点
        Element rootElementResponse = documentResponse.addElement("xml");
        getSuperClassValue(obj, obj.getClass(), rootElementResponse);
        return documentResponse.asXML();
    }

    /**
     * 对象设值
     *
     * @param clazz 类对象
     * @param name  标识要给哪个字段赋值
     * @param value 实际值
     * @param t     对象
     * @throws IllegalAccessException 一般是权限异常 权限修饰符
     */
    private static void setValue(final Class clazz, final String name, final String value, final Object t)
            throws IllegalAccessException {
        // 获取当前类对象的父类对象
        final Class superclass = clazz.getSuperclass();
        // 获取父类对象名
        final String superclassName = superclass.getName();
        // 判断是不是Object类
        if (!JAVA_LANG_OBJECT.equals(superclassName)) {
            // 递归
            setValue(superclass, name, value, t);
        }
        // 获取字段
        final Field[] declaredFields = clazz.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            // 打破封装
            declaredField.setAccessible(true);
            // 字段名大写
            final String s = declaredField.getName().toUpperCase();
            // 比较是否是该字段
            if (s.equals(name.toUpperCase())) {
                // 获取字段属性
                final Class<?> type = declaredField.getType();
                if (LONG.equals(type.getName())) {
                    // long类型数据
                    declaredField.setLong(t, Long.parseLong(value));
                } else {
                    // String类型数据
                    declaredField.set(t, value);
                }
                break;
            }
        }
    }

    /**
     * xml 转 对象的方法
     * <b>要求：
     * 实体对象的属性名与xml节点标签名格式一致
     * 如,xml中有一个<FromToUser>的标签，则对象中要有一个属性名为fromToUser的属性，大小写不限
     * </b>
     *
     * @param element 根元素
     * @param clazz   要转化的类对象
     * @param <T>     泛型
     * @return 对象
     */
    public static <T> T xmlToObject(final Element element, final Class<T> clazz)
            throws IllegalAccessException, InstantiationException {
        T t = clazz.newInstance();
        return xmlToObject(element, null, clazz, t);
    }

    /**
     * xml对象转Object方法
     *
     * @param element       xml对象根节点信息
     * @param parentElement 父节点信息
     * @param clazz         待转类对象
     * @param obj           泛型对象
     * @param <T>           对象
     * @return 对象
     * @throws IllegalAccessException 一般是权限异常 权限修饰符
     */
    private static <T> T xmlToObject(final Element element,
                                     final Element parentElement,
                                     final Class<T> clazz,
                                     final T obj) throws IllegalAccessException {
        if (element.isRootElement()) {
            // 当前节点是根节点,遍历当前节点
            Iterator iterator = element.elementIterator();
            if (iterator.hasNext()) {
                // 根节点下是否有元素
                while (iterator.hasNext()) {
                    Element next = (Element) iterator.next();
                    xmlToObject(next, element, clazz, obj);
                }
            }
        } else {
            // 不是根节点
            Iterator iterator = element.elementIterator();
            // 判断是否有子节点
            if (iterator.hasNext()) {
                //有子节点
                Element next = (Element) iterator.next();
                xmlToObject(next, element, clazz, obj);
            } else {
                //没有子节点
                String name = element.getName();
                String value = parentElement.elementText(element.getQName());
                setValue(clazz, name, value, obj);
            }
        }
        return obj;
    }
}
