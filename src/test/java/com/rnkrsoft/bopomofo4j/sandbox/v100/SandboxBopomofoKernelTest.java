package com.rnkrsoft.bopomofo4j.sandbox.v100;

import org.junit.Test;

import java.io.File;

/**
 * Created by woate on 2019/9/19.
 */
public class SandboxBopomofoKernelTest {

    @Test
    public void testDownload() throws Exception {
        SandboxBopomofoKernel kernel = new SandboxBopomofoKernel();
        String groupId = "com.rnkrsoft.groundwork";
        String artifactId = "groundwork";
        String url = kernel.fetchLastReleaseVersionUrl(null, groupId, artifactId);
        File filePath = kernel.download(url, "./target");
        System.out.println(filePath);
    }

    @Test
    public void testFetchLastReleaseVersionUrl() throws Exception {
        SandboxBopomofoKernel kernel = new SandboxBopomofoKernel();
        String groupId = "com.rnkrsoft.groundwork";
        String artifactId = "groundwork";
        String url = kernel.fetchLastReleaseVersionUrl(null, groupId, artifactId);
        System.out.println(url);
    }
}