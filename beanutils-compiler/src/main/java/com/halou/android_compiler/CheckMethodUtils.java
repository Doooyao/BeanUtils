package com.halou.android_compiler;

import com.halou.android_compiler.elementholder.OKFieldElement;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;

/**
 * Created by ${yyy} on 18-7-25.
 */

public class CheckMethodUtils {

    /**
     * 判断是否存在某属性的 get方法
     * @param methods bean的所有方法元素
     * @param fieldName 需要查找的属性元素
     * @return ExecutableElement 属性的get方法 如果返回null则没有
     */
    public static ExecutableElement checkGetMet(List<ExecutableElement> methods,
                                     VariableElement fieldName) {
        for (ExecutableElement method:methods){
            //返回值不同直接过滤
            if (!method.getReturnType().toString().equals(fieldName.asType().toString()))
                continue;
            //要求输入参数的直接过滤
            if (method.getParameters().size()!=0)
                continue;
            if (method.getSimpleName().toString().equalsIgnoreCase("get"+fieldName.getSimpleName().toString())){
                //方法名符合规则 直接使用
                return method;
            }else if (fieldName.getSimpleName().toString().startsWith("is")){//变量名以is开头
                if (method.getSimpleName().toString().equalsIgnoreCase(fieldName.getSimpleName().toString()))
                    return method;
            }
        }
        return null;
    }

    /**
     * 判断是否存在某属性的 set方法
     * @param methods bean的所有方法元素
     * @param fieldName 需要设置的属性元素
     * @return ExecutableElement 属性的set方法 如果没有就返回null
     */
    public static ExecutableElement checkSetMet(List<ExecutableElement> methods,
                                      VariableElement fieldName) {
        for (ExecutableElement method:methods){
            //过滤输入参数数量不等于1或者输入参数类型和属性不相符的
            if (method.getParameters().size()!=1||
                    !method.getParameters().get(0).asType().toString().equals(fieldName.asType().toString()))
                continue;
            //过滤返回类型不是void的
            if (method.getReturnType().getKind()!= TypeKind.VOID)
                continue;
            if (method.getSimpleName().toString().equalsIgnoreCase("set"+fieldName.getSimpleName().toString())){
                //方法名符合规则 直接使用
                return method;
            }else if (fieldName.getSimpleName().toString().startsWith("is")) {//变量名以is开头
                if (method.getSimpleName().toString().equalsIgnoreCase(fieldName.getSimpleName().toString().replaceFirst("is","set")))
                    return method;
            }
        }
        return null;
    }
}
