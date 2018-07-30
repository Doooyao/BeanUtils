package com.halou.android_compiler.elementholder;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.TypeElement;

/**
 * Created by ${yyy} on 18-7-30.
 * 属性转换方法
 */

public class SimpleOKConvertMethod implements OKConvertMethod{

    List<String> getMethods = new ArrayList<>();

    List<String> setMethods = new ArrayList<>();

    TypeElement fromElement;

    TypeElement targetElement;

    public SimpleOKConvertMethod(TypeElement fromElement,
                                 TypeElement targetElement,
                                 List<OKField> fromFields,
                                 List<OKField> targetFields){
        this.fromElement = fromElement;
        this.targetElement = targetElement;
        for (OKField okField:fromFields){
            getMethods.add(okField.getMethod());
        }
        for (OKField okField:targetFields){
            setMethods.add(okField.setMethod());
        }
    }

    @Override
    public String getConvertFromType() {
        return fromElement.asType().toString();
    }

    @Override
    public String getConvertFromTypeSimpleName() {
        return fromElement.getSimpleName().toString();
    }

    @Override
    public String getConvertToType() {
        return targetElement.asType().toString();
    }

    @Override
    public String getConvertToTypeSimpleName() {
        return targetElement.getSimpleName().toString();
    }

    @Override
    public List<String> getGetMethodsConvertFrom() {
        return getMethods;
    }

    @Override
    public List<String> getSetMethodsConvertTo() {
        return setMethods;
    }
}
