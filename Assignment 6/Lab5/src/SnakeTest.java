import org.junit.Test;
import static org.junit.Assert.*;

public class SnakeTest {
    private static final int TIMEOUT = 2000;
    @Test(expected = SnakeBiteException.class, timeout = TIMEOUT)
    public void TestSnakeShake1() {
        Snake snake = new Snake(5);
        snake.shake();
    }
    @Test(expected = SnakeBiteException.class, timeout = TIMEOUT)
    public void TestSnakeShake2() {
        Snake snake = new Snake(10);
        snake.shake();
    }
    @Test(expected = SnakeBiteException.class, timeout = TIMEOUT)
    public void TestSnakeShake3() {
        Snake snake = new Snake(20);
        snake.shake();
    }
}