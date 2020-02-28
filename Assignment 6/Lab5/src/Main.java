import java.io.*;
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

final class Path implements Serializable{
    private final int path;
    public static final long serialVersionUID = 1234L;
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

final class Player implements Serializable{
    public static final long serialVersionUID = 123L;
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

abstract class Tile implements Serializable{
    public static final long serialVersionUID = 12L;
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

final class Dice implements Serializable{
    public static final long serialVersionUID = 1L;
    int rollDice() {
        Random ran = new Random();
        return 1+ran.nextInt(6);
    }
}

final class Game implements Serializable {
    public static final long serialVersionUID = 12345L;
    private final Path path;
    protected final Player player;
    private transient static int num_games = 0;
    private int total_moves = 0;
    private transient static Dice dice; // remember to correct these post input
    private boolean flag25 = true;
    private boolean flag50 = true;
    private boolean flag75 = true;
    private boolean test = true;
    protected int curPos;
    Game() throws IOException {
        num_games++;
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
                if(length<2001 && length>10)
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
    Game(int position) {
        //for testing purposes
        test = false;
        path = new Path(position);
        player = new Player("test");
        dice = new Dice();
        try {
            play(position);
        }
        catch(IOException ignored) {
        }
    }
    Game(int position, String name) {
        curPos = position;
        path = new Path(position);
        player = new Player(name);
        dice = new Dice();
    }
    @Override
    public boolean equals(Object object) {
        if(object.getClass() == this.getClass())
            return this.curPos == ((Game)object).curPos;
        else
            return false;
    }
    private void play(int position) throws IOException {
        total_moves++;
        if(position == path.getPath()) {
            System.out.println("test test test");
            throw new GameWinnerException("You won");
        }
        boolean save = false;
        if(test && ((position >= (path.getPath()/4) && flag25 && position < (path.getPath()/2)) | (position >= (path.getPath()/2) && flag50 && position < ((3*path.getPath())/4)) | (position >= ((3*path.getPath())/4) && flag75))) {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Do you want to save? (Enter yes to save)");
            String ans = "abc";
            try {
                ans = br.readLine();
            }
            catch(Exception ignored) {
            }
            save = false;
            if(ans.compareTo("yes")==0) {
                save = true;
            }
            else {
                if(position >= (path.getPath()/4) && flag25 && position < (path.getPath()/2)) {
                    flag25 = false;
                }
                if(position >= (path.getPath()/2) && flag50 && position < ((3*path.getPath())/4)) {
                    flag50 = false;
                }
                if(position >= ((3*path.getPath())/4) && flag75) {
                    flag75 = false;
                }
            }
        }
        if(save && (position >= (path.getPath()/4) && flag25 && position < (path.getPath()/2))) {
            flag25 = false;
            curPos = position;
            save = false;
            try {
                Main.serialize(this,true);
            }
            catch (IOException oof) {
                System.out.println("Unable to save");
            }
        }
        if(save && (position >= (path.getPath()/2) && flag50 && position < ((3*path.getPath())/4))) {
            flag50 = false;
            save = false;
            flag25 = false;
            curPos = position;
            try {
                Main.serialize(this,true);
            }
            catch (IOException oof) {
                System.out.println("Unable to save");
            }
        }
        if(save && (position >= ((3*path.getPath())/4) && flag75)) {
            flag75 = false;
            flag25 = false;
            flag50 = false;
            save = false;
            curPos = position;
            try {
                Main.serialize(this, true);
            }
            catch (IOException oof) {
                System.out.println("Unable to save");
            }
            //save this file
        }
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
                total_moves--;
                play(0);
            }
            else {
                System.out.println("OOPs you need a 6 to start");
                play(position);
            }
        }
    }
    public void resume() throws IOException {
        try {
            dice = new Dice();
            play(curPos);
        }
        catch (GameWinnerException win) {
            System.out.println(player.getName()+" wins the race in "+total_moves+" rolls!");
            player.endGame();
            System.exit(0);
        }
    }
    private boolean isSave() throws IOException {
        return true;
    }
}

public class Main {
    public static void main(String[] args) throws IOException {
        int k = 0;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer s;
        while(k!=3) {
            System.out.println("Welcome");
            System.out.println("1) New Game");
            System.out.println("2) Load Game");
            System.out.println("3) Exit Game");
            try {
                s = new StringTokenizer(br.readLine());
                k = Integer.parseInt(s.nextToken());
            }
            catch (NumberFormatException | NoSuchElementException ignored) {
                k = 0;
            }
            if(k==1) {
                Game game = new Game();
            }
            if(k==2) {
                System.out.println("Enter name of the player");
                s = new StringTokenizer(br.readLine());
                String name = s.nextToken();
                Game game;
                try {
                    game = deserialize(name);
                    game.resume();
                }
                catch (ClassNotFoundException | NullPointerException | FileNotFoundException ignored) {
                    System.out.println("Invalid name");
                }
            }
        }
    }
    public static void serialize(Game game, boolean finish) throws IOException {
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(
                    new FileOutputStream(game.player.getName()+".txt"));
            out.writeObject(game);
        }
        finally {
            if(out==null)
                System.out.println("Not saved");
            else
                out.close();
            System.out.println("Exiting program");
        }
        if(finish)
            System.exit(0);
    }
    public static Game deserialize(String name) throws IOException, ClassNotFoundException, NullPointerException {
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(
                    new FileInputStream(name+".txt")
            );
            return (Game) in.readObject();
        }
        finally {
            if(in == null)
                System.out.println("Can't read");
            else
                in.close();
        }
    }
}
