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
    public static final String JSON_FILE_EXTENSION = ".json";
    private Logger logger = LoggerFactory.getLogger(FactsToJsonFile.class);

    public String getMainFacterFolder() {
        return MAIN_FACTER_FOLDER;
    }

    public String getAlternativeFactorFolder() {
        return ALTERNATIVE_FACTOR_FOLDER;
    }


    public File toJsonFileFacts(Map facts, String fileName, File jsonFileFolder) {
        File factsFile = getFactsFile(jsonFileFolder, fileName);
        if (factsFile == null) {
            factsFile = getDefaultFactsFile(fileName);
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

    private File getDefaultFactsFile(String fileName) {
        File mainFileFolder = new File(getMainFacterFolder());
        File alternativeFileFolder = new File(getAlternativeFactorFolder());
        File factsFile = getFactsFile(mainFileFolder, fileName);
        if (factsFile == null) {
            factsFile = getFactsFile(alternativeFileFolder, fileName);
        }

        if (factsFile == null) {
            throw new RuntimeException("None of the facts folders exists");
        }
        return factsFile;
    }

    private File getFactsFile(File targetFolder, String fileName) {
        File factsFile = null;
        if (targetFolder != null && targetFolder.exists()) {
            factsFile = new File(targetFolder.getAbsoluteFile() + File.separator + fileName + JSON_FILE_EXTENSION);
        }
        return factsFile;
    }
}

