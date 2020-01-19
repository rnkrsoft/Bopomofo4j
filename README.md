# Bopomofo4j
[介绍](https://www.oschina.net/p/bopomofo4j)

零依赖，纯Java开发的汉字->拼音转换,简体<->繁体互转,具备沙盒运行模式
1. 实现汉字转拼音
2. 实现汉语单词转拼音
3. 实现汉语句子转拼音，在一定程度解决多音字问题
4. 实现简体，繁体互转
5. 词库实现沙盒模式进行热加载，也可使用本地模式

# 拼音搜索引擎
官网在线搜索：[pinyin.rnkrsoft.com](https://pinyin.rnkrsoft.com) ，当发现不能正确解析词语读音，可以来这里维护词库，向我们提出申请。


[![Maven central](https://maven-badges.herokuapp.com/maven-central/com.rnkrsoft.bopomofo4j/bopomofo4j/badge.svg)](http://search.maven.org/#search|ga|1|g%3A%22com.rnkrsoft.bopomofo4j%22%20AND%20a%3A%22bopomofo4j%22)

```xml
<dependency>
    <groupId>com.rnkrsoft.bopomofo4j</groupId>
    <artifactId>bopomofo4j</artifactId>
    <version>最新版本号</version>
</dependency>
```
最新版本号见上方，本库支持沙盒模式，即使使用低版本依然可以获取最新的功能实现。

纯JavaScript实现的兄弟库 Bopomofo.js [https://github.com/rnkrsoft/Bopomofo.js](https://github.com/rnkrsoft/Bopomofo.js),可以在HTML上轻松使用Bopomofo.js。同时Bopomofo4j中已经整合Bopomofo.js版本，如果使用embedded-tomcat库可以轻松直接使用Bopomofo.js，路径为/bopomofo/bopomofo.min.js
## 1.原理
1. 获取当前汉字的unicode值，如果在[19968,40869]中文区间，则执行第2步，否则直接输出（可能为符号，数字，英文字母或其他语系）
2. 检查当前汉字是否在多音字库中，如果存在返回该汉字发音的拼音和汉字序列数组，将当前句子上下文进行序列匹配，如果能够匹配，则为该发音。如果无返回，则进入第三步
3. 维护一个拼音与汉字映射的字库，遍历字库查找该拼音发音的汉字序列，将当前汉字与汉字序列进行检查是否在其中，如果在其中则返回该拼音。

## 2.沙盒模式
1. 当Bopomofo4j处于沙盒模式下，从Maven中央仓库查询最新的正式版本，使用最新的正式版本URL下载JAR。
2. 使用URL类加载器进行加载，加载成功后实例化IBopomofoKernel实现类，并缓存为proxy。
3. 如果下载过程或者加载过程发生异常，使用本地库作为proxy。
4. 如果人为设置模式为沙盒，则需要在超过1分钟后重新尝试步骤1，步骤2。
5. 如果人为设置模式为本地，则使用v100下的LocalKernel。如果为1.0.1则为v101下的LocalKernel。
## 3.API
整个库使用仅需要com.rnkrsoft.bopomofo4j.Bopomofo4j这个类的访问，提供如下几个方法。
```java
/**
 * 本地库运行拼音转换库
 */
public static final void local();

/**
 * 沙盒运行拼音转换库
 */
public static final void sandbox();

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
public static final String pinyin(String words, ToneType toneType, Boolean upper, Boolean cap, String split);

/**
 * 将繁体中文转换为简体中文
 * @param words 繁体中文句子
 * @return 简体中文句子
 */
public static final String cht2chs(String words);

/**
 * 将简体中文转换为繁体中文
 * @param words 简体中文句子
 * @return 繁体中文句子
 */
public static final String chs2cht(String words);
```



例如：

```
//汉语句子->声母音调拼音
String v1 = Bopomofo4j.pinyin("中国人！",0, false, false, " ");
System.out.println(v1);//控制台输出 zhōng guó rén！

//汉语句子->数字音调拼音
String v2 = Bopomofo4j.pinyin("患难与共的兄弟！！",1, false, false, " ");
System.out.println(v2);//控制台输出 huan4 nan4 yu3 gong4 de0 xiong1 di4！！

//汉语句子->无音调拼音
String v3 = Bopomofo4j.pinyin("this is a pinyin library!这是一个汉语拼音库！！",2, false, false, " ");
System.out.println(v3);//控制台输出 this is a pinyin library! zhe shi yi ge han yu pin yin ku！！

//繁体->简体
String v4 = Bopomofo4j.cht2chs("APM（Actions Per Minute）是一個在遊戲");
System.out.println(v4);//APM（Actions Per Minute）是一个在游戏

//简体->繁体
String v5 = Bopomofo4j.chs2cht("APM（Actions Per Minute）是一个在游戏");
System.out.println(v5);//APM（Actions Per Minute）是一個在遊戲
```

### 3.1沙盒模式
Bopomofo4j在此种设置下将访问"[https://repo1.maven.org/maven2/com/rnkrsoft/bopomofo4j/bopomofo4j](https://repo1.maven.org/maven2/com/rnkrsoft/bopomofo4j/bopomofo4j)"中央仓库地址，获取最新发布的Bopomofo4j运行库，获取后以沙盒方式热加载实现，也就是可以实现不更新Bopomofo4j包文件的情况下使用最新的Bopomofo4j实现。可以方便的获取字库更新的功能和逻辑实现。但是要防止[https://repo1.maven.org](https://repo1.maven.org)是否被localhost配置，如果配置有可能存在加载恶意代码的风险，使用时需要特别注意此点。默认情况下Bopomofo4j开启沙盒模式。可以通过以下代码禁用
```Java
Bopomofo4j.local();//启用本地模式（也就是禁用沙盒）
```

也可以在运行时启用沙盒
```Java
Bopomofo4j.sandbox();//启用沙盒模式
```

沙盒模式和本地模式的切换规则，在沙盒加载远程版本失败以后，要隔1分钟才进行下一次尝试运行沙盒，在这一分钟里Bopomofo4j回退到本地模式运行。

### 3.1.1强制指定远程版本

如果你有自己的私服仓库可以使用以下JVM参数来强制指定下载新版JAR地址

```
-Dbopomofo4j.sandbox.url=https://xxxx.com/bopomofo4j-1.0.0.jar
```

此种方式下将忽略中央仓库自动发现最新版机制，改用参数“bopomofo4j.sandbox.url”指定的地址。

### 3.1.2指定沙盒版本文件存放路径

当运行在沙盒模式时，远程文件被下载到“bopomofo4j.temp.dir”参数指定的路径下，默认情况相当于如下配置

```
-Dbopomofo4j.temp.dir=./bopomofo4j-temp
```

如果需要重新指定路径，则对参数重新设置值即可。

### 3.2本地模式（禁用沙盒）
Bopomofo4j在此种设置下将不再访问中央仓库地址"[https://repo1.maven.org/maven2/com/rnkrsoft/bopomofo4j/bopomofo4j](https://repo1.maven.org/maven2/com/rnkrsoft/bopomofo4j/bopomofo4j)"，也就不会下载最新版的Bopomofo4j来运行。如果禁用沙盒，又想更新版本，则只能替换Jar或者修改Maven,Gradle依赖来实现。
```Java
Bopomofo4j.local();//启用本地模式（也就是禁用沙盒）
```

