import java.util.regex.Pattern;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Additional tests for HumanPlayer command parsing (R2.1)
 * Tests regex pattern matching for all human commands
 *
 * @author Krisha Patel
 */
public class ParserLogicTest {


	/**
     * Test roll command regex
     */
    @Test
    public void testRollRegex() {
        String pattern = "^(?i)roll$";

        // Valid inputs
        assertTrue(Pattern.matches(pattern, "roll"));
        assertTrue(Pattern.matches(pattern, "ROLL"));
        assertTrue(Pattern.matches(pattern, "Roll"));
        assertTrue(Pattern.matches(pattern, "rOlL"));

        // Invalid inputs
        assertFalse(Pattern.matches(pattern, "rolls"));
        assertFalse(Pattern.matches(pattern, "rol"));
        assertFalse(Pattern.matches(pattern, " roll "));
        assertFalse(Pattern.matches(pattern, "abc"));
        assertFalse(Pattern.matches(pattern, "roll now"));
    }

    /**
     * Test go command regex
     */
    @Test
    public void testGoRegex() {
        String pattern = "^(?i)go$";

        // Valid inputs
        assertTrue(Pattern.matches(pattern, "go"));
        assertTrue(Pattern.matches(pattern, "GO"));
        assertTrue(Pattern.matches(pattern, "Go"));
        assertTrue(Pattern.matches(pattern, "gO"));

        // Invalid inputs
        assertFalse(Pattern.matches(pattern, "gos"));
        assertFalse(Pattern.matches(pattern, "go "));
        assertFalse(Pattern.matches(pattern, " go "));
        assertFalse(Pattern.matches(pattern, "abc"));
        assertFalse(Pattern.matches(pattern, "go now"));
    }

    /**
     * Test list command regex
     */
    @Test
    public void testListRegex() {
        String pattern = "^(?i)list$";

        // Valid inputs
        assertTrue(Pattern.matches(pattern, "list"));
        assertTrue(Pattern.matches(pattern, "LIST"));
        assertTrue(Pattern.matches(pattern, "List"));
        assertTrue(Pattern.matches(pattern, "lIsT"));

        // Invalid inputs
        assertFalse(Pattern.matches(pattern, "lists"));
        assertFalse(Pattern.matches(pattern, "list "));
        assertFalse(Pattern.matches(pattern, " list "));
        assertFalse(Pattern.matches(pattern, "abc"));
        assertFalse(Pattern.matches(pattern, "list now"));
    }

    /**
     * Test build settlement command regex
     */
    @Test
    public void testBuildSettlementRegex() {
        String pattern = "^(?i)build settlement \\d+$";

        // Valid inputs
        assertTrue(Pattern.matches(pattern, "build settlement 5"));
        assertTrue(Pattern.matches(pattern, "BUILD SETTLEMENT 12"));
        assertTrue(Pattern.matches(pattern, "Build Settlement 7"));
        assertTrue(Pattern.matches(pattern, "bUiLd sEtTlEmEnT 3"));
        assertTrue(Pattern.matches(pattern, "build settlement 100"));

        // Invalid inputs
        assertFalse(Pattern.matches(pattern, "build settlement"));
        assertFalse(Pattern.matches(pattern, "build settlement x"));
        assertFalse(Pattern.matches(pattern, "build settlement 5 extra"));
        assertFalse(Pattern.matches(pattern, "buildsettlement 5"));
        assertFalse(Pattern.matches(pattern, "build settlement -3"));
        assertFalse(Pattern.matches(pattern, "build settlement 5.5"));
    }

    /**
     * Test build city command regex
     */
    @Test
    public void testBuildCityRegex() {
        String pattern = "^(?i)build city \\d+$";

        // Valid inputs
        assertTrue(Pattern.matches(pattern, "build city 3"));
        assertTrue(Pattern.matches(pattern, "BUILD CITY 8"));
        assertTrue(Pattern.matches(pattern, "Build City 15"));
        assertTrue(Pattern.matches(pattern, "bUiLd CiTy 22"));

        // Invalid inputs
        assertFalse(Pattern.matches(pattern, "build city"));
        assertFalse(Pattern.matches(pattern, "build city x"));
        assertFalse(Pattern.matches(pattern, "build city 5 extra"));
        assertFalse(Pattern.matches(pattern, "buildcity 5"));
        assertFalse(Pattern.matches(pattern, "build city -3"));
        assertFalse(Pattern.matches(pattern, "build city 5.5"));
    }

    /**
     * Test build road command regex
     */
    @Test
    public void testBuildRoadRegex() {
        String pattern = "^(?i)build road \\d+,\\d+$";

        // Valid inputs
        assertTrue(Pattern.matches(pattern, "build road 2,4"));
        assertTrue(Pattern.matches(pattern, "BUILD ROAD 7,9"));
        assertTrue(Pattern.matches(pattern, "Build Road 12,15"));
        assertTrue(Pattern.matches(pattern, "bUiLd RoAd 3,8"));
        assertTrue(Pattern.matches(pattern, "build road 100,200"));

        // Invalid inputs
        assertFalse(Pattern.matches(pattern, "build road 2"));
        assertFalse(Pattern.matches(pattern, "build road 2,4,6"));
        assertFalse(Pattern.matches(pattern, "build road a,b"));
        assertFalse(Pattern.matches(pattern, "build road 2, x"));
        assertFalse(Pattern.matches(pattern, "build road ,4"));
        assertFalse(Pattern.matches(pattern, "build road"));
    }

}