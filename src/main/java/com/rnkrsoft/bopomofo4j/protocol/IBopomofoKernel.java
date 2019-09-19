package com.rnkrsoft.bopomofo4j.protocol;

/**
 * Created by rnkrsoft.com on 2019/9/19.
 * 拼音核心类接口，此接口实现类为热加载实现
 */
public interface IBopomofoKernel {
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
    String pinyin(String words, Integer toneType, Boolean upper, Boolean cap, String split);

    /**
     * 将繁体中文转换为简体中文
     * @param words 繁体中文句子
     * @return 简体中文句子
     */
    String cht2chs(String words);
    /**
     * 将简体中文转换为繁体中文
     * @param words 简体中文句子
     * @return 繁体中文句子
     */
    String chs2cht(String words);
    /**
     * 当前库的版本号
     * @return 版本号
     */
    int version();
}
