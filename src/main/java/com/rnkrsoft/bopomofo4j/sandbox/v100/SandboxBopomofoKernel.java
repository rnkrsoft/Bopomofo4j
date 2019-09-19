package com.rnkrsoft.bopomofo4j.sandbox.v100;


import com.rnkrsoft.bopomofo4j.protocol.IBopomofoKernel;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Iterator;
import java.util.ServiceLoader;
import java.util.UUID;

/**
 * Created by woate on 2019/9/19.
 * 沙盒加载拼音算法核心
 * 1.首先尝试从Maven中央仓库下载最新版的Bopomofo4j库
 * 2.尝试加载最新版的Bopomofo4j库，如果加载成功，则使用最新版的库实现
 * 3.如果远程加载失败，则使用当前库带有的实现
 */
public class SandboxBopomofoKernel implements IBopomofoKernel {
    static final String GROUP_ID = "com.rnkrsoft.bopomofo4j";
    static final String ARTIFACT_ID = "bopomofo4j";
    static final String MAVEN_CENTER = "https://repo1.maven.org/maven2";
    IBopomofoKernel proxy;
    final IBopomofoKernel local = new LocalKernel();
    URLClassLoader sandboxClassLoader;
    Class<? extends IBopomofoKernel> kernelClass;
    /**
     * 默认沙盒模式运行
     */
    boolean sandboxMode = true;
    long lastCheckMs = -1L;
    /**
     * 设置为沙盒模式
     */
    public void sandbox() {
        this.sandboxMode = true;
    }

    /**
     * 设置为本地模式
     */
    public void local() {
        this.sandboxMode = false;
    }

    void check() {
        if (sandboxMode) {
            //如果上次检测时间未超过阈值1分钟，则不进行检测
            if (System.currentTimeMillis() - lastCheckMs > 60* 1000L) {
                if (this.proxy == null || this.proxy == this.local) {//如果代理类没有初始化或者等于本地库
                    //如果JVM参数-Dbopomofo4j.temp.dir没有指定，则使用当前目录
                    String tempDir = System.getProperty("bopomofo4j.temp.dir", "./bopomofo4j-temp");
                    //如果JVM参数-Dbopomofo4j.sandbox.url没有指定，则到中央仓库查找
                    String jarUrl = System.getProperty("bopomofo4j.sandbox.url");
                    try {
                        if (jarUrl == null || jarUrl.isEmpty()) {
                            jarUrl = fetchLastReleaseVersionUrl(MAVEN_CENTER, GROUP_ID, ARTIFACT_ID);
                        }
                        //进行远程下载，存放在临时目录
                        File file = download(jarUrl, tempDir);
                        URL url = file.toURI().toURL();
                        this.sandboxClassLoader = new URLClassLoader(new URL[]{url}, Thread.currentThread().getContextClassLoader());
                        this.kernelClass = (Class<? extends IBopomofoKernel>) sandboxClassLoader.loadClass(LocalKernel.class.getName());
                        ServiceLoader<IBopomofoKernel> serviceLoader = ServiceLoader.load(IBopomofoKernel.class, this.sandboxClassLoader);
                        Iterator<IBopomofoKernel> iterator = serviceLoader.iterator();
                        IBopomofoKernel lastNewKernel = local;
                        while (iterator.hasNext()) {
                            IBopomofoKernel instance = iterator.next();
                            if (instance.version() > local.version()) {
                                lastNewKernel = instance;
                            }
                        }
                        if (lastNewKernel != local) {
                            this.proxy = lastNewKernel;
                            System.out.println("Bopomofo4j Sandbox load success! sandbox version:" + this.proxy.version());
                        } else {
                            this.lastCheckMs = System.currentTimeMillis();
                            this.proxy = local;
                            System.err.println("Bopomofo4j local is last new version! use local version:" + this.proxy.version());
                        }
                    } catch (Exception e) {
                        this.lastCheckMs = System.currentTimeMillis();
                        //远程调用失败，使用本地库实现
                        this.proxy = local;
                        System.err.println("Bopomofo4j Sandbox load happens error! use local version:" + this.proxy.version());
                    }
                }
            }
        } else {
            if (this.proxy == null || this.proxy != this.local) {
                this.proxy = local;
            }
        }
    }

    @Override
    public String pinyin(String words, Integer toneType, Boolean upper, Boolean cap, String split) {
        check();
        //代理给代理实现
        return this.proxy.pinyin(words, toneType, upper, cap, split);
    }


    @Override
    public String cht2chs(String words) {
        check();
        return this.proxy.cht2chs(words);
    }

    @Override
    public String chs2cht(String words) {
        check();
        return this.proxy.chs2cht(words);
    }

    @Override
    public int version() {
        check();
        return this.proxy.version();
    }

    /**
     * @param url
     * @param tempDir
     * @return
     * @throws IOException
     */
    File download(String url, String tempDir) throws IOException {
        HttpRequest http = HttpRequest.get(url)
                .connectTimeout(20000)
                .readTimeout(15000)
                .useCaches(false)//不允许缓存
                .contentType("application/octet-stream", "UTF-8");
        File jarFile = new File(tempDir, "bopomofo4j-" + UUID.randomUUID().toString() + ".jar");
        if (!jarFile.getParentFile().exists()) {
            jarFile.getParentFile().mkdirs();
        }
        http.receive(jarFile);
        if (!http.ok()) {
            //发生异常
            throw new IOException("下载文件失败");
        }
        return jarFile;
    }

    /**
     * @param mavenCenter 中央仓库地址 https://repo1.maven.org/maven2
     * @return
     */
    String fetchLastReleaseVersionUrl(String mavenCenter, final String groupId, final String artifactId) {
        mavenCenter = mavenCenter == null ? "https://repo1.maven.org/maven2" : mavenCenter;
        //https://repo1.maven.org/maven2/com/belerweb/pinyin4j/maven-metadata.xml
        String metadataUrl = mavenCenter + "/" + groupId.replaceAll("\\.", "/") + "/" + artifactId + "/maven-metadata.xml";
        HttpRequest http = HttpRequest.get(metadataUrl)
                .connectTimeout(10 * 1000)
                .readTimeout(8 * 1000)
                .useCaches(false)//不允许缓存
                .contentType("application/octet-stream", "UTF-8");
        if (http.ok()) {
            String xml = http.body();
            if (xml == null || xml.isEmpty()) {
                return null;
            }
            int beginIndex = xml.indexOf("<versioning>");
            int endIndex = xml.indexOf("</versioning>", beginIndex);
            String temp = xml.substring(beginIndex + "<versioning>".length(), endIndex);
            int tsBeginIdx = temp.indexOf("<latest>");
            int tsEndIdx = temp.indexOf("</latest>");
            String latest = temp.substring(tsBeginIdx + "<latest>".length(), tsEndIdx);
            return mavenCenter + "/" + groupId.replaceAll("\\.", "/") + "/" + artifactId + "/" + latest + "/" + artifactId + "-" + latest + ".jar";
        }
        throw new RuntimeException("[" + groupId + ":" + artifactId + "] is not found!");
    }


}
