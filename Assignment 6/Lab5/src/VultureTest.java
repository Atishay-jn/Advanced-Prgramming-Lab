import org.junit.Test;
import static org.junit.Assert.*;

public class VultureTest {
    private static final int TIMEOUT = 2000;
    @Test(expected = VultureBiteException.class, timeout = TIMEOUT)
    public void TestVultureShake1() {
        Vulture vulture = new Vulture(5);
        vulture.shake();
    }
    @Test(expected = VultureBiteException.class, timeout = TIMEOUT)
    public void TestVultureShake2() {
        Vulture vulture = new Vulture(5);
        vulture.shake();
    }
    @Test(expected = VultureBiteException.class, timeout = TIMEOUT)
    public void TestVultureShake3() {
        Vulture vulture = new Vulture(5);
        vulture.shake();
    }
}
