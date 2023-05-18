import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.Assert.fail;


public class TestSemantics {

    public static final String TEST_FILES_LOCATION = "test/resources/Semantics/";
    public static final String TEST_FILES_INPUT_EXTENSION = ".java";
    public static final String TEST_FILES_EXPECTED_EXTENSION = ".expected";
    public static final String TEST_FILES_ERROR_EXTENSION = ".stderr";

    /*
        You may be able to reuse this private helper method for your own
        testing of the MiniJava scanner.
    */
    private void runSemanticsTestCase(String testCaseName) {
        try {
            new MiniJavaTestBuilder()
                    .assertSystemOutMatchesContentsOf(
                            Path.of(TEST_FILES_LOCATION,
                                    testCaseName + TEST_FILES_EXPECTED_EXTENSION))
                    .assertExitSuccess()
                    .assertSystemErrIsEmpty()
                    .testCompiler("-T", TEST_FILES_LOCATION + testCaseName + TEST_FILES_INPUT_EXTENSION);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    private void runSemanticsTestCaseWithErr(String testCaseName) {
        try {
            new MiniJavaTestBuilder()
                    .assertSystemOutMatchesContentsOf(
                            Path.of(TEST_FILES_LOCATION,
                                    testCaseName + TEST_FILES_EXPECTED_EXTENSION))
                    .assertSystemErrMatchesContentsOf(
                            Path.of(TEST_FILES_LOCATION,
                                    testCaseName + TEST_FILES_ERROR_EXTENSION))
                    .assertExitFailure()
                    .testCompiler("-T", TEST_FILES_LOCATION + testCaseName + TEST_FILES_INPUT_EXTENSION);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testSimple() {
        runSemanticsTestCase("Simple");
    }

    @Test
    public void testInherit() {
        runSemanticsTestCaseWithErr("Inherit");
    }

    @Test
    public void testTypeComparison() {
        runSemanticsTestCaseWithErr("TypeComparison");
    }

    @Test
    public void testScope() {
        runSemanticsTestCaseWithErr("Scope");
    }

    @Test
    public void testLogic() {
        runSemanticsTestCaseWithErr("Logic");
    }

    @Test
    public void testOperators() {
        runSemanticsTestCase("Operators");
    }
}