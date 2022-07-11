#

## Build
Build the tool by running `mvn package`. </br>
Executable to use will be located in `target` dir or in local maven repository. Artifact name is `csv-processor-1.0-SNAPSHOT-jar-with-dependencies.jar`

## Usage
`-s` `--source` - path to input DSV file.
`-t` `--target` - path for output directory. Will be created if not existing.
`-d` `--delimiter` - delimiter to be used.

`java -jar .\target\csv-processor-1.0-SNAPSHOT-jar-with-dependencies.jar --source '.\src\test\resources\DSV input 1.txt' --delimiter ','`


