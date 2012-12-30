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
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.*;

public class FactsToJsonFileTest {

    public static final File FACTS_LOCATION = new File("./facter/facter.d");
    public static final String FACTS_JSON_FILE_NAME = "myProps";
    FactsToJsonFile factsToJsonFile;
    @Test
    public void writeFactsFile() throws Exception {
        HashMap<String, String> props = new HashMap<String, String>();
        props.put("Dog", "Labrador");
        props.put("Cat", "Lion");

        factsToJsonFile = prepareMock(props,null);

        String jsonFacts = FileUtils.readFileToString(factsToJsonFile.toJsonFileFromMapFacts(props, FACTS_JSON_FILE_NAME,null));
        HashMap<String, String> factsFromFile = new Gson().fromJson(jsonFacts, HashMap.class);

        Assert.assertEquals("Number of facts got from file is wrong", factsFromFile.size(), 2);
        Assert.assertEquals("Fact is different", factsFromFile.get("Dog"), "Labrador");
        Assert.assertEquals("Fact is different", factsFromFile.get("Cat"), "Lion");
        verify(factsToJsonFile, times(1)).getExternalFactsFolder();
        verify(factsToJsonFile, times(1)).toJsonFileFromMapFacts(same(props), same(FACTS_JSON_FILE_NAME),isNull(Set.class));
    }

    @Test
    public void writeFactsFileAndOverrideIt() throws Exception {
        HashMap<String, String> props = new HashMap<String, String>();
        props.put("Dog", "Labrador");
        props.put("Cat", "Lion");

        Set<String> obfuscateEntities = new HashSet<String>();
        obfuscateEntities.add("Fish");
        factsToJsonFile = prepareMock(props, obfuscateEntities);
        factsToJsonFile.toJsonFileFromMapFacts(props, FACTS_JSON_FILE_NAME,obfuscateEntities);

        props.clear();
        props.put("Fish", "Jawless");
        props.put("Monkey", "Gorilla");
        props.put("Snake", "Mamba");

        File factsFile = factsToJsonFile.toJsonFileFromMapFacts(props, FACTS_JSON_FILE_NAME,obfuscateEntities);
        String jsonFacts = FileUtils.readFileToString(factsFile);
        HashMap<String, String> factsFromFile = new Gson().fromJson(jsonFacts, HashMap.class);

        Assert.assertEquals("Facts file is not same as expected", factsFile, new File(FACTS_LOCATION, FACTS_JSON_FILE_NAME + FactsToJsonFile.JSON_FILE_EXTENSION));
        Assert.assertEquals("Number of facts got from file is wrong", factsFromFile.size(), 3);
        Assert.assertEquals("Number of facts got from file is wrong", factsFromFile.size(), 3);
        Assert.assertEquals("Fact is different", factsFromFile.get("Monkey"), "Gorilla");
        Assert.assertEquals("Fact is different", factsFromFile.get("Fish"), FactsToJsonFile.OBFUSCATE_VALUE);
        Assert.assertEquals("Fact is different", factsFromFile.get("Snake"), "Mamba");
        verify(factsToJsonFile, times(2)).getExternalFactsFolder();
        verify(factsToJsonFile, times(2)).toJsonFileFromMapFacts(any(HashMap.class), same(FACTS_JSON_FILE_NAME),same(obfuscateEntities));
    }


    private FactsToJsonFile prepareMock(HashMap<String, String> props, Set<String> obfuscateEntities) {
        FactsToJsonFile factsToJsonFile = Mockito.mock(FactsToJsonFile.class);
        Mockito.when(factsToJsonFile.getExternalFactsFolder()).thenReturn(FACTS_LOCATION);
        Mockito.when(factsToJsonFile.toJsonFileFromMapFacts(props, FACTS_JSON_FILE_NAME,obfuscateEntities)).thenCallRealMethod();
        return factsToJsonFile;
    }


    @After
    public void afterEachTest() {
        verifyNoMoreInteractions(factsToJsonFile);
        FileUtils.deleteQuietly(FACTS_LOCATION);
    }


    @Before
    public void beforeEachTest() {
        FACTS_LOCATION.mkdirs();
    }

}
