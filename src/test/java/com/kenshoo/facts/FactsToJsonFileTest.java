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

    @Test
    public void writeFactsFile() throws Exception {
        HashMap<String, String> props = new HashMap<String, String>();
        props.put("Alon", "Levi");
        props.put("Tzach", "Zohar");

        FactsToJsonFile factsToJsonFile = new FactsToJsonFile();

        String jsonFacts = FileUtils.readFileToString(factsToJsonFile.toFactsJsonFile(props, FACTS_LOCATION));
        HashMap<String, String> factsFromFile = new Gson().fromJson(jsonFacts, HashMap.class);

        Assert.assertEquals("Number of facts got from file is wrong", factsFromFile.size(), 2);
        Assert.assertEquals("Fact is different", factsFromFile.get("Alon"), "Levi");
        Assert.assertEquals("Fact is different", factsFromFile.get("Tzach"), "Zohar");
    }

    @Test
    public void writeFactsFileAndOverideIt() throws Exception {
        HashMap<String, String> props = new HashMap<String, String>();
        props.put("Alon", "Levi");
        props.put("Tzach", "Zohar");
        FactsToJsonFile factsToJsonFile = new FactsToJsonFile();
        factsToJsonFile.toFactsJsonFile(props, FACTS_LOCATION);

        props.clear();
        props.put("Tal", "Salmona");
        props.put("Matti", "Bar-Zeev");
        props.put("Levi", "Alon");

        String jsonFacts = FileUtils.readFileToString(factsToJsonFile.toFactsJsonFile(props, FACTS_LOCATION));
        HashMap<String, String> factsFromFile = new Gson().fromJson(jsonFacts, HashMap.class);

        Assert.assertEquals("Number of facts got from file is wrong", factsFromFile.size(), 3);
        Assert.assertEquals("Fact is different", factsFromFile.get("Levi"), "Alon");
        Assert.assertEquals("Fact is different", factsFromFile.get("Matti"), "Bar-Zeev");
        Assert.assertEquals("Fact is different", factsFromFile.get("Tal"), "Salmona");
    }

    @Test
    public void writeFactsFileToMainLocation() throws Exception {
        MAIN_LOCATION.mkdirs();
        HashMap<String, String> props = new HashMap<String, String>();
        props.put("Alon", "Levi");
        props.put("Tzach", "Zohar");

        FactsToJsonFile factsToJsonFile = prepareMock(props);

        File factsFile = factsToJsonFile.toFactsJsonFile(props, null);
        String jsonFacts = FileUtils.readFileToString(factsFile);
        HashMap<String, String> factsFromFile = new Gson().fromJson(jsonFacts, HashMap.class);

        Assert.assertEquals(factsFile.getParentFile().getAbsoluteFile(), MAIN_LOCATION.getAbsoluteFile());
        Assert.assertEquals("Number of facts got from file is wrong", factsFromFile.size(), 2);
        Assert.assertEquals("Fact is different", factsFromFile.get("Alon"), "Levi");
        Assert.assertEquals("Fact is different", factsFromFile.get("Tzach"), "Zohar");

    }

    private FactsToJsonFile prepareMock(HashMap<String, String> props) {
        FactsToJsonFile factsToJsonFile = Mockito.mock(FactsToJsonFile.class);
        Mockito.when(factsToJsonFile.getMainFacterFolder()).thenReturn(MAIN_LOCATION.getPath());
        Mockito.when(factsToJsonFile.getAlternativeFactorFolder()).thenReturn(ALTERNATIVE_LOCATION.getPath());
        Mockito.when(factsToJsonFile.toFactsJsonFile(props, null)).thenCallRealMethod();
        return factsToJsonFile;
    }

    @Test
    public void writeFactsFileToAlternativeLocation() throws Exception {
        ALTERNATIVE_LOCATION.mkdirs();
        HashMap<String, String> props = new HashMap<String, String>();
        props.put("Alon", "Levi");
        props.put("Tzach", "Zohar");

        FactsToJsonFile factsToJsonFile = prepareMock(props);

        File factsFile = factsToJsonFile.toFactsJsonFile(props, null);
        String jsonFacts = FileUtils.readFileToString(factsFile);
        HashMap<String, String> factsFromFile = new Gson().fromJson(jsonFacts, HashMap.class);

        Assert.assertEquals(factsFile.getParentFile().getAbsoluteFile(), ALTERNATIVE_LOCATION.getAbsoluteFile());
        Assert.assertEquals("Number of facts got from file is wrong", factsFromFile.size(), 2);
        Assert.assertEquals("Fact is different", factsFromFile.get("Alon"), "Levi");
        Assert.assertEquals("Fact is different", factsFromFile.get("Tzach"), "Zohar");

    }

    @After
    public void afterEachTest() {
        FileUtils.deleteQuietly(new File("." + File.separator + FactsToJsonFile.FACTS_FILE_NAME));
        FileUtils.deleteQuietly(MAIN_LOCATION);
        FileUtils.deleteQuietly(ALTERNATIVE_LOCATION);
    }


}
