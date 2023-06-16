
load("@rules_jvm_external//:defs.bzl", "maven_install")

def augmentation_backend_java_deps():
    maven_install(
        artifacts = [
            # springboot
            'org.springframework.boot:spring-boot-autoconfigure:2.1.5.RELEASE', 
            'org.springframework.boot:spring-boot-starter-web:2.1.5.RELEASE',
            'org.springframework.boot:spring-boot:2.1.5.RELEASE',
            'org.springframework:spring-web:5.1.5.RELEASE',
            
            # junit
            'junit:junit:4.12',

            # mockito-core
            'org.mockito:mockito-core:3.12.4',

            # janino
            'org.codehaus.janino:janino:2.7.5',

            # slf4j-api
            'org.slf4j:slf4j-api:1.7.30',

            # stephenc
            'com.github.stephenc.findbugs:findbugs-annotations:1.0-1',

            # findbugs
            'com.google.code.findbugs:jsr305:3.0.2', # javax.annotation.Nonnull
            'com.google.code.findbugs:annotations:3.0.0', # edu.umd.cs.findbugs.annotations

            # gson
            'com.google.code.gson:gson:2.8.6',

            # guava
            'com.google.guava:guava:27.0-jre', # com.google.common.collect

            # logback
            'ch.qos.logback:logback-classic:1.2.3',
            'ch.qos.logback:logback-core:1.2.3',

            # jsch
            'com.jcraft:jsch:0.1.55',

            # jackson-dataformat-xml
            'com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.9.9',

            # sqlite-jdbc
            'org.xerial:sqlite-jdbc:3.41.2.1',

            # javax
            'javax.json:javax.json-api:1.1.4',
            'javax.activation:activation:1.1.1',
            'javax.xml.parsers:jaxp-api:1.4.5',

            # protobuf-java
            'com.google.protobuf:protobuf-java:3.9.2',

            # graphhopper
            'com.graphhopper:graphhopper-api:0.13.0',
            'com.graphhopper:graphhopper-core:0.13.0',

            # jts-core
            'org.locationtech.jts:jts-core:1.19.0',

            # xmlgraphics-commons
            'org.apache.xmlgraphics:xmlgraphics-commons:2.8',

            # commons-configuration2
            'org.apache.commons:commons-configuration2:2.9.0',

            # commons-jxpath
            'commons-jxpath:commons-jxpath:1.3',

            # commons-cli
            'commons-cli:commons-cli:1.5.0',

            # commons-math3
            'org.apache.commons:commons-math3:3.6.1',

            # commons-lang3
            'org.apache.commons:commons-lang3:3.9',

            # commons-io
            'commons-io:commons-io:2.9.0',

            # justify
            'org.leadpony.justify:justify:1.1.0',

            # icu4j
            'com.ibm.icu:icu4j:73.1',

            # johnzon-core
            'org.apache.johnzon:johnzon-core:1.1.13', # json validation

            # stax-api
            'org.codehaus.woodstox:stax2-api:4.2.1',
            'org.codehaus.woodstox:woodstox-core-asl:4.4.1',

            # hppc
            'com.carrotsearch:hppc:0.8.1',

            # java-websocket
            'org.java-websocket:Java-WebSocket:1.3.9',

            # w3c
            'org.w3c:dom:2.3.0-jaxb-1.0.6',

            # libsumo
            'org.eclipse.sumo:libsumo:1.17.0',

            # docker-java
            'com.github.docker-java:docker-java:3.2.10',
            'com.github.docker-java:docker-java-api:3.2.10',
            'com.github.docker-java:docker-java-core:3.2.10',

        ],
        repositories = [
            'https://repo1.maven.org/maven2',
            'https://mvnrepository.com/artifact',
            'https://repo.eclipse.org/content/groups/releases',
        ],
        fetch_sources = True,
    )