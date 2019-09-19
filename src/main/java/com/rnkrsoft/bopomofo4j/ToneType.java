package com.rnkrsoft.bopomofo4j;

/**
 * Created by rnkrsoft.com on 2019/9/19.
 * 音调样式
 */
public enum ToneType {
    /**
     * 使用韵母带有调值符号的样式
     */
    WITH_VOWEL_TONE(0, "带韵母声调"),
    /**
     * 用数字0，1,2,3,4分别表示轻声，(一声)阴平，(二声)阳平，(三声)上声，(四声)去声的样式
     */
    WITH_NUMBER_TONE(1, "带数字声调"),
    /**
     * 无声调
     */
    WITHOUT_TONE(2, "没有声调");
    int code;
    String desc;

    ToneType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
