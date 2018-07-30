package com.halou.android_compiler.elementholder;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;

/**
 * Created by ${yyy} on 18-7-25.
 * 可以使用的属性Element 集合
 * 1 有一个属性元素
 * 2 有get set方法
 */

public class OKFieldElement implements OKField{

    String type;

    String matchName;

    String field;

    String getMethod;

    String setMethod;

    public void setType(String type) {
        this.type = type;
    }

    public void setMatchName(String matchName) {
        this.matchName = matchName;
    }

    public void setField(String field) {
        this.field = field;
    }

    public void setGetMethod(String getMethod) {
        this.getMethod = getMethod;
    }

    public void setSetMethod(String setMethod) {
        this.setMethod = setMethod;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getMatchName() {
        return matchName;
    }

    @Override
    public String getField() {
        return field;
    }

    @Override
    public String getMethod() {
        return getMethod;
    }

    @Override
    public String setMethod() {
        return setMethod;
    }
}
