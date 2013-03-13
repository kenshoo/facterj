facterj
=======

Utility to generate [facter](http://puppetlabs.com/puppet/related-projects/facter/) facts from Java application.
Gets a map of facts and creats from the map a json file located under "/etc/facter/facts.d".

example: new FactsToJsonFile(map,jsonFileNmae,setOfFactsToobfuscate)
result : a new file "/etc/facter/facts.d/jsonFileNmae.json" (always override existing)

## License
This code is released under the Apache Public License 2.0.
