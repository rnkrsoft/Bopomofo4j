package com.rnkrsoft.bopomofo4j;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by rnkrsoft.com on 2019/9/19.
 */
public class Bopomofo4jTest {
    @Test
    public void testPinyin1() throws Exception {
        {
            String py = Bopomofo4j.pinyin("在这里输入你要转换的中文，然后点下面APM（Actions Per Minute）是一个在游戏领域常见的概念", ToneType.WITHOUT_TONE, null, null, null);
            Assert.assertEquals("zai zhe li shu ru ni yao zhuan huan de zhong wen， ran hou dian xia mianAPM（Actions Per Minute） shi yi ge zai you xi ling yu chang jian de gai nian", py);
        }
        {
            String py = Bopomofo4j.pinyin("在这里输入你要转换的中文，然后点下面APM（Actions Per Minute）是一个在游戏领域常见的概念", ToneType.WITH_VOWEL_TONE, null, null, null);
            Assert.assertEquals("zài zhè lǐ shū rù nǐ yào zhuǎn huàn de zhōng wén， rán hòu diǎn xià miànAPM（Actions Per Minute） shì yī gè zài yóu xì lǐng yù cháng jiàn de gài niàn", py);
        }
    }

    @Test
    public void testPinyin() throws Exception {
//        Bopomofo4j.local();
        System.setProperty("bopomofo4j.temp.dir", "./target/temp");
        System.setProperty("bopomofo4j.sandbox.url", "https://oss.sonatype.org/service/local/repositories/comrnkrsoft-1081/content/com/rnkrsoft/bopomofo4j/bopomofo4j/1.0.0/bopomofo4j-1.0.0.jar");
        for (int i = 0; i < 10000; i++) {
            String py1 = Bopomofo4j.pinyin("I am chinese!1234", ToneType.WITHOUT_TONE, null, null, null);
            Assert.assertEquals("I am chinese!1234", py1);
            String py2 = Bopomofo4j.pinyin("I am chinese!1234中国人", ToneType.WITH_VOWEL_TONE, null, null, null);
            Assert.assertEquals("I am chinese!1234 zhōng guó rén", py2);
            String py3 = Bopomofo4j.pinyin("I am chinese!1234我们是患难与共的兄弟", ToneType.WITH_NUMBER_TONE, null, null, null);
            Assert.assertEquals("I am chinese!1234 wo3 men0 shi4 huan0 nan0 yu3 gong0 de0 xiong0 di4", py3);
        }
    }

    @Test
    public void testCht2chs() throws Exception {
        Bopomofo4j.local();
        String chs = Bopomofo4j.cht2chs("APM（Actions Per Minute）是一個在遊戲領域常見的概念，主要在Starcraft等實時對戰遊戲中用到。一般職業選手的APM能高達300甚至更多。本測試采用一個簡單的模型來測試你的APM：從50到1，快速點擊相應的圓圈，第一次點擊時開始計時。您有充足的時間準備，所以請不要著急，觀察好之後再出手！");
        Assert.assertEquals("APM（Actions Per Minute）是一个在游戏领域常见的概念，主要在Starcraft等实时对战游戏中用到。一般职业选手的APM能高达300甚至更多。本测试采用一个简单的模型来测试你的APM：从50到1，快速点击相应的圆圈，第一次点击时开始计时。您有充足的时间准备，所以请不要着急，观察好之后再出手！", chs);
        System.out.println(chs);
    }

    @Test
    public void testChs2cht() throws Exception {
        Bopomofo4j.local();
        String cht = Bopomofo4j.chs2cht("APM（Actions Per Minute）是一个在游戏领域常见的概念，主要在Starcraft等实时对战游戏中用到。一般职业选手的APM能高达300甚至更多。本测试采用一个简单的模型来测试你的APM：从50到1，快速点击相应的圆圈，第一次点击时开始计时。您有充足的时间准备，所以请不要着急，观察好之后再出手！");
        Assert.assertEquals("APM（Actions Per Minute）是一個在遊戲領域常見的概念，主要在Starcraft等實時對戰遊戲中用到。一般職業選手的APM能高達300甚至更多。本測試采用一個簡單的模型來測試你的APM：從50到1，快速點擊相應的圓圈，第一次點擊時開始計時。您有充足的時間準備，所以請不要著急，觀察好之後再出手！", cht);
        System.out.println(cht);
    }
}