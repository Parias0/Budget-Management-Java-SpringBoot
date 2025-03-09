import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SimpleTest {

    @Test
    public void testSimpleAddition() {
        int result = 2 + 2;
        System.out.println("Test result: " + result);  // Logowanie wyniku w konsoli
        assertEquals(4, result, "2 + 2 should equal 4");
    }
}
