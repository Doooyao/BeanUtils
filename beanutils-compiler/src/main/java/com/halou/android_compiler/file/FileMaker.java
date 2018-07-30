package com.halou.android_compiler.file;

/**
 * Created by ${yyy} on 18-7-26.
 * 文件生成器的接口
 */

public interface FileMaker {

    /**
     * 生成文件
     * @param fileWholeName 文件全名 包括包名 文件名 不要后缀
     * @param fileContent 文件内容字符串
     */
    void makeFile(String fileWholeName,String fileContent);
}
