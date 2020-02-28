import org.junit.Test;
import static org.junit.Assert.*;

public class TrampolineTest {
    private static final int TIMEOUT = 2000;
    @Test(expected = TrampolineException.class, timeout = TIMEOUT)
    public void TestTrampolineShake1() {
        Trampoline trampoline = new Trampoline(5);
        trampoline.shake();
    }
    @Test(expected = TrampolineException.class, timeout = TIMEOUT)
    public void TestTrampolineShake2() {
        Trampoline trampoline = new Trampoline(5);
        trampoline.shake();
    }
    @Test(expected = TrampolineException.class, timeout = TIMEOUT)
    public void TestTrampolineShake3() {
        Trampoline trampoline = new Trampoline(5);
        trampoline.shake();
    }
}
