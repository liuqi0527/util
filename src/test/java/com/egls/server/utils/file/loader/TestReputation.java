package com.egls.server.utils.file.loader;

import java.io.File;

import com.egls.server.utils.file.TextFileUtil;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author mayer - [Created on 2018-09-04 11:32]
 */
public class TestReputation {

    @Test
    public void test0() {
        ReputationConfig reputationConfig = new ReputationConfig();
        reputationConfig.loadDirectory();
        Assert.assertEquals(2, ReputationConfig.reputationMap.size());

        Reputation reputation1 = ReputationConfig.reputationMap.get("repu1");
        Reputation reputation2 = ReputationConfig.reputationMap.get("repu2");

        Assert.assertEquals("repu1_buff", reputation1.buff);
        Assert.assertEquals("repu2_buff", reputation2.buff);

        Assert.assertNotEquals(reputation1.oldName, reputation1.newName);
        Assert.assertNotEquals(reputation2.oldName, reputation2.newName);

    }

    @Test
    public void test1() throws Exception {
        ReputationConfig reputationConfig = new ReputationConfig();
        LoaderManager.startup();
        LoaderManager.registerDirectory(new File(ReputationConfig.PATH), reputationConfig);

        File file = new File(ReputationConfig.PATH + "repu1.xml");
        final String content = TextFileUtil.read(file, false);
        TextFileUtil.write(content, file, false, false);

        LoaderManager.reload();

        Assert.assertEquals(2, ReputationConfig.reputationMap.size());
        Reputation reputation1 = ReputationConfig.reputationMap.get("repu1");
        Reputation reputation2 = ReputationConfig.reputationMap.get("repu2");

        Assert.assertEquals("repu1_buff", reputation1.buff);
        Assert.assertEquals("repu2_buff", reputation2.buff);

        Assert.assertNotEquals(reputation1.oldName, reputation1.newName);
        Assert.assertNotEquals(reputation2.oldName, reputation2.newName);
    }

}
