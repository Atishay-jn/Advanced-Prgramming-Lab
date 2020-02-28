import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.StringTokenizer;

abstract class Hero {
    final private int id;
    private static int count;
    protected String username;
    protected int x;
    protected int y;
    protected int attack;
    protected int defense;
    protected int health;
    protected int XP;
    static {
        count = 1;
    }
    Hero() {
        id = count++;
        x = 4;
        y = 4;
        health = 100;
        XP = 0;
    }
    public int getDefence() {
        return defense;
    }
    public int getX() {
        return x;
    }
    public void XPboost(int k) {
        if(XP<20) {
            if(XP+k>=60) {
                attack +=3;
                defense += 3;
                System.out.println("Level up: level 4");
            }
            else if(XP+k>=40) {
                attack += 2;
                defense += 2;
                System.out.println("Level up: level 3");
            }
            else {
                attack++;
                defense++;
                System.out.println("Level up: level 2");
            }
        }
        else if(XP<40) {
            if(XP+k>=60) {
                attack += 2;
                defense += 2;
                System.out.println("Level up: level 4");
            }
            else {
                attack++;
                defense++;
                System.out.println("Level up: level 3");
            }
        }
        else if(XP<60) {
            attack++;
            defense++;
            System.out.println("Level up: level 4");
        }
        XP += k;
        if(XP>=20)
            health = 150;
        if(XP>=40)
            health = 200;
        if(XP>=60)
            health = 250;
    }
    public int getHealth() {
        return health;
    }
    public int getY() {
        return y;
    }
    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }
    public void setHealth(int health) {
        this.health = health;
    }
    public String getUsername() {
        return username;
    }
    abstract void super_power();
    public int getAttack() {
        return attack;
    }
    public int getDefense() {
        return defense;
    }
}

class Warrior extends Hero {
    Warrior(String username) {
        attack = 10;
        defense = 3;
        this.username = username;
    }
    @Override
    void super_power() {
    }

}

class Mage extends Hero {
    Mage(String username) {
        attack = 5;
        defense = 5;
        this.username = username;
    }
    @Override
    void super_power() {
    }
}

class Thief extends Hero {
    Thief(String username) {
        attack = 6;
        defense = 4;
        this.username = username;
    }
    @Override
    void super_power() {
    }
}

class Healer extends Hero {
    Healer(String username) {
        attack = 4;
        defense = 8;
        this.username = username;
    }
    @Override
    void super_power() {
        //increase health by 5%
    }
}

abstract class Monster {
    int health;
    int max_health;
    public int attack() {
        Random ran = new Random();
        double k = 5+ran.nextGaussian();
        while(k<0 || k>10) {
            k = 5 + ran.nextGaussian();
        }
        k = k/10;
        double attack = (health*k)/4;
        return (int)attack;
    }
    public int getHealth() {
        return health;
    }
    public int attacked(int k) {
        health -= k;
        return health;
    }
    public void setHealth(int health) {
        this.health = health;
    }
    public int getMax_health() {
        return max_health;
    }
}

class Goblin extends Monster {
    Goblin() {
        health = 100;
        max_health = 200;
    }
}

class Zombies extends Monster {
    Zombies() {
        health = 150;
        max_health = 150;
    }
}

class Fiends extends Monster {
    Fiends() {
        health = 200;
        max_health = 200;
    }
}

class Lionfang extends Monster {
    Lionfang() {
        health = 250;
        max_health = 250;
    }
}

