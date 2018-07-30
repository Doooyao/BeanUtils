package com.halou.android_compiler;

import java.util.HashMap;

import javax.annotation.processing.Messager;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * Created by ${yyy} on 18-7-26.
 * 用来缓存处理好的bean
 */

public class ElementExtractorCache {

    private Elements elements;

    private Messager messager;

    private HashMap<String,ElementExtractor> cacheHashMap = new HashMap<>();

    public ElementExtractorCache(Elements elements, Messager messager){
        this.elements = elements;
        this.messager = messager;
    }

    public ElementExtractor get(TypeElement typeElement){
        String name = typeElement.getQualifiedName().toString();
        ElementExtractor extractor = cacheHashMap.get(name);
        if (extractor!=null)
            return extractor;
        extractor = new ElementExtractor(typeElement,elements,messager);
        cacheHashMap.put(name,extractor);
        return extractor;
    }
}
