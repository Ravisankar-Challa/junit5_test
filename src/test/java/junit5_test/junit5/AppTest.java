package junit5_test.junit5;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assume.assumeTrue;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assumptions.assumingThat;

import java.util.EnumSet;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.EnumSource.Mode;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

public class AppTest {
	
	@BeforeAll
	static void initAll() {
		//System.out.println("Before all the tests started");
	}
	
	@AfterAll
	static void tearDownAll() {
		//System.out.println("After all the tests finished");
	}
	
	@BeforeEach
	void setup() {
		//System.out.println("Execute before each test");
	}
    
	@AfterEach
	void tearDown() {
		//System.out.println("Execute after each test");
	}
	
	@Test
	@DisplayName("Simple assert statement")
	void simpleAssertStatement1() {
		assertTrue( 2 == 1, "Two is not equal to one");
	}
	
	@Test
	@DisplayName("Assertion messages can be lazily evaluated using a lamda to avoid constructing complex messages unnecessarily.")
	void simpleAssertStatement2() {
		assertTrue( 2 == 1, () -> "Two is not equal to one");
	}
	
	@Test
	@DisplayName("Getting test related infomration from with in the test using dependency injection")
	void testMetaData(TestInfo testInfo) {
		assertThat(testInfo.getDisplayName(), equalTo("Getting test related infomration from with in the test using dependency injection"));
	}
	
	@Test
	@DisplayName("Grouped assertions test")
	void groupedAssertions() {
		App app = new App("MyApp", "v1", "Ravi");
		// In a grouped assertion all assertions are executed, and any failures will be reported together.
		assertAll(
			() -> assertThat(app.getName(), is(equalTo("Unknown Name"))),
			() -> assertThat(app.getVersion(), is(equalTo("v1"))),
			() -> assertThat(app.getAuthor(), is(equalTo("Sankar"))),
			() -> assertEquals(2, 4, "Custom error message in assertEquals")
		);
	}
	
	@Test
	@DisplayName("Exception testing")
	void testException() {
		ArithmeticException exception = assertThrows(ArithmeticException.class, () -> App.divide(1, 0));
		assertThat(exception.getMessage(), is(equalTo("/ by zero")));
	}
	
	@Test
	@DisplayName("Test Assumptions")
	void testAssumption() {
		assumeTrue(System.getProperty("os.name").startsWith("Windows"));
		assumingThat(System.getProperty("os.name").startsWith("Windows"), 
			() -> assertThat(App.divide(11, 4), is(equalTo(2))));
	}
	
	@Test
	@Disabled
	@DisplayName("Disabled test")
	void donotrun() {
		assertEquals("1", "2");
	}
	
    @DisplayName("Repeat test")
	@RepeatedTest(value = 2, name = "{displayName} {currentRepetition}/{totalRepetitions}")
    void customDisplayName(TestInfo testInfo, RepetitionInfo repetitionInfo) {
        assertEquals(testInfo.getDisplayName(), "Repeat test " + repetitionInfo.getCurrentRepetition() + "/"+ repetitionInfo.getTotalRepetitions());
    }
    
    @ParameterizedTest
    @DisplayName("Test with value source")
    @ValueSource(ints = { 1, 2, 3 })
    void testWithValueSource(int argument) {
        assertThat(App.divide(argument, argument), equalTo(1));
    }
    
    @DisplayName("Test with enum source")
    @ParameterizedTest(name = "{index} => argument : {0}")
    @EnumSource(value = TimeUnit.class, mode = Mode.EXCLUDE, names = { "DAYS", "HOURS" })
    void testWithEnumSourceExclude(TimeUnit timeUnit) {
        assertFalse(EnumSet.of(TimeUnit.DAYS, TimeUnit.HOURS).contains(timeUnit));
        assertTrue(timeUnit.name().length() > 5);
    }
    
    @ParameterizedTest
    @DisplayName("Test with method source")
    @MethodSource("stringProvider")
    void testWithSimpleMethodSource(String first, int second) {
        assertNotNull(first);
        assertTrue(second > 0, "Should be grater than zero");
    }

    static Stream<Arguments> stringProvider() {
        return Stream.of(Arguments.of("foo", 1), Arguments.of("bar", 2));
    }
    
    @DisplayName("Test with csv source")
    @ParameterizedTest(name = "{index} ==> first=''{0}'', second={1}")
    @CsvSource({ 
    	"foo, 1", 
    	"bar, 2", 
    	"'foo, bar', 3", 
    	"fb, ''", 
    	"fb, "
    })
    void testWithCsvSource(String first, String second) {
        assertNotNull(first);
        assertNotNull(second);
    }
    
    @DisplayName("Test with csv file source")
    @ParameterizedTest(name = "{index} ==> word = ''{0}'', length = {1}")
    @CsvFileSource(resources = { "/test.csv" })
    void testWithCsvFileSource(String word, Integer length) {
        assertThat(App.wordCount(word), equalTo(length));
    }
    
    @DisplayName("Test with csv file source with 3 inputs")
    @ParameterizedTest(name = "{index} ==> arg1 = {0}, arg2 = {1}, result = {2}")
    @CsvFileSource(resources = { "/division_input.csv" })
    void testWithCsvFileSource(int arg1, int  arg2, int result) {
        assertThat(App.divide(arg1, arg2), equalTo(result));
    }
}
