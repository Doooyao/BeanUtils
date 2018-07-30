package com.halou.android_compiler;

import com.halou.android_annotation.ConvertBean;
import com.halou.android_annotation.ConvertName;
import com.halou.android_compiler.elementholder.OKField;
import com.halou.android_compiler.elementholder.OKFieldElement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.processing.Messager;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

/**
 * Created by ${yyy} on 18-7-26.
 * 用于从element中提取需要的元素
 */

public class ElementExtractor {

    /**
     * bean元素
     */
    private TypeElement rootElement;

    /**
     * 使用 {@link com.halou.android_annotation.ConvertName} 更换bean的匹配名字
     */
    private Map<String,VariableElement> fieldsElement = new HashMap<>();

    /**
     * bean下的所有方法元素
     */
    private List<ExecutableElement> methods = new ArrayList<>();

    /**
     * bean 需要转化(可以转化)成的所有其他bean
     */
    private Map<String,TypeElement> convertBeans = new HashMap<>();

    /**
     * 查询好的可以直接用的属性以及get set 方法
     */
    private Map<String,OKField> okFieldList = new HashMap<>();

    /**
     * 获取需要转换成的beanList
     */
    public Map<String,TypeElement> getConvertTypes(){
        return convertBeans;
    }

    public Map<String,OKField> getOkFieldList(){
        return okFieldList;
    }

    public ElementExtractor(TypeElement rootElement,
                            Elements mElementUtils,
                            Messager messager){
        this.rootElement = rootElement;
        messager.printMessage(Diagnostic.Kind.NOTE,rootElement.getQualifiedName().toString());
        //先初始化所有的数据
        List<? extends Element> enclosedElements = rootElement.getEnclosedElements();
        for (Element element:enclosedElements){
            if (element.getKind() == ElementKind.FIELD){//属性
                ConvertName anno = element.getAnnotation(ConvertName.class);
                String name = anno==null?element.getSimpleName().toString():anno.value();
                messager.printMessage(Diagnostic.Kind.NOTE,name);
//                messager.printMessage(Diagnostic.Kind.WARNING,fieldsElement.get(name).getSimpleName().toString());
                if (fieldsElement.get(name)==null||anno!=null){
                    messager.printMessage(Diagnostic.Kind.NOTE,"==="+name);
                    fieldsElement.put(name, (VariableElement) element);
                }
            }else if (element.getKind() == ElementKind.METHOD){
                methods.add((ExecutableElement) element);
            }
        }
        //获取需要转化的bean
        List<? extends AnnotationMirror> annotationMirros = rootElement.getAnnotationMirrors();
        AnnotationValue annoValue = null;
        TypeMirror convertBeanAnnoType = mElementUtils.
                getTypeElement(ConvertBean.class.getName())
                .asType();
        for (AnnotationMirror annotationMirror :annotationMirros){
            if(annotationMirror.getAnnotationType().equals(convertBeanAnnoType)){
                for(Map.Entry <? extends ExecutableElement,?extends AnnotationValue> entry:annotationMirror.getElementValues().entrySet())
                {
                    if("value".equals(entry.getKey().getSimpleName().toString())){
                        annoValue = entry.getValue();
                        break;
                    }
                }
            }
        }
        if (annoValue != null)
            for (Object object:(List) annoValue.getValue()){
                String toEleName = object.toString().replace(".class","");
                Element toBeanElement = mElementUtils.getTypeElement(toEleName);
                convertBeans.put(toEleName,(TypeElement) toBeanElement);
            }
        for (Map.Entry<String,VariableElement> entry:fieldsElement.entrySet()){
            OKFieldElement okFieldElement = new OKFieldElement();
            ExecutableElement get = CheckMethodUtils.checkGetMet(methods,entry.getValue());
            okFieldElement.setGetMethod(get==null?null:get.getSimpleName().toString());
            ExecutableElement set = CheckMethodUtils.checkSetMet(methods,entry.getValue());
            okFieldElement.setSetMethod(set==null?null:set.getSimpleName().toString());
            okFieldElement.setField(entry.getValue().getSimpleName().toString());
            okFieldElement.setType(entry.getValue().asType().toString());
            okFieldElement.setMatchName(entry.getKey());
            okFieldList.put(entry.getKey(),okFieldElement);
        }
    }
}
