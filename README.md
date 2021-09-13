# spock-embedded-server
Spock extension for running an embedded server per spec or feature.

## Quickstart
```xml
<dependency>
    <groupId>com.github.fairdevkit</groupId>
    <artifactId>spock-embedded-server-core</artifactId>
    <version>0.1.0-SNAPSHOT</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>com.github.fairdevkit</groupId>
    <artifactId>spock-embedded-server-httpserver</artifactId>
    <version>0.1.0-SNAPSHOT</version>
    <scope>test</scope>
</dependency>
```

```groovy
class ExampleSpec extends Specification {
    @EmbedResource(resource = "/example.txt", path = "/example/")
    def "resolve the content of the embedded resource"(port) {
        expect:
        new URL("http://localhost:$port/example/").text == "hello world"
    }
}
```

```groovy
// src/test/resources/SpockConfig.groovy
embedded_server {
    port 50213
}
```

## License
This project is licensed under the [MIT License](LICENSE).
