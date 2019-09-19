package com.rnkrsoft.bopomofo4j;

import com.rnkrsoft.bopomofo4j.sandbox.v100.SandboxBopomofoKernel;

/**
 * Created by rnkrsoft.com on 2019/9/19.
 * Bopomofo4j拼音库，用于将汉字转换为拼音
 */
public abstract class Bopomofo4j {
    /**
     * Bopomofo4j核心，核心根据运行情况选择核心实现类
     */
    final static SandboxBopomofoKernel KERNEL = new SandboxBopomofoKernel();

    /**
     * 本地库运行拼音转换库
     */
    public static final void local(){
        KERNEL.local();
    }

    /**
     * 沙盒运行拼音转换库
     */
    public static final void sandbox(){
        KERNEL.sandbox();
    }
    /**
     * 将汉字句子转换拼音，支持声母带音调，数字音调，无音调三种格式
     *
     * @param words    句子
     * @param toneType 拼音样式 0-声母带音调，1-数字音调在最后，2-无音调，默认值0
     * @param upper    是否大写,默认为假（小写）
     * @param cap      是否首字母大写,在upper为假时有效,默认为假（小写）
     * @param split    分割符号，默认一个空格
     * @return 拼音
     */
    public static final String pinyin(String words, ToneType toneType, Boolean upper, Boolean cap, String split) {
        return pinyin(words, toneType.getCode(), upper, cap, split);
    }

    /**
     * 将汉字句子转换拼音，支持声母带音调，数字音调，无音调三种格式
     *
     * @param words    句子
     * @param toneType 拼音样式 0-声母带音调，1-数字音调在最后，2-无音调，默认值0
     * @param upper    是否大写,默认为假（小写）
     * @param cap      是否首字母大写,在upper为假时有效,默认为假（小写）
     * @param split    分割符号，默认一个空格
     * @return 拼音
     */
    public static final String pinyin(String words, Integer toneType, Boolean upper, Boolean cap, String split) {
        return KERNEL.pinyin(words, toneType, upper, cap, split);
    }

    /**
     * 将繁体中文转换为简体中文
     * @param words 繁体中文句子
     * @return 简体中文句子
     */
    public static final String cht2chs(String words){
        return KERNEL.cht2chs(words);
    }
    /**
     * 将简体中文转换为繁体中文
     * @param words 简体中文句子
     * @return 繁体中文句子
     */
    public static final String chs2cht(String words){
        return KERNEL.chs2cht(words);
    }

}
