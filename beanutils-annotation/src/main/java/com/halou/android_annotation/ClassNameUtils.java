package com.halou.android_annotation;

/**
 * Created by ${yyy} on 18-7-24.
 */

public class ClassNameUtils {

    public static String PREFIX_CONVERT_CLASS_NAME = "Convert_";

    public static String CONVERT_UTILS_NAME = "ConvertUtils";

    public static String CONVERT_UTILS_METHOD_NAME = "convert";

    public static String getClassNameFromQualifiedName(String qualifiedName){
        String nameChanged = qualifiedName.replaceAll("\\.","_");
        return PREFIX_CONVERT_CLASS_NAME + nameChanged;
    }
    public static String getMethodNameFromBeanName(String beanName){
        return "convertTo"+beanName;
    }
}
