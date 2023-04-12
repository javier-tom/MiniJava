import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.Assert.fail;


public class TestScanner {

    public static final String TEST_FILES_LOCATION = "test/resources/Scanner/";
    public static final String TEST_FILES_INPUT_EXTENSION = ".java";
    public static final String TEST_FILES_EXPECTED_EXTENSION = ".expected";

    /*
        You may be able to reuse this private helper method for your own
        testing of the MiniJava scanner.
    */
    private void runScannerTestCase(String testCaseName) {
        try {
            new MiniJavaTestBuilder()
                    .assertSystemOutMatchesContentsOf(
                            Path.of(TEST_FILES_LOCATION,
                                    testCaseName + TEST_FILES_EXPECTED_EXTENSION))
                    .testCompiler("-S", TEST_FILES_LOCATION + testCaseName + TEST_FILES_INPUT_EXTENSION);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testSimple() {
        runScannerTestCase("Simple");
    }

    @Test
    public void Comments() {
        runScannerTestCase("Comments");
    }
}