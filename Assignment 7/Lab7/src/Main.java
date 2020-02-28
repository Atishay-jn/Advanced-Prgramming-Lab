import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

class Fibonacci {
    // Class to store Fibonacci numbers computed. Implements design pattern flyweight
    private static volatile HashMap<Integer, Fibonacci> map = new HashMap<>();
    int n;
    long computation;
    int value;
    static {
        // Initilization block
        Fibonacci temp = new Fibonacci(0);
        temp.setValue(0);
        map.put(0,temp);
        temp = new Fibonacci(1);
        temp.setValue(1);
        map.put(1,temp);
    }
    Fibonacci(int n) {
        // Contructor
        this.n = n;
        value = -1;
    }
    public void setComputation(long computation) {
        // Accessor function
        this.computation = computation;
    }
    public int getValue() {
        // Accessor function
        return value;
    }
    public void setValue(int value) {
        // Accessor function
        this.value = value;
    }
    public void run() {
        // storing value of computed numbers
        value = calculate(n);
        map.put(n,this);
    }
    public static int query(int num) {
        // Accessor function
        return map.get(num).getValue();
    }
    public int getN() {
        // Accessor function
        return n;
    }
    private static int calculate(int n) {
        // Does the actual compute. Function name must be compute
        if(n<0)
            System.out.println("Invalid input");
        if(map.containsKey(n))
            return map.get(n).getValue();
        else {
            if(map.containsKey(n-1)) {
                return map.get(n-1).getValue() + map.get(n-2).getValue();
            }
            Fibonacci temp = new Fibonacci(n-1);
            temp.setValue(calculate(n-1));
            map.put(n-1,temp);
            return map.get(n-1).getValue() + map.get(n-2).getValue();
        }
    }
}

class CustomThread extends Thread {
    private final Object obj;
    private static volatile ArrayList<Fibonacci> pending;
    private static volatile int pending_position;
    private static boolean flag = true;
    static volatile long[] computationArray = new long[1000];
    static volatile ArrayList<Integer> map = new ArrayList<>();
    static {
        pending_position = 0;
        pending = new ArrayList<>();
    }
    CustomThread(Object obj) {
        this.obj = obj;
    }
    public static void addPending(Fibonacci fib) {
        pending.add(fib);
    }
    @Override
    public void run() {
        // Function called when thread executed, can't change its name
        while(flag) {
            long computation = 0;
            Fibonacci temp = null;
            int position = 0;
            synchronized (obj) {
                try {
                    obj.wait();
                    computation = System.currentTimeMillis();
                    if(pending.size()>pending_position) {
                        temp = pending.get(pending_position);
                        position = pending_position;
                        pending_position++;
                    }
                }
                catch (InterruptedException ignored) {
                }
            }
            if (temp != null)
                temp.run();
            computationArray[position] = System.currentTimeMillis() - computation;
        }
    }
    private static synchronized Fibonacci get_task() {
        if(pending.size()>pending_position) {
            pending_position++;
            return pending.get(pending_position-1);
        }
        return null;
    }
    public static boolean isWork() {
        return pending_position == pending.size();
    }
    public static void stopProgram() {
        flag = false;
    }
}

class Multiprocessing {
    private final Object obj;
    CustomThread[] t;
    private ArrayList<Integer> answer;
    private int answer_position;
    Multiprocessing(int num_threads) {
        // Constructor
        obj = new Object();
        answer = new ArrayList<>();
        t = new CustomThread[num_threads];
        for(int i=0;i<num_threads;i++) {
            t[i] = new CustomThread(obj);
            t[i].start();
        }
        answer_position = 0;
    }
    public void runInput() throws IOException, InterruptedException {
        // Implementing Design Pattern Facade with the help of swithc case
        System.out.println("1) Calculate the nth Fibonacci number: ");
        System.out.println("2) Display all pending values");
        System.out.println("3) Display all pending values and exit");
        System.out.print("Enter Choice: ");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer s = new StringTokenizer(br.readLine());
        int choice = Integer.parseInt(s.nextToken());
        switch(choice) {
            case 1:
                System.out.print("Enter number: ");
                s = new StringTokenizer(br.readLine());
                int n = Integer.parseInt(s.nextToken());
                Fibonacci fib = new Fibonacci(n);
                boolean flag = true;
                answer.add(n);
                CustomThread.addPending(fib);
                synchronized (obj) {
                    obj.notifyAll();
                }
                break;
            case 2:
                while(!CustomThread.isWork()) {
                    obj.notifyAll();
                    Thread.sleep(100);
                }
                for(;answer_position<answer.size();answer_position++) {
                    System.out.println(answer.get(answer_position)+": "+Fibonacci.query(answer.get(answer_position))+" \nComputation time: "+CustomThread.computationArray[answer_position]);
                }
                break;
            case 3:
                while(!CustomThread.isWork()) {
                    obj.notifyAll();
                    Thread.sleep(100);
                }
                for(;answer_position<answer.size();answer_position++) {
                    System.out.println(answer.get(answer_position)+": "+Fibonacci.query(answer.get(answer_position))+" \nComputation time: "+CustomThread.computationArray[answer_position]);
                }
                System.out.println("Exiting from program");
                CustomThread.stopProgram();
                synchronized (obj) {
                    obj.notifyAll(); // this ensures all thread stop before exiting
                }
                Thread.sleep(100);
                System.exit(0);
                break;
            default:
                System.out.println("Invalid input, please try again");
        }
        runInput();
    }
}

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        // Main function used for I/O operations primarily
        System.out.print("Number of Consumer Threads: ");
        int k;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer s = new StringTokenizer(br.readLine());
        k = Integer.parseInt(s.nextToken());
        Multiprocessing exec = new Multiprocessing(k); //Creating instance of Multiprocessing
        exec.runInput(); //Running for number k
    }
}