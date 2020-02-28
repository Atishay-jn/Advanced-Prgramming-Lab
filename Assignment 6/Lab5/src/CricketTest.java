import org.junit.Test;
import static org.junit.Assert.*;

public class CricketTest {
    private static final int TIMEOUT = 2000;
    @Test(expected = CricketBiteException.class, timeout = TIMEOUT)
    public void TestCricketShake1() {
        Cricket cricket = new Cricket(5);
        cricket.shake();
    }
    @Test(expected = CricketBiteException.class, timeout = TIMEOUT)
    public void TestCricketShake2() {
        Cricket cricket = new Cricket(10);
        cricket.shake();
    }
    @Test(expected = CricketBiteException.class, timeout = TIMEOUT)
    public void TestCricketShake3() {
        Cricket cricket = new Cricket(20);
        cricket.shake();
    }
}
