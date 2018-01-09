# Podcast Parser

[![Maven Central badge](https://maven-badges.herokuapp.com/maven-central/be.ceau/podcast-feed-parser/badge.svg)](https://mvnrepository.com/artifact/be.ceau/podcast-feed-parser)  
[![Javadocs](https://javadoc.io/badge/be.ceau/podcast-feed-parser.svg)](https://javadoc.io/doc/be.ceau/podcast-feed-parser)  
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0.txt)

Java library for parsing podcast feed XML files.

### Usage

```Java
Feed feed = new PodcastParser().parse(xml);
```

### Requirements
This library requires Java 8 or higher.

### Maven Central
Include this project directly from Maven Central
```XML
<dependency>
	<groupId>be.ceau</groupId>
	<artifactId>podcast-feed-parser</artifactId>
	<version>${podcast-feed-parser.version}</version>
</dependency>
```

### GnuPG public key
Verify signature files with my [GnuPG public key](https://www.ceau.be/pubkey.gpg).

### License
Licensed under [the Apache 2.0 license](http://www.apache.org/licenses/LICENSE-2.0.txt).