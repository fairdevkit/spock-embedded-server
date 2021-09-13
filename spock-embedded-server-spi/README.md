# spock-embedded-server-spi
SPI plugin mechanism for the [spock-embedded-server](..) extension.

## Quickstart
- Implement the `EmbeddedServer` interface.
- Create the directory structure `src/main/resources/META-INF/services`.
  - Create a file named `com.github.fairdevkit.spock.extension.server.spi.EmbeddedServer` in the newly created directory.
  - Within the newly created file, put the fully qualified classname of the `EmbeddedServer` implementation.
