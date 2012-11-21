package com.kenshoo.facts;


import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;


public class FactsToJsonFile {
    private static final String MAIN_FACTER_FOLDER = "/etc/facter/facts.d";  // /etc/puppetlabs/facter/facts.d
    private static final String ALTERNATIVE_FACTOR_FOLDER = "/etc/facts.d/";
    public static final String FACTS_FILE_NAME = "kenshoo.properties.json";
    private Logger logger = LoggerFactory.getLogger(FactsToJsonFile.class);

    public String getMainFacterFolder() {
        return MAIN_FACTER_FOLDER;
    }

    public String getAlternativeFactorFolder() {
        return ALTERNATIVE_FACTOR_FOLDER;
    }


    public File toFactsJsonFile(Map facts, File jsonFileFolder) {
        File factsFile = getFactsFile(jsonFileFolder);
        if (factsFile == null) {
            factsFile = getDefaultFactsFile();
        }

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

    private File getDefaultFactsFile() {
        File mainFileFolder = new File(getMainFacterFolder());
        File alternativeFileFolder = new File(getAlternativeFactorFolder());
        File factsFile = getFactsFile(mainFileFolder);
        if (factsFile == null) {
            factsFile = getFactsFile(alternativeFileFolder);
        }

        if (factsFile == null) {
            throw new RuntimeException("None of the facts folders exists");
        }
        return factsFile;
    }

    private File getFactsFile(File targetFolder) {
        File factsFile = null;
        if (targetFolder != null && targetFolder.exists()) {
            factsFile = new File(targetFolder.getAbsoluteFile() + File.separator + FACTS_FILE_NAME);
        }
        return factsFile;
    }
}

