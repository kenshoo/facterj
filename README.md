facterj
=======

Utility to generate [facter](http://puppetlabs.com/puppet/related-projects/facter/) facts from Java application.
Gets a map of facts and generates a json file located under "/etc/facter/facts.d".

## Usage
```
HashMap<String, String> props = new HashMap<String, String>();
props.put("Dog", "Labrador");
props.put("Cat", "Lion");
File jsonFile = new FactsToJsonFile(props, "properties");
```
A file named **/etc/facter/facts.d/properties.json** will be created with the following contents:
```
{"Cat":"Lion","Dog":"Labrador"}
```

If some of your properties shouldn't be exposed, you can pass a set of properties to **obfuscate**:
```
HashMap<String, String> props = new HashMap<String, String>();
props.put("Secret Fish", "Jawless");
props.put("Monkey", "Gorilla");
props.put("Snake", "Mamba");
File jsonFile = new FactsToJsonFile(props, "properties", Collections.singleton("Secret Fish"));
```
Then, the value of "Secret Fish" will be obfuscated:
```
{"Snake":"Mamba","Secret Fish":"****","Monkey":"Gorilla"}
```

## License
This code is released under the Apache Public License 2.0.
