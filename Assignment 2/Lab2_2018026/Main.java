import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.lang.System;

interface Shop {
    public int search(String str, int k);
    public int searchp(String str, int k);
    public float reward(String str, int idx, float quantity);
    public String getName();
    public int display() throws IOException;
    public String getCategory(int idx);
    public void details();
    public String finish(String str, int idx, int quantity);
    public ArrayList<String> getCart();
}

class Items {
    private String name;
    private int price;
    private int quantity;
    private String category;
    private int offer;
    Items(String name, int price, int quantity, String category) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
        this.offer = 0;
        display();
    }
    Items(Items c) {
        name = c.name;
        price = c.price;
        quantity = c.quantity;
        category = c.category;
        offer = c.offer;
    }
    public String getName() {
        return name;
    }
    public int getOffer() {
        return offer;
    }
    public int getPrice() {
        return price;
    }
    public int getQuantity() {
        return quantity;
    }
    public String getCategory() {
        return category;
    }
    public int checkCategory(String category, int cnt) {
        if(category.compareTo(this.category)==0) {
            System.out.print(cnt+") ");
            cnt++;
            display();
        }
        return cnt;
    }
    public int checkCategoryp(String category, int cnt) {
        if(category.compareTo(this.category)==0) {
            cnt++;
        }
        return cnt;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
        display();
    }
    public void setPrice(int price) {
        this.price = price;
    }
    public void setOffer(int offer) {
        this.offer = offer;
    }
    public void display() {
        System.out.print(name+" "+price+" "+quantity+" ");
        if(offer == 0) {
            System.out.print("None");
        }
        else if(offer == 1) {
            System.out.print("Buy 1 get 1 free");
        }
        else {
            System.out.println("25% off");
        }
        System.out.println(" "+category);
    }
}

class Customer implements Shop {
     public static ArrayList<String> all_category;
     private String name;
     private float cash;
     private String address;
     private int rewards;
     public ArrayList<String> cart;
     private ArrayList<String> history;
     static {
         all_category = new ArrayList<String>();
     }
     Customer(String name, String address) {
         this.name = name;
         this.address = address;
         this.history = new ArrayList<String>();
         this.cart = new ArrayList<String>();
         this.cash = 100;
         this.rewards = 0;
     }
    @Override
    public void details() {
        System.out.println("Name: "+name+" Address "+address+" Number of purchases "+rewards);
    }
    @Override
    public ArrayList<String> getCart() {
        return cart;
    }
    @Override
    public String getName() {
        return name;
    }
    @Override
    public int search(String str, int k) {
        System.out.println("");
        return 1;
    }
    @Override
    public float reward(String str, int quantity, float price) {
        if(price>cash)
            return -1;
        cash -= price;
        rewards++;
        if(rewards%5==0)
            cash+=10;
        return price;
    }
    @Override
    public String finish(String str, int idx, int quantity) {
         history.add(str);
         return "a";
    }
    @Override
    public int searchp(String str, int k) {
         cart.add(str);
         return 1;
    }
    @Override
    public int display() throws IOException {
        System.out.println("Welcome "+name);
        System.out.println("Customer Menu");
        System.out.println("1) Search item");
        System.out.println("2) Checkout cart");
        System.out.println("3) Reward won");
        System.out.println("4) Print latest orders");
        System.out.println("5) Exit");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer s = new StringTokenizer(br.readLine());
        int ch = Integer.parseInt(s.nextToken());
        int k = -1;
        switch(ch) {
            case 1:
                System.out.println("Choose category");
                int cnt = 1;
                for(String category: all_category) {
                    System.out.println(cnt+") "+category);
                    cnt++;
                }
                s = new StringTokenizer(br.readLine());
                k = Integer.parseInt(s.nextToken());
                break;
            case 2:
                System.out.println("Items bought:");
                k = -2;
                break;
            case 3: // rewards won
                System.out.println("Rewards won " + (int)(rewards/5));
                break;
            case 4: //print history
                System.out.println("Latest transactions");
                for(int i=history.size()-1;i>=0 && i>history.size()-11;i--)
                    System.out.println(history.get(i));
                break;
            case 5: k = 0;
                break;
            default:
                System.out.println("Invalid choice");
        }
        return k;
    }
    @Override
    public String getCategory(int idx) {
        System.out.println("");
        return "a";
    }
}

