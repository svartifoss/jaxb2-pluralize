package is.svartifoss.jaxb2.plugin.pluralize;

import com.sun.tools.xjc.Driver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PluralizePluginIntegrationTest {

    private Path directory;

    @BeforeEach
    public void setUp() throws IOException {
        directory = Paths.get("target", "dummy");
        if (!Files.exists(directory)) {
            Files.createDirectory(directory);
        }
    }

    @AfterEach
    public void tearDown() throws IOException {
        if (Files.exists(directory)) {
            Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }
            });
        }
    }

    @Test
    public void test() throws Exception {
        runXjc("Test1");
        assertTrue(Files.exists(Paths.get("target", "dummy", "generated", "TestType.java")));
    }

    private void runXjc(final String name) throws Exception {
        final String[] args = {"-classpath", "target/xjc-pluralize-1.0-SNAPSHOT.jar", "-Xpluralize", "-d", directory.toString(), "src/test/resources/" + name + ".xsd"};
        Driver.run(args, System.out, System.out);
    }
}
