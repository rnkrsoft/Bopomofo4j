package com.rnkrsoft.bopomofo4j.sandbox.v100;

import com.rnkrsoft.bopomofo4j.protocol.IBopomofoKernel;
import com.rnkrsoft.bopomofo4j.protocol.IPinyinLibrary;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rnkrsoft.com on 2019/9/19.
 * 零依赖实现的汉字转拼音库
 * 1. 实现汉字转拼音
 * 2. 实现汉语单词转拼音
 * 3. 实现汉语句子转拼音，在一定程度解决多音字问题
 */
public class LocalKernel implements IBopomofoKernel {
    IPinyinLibrary pinyinLibrary = LocalPinyinLibrary.getPinyinLibrary();

    @Override
    public int version() {
        return 100;
    }

    @Override
    public final String pinyin(String words, Integer toneType, Boolean upper, Boolean cap, String split) {
        toneType = toneType == null ? 0 : toneType;
        upper = upper == null ? false : upper;
        cap = cap == null ? false : cap;
        split = split == null ? " " : split;
        char[] chars = words.toCharArray();
        final List<String> result = new ArrayList<String>();
        final List<Integer> types = new ArrayList<Integer>();
        int lastPolyphoneIndex = 0;
        final int len = chars.length;
        for (int i = 0; i < len; ) {
            char _char = chars[i];
            //如果unicode在符号，英文，数字或其他语系，则直接返回
            if (_char >= 40869 || _char <= 12295) {
                result.add(Character.toString(_char));
                types.add(0);
            } else {//如果是支持的中文，则获取汉字的所有拼音
                String[] pys = pinyinLibrary.getPinyins(_char);
                if (pys.length == 1) {//单音字
                    String py = pys[0];
                    result.add(py);
                    types.add(1);
                } else if (pys.length > 1) {//多音字，需要进行特殊处理
                    //
                    IPinyinLibrary.Polyphone data = pinyinLibrary.getPolyphoneWord(words, _char, i, lastPolyphoneIndex);
                    if (data != null) {
                        for (int k = 0; k < data.getOffset(); k++) {
                            result.remove(i - data.getOffset() + k);
                            types.remove(i - data.getOffset() + k);
                        }
                        for (int k = 0; k < data.getWords().length; k++) {
                            result.add(data.getWords()[k]);
                            types.add(2);
                        }
                        //修正偏移，有可能当前字是词组中非第一个字
                        i = i - data.getOffset() + data.getWords().length - 1;
                        //最后处理过的多音字位置，以防止一个多音字词组有多个多音字，例如患难与共，难和共都是多音字
                        lastPolyphoneIndex = i;
                    } else {//没有找到多音字的词组，默认使用第一个发音
                        String py = pys[0];
                        result.add(py);
                        types.add(1);
                    }
                } else {//未发现
                    result.add(Character.toString(_char));
                    types.add(0);
                }
            }
            i++;
        }
        return handlePinyin(result, types, toneType, upper, cap, split);
    }

    @Override
    public String cht2chs(String words) {
        char[] chars = words.toCharArray();
        int len = chars.length;
        char[] chsChars = new char[len];
        for (int i = 0; i < len; i++) {
            char _char = chars[i];
            if (_char >= 19999 || _char <= 40860) {
                String n = pinyinLibrary.chs(_char);
                chsChars[i] = n == null ? _char : n.charAt(0);
            } else {
                chsChars[i] = _char;
            }
        }
        return new String(chsChars);
    }

    @Override
    public String chs2cht(String words) {
        char[] chars = words.toCharArray();
        int len = chars.length;
        char[] chtChars = new char[len];
        for (int i = 0; i < len; i++) {
            char _char = chars[i];
            if (_char >= 19999 || _char <= 40860) {
                String n = pinyinLibrary.cht(_char);
                chtChars[i] = n == null ? _char : n.charAt(0);
            } else {
                chtChars[i] = _char;
            }
        }
        return new String(chtChars);
    }

    /**
     * 进行拼音处理
     *
     * @param result
     * @param types
     * @param toneType
     * @param upper
     * @param cap
     * @param split
     * @return
     */
    final String handlePinyin(List<String> result, List<Integer> types, int toneType, boolean upper, boolean cap, String split) {
        String pys = "";
        for (int i = 0; i < result.size(); i++) {
            String py = result.get(i);
            int type = types.get(i);
            String py1 = "";
            if (type == 1 || type == 2) {//如果是拼音或者多音字
                if (toneType == 1 || toneType == 2) {//如需要数字声调或者无声调
                    int tone = -1;//音调数字形式
                    char[] chars = py.toCharArray();
                    for (int idx = 0; idx < chars.length; idx++) {
                        char w = chars[idx];
                        //寻找在有声调声母中的位置
                        char[] cs = Vowels.parse(w);
                        py1 += (cs == null ? w : cs[0]);
                        tone = (cs == null ? -1 : Character.digit(cs[1], 10));
                    }
                    //如果是带音调数字形式，则将音调添加到末尾
                    py1 = py1 + (toneType == 1 ? tone + 1 : "");
                } else {
                    py1 = py;
                }
                if (upper) {
                    py1 = py1.toUpperCase();
                } else {
                    py1 = cap ? capitalize(py1) : py1;
                }
                py1 = split.length() > 0 && pys.length() > 0 ? split + py1 : py1;
            } else {//如果不需要处理的非拼音
                py1 = py;
            }
            pys += py1;
        }
        return pys;
    }

    /**
     * 单个汉字拼音，进行首字母大写
     *
     * @param py 单个汉字拼音
     * @return
     */
    final String capitalize(String py) {
        if (!py.isEmpty()) {
            String first = py.substring(0, 1).toUpperCase();
            String spare = py.substring(1, py.length());
            return first + spare;
        } else {
            return py;
        }
    }
}
