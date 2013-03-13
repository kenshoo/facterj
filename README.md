facterj
=======

Utility to generate [facter](http://puppetlabs.com/puppet/related-projects/facter/) facts from Java application.
Gets a map of facts and generates a json file located under "/etc/facter/facts.d".

## Usage
new FactsToJsonFile(map, jsonFileNmae, setOfFactsToObfuscate)
returns a new File "/etc/facter/facts.d/<jsonFileNmae>.json" (always override existing)

## License
This code is released under the Apache Public License 2.0.
