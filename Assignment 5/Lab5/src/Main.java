import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

final class SnakeBiteException extends RuntimeException {
    SnakeBiteException(String message) {
        super(message);
    }
}

final class VultureBiteException extends RuntimeException {
    VultureBiteException(String message) {
        super(message);
    }
}

final class TrampolineException extends RuntimeException {
    TrampolineException(String message) {
        super(message);
    }
}

final class CricketBiteException extends RuntimeException {
    CricketBiteException(String message) {
        super(message);
    }
}

final class GameWinnerException extends RuntimeException {
    GameWinnerException(String message) {
        super(message);
    }
}

final class Path {
    private final int path;
    private Player player;
    private ArrayList<Tile> layout;
    private Tile snake;
    private Tile cricket;
    private Tile vulture;
    private Tile trampoline;
    Path(int path) {
        this.path = path;
        layout = new ArrayList<Tile>();
        for(int i=0;i<path;i++) {
            Tile k = new White(0);
            layout.add(k);
        }
        makePath();
    }
    public void setPlayer(Player player) {
        this.player = player;
    }
    public int getPath() {
        return path;
    }
    private void makePath() {
        Random ran = new Random();
        snake = new Snake(-1-ran.nextInt(10));
        vulture = new Vulture(-1-ran.nextInt(10));
        cricket = new Cricket(-1-ran.nextInt(10));
        trampoline = new Trampoline(1+ran.nextInt(10));
        int num_vulture = 0;
        int num_snakes = 0;
        int num_cricket = 0;
        int num_trampoline = 0;
        try {
            num_vulture = ran.nextInt(Math.min(path / 6, 10));
            num_snakes = ran.nextInt(Math.min(path / 6, 10));
            num_cricket = ran.nextInt(Math.min(path / 6, 10));
            num_trampoline = ran.nextInt(Math.min(path / 6, 10));
        }
        catch (IllegalArgumentException ignored) {
        }
        finally {
            for (int i = 0; i < num_vulture; i++) {
                Tile temp = new Vulture(vulture.getBonus());
                layout.set(1 + ran.nextInt(path - 1), temp);
            }
            for (int i = 0; i < num_snakes; i++) {
                Tile temp = new Snake(snake.getBonus());
                layout.set(1 + ran.nextInt(path - 1), temp);
            }
            for (int i = 0; i < num_cricket; i++) {
                Tile temp = new Cricket(cricket.getBonus());
                layout.set(1 + ran.nextInt(path - 1), temp);
            }
        }
        try {
            for (int i = 0; i < num_trampoline; i++) {
                Tile temp = new Trampoline(trampoline.getBonus());
                layout.set(1 + ran.nextInt(path - trampoline.getBonus()-1), temp);
            }
        }
        catch (Exception ignored) {
        }
        num_cricket = 0;
        num_snakes = 0;
        num_trampoline = 0;
        num_vulture = 0;
        for(int i=0;i<path;i++) {
            if(layout.get(i).getClass().getSimpleName().compareTo("Snake")==0)
                num_snakes++;
            else if(layout.get(i).getClass().getSimpleName().compareTo("Vulture")==0)
                num_vulture++;
            if(layout.get(i).getClass().getSimpleName().compareTo("Cricket")==0)
                num_cricket++;
            if(layout.get(i).getClass().getSimpleName().compareTo("Trampoline")==0)
                num_trampoline++;
        }
        System.out.println("Danger: There are "+num_snakes+", "+num_cricket+", "+num_vulture+" number of Snakes, Crickets and Vultures respectively on your track!");
        System.out.println("Danger: Each Snake, Cricket and Vulture can throw you back by "+(-snake.getBonus())+", "+(-cricket.getBonus())+", "+(-vulture.getBonus())+" number of tiles respectively!");
        System.out.println("Good News: There are "+num_trampoline+" number of Trampolines on your track!");
        System.out.println("Good News: Each Trampoline can help you advance by "+trampoline.getBonus()+" number of Tiles");
    }
    public int move(int pos) {
        try {
            layout.get(pos).shake();
            return 0;
        }
        catch (VultureBiteException v) {
            System.out.println(v.getMessage());
            player.incVultureBites();
            return vulture.getBonus();
        }
        catch (CricketBiteException c) {
            System.out.println(c.getMessage());
            player.incCricketBites();
            return cricket.getBonus();
        }
        catch (SnakeBiteException s) {
            System.out.println(s.getMessage());
            player.incSnakeBites();
            return snake.getBonus();
        }
        catch (TrampolineException t) {
            System.out.println(t.getMessage());
            player.incTrampoline();
            return trampoline.getBonus();
        }
    }
}

