package com.halou.mybutterknife;

import com.halou.android_annotation.ClassNameUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by ${yyy} on 18-7-25.
 */

public class ConvertUtils {

    public static <F,T> void convert(F fromBean,T toBean){
        if (fromBean==null)
            throw new IllegalStateException("fromBean cant be null");
        if (toBean==null)
            throw new IllegalStateException("toBean cant be null");
        Class fromClass = fromBean.getClass();
        Class toClass = toBean.getClass();
        String fromName = fromClass.getName();
        String toName = toClass.getSimpleName();
        String convertClassName = fromBean.getClass().getPackage().getName()+"."+ClassNameUtils.getClassNameFromQualifiedName(fromName);
        String convertMethodName = ClassNameUtils.getMethodNameFromBeanName(toName);
        try {
            Class convertClass = Class.forName(convertClassName);
            Method method = convertClass.getMethod(convertMethodName, fromBean.getClass(),toBean.getClass());
            method.invoke(null, fromBean,toBean);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException(e);
        }
    }
}
