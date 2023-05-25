import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.Assert.fail;


public class TestCodegen {

    public static final String TEST_FILES_LOCATION = "test/resources/Codegen/";
    public static final String TEST_FILES_INPUT_EXTENSION = ".java";
    public static final String TEST_FILES_EXPECTED_EXTENSION = ".expected";
    public static final String TEST_FILES_ERROR_EXTENSION = ".stderr";

    /*
        You may be able to reuse this private helper method for your own
        testing of the MiniJava scanner.
    */
    private void runCodegenTestCase(String testCaseName) {
        try {
            new MiniJavaTestBuilder()
                    .assertSystemOutMatchesContentsOf(
                            Path.of(TEST_FILES_LOCATION,
                                    testCaseName + TEST_FILES_EXPECTED_EXTENSION))
                    .testCompiler(TEST_FILES_LOCATION + testCaseName + TEST_FILES_INPUT_EXTENSION);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    private void runCodegenTestCaseWithErr(String testCaseName) {
        try {
            new MiniJavaTestBuilder()
                    .assertSystemOutMatchesContentsOf(
                            Path.of(TEST_FILES_LOCATION,
                                    testCaseName + TEST_FILES_EXPECTED_EXTENSION))
                    .assertSystemErrMatchesContentsOf(
                            Path.of(TEST_FILES_LOCATION,
                                    testCaseName + TEST_FILES_ERROR_EXTENSION))
                    .testCompiler(TEST_FILES_LOCATION + testCaseName + TEST_FILES_INPUT_EXTENSION);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testSimple() {
        runCodegenTestCase("Simple");
    }

    @Test
    public void testBlock() {
        runCodegenTestCase("Block");
    }
}