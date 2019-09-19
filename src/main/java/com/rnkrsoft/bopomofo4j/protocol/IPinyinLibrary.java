package com.rnkrsoft.bopomofo4j.protocol;


/**
 * Created by woate on 2019/9/19.
 */
public interface IPinyinLibrary {
    /**
     * 封装多音字
     */
    final class Polyphone {
        String[] words;
        int offset;
        int length;

        public Polyphone(String[] words, int offset, int length) {
            this.words = words;
            this.offset = offset;
            this.length = length;
        }

        public String[] getWords() {
            return words;
        }

        public int getOffset() {
            return offset;
        }

        public int getLength() {
            return length;
        }
    }
    /**
     * 将汉字转换为拼音数组
     * @param w 汉字
     * @return 拼音数组
     */
    String[] getPinyins(char w);

    /**
     * 获取多音字词，返回时返回替换起始位置和结束位置
     * @param words 句子
     * @param current 当前字
     * @param pos 当前汉字的位置
     * @param lastPolyphoneIndex 最近一次多音字词组处理索引
     * @return 多音字词组
     */
    Polyphone getPolyphoneWord(String words, char current, int pos, int lastPolyphoneIndex);

    /**
     * 将繁体转换为简体
     * @param w 繁体
     * @return 简体
     */
    String chs(char w);
    /**
     * 将简体转换为繁体
     * @param w 简体
     * @return 繁体
     */
    String cht(char w);
}
