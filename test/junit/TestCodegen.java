import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.Assert.fail;


public class TestCodegen {

    public static final String TEST_FILES_LOCATION = "test/resources/Codegen/";
    public static final String SAMPLE_TEST_FILES_LOCATION = "SamplePrograms/SampleMiniJavaPrograms/";
    public static final String TEST_FILES_INPUT_EXTENSION = ".java";
    public static final String TEST_FILES_EXPECTED_EXTENSION = ".expected";
    public static final String TEST_FILES_ERROR_EXTENSION = ".stderr";

    /*
        You may be able to reuse this private helper method for your own
        testing of the MiniJava scanner.
    */
    private void executeCodegenTestCase(String testCaseName) {
        try {
            new MiniJavaTestBuilder()
                .testCompiledProgramOutputMatchesJava(
                    Path.of(TEST_FILES_LOCATION, testCaseName + TEST_FILES_INPUT_EXTENSION)
                );
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    private void executeSampleProgramTestCase(String testCaseName) {
        try {
            new MiniJavaTestBuilder()
                    .testCompiledProgramOutputMatchesJava(
                            Path.of(SAMPLE_TEST_FILES_LOCATION, testCaseName + TEST_FILES_INPUT_EXTENSION)
                    );
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

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

    @Test
    public void testSimple() {
        executeCodegenTestCase("Simple");
        runCodegenTestCase("Simple");
    }

    @Test
    public void testBlock() {
        executeCodegenTestCase("Block");
        runCodegenTestCase("Block");
    }

    @Test
    public void testShortCircuit() {
        executeCodegenTestCase("ShortCircuit");
    }

    @Test
    public void testAssignment() {
        executeCodegenTestCase("Assignment");
    }

    @Test
    public void testInheritance() {
        executeCodegenTestCase("Inheritance");
    }

    @Test
    public void testConditionals() {
        executeCodegenTestCase("Conditionals");
    }

    @Test
    public void testArrayLength() {
        executeCodegenTestCase("ArrayLength");
    }

    @Test
    public void testComplex() {
        executeCodegenTestCase("Complex");
    }

    @Test
    public void testLogicOperators() {
        executeCodegenTestCase("LogicOperators");
    }

    @Test
    public void testArray() {
        executeCodegenTestCase("Array");
    }

    @Test
    public void testBinarySearch() {
        executeSampleProgramTestCase("BinarySearch");
    }

    @Test
    public void testBinaryTree() {
        executeSampleProgramTestCase("BinaryTree");
    }

    @Test
    public void testBubbleSort() {
        executeSampleProgramTestCase("BubbleSort");
    }

    @Test
    public void testFactorial() {
        executeSampleProgramTestCase("Factorial");
    }

    @Test
    public void testLinearSearch() {
        executeSampleProgramTestCase("LinearSearch");
    }

    @Test
    public void testLinkedList() {
        executeSampleProgramTestCase("LinkedList");
    }

    @Test
    public void testQuickSort() {
        executeSampleProgramTestCase("QuickSort");
    }

    @Test
    public void testTreeVisitor() {
        executeSampleProgramTestCase("TreeVisitor");
    }

    @Test
    public void testParen() {
        executeCodegenTestCase("Paren");
    }
}