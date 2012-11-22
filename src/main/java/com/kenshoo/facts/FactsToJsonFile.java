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

public class FactsToJsonFile {
    private static final String FACTER_EXTERNAL_FACTS_FOLDER = "/etc/facter/facts.d";  // /etc/puppetlabs/facter/facts.d
    public static final String JSON_FILE_EXTENSION = ".json";
    private Logger logger = LoggerFactory.getLogger(FactsToJsonFile.class);

    public File getExternalFactsFolder() {
        File externalFactsFolder = new File(FACTER_EXTERNAL_FACTS_FOLDER);
        if (!externalFactsFolder.exists()) {
            externalFactsFolder.mkdirs();
        }
        return externalFactsFolder;
    }

    public File toJsonFileFromMapFacts(Map facts, String fileName) {
        File factsFile = getFactsFile(fileName);

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
}

