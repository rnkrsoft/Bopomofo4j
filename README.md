# Bopomofo4j
零依赖，纯Java开发的汉字转拼音库
1. 实现汉字转拼音
2. 实现汉语单词转拼音
3. 实现汉语句子转拼音，在一定程度解决多音字问题
4. 实现简体，繁体互转
5. 词库实现沙盒模式进行热加载，也可使用本地模式

## 原理
1. 获取当前汉字的unicode值，如果在[19968,40869]中文区间，则执行第2步，否则直接输出（可能为符号，数字，英文字母或其他语系）
2. 检查当前汉字是否在多音字库中，如果存在返回该汉字发音的拼音和汉字序列数组，将当前句子上下文进行序列匹配，如果能够匹配，则为该发音。如果无返回，则进入第三步
3. 维护一个拼音与汉字映射的字库，遍历字库查找该拼音发音的汉字序列，将当前汉字与汉字序列进行检查是否在其中，如果在其中则返回该拼音。

## 沙盒模式
1. 当Bopomofo4j处于沙盒模式下，从Maven中央仓库查询最新的正式版本，使用最新的正式版本URL下载JAR。
2. 使用URL类加载器进行加载，加载成功后实例化IBopomofoKernel实现类，并缓存为proxy。
3. 如果下载过程或者加载过程发生异常，使用本地库作为proxy。
4. 如果人为设置模式为沙盒，则需要在超过1分钟后重新尝试步骤1，步骤2。
5. 如果人为设置模式为本地，则使用v100下的LocalKernel。如果为1.0.1则为v101下的LocalKernel。
## API

```java
/**
 * 将汉字句子转换拼音，支持声母带音调，数字音调，无音调三种格式
 *
 * @param words    句子
 * @param toneType 拼音样式 0-声母带音调，1-数字音调在最后，2-无音调，默认值0
 * @param upper    是否大写,默认为假（小写）
 * @param cap      是否首字母大写,在upper为假时有效,默认为假（小写）
 * @param split    分割符号，默认一个空格
 * @return 拼音
 */
public static final String pinyin(String words, ToneType toneType, Boolean upper, Boolean cap, String split) {
    //输出拼音
}
```



例如：

```
String v1 = Bopomofo4j.pinyin('中国人！',0, false, false, " ");
System.out.println(v1);//控制台输出 zhōng guǒ rén！
String v2 = Bopomofo4j.pinyin('患难与共的兄弟！！',1, false, false, " ");
System.out.println(v2);//控制台输出 huan4 nan4 yu3 gong4 de0 xiong1 di4！！
String v3 = Bopomofo4j.pinyin('this is a pinyin library!这是一个汉语拼音库！！',2, false, false, " ");
System.out.println(v3);//控制台输出 this is a pinyin library! zhe shi yi ge han yu pin yin ku！！
```

