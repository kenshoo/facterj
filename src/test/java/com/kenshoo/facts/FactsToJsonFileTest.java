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
import junit.framework.Assert;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.File;
import java.util.HashMap;


public class FactsToJsonFileTest {

    public static final File FACTS_LOCATION = new File(".");
    public static final File ALTERNATIVE_LOCATION = new File("./alternative");
    public static final File MAIN_LOCATION = new File("./main");
    public static final String PROPS_FILE_NAME = "myProps";

    @Test
    public void writeFactsFile() throws Exception {
        HashMap<String, String> props = new HashMap<String, String>();
        props.put("Bar", "Refaeli");
        props.put("Hanin", "Zuabi");

        FactsToJsonFile factsToJsonFile = new FactsToJsonFile();

        String jsonFacts = FileUtils.readFileToString(factsToJsonFile.toJsonFileFacts(props, PROPS_FILE_NAME, FACTS_LOCATION));
        HashMap<String, String> factsFromFile = new Gson().fromJson(jsonFacts, HashMap.class);

        Assert.assertEquals("Number of facts got from file is wrong", factsFromFile.size(), 2);
        Assert.assertEquals("Fact is different", factsFromFile.get("Bar"), "Refaeli");
        Assert.assertEquals("Fact is different", factsFromFile.get("Hanin"), "Zuabi");
    }

    @Test
    public void writeFactsFileAndOverideIt() throws Exception {
        HashMap<String, String> props = new HashMap<String, String>();
        props.put("Bar", "Refaeli");
        props.put("Hanin", "Zuabi");
        FactsToJsonFile factsToJsonFile = new FactsToJsonFile();
        factsToJsonFile.toJsonFileFacts(props, PROPS_FILE_NAME, FACTS_LOCATION);

        props.clear();
        props.put("Catherine", "McNeil");
        props.put("Iris", "Strubegger");
        props.put("Karen", "Elson");

        String jsonFacts = FileUtils.readFileToString(factsToJsonFile.toJsonFileFacts(props, PROPS_FILE_NAME, FACTS_LOCATION));
        HashMap<String, String> factsFromFile = new Gson().fromJson(jsonFacts, HashMap.class);

        Assert.assertEquals("Number of facts got from file is wrong", factsFromFile.size(), 3);
        Assert.assertEquals("Fact is different", factsFromFile.get("Iris"), "Strubegger");
        Assert.assertEquals("Fact is different", factsFromFile.get("Catherine"), "McNeil");
        Assert.assertEquals("Fact is different", factsFromFile.get("Karen"), "Elson");
    }

    @Test
    public void writeFactsFileToMainLocation() throws Exception {
        MAIN_LOCATION.mkdirs();
        HashMap<String, String> props = new HashMap<String, String>();
        props.put("Bar", "Refaeli");
        props.put("Hanin", "Zuabi");

        FactsToJsonFile factsToJsonFile = prepareMock(props);

        File factsFile = factsToJsonFile.toJsonFileFacts(props, PROPS_FILE_NAME, null);
        String jsonFacts = FileUtils.readFileToString(factsFile);
        HashMap<String, String> factsFromFile = new Gson().fromJson(jsonFacts, HashMap.class);

        Assert.assertEquals(factsFile.getParentFile().getAbsoluteFile(), MAIN_LOCATION.getAbsoluteFile());
        Assert.assertEquals("Number of facts got from file is wrong", factsFromFile.size(), 2);
        Assert.assertEquals("Fact is different", factsFromFile.get("Bar"), "Refaeli");
        Assert.assertEquals("Fact is different", factsFromFile.get("Hanin"), "Zuabi");

    }

    private FactsToJsonFile prepareMock(HashMap<String, String> props) {
        FactsToJsonFile factsToJsonFile = Mockito.mock(FactsToJsonFile.class);
        Mockito.when(factsToJsonFile.getMainFacterFolder()).thenReturn(MAIN_LOCATION.getPath());
        Mockito.when(factsToJsonFile.getAlternativeFactorFolder()).thenReturn(ALTERNATIVE_LOCATION.getPath());
        Mockito.when(factsToJsonFile.toJsonFileFacts(props, PROPS_FILE_NAME, null)).thenCallRealMethod();
        return factsToJsonFile;
    }

    @Test
    public void writeFactsFileToAlternativeLocation() throws Exception {
        ALTERNATIVE_LOCATION.mkdirs();
        HashMap<String, String> props = new HashMap<String, String>();
        props.put("Bar", "Refaeli");
        props.put("Hanin", "Zuabi");

        FactsToJsonFile factsToJsonFile = prepareMock(props);

        File factsFile = factsToJsonFile.toJsonFileFacts(props, PROPS_FILE_NAME, null);
        String jsonFacts = FileUtils.readFileToString(factsFile);
        HashMap<String, String> factsFromFile = new Gson().fromJson(jsonFacts, HashMap.class);

        Assert.assertEquals(factsFile.getParentFile().getAbsoluteFile(), ALTERNATIVE_LOCATION.getAbsoluteFile());
        Assert.assertEquals("Number of facts got from file is wrong", factsFromFile.size(), 2);
        Assert.assertEquals("Fact is different", factsFromFile.get("Bar"), "Refaeli");
        Assert.assertEquals("Fact is different", factsFromFile.get("Hanin"), "Zuabi");

    }

    @After
    public void afterEachTest() {
        FileUtils.deleteQuietly(new File("." + File.separator + PROPS_FILE_NAME + FactsToJsonFile.JSON_FILE_EXTENSION));
        FileUtils.deleteQuietly(MAIN_LOCATION);
        FileUtils.deleteQuietly(ALTERNATIVE_LOCATION);
    }


}
