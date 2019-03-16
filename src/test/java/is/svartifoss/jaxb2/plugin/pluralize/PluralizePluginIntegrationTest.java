package is.svartifoss.jaxb2.plugin.pluralize;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.utils.CodeGenerationUtils;
import com.github.javaparser.utils.SourceRoot;
import com.sun.tools.xjc.Driver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

import static org.assertj.core.api.Assertions.assertThat;

public class PluralizePluginIntegrationTest {

    private Path directory;

    private ByteArrayOutputStream statusByteArrayOutputStream;

    private ByteArrayOutputStream outByteArrayOutputStream;

    @BeforeEach
    public void setUp() throws IOException {
        directory = Paths.get("target", "dummy");
        if (!Files.exists(directory)) {
            Files.createDirectory(directory);
        }
        statusByteArrayOutputStream = new ByteArrayOutputStream();
        outByteArrayOutputStream = new ByteArrayOutputStream();
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
        runXjc("TestTypeWithACollectionOfElements");
        final Path path = Paths.get("target", "dummy", "generated", "TestType.java");
        final SourceRoot sourceRoot = new SourceRoot(CodeGenerationUtils.mavenModuleRoot(PluralizePlugin.class)
                                                                        .resolve("target/dummy/generated"));
        final CompilationUnit compilationUnit = sourceRoot.parse("", "TestType.java");
        assertThat(compilationUnit.getTypes()).hasSize(1);
        assertThat(compilationUnit.getType(0)).isInstanceOf(ClassOrInterfaceDeclaration.class);
        assertThat(((ClassOrInterfaceDeclaration) compilationUnit.getType(0)).isInterface()).isFalse();
        assertThat(compilationUnit.getType(0).getFields()).hasSize(1);
        assertThat(compilationUnit.getType(0).getFields().get(0).getVariables()).hasSize(1);
        assertThat(compilationUnit.getType(0)
                                  .getFields()
                                  .get(0)
                                  .getVariables()
                                  .get(0)
                                  .getName()
                                  .getIdentifier()).isEqualTo("elements");
        assertThat(compilationUnit.getType(0).getMethods()).hasSize(1);
        assertThat(compilationUnit.getType(0).getMethods().get(0).getName().getIdentifier()).isEqualTo("getElements");
    }

    private void runXjc(final String name) throws Exception {
        final String[] args = {"-classpath", "target/xjc-pluralize-1.0-SNAPSHOT.jar", "-Xpluralize", "-d", directory.toString(), "src/test/resources/" + name + ".xsd"};
        Driver.run(args, new PrintStream(statusByteArrayOutputStream), new PrintStream(outByteArrayOutputStream));
    }
}
