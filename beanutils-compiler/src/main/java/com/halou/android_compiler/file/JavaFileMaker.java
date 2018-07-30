package com.halou.android_compiler.file;

import java.io.IOException;
import java.io.Writer;

import javax.annotation.processing.Filer;
import javax.tools.JavaFileObject;

/**
 * Created by ${yyy} on 18-7-26.
 * java 文件生成器
 *
 */

public class JavaFileMaker implements FileMaker {

    private Filer mFiler;

    public JavaFileMaker(Filer mFiler){
        this.mFiler = mFiler;
    }

    @Override
    public void makeFile(String fileWholeName, String fileContent) {
        try {
            // 创建文件
            JavaFileObject javaFileObject = mFiler.createSourceFile(fileWholeName);
            Writer writer = javaFileObject.openWriter();
            // 把内容写入到文件中
            writer.write(fileContent);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
