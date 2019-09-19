package com.rnkrsoft.bopomofo4j.sandbox.v100;

/**
 * Created by rnkrsoft.com on 2019/9/19.
 */
class Vowels {
    static final char[] VOWELS = "aeiouv".toCharArray();
    static final String TONE_VOWELS = "āáǎàēéěèīíǐìōóǒòūúǔùǖǘǚǜ";
    public static char[] parse(char w){
        //寻找在有声调声母中的位置
        int k;
        if((k = TONE_VOWELS.indexOf(w)) > -1){
            int tone = (k % 4);
            //计算当前声母在无音调声母的位置
            int pos = k / 4;
            char vowel = VOWELS[pos];
            return new char[]{vowel, Character.forDigit(tone, 10)};
        }else{
            //原样
           return null;
        }
    }
}
