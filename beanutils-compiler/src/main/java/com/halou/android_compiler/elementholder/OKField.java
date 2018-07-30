package com.halou.android_compiler.elementholder;

/**
 * Created by ${yyy} on 18-7-26.
 * 带有get set 方法的属性
 */

public interface OKField {

    /**
     * 获取属性类型(全名)
     * @return 属性类型全名
     */
    String getType();

    /**
     * 获取和其他bean中的属性相匹配的名字
     * 如果没有使用{@link com.halou.android_annotation.ConvertName} 另外设置 则返回属性变量名
     * 如果两个bean中的匹配名相等就认为是可以复制过去的属性
     */
    String getMatchName();

    /**
     * 获取属性名
     * @return 属性名
     */
    String getField();

    /**
     * 获取属性get方法的名字
     * @return 方法名
     */
    String getMethod();

    /**
     * 获取属性set方法的名字
     * @return 方法名
     */
    String setMethod();

}
