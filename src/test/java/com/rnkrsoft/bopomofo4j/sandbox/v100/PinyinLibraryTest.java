package com.rnkrsoft.bopomofo4j.sandbox.v100;

import com.rnkrsoft.bopomofo4j.protocol.IPinyinLibrary;
import org.junit.Test;

import java.util.Arrays;

/**
 * Created by woate on 2019/9/19.
 */
public class PinyinLibraryTest {

    @Test
    public void testGetPinyins() throws Exception {
        IPinyinLibrary pinyinLibrary = LocalPinyinLibrary.getPinyinLibrary();
        System.out.println(Arrays.toString(pinyinLibrary.getPinyins('我')));
        System.out.println(Arrays.toString(pinyinLibrary.getPinyins('的')));
    }

    @Test
    public void testGetPolyphoneWord() throws Exception {
        IPinyinLibrary pinyinLibrary = LocalPinyinLibrary.getPinyinLibrary();
        IPinyinLibrary.Polyphone polyphone = pinyinLibrary.getPolyphoneWord("是患难与共的", '难', 2, 0);
        System.out.println(Arrays.toString(polyphone.getWords()));
    }
}