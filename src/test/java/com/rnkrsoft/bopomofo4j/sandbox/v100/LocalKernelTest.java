package com.rnkrsoft.bopomofo4j.sandbox.v100;

import com.rnkrsoft.bopomofo4j.ToneType;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by woate on 2019/9/19.
 */
public class LocalKernelTest {

    @Test
    public void testPinyin() throws Exception {
        Assert.assertEquals("I am chinese!1234", new LocalKernel().pinyin("I am chinese!1234", ToneType.WITH_NUMBER_TONE.getCode(), null, null, null));
        Assert.assertEquals("wo3 men0 shi4 huan0 nan0 yu3 gong0 de0", new LocalKernel().pinyin("我们是患难与共的", ToneType.WITH_NUMBER_TONE.getCode(), null, null, null));
        Assert.assertEquals("mu4 di4 di4", new LocalKernel().pinyin("目的地", ToneType.WITH_NUMBER_TONE.getCode(), null, null, null));
    }

    @Test
    public void testHandlePinyin() throws Exception {

    }

    @Test
    public void testCapitalize() throws Exception {
        String py = new LocalKernel().capitalize("che");
        Assert.assertEquals("Che", py);
    }
}