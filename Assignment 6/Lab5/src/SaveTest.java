import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.*;

public class SaveTest {
    private static final int TIMEOUT = 2000;
    @Test(timeout = TIMEOUT)
    public void savedSerialize() throws IOException, ClassNotFoundException {
        Game game = Main.deserialize("Tester");
        assertEquals(52,game.curPos);
    }
    @Test(timeout = TIMEOUT)
    public void savedSerialize2() throws IOException, ClassNotFoundException {
        Game game = new Game(10,"Hellooo");
        Main.serialize(game,false);
        assertEquals(game,(Game)Main.deserialize("Hellooo"));
    }
    @Test(timeout = TIMEOUT)
    public void savedSerialize3() throws IOException, ClassNotFoundException {
        Game game = new Game(20,"Hellooo123");
        Main.serialize(game,false);
        assertEquals(game,(Game)Main.deserialize("Hellooo123"));
    }
}