class Merchant implements Shop {
    private String name;
    private String address;
    private ArrayList<Items> items;
    private float cash;
    private int max_items;
    private int num_items;
    ArrayList <String> category;
    Merchant(String name, String address) {
        this.name = name;
        this.items = new ArrayList<Items>();
        this.cash = 0;
        this.address = address;
        this.max_items = 10;
        this.num_items = 0;
        this.category = new ArrayList<String>();
    }
    @Override
    public void details() {
        System.out.println("Name "+ name + " Address " + address + " Contribution "+ (cash*0.01));
    }
    public ArrayList<String> getCart() {
        return null;
    }
    public void addItem() throws IOException {
        num_items++;
        if(num_items>max_items) {
            num_items--;
            System.out.println("Can't add new item");
            return;
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter item details");
        System.out.println("Item name:");
        String name = br.readLine();
        System.out.println("Item price:");
        StringTokenizer s = new StringTokenizer(br.readLine());
        int price = Integer.parseInt(s.nextToken());
        System.out.println("Item quantity:");
        s = new StringTokenizer(br.readLine());
        int quantity = Integer.parseInt(s.nextToken());
        System.out.println("Item category:");
        String cat = br.readLine();
        Items temp_item = new Items(name,price,quantity,cat);
        items.add(temp_item);
        boolean flag = true;
        for (String value : category)
            if (cat.compareTo(value) == 0) {
                flag = false;
                break;
            }
        if(flag) {
            category.add(cat);
            for (String value : Customer.all_category)
                if (cat.compareTo(value) == 0) {
                    flag = false;
                    break;
                }
            if (flag)
                Customer.all_category.add(cat);
        }
    }
    public void editItem() throws IOException {
        System.out.println("Choose item by code");
        int k = 1;
        for (Items item : items) {
            System.out.print(k+") ");
            item.display();
            k++;
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer s = new StringTokenizer(br.readLine());
        int idx = Integer.parseInt(s.nextToken());
        System.out.println("Enter edit details");
        System.out.println("Item price");
        s = new StringTokenizer(br.readLine());
        int price = Integer.parseInt(s.nextToken());
        items.get(idx-1).setPrice(price);
        System.out.println("Item quantity");
        s = new StringTokenizer(br.readLine());
        int quantity = Integer.parseInt(s.nextToken());
        items.get(idx-1).setQuantity(quantity);
    }
    @Override
    public int display() throws IOException {
        System.out.println("Welcome "+name);
        System.out.println("Merchant Menu");
        System.out.println("1) Add item");
        System.out.println("2) Edit item");
        System.out.println("3) Search by category");
        System.out.println("4) Add offer");
        System.out.println("5) Rewards won");
        System.out.println("6) Exit");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer s = new StringTokenizer(br.readLine());
        int ch = Integer.parseInt(s.nextToken());
        int k = -1;
        switch(ch) {
            case 1: // add new item
                this.addItem();
                break;
            case 2: // edit item
                this.editItem();
                break;
            case 3: //search
                System.out.println("Choose a category");
                for(int i=0;i<category.size();i++) {
                    System.out.println(i+1 + ") " + category.get(i));
                }
                s = new StringTokenizer(br.readLine());
                k = Integer.parseInt(s.nextToken());
                break;
            case 4:
                System.out.println("Choose item by code");
                int cnt = 1;
                for (Items item : items) {
                    System.out.print(cnt+") ");
                    item.display();
                    cnt++;
                }
                s = new StringTokenizer(br.readLine());
                int temp = Integer.parseInt(s.nextToken());
                System.out.println("Choose offer");
                System.out.println("1) buy one get one free");
                System.out.println("2) 25% off");
                s = new StringTokenizer(br.readLine());
                int choice = Integer.parseInt(s.nextToken());
                items.get(temp-1).setOffer(choice);
                break;
            case 5:
                System.out.println("Rewards won: "+ (10 - max_items));
                break;
            case 6:
                k = 0;
                break;
            default:
                System.out.println("Invalid option");
        }
        return k;
    }
    @Override
    public int search(String str, int k) {
        for (Items item : items) {
            k = item.checkCategory(str,k);
        }
        return k;
    }
    @Override
    public int searchp(String str, int k) {
        for (Items item : items) {
            k = item.checkCategoryp(str,k);
        }
        return k;
    }
    @Override
    public float reward(String str, int idx, float quantity) {
        int total = 0;
        int temp = 0;
        for(Items item: items) {
            temp = item.checkCategoryp(str,temp);
            if(temp == idx) {
                if(item.getQuantity()<(int)quantity)
                    return -1;
                else {
                    if(item.getOffer()==0)
                        return item.getPrice()*(int)quantity;
                    else if(item.getOffer()==1)
                        return item.getPrice()*((int)((int)quantity+1)/2);
                    else
                        return (float)(item.getPrice()*(quantity)*0.75);
                }
            }
        }
        return total;
    }
    @Override
    public String finish(String str, int idx, int quantity) {
        int temp = 0;
        String s = "Bought item ";
        for(Items item: items) {
            temp = item.checkCategoryp(str,temp);
            if(temp == idx) {
                item.setQuantity(item.getQuantity()-quantity);
                s += item.getName() + " quantity: " + quantity + " for Rs: " + quantity*item.getPrice() + " from Merchant " + this.name;
                if(item.getOffer() == 0)
                    cash += quantity*item.getPrice();
                else if(item.getOffer() == 1)
                    cash += (float)(quantity+1)*item.getPrice()/2;
                else
                    cash += (float)(0.75)*item.getPrice()*quantity;
                max_items = ((int)cash)%20000;
                return s;
            }
        }
        return s;
    }
    @Override
    public String getCategory(int idx) {
        return category.get(idx-1);
    }
    @Override
    public String getName() {
        return name;
    }
}

public class Main {
    public static void main(String[] args) throws IOException {
        float account=0;
        boolean finish = true;
        ArrayList<Shop> merch = new ArrayList<Shop>();
        Shop sh = new Merchant("Jack", "A");
        merch.add(sh);
        sh = new Merchant("John", "B");
        merch.add(sh);
        sh = new Merchant("James", "C");
        merch.add(sh);
        sh = new Merchant("Jeff", "D");
        merch.add(sh);
        sh = new Merchant("Joseph", "E");
        merch.add(sh);
        ArrayList<Shop> cust = new ArrayList<Shop>();
        sh = new Customer("Ali", "A");
        cust.add(sh);
        sh = new Customer("Nobby", "B");
        cust.add(sh);
        sh = new Customer("Bruno", "C");
        cust.add(sh);
        sh = new Customer("Borat", "D");
        cust.add(sh);
        sh = new Customer("Aladeen", "E");
        cust.add(sh);
        while(finish) {
            System.out.println("Welcome to Mercury");
            System.out.println("1) Enter as Merchant");
            System.out.println("2) Enter as Coustomer");
            System.out.println("3) See user details");
            System.out.println("4) Company account balance");
            System.out.println("5) Exit");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            StringTokenizer s = new StringTokenizer(br.readLine());
            int check = Integer.parseInt(s.nextToken());
            switch (check) {
                case 1: // Enter as Merchant
                    for(int i=0;i<merch.size();i++) {
                        System.out.println(i+1 + ") " + merch.get(i).getName());
                    }
                    s = new StringTokenizer(br.readLine());
                    int merchnum = Integer.parseInt(s.nextToken());
                    int k = -1;
                    while(k!=0) {
                        k = merch.get(merchnum-1).display();
                        if(k!=-1 && k!=0) {
                            String str = merch.get(merchnum-1).getCategory(k);
                            int num = 1;
                            for (Shop shop : merch) {
                                num = shop.search(str,num);
                            }
                        }
                    }
                    break;
                case 2: // Enter as Cust
                    for(int i=0;i<cust.size();i++) {
                        System.out.println(i+1 + ") " + cust.get(i).getName());
                    }
                    s = new StringTokenizer(br.readLine());
                    int custnum = Integer.parseInt(s.nextToken());
                    k = -1;
                    while(k!=0) {
                        k = cust.get(custnum-1).display();
                        if(k == -2) {
                            ArrayList<String> cart = cust.get(custnum-1).getCart();
                            boolean flag = false;
                            for(String str1 : cart) {
                                s = new StringTokenizer(str1);
                                String str = s.nextToken();
                                int item_code = Integer.parseInt(s.nextToken());
                                int item_quantity = Integer.parseInt(s.nextToken());
                                int num = 0;
                                int j=0;
                                int prev = 0;
                                for(;j<merch.size();j++) {
                                    num = merch.get(j).searchp(str, num);
                                    if (num < item_code) {
                                        prev = num;
                                        continue;
                                    }
                                    int idx = num - prev;
                                    float price = merch.get(j).reward(str, idx, (float) item_quantity);
                                    if (price == -1) {
                                        System.out.println("Unsuccessful transaction");
                                        flag = true;
                                        break;
                                    }
                                    else {
                                        price = cust.get(custnum - 1).reward(str, idx, price);
                                        if (price == -1) {
                                            System.out.println("Not enough money in accound");
                                            flag = true;
                                            break;
                                        }
                                        else {
                                            account += 0.01 * price;
                                            str = merch.get(j).finish(str, idx, item_quantity);
                                            cust.get(custnum - 1).finish(str, idx, (int) price);
                                            System.out.println("Item successfully bought");
                                            break;
                                        }
                                    }
                                }
                                if(flag)
                                    break;
                            }
                        }
                        else if(k>0) {
                            String str = Customer.all_category.get(k-1);
                            int num = 1;
                            for (Shop shop : merch) {
                                num = shop.search(str,num);
                            }
                            System.out.println("Enter Item code");
                            s = new StringTokenizer(br.readLine());
                            int item_code = Integer.parseInt(s.nextToken());
                            System.out.println("Enter item quantity");
                            s = new StringTokenizer(br.readLine());
                            int item_quantity = Integer.parseInt(s.nextToken());
                            System.out.println("Choose a method of transaction");
                            System.out.println("1) Buy now");
                            System.out.println("2) Add to cart");
                            System.out.println("3) Exit");
                            s = new StringTokenizer(br.readLine());
                            int choice = Integer.parseInt(s.nextToken());
                            if(choice>0) {
                                // figure out which item customer chose
                                num = 0;
                                int j=0;
                                int prev = 0;
                                for(;j<merch.size();j++) {
                                    num = merch.get(j).searchp(str, num);
                                    if (num < item_code) {
                                        prev = num;
                                        continue;
                                    }
                                    int idx = num - prev;
                                    if (choice == 1) {
                                        float price = merch.get(j).reward(str, idx, (float)item_quantity);
                                        if(price == -1) {
                                            System.out.println("Unsuccessful transaction");
                                        }
                                        else {
                                            price = cust.get(custnum-1).reward(str, idx, price);
                                            if(price == -1) {
                                                System.out.println("Not enough money in accound");
                                            }
                                            else {
                                                account += 0.01 * price;
                                                str = merch.get(j).finish(str,idx,item_quantity);
                                                cust.get(custnum-1).finish(str,idx,(int)price);
                                                // add history to customer name of merchant item name quantity item price
                                                System.out.println("Item successfully bought");
                                            }
                                        }
                                    }
                                    if(choice == 2) {
                                        str = str + " " + num + " " + (int)(item_quantity);
                                        cust.get(custnum-1).searchp(str,idx);
                                        System.out.println("Item added to cart");
                                    }
                                    break;
                                }
                            }
                        }
                    }
                    break;
                case 3:
                    System.out.println("Merchant or Customer? (M/C)");
                    s = new StringTokenizer(br.readLine());
                    String ch = s.nextToken();
                    if(ch.compareTo("M")==0) {
                        for(Shop shop : merch)
                            shop.details();
                    }
                    else {
                        for(Shop shop: cust) {
                            shop.details();
                        }
                    }
                    break;
                case 4:
                    System.out.println("Balance: "+account);
                    break;
                case 5: finish = false;
            }
        }
    }
}