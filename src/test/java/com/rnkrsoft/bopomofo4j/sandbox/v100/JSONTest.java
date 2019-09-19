package com.rnkrsoft.bopomofo4j.sandbox.v100;

import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by rnkrsoft.com on 2019/9/19.
 */
public class JSONTest {

    @Test
    public void testParse() throws Exception {
        {
            Map<String, String> map = JSON.parse(" {   \"key1\"    :  \"value1\"  } ");
            Assert.assertEquals(1, map.size());
            Assert.assertEquals("value1", map.get("key1"));
        }
        {
            Map<String, String> map = JSON.parse(" {   \"key1\"    :  \"value1\" , \"key2\"    :  \"value2\" } ");
            Assert.assertEquals(2, map.size());
            Assert.assertEquals("value1", map.get("key1"));
        }
        {
            Map<String, String> map = JSON.parse(" {   \"key1\"    :  1 } ");
            Assert.assertEquals(1, map.size());
            Assert.assertEquals("1", map.get("key1"));
        }

        {
            Map<String, String> map = JSON.parse(" {   \"key1\"    :  -1 } ");
            Assert.assertEquals(1, map.size());
            Assert.assertEquals("-1", map.get("key1"));
        }

        {
            Map<String, String> map = JSON.parse(" {   \"key1\"    :  -123456.7890 } ");
            Assert.assertEquals(1, map.size());
            Assert.assertEquals("-123456.7890", map.get("key1"));
        }

        {
            Map<String, String> map = JSON.parse(" {   \"key1\"    :  \"\" , \"key2\"    :  \"1234\" , \"key3\"    :  123456789 , \"key4\"    :  -123456.7890 , \"key5\"    :  0.7890 , \"key,6\"    :  \"1234,5678\" }");
            System.out.println(map);
            Assert.assertEquals(6, map.size());
            Assert.assertEquals("", map.get("key1"));
            Assert.assertEquals("1234", map.get("key2"));
            Assert.assertEquals("123456789", map.get("key3"));
            Assert.assertEquals("-123456.7890", map.get("key4"));
            Assert.assertEquals("0.7890", map.get("key5"));
            Assert.assertEquals("1234,5678", map.get("key,6"));
        }
    }

    @Test
    public void testParse2() throws Exception {
        Map<String, String> map = JSON.parse(JSONTest.class.getClassLoader().getResource("META-INF/resources/bopomofo/libs/v100/polyphones.json"));
        System.out.println(map);
    }
}