final class Player {
    private final String name;
    private int snakeBites;
    private int vultureBites;
    private int cricketBites;
    private int trampoline;
    Player(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public void endGame() {
        System.out.println("Total number of Snake Bites = "+snakeBites);
        System.out.println("Total number of Vulture Bites = "+vultureBites);
        System.out.println("Total number of Cricket Bites = "+cricketBites);
        System.out.println("Total number of Trampolines = "+trampoline);
    }
    public void incSnakeBites() {
        snakeBites++;
    }
    public void incTrampoline() {
        trampoline++;
    }
    public void incVultureBites() {
        vultureBites++;
    }
    public void incCricketBites() {
        cricketBites++;
    }
}

abstract class Tile {
    protected final int bonus;
    Tile(int bonus) {
        this.bonus = bonus;
    }
    public int getBonus() {
        return bonus;
    }
    abstract void shake();
}

final class Snake extends Tile {
    Snake(int bonus) {
        super(bonus);
    }
    @Override
    void shake() {
        String str = "    Hiss...! I am a Snake, you go back "+(-this.bonus)+" tiles! ";
        throw new SnakeBiteException(str);
    }
}

final class Vulture extends Tile {
    Vulture(int bonus) {
        super(bonus);
    }
    @Override
    void shake() {
        String str = "    Yapping...! I am a Vulture, you go back "+(-this.bonus)+" tiles! ";
        throw new VultureBiteException(str);
    }
}

final class Cricket extends Tile {
    Cricket(int bonus) {
        super(bonus);
    }
    @Override
    void shake() {
        String str = "    Chirp...! I am a Cricket, you go back "+(-this.bonus)+" tiles! ";
        throw new CricketBiteException(str);
    }
}

final class Trampoline extends Tile {
    Trampoline(int bonus) {
        super(bonus);
    }
    @Override
    void shake() {
        String str = "    PingPong! I am a Trampoline, you advance "+this.bonus+" tiles";
        throw new TrampolineException(str);
    }
}

final class White extends Tile {
    White(int bonus) {
        super(bonus);
    }
    @Override
    void shake() {
        System.out.println("    I am a White Tile");
    }
}

final class Dice {
    int rollDice() {
        Random ran = new Random();
        return 1+ran.nextInt(6);
    }
}

final class Game {
    private final Path path;
    private final Player player;
    private int total_moves = 0;
    private final Dice dice;
    Game() throws IOException {
        dice = new Dice();
        int length = 0;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer s;
        boolean flag =  true;
        while(flag) {
            try {
                System.out.println("Enter total number of tiles on the race track (length) (max 2000)");
                s = new StringTokenizer(br.readLine());
                length = Integer.parseInt(s.nextToken());
                if(length<2001)
                    flag = false;
            }
            catch (NumberFormatException | NoSuchElementException ignored) {
                System.out.println("Invalid input");
            }
        }
        flag = true;
        System.out.println("Setting up the race track...");
        path = new Path(length);
        String name = "";
        while(flag) {
            try {
                System.out.println("Enter the Player Name");
                name = br.readLine();
                if(name.compareTo("")!=0)
                    flag = false;
            }
            catch (NoSuchElementException ignored) {
                System.out.println("Invalid input");
            }
        }
        player = new Player(name);
        path.setPlayer(player);
        try {
            System.out.println("Starting the game with "+player.getName()+" at Tile-1");
            System.out.println("Control transferred to Computer for rolling the the Dice for "+player.getName());
            System.out.print("Hit enter to start the game");
            br.readLine();
            System.out.println("Game Started ====================================================>");
            play(-1);
        }
        catch (GameWinnerException win) {
            System.out.println(player.getName()+" wins the race in "+total_moves+" rolls!");
            player.endGame();
        }
    }
    private void play(int position) {
        total_moves++;
        int num = dice.rollDice();
        if(position>=0) {
            if(num+position+1==path.getPath()) {
                System.out.println("[Roll-" + total_moves + "]: " + player.getName() + " rolled a " + num + " at Tile-" + (position + 1) + ", landed on Tile-" + (num + position + 1) + ".");
                throw new GameWinnerException("You won");
            }
            else if(num+position+1>path.getPath()) {
                System.out.println("[Roll-"+total_moves+"]: "+player.getName()+" rolled a "+num+" at Tile-"+(position+1)+", landed on Tile-"+(position+1)+".");
                play(position);
            }
            else {
                System.out.println("[Roll-" + total_moves + "]: " + player.getName() + " rolled a " + num + " at Tile-" + (position + 1) + ", landed on Tile-" + (num + position + 1) + ".");
                System.out.println("    Trying to shake the Tile-" + (num + position + 1));
                int k = path.move(num + position);
                position = position + num + k;
                if (position < 1)
                    position = 0;
                System.out.println("    "+player.getName() + " moved to Tile-" + (position + 1));
                if (position == 0)
                    play(-1);
                else
                    play(position);
            }
        }
        else {
            System.out.print("[Roll-"+total_moves+"]: "+player.getName()+" rolled a "+num+" at Tile-1.");
            if(num==6) {
                System.out.println("You are out of the cage! You get a free roll");
                play(0);
            }
            else {
                System.out.println("OOPs you need a 6 to start");
                play(position);
            }
        }
    }
}

public class Main {
    public static void main(String[] args) throws IOException {
        Game game = new Game();
    }
}
