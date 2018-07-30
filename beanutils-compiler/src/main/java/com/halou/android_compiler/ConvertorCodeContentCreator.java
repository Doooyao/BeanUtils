package com.halou.android_compiler;

import com.halou.android_annotation.ClassNameUtils;
import com.halou.android_compiler.elementholder.OKConvertMethod;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Modifier;

/**
 * Created by ${yyy} on 18-7-26.
 * bean类互相转换的代码生成
 *
 */

public class ConvertorCodeContentCreator implements CodeContentCreator{

    private String packageName;

    private List<OKConvertMethod> methods;

    private String clsName;

    public ConvertorCodeContentCreator(String packageName,
                                       String fromeClassWholeName,
                                       List<OKConvertMethod> methods){
        this.packageName = packageName;
        this.methods = methods;
        this.clsName = ClassNameUtils.getClassNameFromQualifiedName(fromeClassWholeName);
    }

    @Override
    public String buildCodeContent() {
        return getContentFromJavaPoet(packageName,
                clsName,methods);
    }

    /**
     * 创建要生成代码的字符串
     */
    private String getContentFromJavaPoet(String packageName,
                                          String clsName,
                                          List<OKConvertMethod> methods){//要添加的转换方法的集合
        List<MethodSpec> staticConverterList = new ArrayList<>();
        for (OKConvertMethod method:methods){
            ClassName toClassName = ClassName.get(packageName, method.getConvertToType());
            ClassName fromClassName = ClassName.get(packageName, method.getConvertFromType());
            MethodSpec.Builder converterBuilder =
                    MethodSpec.methodBuilder(ClassNameUtils.getMethodNameFromBeanName(method.getConvertToTypeSimpleName()))
                    .addModifiers(Modifier.PUBLIC)
                    .addModifiers(Modifier.STATIC)
                    .addParameter(fromClassName,"from")
                    .addParameter(toClassName,"target");
            for (int i = 0;i<method.getGetMethodsConvertFrom().size()
                    &&i<method.getSetMethodsConvertTo().size();i++){
                converterBuilder.addStatement("target.$L(from.$L())",
                        method.getSetMethodsConvertTo().get(i),
                        method.getGetMethodsConvertFrom().get(i));
            }
            staticConverterList.add(converterBuilder.build());
        }

        TypeSpec.Builder typeSpecBuilder = TypeSpec.classBuilder(clsName)
                .addModifiers(Modifier.PUBLIC);
        for (MethodSpec methodSpec:staticConverterList){
            typeSpecBuilder.addMethod(methodSpec);
        }
        return JavaFile.builder(packageName, typeSpecBuilder.build()).build().toString();
    }
}
