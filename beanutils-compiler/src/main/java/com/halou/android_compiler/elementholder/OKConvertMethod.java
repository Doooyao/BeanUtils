package com.halou.android_compiler.elementholder;

import java.util.List;

/**
 * Created by ${yyy} on 18-7-26.
 */

public interface OKConvertMethod {

    /**
     * 获取父bean的类型字符串 包括包名
     */
    String getConvertFromType();

    /**
     * 不包括包名
     */
    String getConvertFromTypeSimpleName();

    /**
     * 获取目标bean的类型字符串 包括包名
     */
    String getConvertToType();

    /**
     * 不包括包名
     */
    String getConvertToTypeSimpleName();

    /**
     * 获取父bean中属性的get方法的list
     */
    List<String> getGetMethodsConvertFrom();

    /**
     * 获取目标bean中属性的set方法的list
     */
    List<String> getSetMethodsConvertTo();


}
