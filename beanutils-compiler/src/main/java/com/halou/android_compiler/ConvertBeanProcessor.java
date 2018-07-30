package com.halou.android_compiler;

import com.google.auto.service.AutoService;
import com.halou.android_annotation.ClassNameUtils;
import com.halou.android_annotation.ConvertBean;
import com.halou.android_compiler.elementholder.OKConvertMethod;
import com.halou.android_compiler.elementholder.OKField;
import com.halou.android_compiler.elementholder.OKFieldElement;
import com.halou.android_compiler.elementholder.SimpleOKConvertMethod;
import com.halou.android_compiler.file.JavaFileMaker;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

/**
 * Created by ${yyy} on 18-7-24.
 * 注解处理器 生成拷贝bean的代码
 */
@SupportedAnnotationTypes({
        "com.halou.android_annotation.ConvertBean",
        "com.halou.android_annotation.MethodOf",
        "com.halou.android_annotation.ConvertName",
})
@SupportedSourceVersion(SourceVersion.RELEASE_7)
@AutoService(Processor.class)
public class ConvertBeanProcessor extends AbstractProcessor {

    // 用于解析 Element
    private Elements mElementUtils;

    private Messager messager;

    private ElementExtractorCache extractorCache;

    private JavaFileMaker fileMaker;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mElementUtils = processingEnvironment.getElementUtils();
        fileMaker = new JavaFileMaker(processingEnvironment.getFiler());
        messager = processingEnvironment.getMessager();
        extractorCache = new ElementExtractorCache(mElementUtils,messager);
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        messager.printMessage(Diagnostic.Kind.WARNING,"开始生成beanutils代码");
        for (Element element:roundEnvironment.getElementsAnnotatedWith(ConvertBean.class)){
            if (element.getKind()!= ElementKind.CLASS){
                continue;
            }

            ElementExtractor fromExtractor = extractorCache.get((TypeElement) element);//解析数据来源Bean

            List<OKConvertMethod> convertMethods = new ArrayList<>();
            //遍历需要转换的bean 依次生成类
            for (Map.Entry<String,TypeElement> entry:fromExtractor.getConvertTypes().entrySet()){
                ElementExtractor targetExtractor = extractorCache.get(entry.getValue());
                List<OKField> fromElements = new ArrayList<>();
                List<OKField> targetElements = new ArrayList<>();
                for (OKField fromField:fromExtractor.getOkFieldList().values()){
                    OKField targetField = targetExtractor.getOkFieldList().get(fromField.getMatchName());
                    //进行判断
                    if (fromField.getMethod()!=null
                            &&targetField!=null
                            &&targetField.setMethod()!=null
                            &&targetField.getType().equals(fromField.getType())){//暂时只处理类型相同的情况
                        //符合转换规范 可以开始转换
                         fromElements.add(fromField);
                         targetElements.add(targetField);
                    }
                }
                convertMethods.add(new SimpleOKConvertMethod((TypeElement) element,entry.getValue(),fromElements,targetElements));
            }
            String code = new ConvertorCodeContentCreator(mElementUtils.getPackageOf(element).getQualifiedName().toString(),
                    ((TypeElement) element).getQualifiedName().toString(),
                    convertMethods).buildCodeContent();
            fileMaker.makeFile(ClassNameUtils.getClassNameFromQualifiedName(((TypeElement) element).getQualifiedName().toString()),
                    code);
        }
        messager.printMessage(Diagnostic.Kind.WARNING,"生成beanutils代码完毕");
        return true;
    }
}
