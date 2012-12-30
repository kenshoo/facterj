/*
* Copyright 2012 Kenshoo.com
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package com.kenshoo.facts;


import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class FactsToJsonFile {
    private static final String FACTER_EXTERNAL_FACTS_FOLDER = "/etc/facter/facts.d";  // /etc/puppetlabs/facter/facts.d
    public static final String JSON_FILE_EXTENSION = ".json";
    public static final String OBFUSCATE_VALUE = "****";
    private Logger logger = LoggerFactory.getLogger(FactsToJsonFile.class);

    public File getExternalFactsFolder() {
        File externalFactsFolder = new File(FACTER_EXTERNAL_FACTS_FOLDER);
        if (!externalFactsFolder.exists()) {
            externalFactsFolder.mkdirs();
        }
        return externalFactsFolder;
    }

    public File toJsonFileFromMapFacts(Map facts, String fileName, Set<String> obfuscateEntities) {
        File factsFile = getFactsFile(fileName);
        obfuscateEntities(obfuscateEntities,facts);
        String factsJson = new Gson().toJson(facts);

        FileWriter writer = null;
        try {
            writer = new FileWriter(factsFile);
            writer.write(factsJson);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
        return factsFile;
    }

    private File getFactsFile(String fileName) {
        return new File(getExternalFactsFolder(), fileName + JSON_FILE_EXTENSION);
    }

    private  void obfuscateEntities(Set<String> obfuscateEntities,Map<String,String> facts){
        if(obfuscateEntities!=null){
            for(String key:obfuscateEntities){
                if(facts.containsKey(key)){
                    facts.put(key, OBFUSCATE_VALUE);
                }
            }
        }
    }
}

