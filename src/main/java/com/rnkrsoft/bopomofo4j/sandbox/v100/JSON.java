package com.rnkrsoft.bopomofo4j.sandbox.v100;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rnkrsoft.com on 2019/9/19.
 * 一个简单的解析JSON文本到键值对的工具
 */
public class JSON {
    /**
     * 将传入的URL解析为Map
     *
     * @param url URL对象
     * @return 键值对
     * @throws IOException
     */
    public static final Map<String, String> parse(URL url) throws IOException {
        InputStream is = null;
        try {
            is = url.openStream();
            String json = read(is);
            return JSON.parse(json);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                is = null;
            }
        }
    }

    /**
     * 将传入的JSON解析为MAP对象
     *
     * @param json JSON数据
     * @return 键值对
     */
    public static final Map<String, String> parse(String json) {
        char[] chars = json.toCharArray();
        final Map<String, String> map = new HashMap<String, String>();
        int length = chars.length;
        int beginObjectIdx = -1;
        boolean matchKey = false;
        boolean matchValue = false;
        int quotationIdx = -1;
        int matchComma = -1;
        int commaIdx = -1;
        boolean gameOver = false;
        int beginValueIdx = -1;
        boolean firstMatchChar = false;
        boolean matchColon = false;
        String key = null;
        for (int i = 0; i < length; i++) {
            char c = chars[i];
            if (c == ' ' || c == '\n' || c == '\r') {
                if (matchKey && matchColon && firstMatchChar && !matchValue) {
                    String value = json.substring(beginValueIdx, i);
                    map.put(key, value);
                    matchValue = true;
                }
            } else if (c == ':') {
                matchColon = true;
            } else {
                if (gameOver) {
                    throw new IllegalArgumentException("illegal json format!");
                }
                if (c == '{') {
                    if (beginObjectIdx == -1) {
                        beginObjectIdx = i;
                    }
                } else if (c == '}') {
                    if (matchComma == 1) {
                        throw new IllegalArgumentException("illegal json format! cause: index[" + commaIdx + "] = \"" + chars[commaIdx] + "\" is illegal!");
                    }
                    gameOver = true;
                } else if (c == ',') {
                    //如果不在引号包裹里的逗号，是语法符号
                    if (quotationIdx == -1) {
                        matchKey = false;
                        matchValue = false;
                        matchColon = false;
                        beginValueIdx = -1;
                        commaIdx = i;
                        matchComma = 1;
                        firstMatchChar = false;
                    }
                } else if (c == '"') {
                    if (matchComma == -1 || matchComma == 1) {
                        matchComma = 0;
                        commaIdx = -1;
                    }
                    if (quotationIdx == -1) {
                        quotationIdx = i;
                    }else if (quotationIdx != -1) {
                        if (!matchKey) {
                            if (quotationIdx > -1) {
                                matchKey = true;
                                key = json.substring(quotationIdx + 1, i);
                            }
                        } else if (!matchValue) {
                                String value = json.substring(quotationIdx + 1, i);
                                map.put(key, value);
                                matchValue = true;
                        } else if (matchKey && matchValue) {
                            throw new IllegalArgumentException("illegal json format! cause: index[" + i + "] = \"" + c + "\" is illegal!");
                        }
                        quotationIdx = -1;
                    }
                } else if (c >= '0' || c <= '9' || c == '-' || c == '.') {
                    if (matchKey && matchColon) {
                        if (!firstMatchChar && beginValueIdx == -1) {
                            firstMatchChar = true;
                            beginValueIdx = i;
                        }
                    } else if (matchKey && matchColon && firstMatchChar && matchValue) {
                        throw new IllegalArgumentException("illegal json format! cause: index[" + i + "] = \"" + c + "\" is illegal!");
                    }
                }
            }
        }
        return map;
    }

    static String read(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        long count;
        int n;
        byte[] buffer = new byte[4096];
        for (count = 0L; -1 != (n = input.read(buffer)); count += (long) n) {
            output.write(buffer, 0, n);
        }
        return new String(output.toByteArray(), "UTF-8");
    }
}
