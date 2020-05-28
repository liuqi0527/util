package com.egls.server.utils.file.loader;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.egls.server.utils.file.FileType;

/**
 * @author mayer - [Created on 2018-09-03 20:06]
 */
class ReputationConfig extends BaseDirectoryLoader<Reputation> {

    static final String PATH = "./src/test/java/com/egls/server/utils/file/loader/";

    static volatile Map<String, Reputation> reputationMap = new HashMap<>();

    public ReputationConfig() {
        super(FileType.XML);
    }

    @Override
    protected Reputation loadFile(File file) {
        Reputation reputation = new Reputation();
        reputation.load(file);
        return reputation;
    }

    @Override
    public void loadDirectory() {
        List<Reputation> reputations = loadDirectory(new File(PATH));
        Map<String, Reputation> tempReputationMap = new HashMap<>();
        reputations.forEach(reputation -> tempReputationMap.put(reputation.id, reputation));
        reputationMap = tempReputationMap;
    }

}