public class Lab {
    private static int[][][] game; //game starts 5,4;
    public static void main(String[] args) throws IOException {
        setGame();
        ArrayList<Hero> players = new ArrayList<>();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer s;
        int k = 0;
        while(k!=3) {
            start();
            s = new StringTokenizer(br.readLine());
            k = Integer.parseInt(s.nextToken());
            if(k==1) {
                System.out.println("Enter Username");
                String str = br.readLine();
                Hero temp = hero_choice(str);
                players.add(temp);
                System.out.println("User Creation done. Username: "+str+". Hero type: "+temp.getClass().getSimpleName() + ". Log in to play the game. Exiting");
            }
            else if(k==2) {
                System.out.println("Enter Username");
                String str = br.readLine();
                boolean flag = true;
                for (Hero player : players) {
                    if (str.compareTo(player.getUsername()) == 0) {
                        flag = false;
                        System.out.println("User Found... logging in");
                        play(player);
                        break;
                    }
                }
                if(flag)
                    System.out.println("User not found");
            }
            else if(k>3)
                System.out.println("Invalid option");
            else {
                System.out.println("Terminating...");
            }
        }
    }
    private static void play(Hero k) throws IOException {
        System.out.println("Welcome "+k.getUsername());
        while(true) {
            int location = k.getX() * 10 + k.getY();
            location = valid_path(location);
            if (location == -1)
                return;
            int x = (location - (location % 10)) / 10;
            int y = location % 10;

            if (game[x][y][0] == 0) {
                System.out.println("Invalid path. Terminating the game...");
                return;
            }
            if (game[x][y][0] == 1) {
                boolean flag = fight(k, new Goblin(), k.getHealth());
                if (flag) {
                    System.out.println("Monster killed!");
                    System.out.println("20 XP awarded");
                    k.XPboost(20);
                    System.out.println("Fight won, proceed to the next location");
                } else {
                    System.out.println("You lost");
                    k.setY(4);
                    k.setX(4);
                    return;
                }
            }
            else if (game[x][y][0] == 2) {
                boolean flag = fight(k, new Zombies(), k.getHealth());
                if (flag) {
                    System.out.println("Monster killed!");
                    System.out.println("40 XP awarded");
                    k.XPboost(40);
                    System.out.println("Fight won, proceed to the next location");
                } else {
                    System.out.println("You lost");
                    k.setY(4);
                    k.setX(4);
                    return;
                }
            }
            else if (game[x][y][0] == 3) {
                // fight with Fiends
                boolean flag = fight(k, new Fiends(), k.getHealth());
                if (flag) {
                    System.out.println("Monster killed!");
                    System.out.println("60 XP awarded");
                    k.XPboost(60);
                    System.out.println("Fight won, proceed to the next location");
                } else {
                    System.out.println("You lost");
                    k.setY(4);
                    k.setX(4);
                    return;
                }
            } else {
                boolean flag = fight(k, new Lionfang(), k.getHealth(), true);
                if(flag) {
                    System.out.println("You Won!");
                }
                else {
                    System.out.println("You lost");
                }
                k.setY(4);
                k.setX(4);
                return;
            }
            k.setX((location - (location % 10)) / 10);
            k.setY(location % 10);
        }
    }
    private static boolean fight(Hero hero, Lionfang lion, int health, boolean flag) throws IOException {
        System.out.println("Fight Started. You are fighting Lionfang (Level 4, Boss)");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer s;
        int counter = 0;
        int bonus_attack = 0;
        int bonus_defense = 0;
        int extra_health = 0;
        int dec_health = 0;
        int defense;
        while((lion.getHealth()-dec_health>0) && (health+extra_health)>0) {
            defense = 0;
            counter++;
            System.out.println("Chose move:");
            System.out.println("1) Attack");
            System.out.println("2) Defense");
            if (counter > 3) {
                System.out.println("3) Special Attack");
                bonus_attack = 0;
                extra_health = 0;
                bonus_defense = 0;
                dec_health = 0;
            }
            s = new StringTokenizer(br.readLine());
            int ch = Integer.parseInt(s.nextToken());
            if (ch == 1) {
                System.out.println("You choose to attack");
                int attack = hero.getAttack() + bonus_attack;
                System.out.println("You attacked and inflicted " + attack + " damage to the monster.");
                if (lion.attacked(attack) < 0)
                    lion.setHealth(0);
                System.out.println("Your HP: " + (extra_health + health) + "/" + hero.getHealth() + " Monster's HP: " + (lion.getHealth() - dec_health) + "/250");
            } else if (ch == 2) {
                System.out.println("You choose to defend");
                System.out.println("Monster attack reduced by " + hero.getDefence() + "!");
                System.out.println("Your HP: " + (extra_health + health) + "/" + hero.getHealth() + " Monster's HP: " + (lion.getHealth() - dec_health) + "/250");
                defense = hero.getDefense();
            } else if (ch == 3 && counter > 3) {
                counter = 0;
                System.out.println("Special power activated");
                System.out.println("Performing special attack");
                hero.super_power();
                if (hero.getClass().getSimpleName().compareTo("Warrior") == 0) {
                    bonus_attack = 5;
                    bonus_defense = 5;
                    System.out.println("Increased attack and defense by 5 for the next 3 moves");
                }
                else if (hero.getClass().getSimpleName().compareTo("Healer") == 0) {
                    extra_health = (int) ((float) (health) * 0.05);
                    System.out.println("Gained extra " + extra_health + " Hp for next 3 moves");
                }
                else if (hero.getClass().getSimpleName().compareTo("Mage") == 0) {
                    System.out.println("Decreased monster's health by 5 for next 3 moves");
                    dec_health = 5;
                }
                else {
                    health += (int) ((float) (lion.getHealth()) * 0.3);
                    System.out.println("You have stolen " + (int) ((float) (lion.getHealth()) * 0.3) + " Hp from the monster");
                    lion.setHealth((int) ((float) (lion.getHealth()) * 0.7));
                }
                System.out.println("Your HP: " + (health + extra_health) + "/" + hero.getHealth() + " Monster's HP: " + (lion.getHealth() - dec_health) + "/" + lion.getMax_health());
            }
            else {
                System.out.println("Invalid choice");
                counter--;
                continue;
            }
            System.out.println("Monster attack!");
            if (defense > 0)
                defense += bonus_defense;
            int attack = lion.attack() - defense;
            Random ran = new Random();
            int special = ran.nextInt(10);
            if (attack < 0)
                attack = 0;
            if(special==5) {
                System.out.println("Lionfang used special power and inflicted " + (int)((float)health/2) + " damage to you.");
                health = (int)((float)health/2);
            }
            else {
                health -= attack;
                System.out.println("The monster attacked and inflicted " + attack + " damage to you.");
            }
            System.out.println("Your HP: " + (health + extra_health) + "/" + hero.getHealth() + " Monster's HP: " + (lion.getHealth() - dec_health) + "/250");
        }
        return health>0;
    }
    private static boolean fight(Hero hero, Monster monster, int health) throws IOException {
        if(monster.getClass().getSimpleName().compareTo("Goblin")==0) {
            System.out.println("Fight Started. You are fighting a level 1 Monster");
        }
        else if(monster.getClass().getSimpleName().compareTo("Zombies")==0) {
            System.out.println("Fight Started. You are fighting a level 2 Monster");
        }
        else {
            System.out.println("Fight Started. You are fighting a level 3 Monster");
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer s;
        int counter = 0;
        int bonus_attack = 0;
        int bonus_defense = 0;
        int extra_health = 0;
        int dec_health = 0;
        int defense;
        while((monster.getHealth()-dec_health)>0 && (health+extra_health)>0) {
            defense = 0;
            counter++;
            System.out.println("Chose move:");
            System.out.println("1) Attack");
            System.out.println("2) Defense");
            if (counter > 3) {
                System.out.println("3) Special Attack");
                bonus_attack = 0;
                extra_health = 0;
                bonus_defense = 0;
                dec_health = 0;
            }
            s = new StringTokenizer(br.readLine());
            int ch = Integer.parseInt(s.nextToken());
            if (ch == 1) {
                System.out.println("You choose to attack");
                int attack = hero.getAttack() + bonus_attack;
                System.out.println("You attacked and inflicted " + attack + " damage to the monster.");
                if (monster.attacked(attack) < 0)
                    monster.setHealth(0);
                System.out.println("Your HP: " + (extra_health + health) + "/" + hero.getHealth() + " Monster's HP: " + (monster.getHealth() - dec_health) + "/" + monster.getMax_health());
            } else if (ch == 2) {
                System.out.println("You choose to defend");
                System.out.println("Monster attack reduced by " + hero.getDefence() + "!");
                System.out.println("Your HP: " + (extra_health + health) + "/" + hero.getHealth() + " Monster's HP: " + (monster.getHealth() - dec_health) + "/" + monster.getMax_health());
                defense = hero.getDefense();
            } else if (ch == 3 && counter > 3) {
            counter = 0;
            System.out.println("Special power activated");
            System.out.println("Performing special attack");
            hero.super_power();
            if (hero.getClass().getSimpleName().compareTo("Warrior") == 0) {
                bonus_attack = 5;
                bonus_defense = 5;
                System.out.println("Increased attack and defense by 5 for the next 3 moves");
            }
            else if (hero.getClass().getSimpleName().compareTo("Healer") == 0) {
                extra_health = (int) ((float) (health) * 0.05);
                System.out.println("Gained extra " + extra_health + " Hp for next 3 moves");
            }
            else if (hero.getClass().getSimpleName().compareTo("Mage") == 0) {
                System.out.println("Decreased monster's health by 5 for next 3 moves");
                    dec_health = 5;
            }
            else {
                    health += (int) ((float) (monster.getHealth()) * 0.3);
                    System.out.println("You have stolen " + (int) ((float) (monster.getHealth()) * 0.3) + " Hp from the monster");
                    monster.setHealth((int) ((float) (monster.getHealth()) * 0.7));
            }
            System.out.println("Your HP: " + (health + extra_health) + "/" + hero.getHealth() + " Monster's HP: " + (monster.getHealth() - dec_health) + "/" + monster.getMax_health());
            }
            else {
                System.out.println("Invalid choice");
                counter--;
                continue;
            }
            System.out.println("Monster attack!");
            if (defense > 0)
                defense += bonus_defense;
            int attack = monster.attack() - defense;
            if (attack < 0)
                attack = 0;
            health -= attack;
            System.out.println("The monster attacked and inflicted " + attack + " damage to you.");
            System.out.println("Your HP: " + (health + extra_health) + "/" + hero.getHealth() + " Monster's HP: " + (monster.getHealth() - dec_health) + "/" + monster.getMax_health());
        }
        return health>0;
    }
    private static int valid_path(int location) throws IOException {
        if(location == 44) {
            System.out.println("You are at the starting location. Choose path");
            System.out.println("1) Go to location "+(location-10));
            System.out.println("2) Go to location "+(location+1));
            System.out.println("3) Go to location "+(location -1));
            System.out.println("Enter -1 to exit");
            System.out.println("Recommended location: "+game[4][4][1]);
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            StringTokenizer s = new StringTokenizer(br.readLine());
            int choice = Integer.parseInt(s.nextToken());
            if(choice==1)
                return location-10;
            else if(choice==2)
                return location+1;
            else if(choice==3)
                return location-1;
            else
                return -1;
        }
        else {
            int cnt = 1;
            if(game[1 + ((location - (location%10))/10)][location%10][0]!=0) {
                System.out.println(cnt + ") Go to location " + (location + 10));
                cnt++;
            }
            if(game[((location - (location%10))/10)-1][location%10][0]!=0) {
                System.out.println(cnt + ") Go to location " + (location - 10));
                cnt++;
            }
            if(game[(location - (location%10))/10][1+(location%10)][0]!=0) {
                System.out.println(cnt+") Go to location " + (location+1));
                cnt++;
            }
            if(game[(location - (location%10))/10][-1+(location%10)][0]!=0) {
                System.out.println(cnt+") Go to location " + (location - 1));
            }
        }
        System.out.println("Enter -1 to exit");
        System.out.println("Recommended location: "+game[(location-(location%10))/10][location%10][1]);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer s = new StringTokenizer(br.readLine());
        int choice = Integer.parseInt(s.nextToken());
        if(choice==-1)
            return choice;
        if(game[1 + ((location - (location%10))/10)][location%10][0]!=0) {
            choice--;
        }
        if(choice == 0) {
            return location+10;
        }
        if(game[((location - (location%10))/10)-1][location%10][0]!=0) {
            choice--;
        }
        if(choice == 0) {
            return location-10;
        }
        if(game[(location - (location%10))/10][1+(location%10)][0]!=0) {
            choice--;
        }
        if(choice==0)
            return location+1;
        if(game[(location - (location%10))/10][-1+(location%10)][0]!=0) {
            choice--;
        }
        if(choice==0)
            return location-1;
        return -1;
    }
    private static Hero hero_choice(String str) throws IOException {
        System.out.println("1) Warrior");
        System.out.println("2) Thief");
        System.out.println("3) Mage");
        System.out.println("4) Healer");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer s = new StringTokenizer(br.readLine());
        int ch = Integer.parseInt(s.nextToken());
        Hero obj;
        if(ch==1) {
            obj = new Warrior(str);
        }
        else if (ch==2) {
            obj = new Thief(str);
        }
        else if (ch==3) {
            obj = new Mage(str);
        }
        else {
            obj = new Healer(str);
        }
        return obj;
    }
    private static void start() {
        System.out.println("Welcome to ArchLegends");
        System.out.println("Choose your option");
        System.out.println("1) New User");
        System.out.println("2) Existing User");
        System.out.println("3) Exit");
    }
    private static int best(int i,int j, int k) {
        if(i==4 && j==4) {
            int mloc = 45;
            if(game[i][j-1][0]<game[(mloc-(mloc%10))/10][mloc%10][0]) {
                mloc = (10*i)+(j-1);
            }
            if(game[i-1][j][0]<game[(mloc-(mloc%10))/10][mloc%10][0]) {
                mloc = (10*(i-1))+j;
            }
            return mloc;
        }
        else {
            int mloc = (10*i)+(j+1);
            if(game[(mloc-(mloc%10))/10][mloc%10][0]!=0) {
                if(game[(mloc-(mloc%10))/10][mloc%10][0]>k) {
                    if(game[i][j-1][0]!=0 && game[i][j-1][0]<game[(mloc-(mloc%10))/10][mloc%10][0] && game[i][j-1][0]==k)
                        mloc = (10*i)+(j-1);
                    if(game[i+1][j][0]!=0 && game[i+1][j][0]<game[(mloc-(mloc%10))/10][mloc%10][0] && game[i+1][j][0]==k)
                        mloc = (10*(i+1))+j;
                    if(game[i-1][j][0]!=0 && game[i-1][j][0]<game[(mloc-(mloc%10))/10][mloc%10][0] && game[i-1][j][0]==k)
                        mloc = (10*(i-1))+j;
                    if(game[(mloc-(mloc%10))/10][mloc%10][0]!=k) {
                        if(game[i][j-1][0]!=0 && game[i][j-1][0]<game[(mloc-(mloc%10))/10][mloc%10][0])
                            mloc = (10*i)+(j-1);
                        if(game[i+1][j][0]!=0 && game[i+1][j][0]<game[(mloc-(mloc%10))/10][mloc%10][0])
                            mloc = (10*(i+1))+j;
                        if(game[i-1][j][0]!=0 && game[i-1][j][0]<game[(mloc-(mloc%10))/10][mloc%10][0])
                            mloc = (10*(i-1))+j;
                    }
                }
                else if (game[(mloc-(mloc%10))/10][mloc%10][0]<=k)
                    return mloc;
                return mloc;
            }
            mloc = (10*i) + (j-1);
            if(game[(mloc-(mloc%10))/10][mloc%10][0]!=0) {
                if(game[(mloc-(mloc%10))/10][mloc%10][0]>k) {
                    if(game[i+1][j][0]!=0 && game[i+1][j][0]<game[(mloc-(mloc%10))/10][mloc%10][0] && game[i+1][j][0]==k)
                        mloc = (10*(i+1))+j;
                    if(game[i-1][j][0]!=0 && game[i-1][j][0]<game[(mloc-(mloc%10))/10][mloc%10][0] && game[i-1][j][0]==k)
                        mloc = (10*(i-1))+j;
                    if(game[(mloc-(mloc%10))/10][mloc%10][0]!=k) {
                        if(game[i+1][j][0]!=0 && game[i+1][j][0]<game[(mloc-(mloc%10))/10][mloc%10][0])
                            mloc = (10*(i+1))+j;
                        if(game[i-1][j][0]!=0 && game[i-1][j][0]<game[(mloc-(mloc%10))/10][mloc%10][0])
                            mloc = (10*(i-1))+j;
                    }
                }
                else if (game[(mloc-(mloc%10))/10][mloc%10][0]<=k)
                    return mloc;
                return mloc;
            }
            return mloc;
        }
    }
    private static void setGame() {
        game = new int[7][9][2];
        Random ran = new Random();
        for(int i=5;i<9;i++) {
            game[0][i][0] = 0;
        }
        for(int i=1;i<6;i++) {
            game[i][0][0] = 4;
            game[i][8][0] = 4;
        }
        for(int i=0;i<9;i++) {
            game[6][i][0] = 0;
        }
        for(int i=1;i<4;i++)
            for(int j=1;j<3;j++)
                game[j][i][0] = 0;
        game[0][4][0] = 4;
        game[1][4][0] = 1+ran.nextInt(3);
        game[1][5][0] = 1+ran.nextInt(3);
        game[1][6][0] = 1+ran.nextInt(3);
        game[1][7][0] = 1+ran.nextInt(3);
        game[2][4][0] = 1+ran.nextInt(3);
        game[2][5][0] = 1+ran.nextInt(3);
        game[2][6][0] = 1+ran.nextInt(3);
        game[2][7][0] = 1+ran.nextInt(3);
        game[3][1][0] = 1+ran.nextInt(3);
        game[3][2][0] = 1+ran.nextInt(3);
        game[3][3][0] = 1+ran.nextInt(3);
        game[3][4][0] = 1+ran.nextInt(3);
        game[3][5][0] = 0;
        game[3][6][0] = 0;
        game[3][7][0] = 0;
        game[4][1][0] = 1+ran.nextInt(3);
        game[4][2][0] = 1+ran.nextInt(3);
        game[4][3][0] = 1+ran.nextInt(3);
        game[4][4][0] = 1+ran.nextInt(3);
        game[4][5][0] = 1+ran.nextInt(3);
        game[4][6][0] = 1+ran.nextInt(3);
        game[4][7][0] = 1+ran.nextInt(3);
        game[5][1][0] = 0;
        game[5][2][0] = 0;
        game[5][3][0] = 4;
        game[5][4][0] = 1+ran.nextInt(3);
        game[5][5][0] = 1+ran.nextInt(3);
        game[5][6][0] = 1+ran.nextInt(3);
        game[5][7][0] = 1+ran.nextInt(3);
        // for(int i=0;i<6;i++) {
        //     for(int j=0;j<9;j++)
        //         System.out.print(game[i][j][0]+" ");
        //     System.out.println();
        // }
        game[1][4][1] = best(1,4,4);
        game[1][5][1] = best(1,5,4);
        game[1][6][1] = best(1,6,4);
        game[1][7][1] = best(1,7,4);
        game[2][4][1] = best(2,4,3);
        game[2][5][1] = best(2,5,4);
        game[2][6][1] = best(2,6,4);
        game[2][7][1] = best(2,7,4);
        game[3][1][1] = best(3,1,4);
        game[3][2][1] = best(3,2,4);
        game[3][3][1] = best(3,3,3);
        game[3][4][1] = best(3,4,2);
        game[4][1][1] = best(4,1,4);
        game[4][2][1] = best(4,2,3);
        game[4][3][1] = best(4,3,2);
        game[4][4][1] = best(4,4,1);
        game[4][5][1] = best(4,5,2);
        game[4][6][1] = best(4,6,3);
        game[4][7][1] = best(4,7,3);
        game[5][4][1] = best(5,4,4);
        game[5][5][1] = best(5,5,3);
        game[5][6][1] = best(5,6,4);
        game[5][7][1] = best(5,7,4);
    }
}
