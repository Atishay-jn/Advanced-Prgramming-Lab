import org.junit.Test;
import static org.junit.Assert.*;

public class GameWinnerTest {
    private static final int TIMEOUT = 2000;
    @Test(expected = GameWinnerException.class, timeout = TIMEOUT)
    public void TestGameShake1() {
        Game game = new Game(5);
    }
    @Test(expected = GameWinnerException.class, timeout = TIMEOUT)
    public void TestGameShake2() {
        Game game = new Game(10);
    }
    @Test(expected = GameWinnerException.class, timeout = TIMEOUT)
    public void TestGameShake3() {
        Game game = new Game(20);
    }
}