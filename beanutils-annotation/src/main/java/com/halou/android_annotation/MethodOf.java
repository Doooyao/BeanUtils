package com.halou.android_annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by ${yyy} on 18-7-26.
 * bean类中使用此注解指定某一属性的get set方法
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface MethodOf {

    /**
     * bean中的属性名字
     */
    String value();
}
