package com.rnkrsoft.bopomofo4j.sandbox.v100;

import org.junit.Assert;
import org.junit.Test;


/**
 * Created by woate on 2019/9/19.
 */
public class VowelsTest {

    @Test
    public void testParse() throws Exception {
        char[] chars = Vowels.parse('ǎ');
        Assert.assertEquals('a', chars[0]);
        Assert.assertEquals('3', chars[1]);
    }
}