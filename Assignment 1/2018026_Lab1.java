import java.util.*;
import java.io.*;
import javafx.util.*;

class Student {
    static private int last_roll = 0;
    //keeps track of how many students have been entered and distinct roll no. (isn't object dependent)
    static private int placedcounter = 0;
    // keeps track of how many students have been placed (isn't object dependent)
    private int roll_number;
    // roll number of the given student
    private float gpa;
    // gpa of the given student
    private String stream;
    // stream of the given student
    private ArrayList<Pair<String, Integer>> test;
    // tests the student has given, string stroes name of company, and int stores the score
    private boolean placed;
    // boolean to represent whether the student is placed or not
    private String company;
    // used to represent which company the student got placed in
    Student(float _gpa, String _stream) {
        /*
         Contructor for student class. A lot of attributes don't have setter functions like roll no and gpa
         and are assigned only once here
        */
        last_roll++;
        test = new ArrayList<Pair<String, Integer>>();
        company = "";
        roll_number = last_roll;
        placed = false;
        gpa = _gpa;
        stream = _stream;
    }
    public void applied() {
        // prints the name of company along with the makes in the technical round of the student
        for(int i=0;i<test.size();i++) {
            System.out.println(test.get(i).getKey()+" "+test.get(i).getValue());
        }
    }
    public float getGpa() {
        //accessor function for gpa attribute
        return gpa;
    }
    public void setPlaced(String _company) {
        /*
         setter function for placed attribute. Takes the name of the company as argument as well
         This is done because we know both these info at the same amount of time and don't want a record of a placed
         student without knowing which company he got placed to
        */
        placed = true;
        placedcounter++;
        company = _company;
    }
    public void display() {
        // displays all the relevant information about the student
        System.out.println(roll_number);
        System.out.println(gpa);
        System.out.println(stream);
        if(placed) {
            System.out.println("Placement Status: Placed");
            System.out.println(company);
        }
        else {
            System.out.println("Placement Status: Not Placed");
        }
    }
    public static int getUnplaced() {
        // returns the number of unplaced students.
        return last_roll - placedcounter;
    }
    public boolean getPlaced() {
        // accessor function to check whether given student has been placed or not
        return placed;
    }
    public static boolean stop() {
        // Used to determine the termination condition of the program, i.e all students have been placed
        if (placedcounter<last_roll)
            return true;
        else
            return false;
    }
    public boolean stream_check(String i) {
        // To ensure that the company only has restricted information about the students, i.e in this case whether the
        // student has the stream they are interested or not
        if ((i.compareTo(stream)) == 0)
            return true;
        else
            return false;
    }
    public int getRoll_number() {
        // accessor function for the roll_number attribute to uniquely recognize the student
        return roll_number;
    }
    public void addScore(int score, String name) {
        // To add a company and how much the student scored in its technical round
        Pair<String,Integer> p = new Pair<String,Integer>(name, score);
        test.add(p);
    }
}

class Company {
    private String name;
    // Stores the name of the company
    private ArrayList<String> courses;
    // Stores the list of courses eligible courses
    private int required;
    // Stores the number of students required by the company
    private boolean status;
    // Used to represent the application status of the company. true -> OPEN and false ->CLOSe
    private ArrayList<Integer> selected;
    // Stores the list of students (their roll numbers) who have been selected by the company
    private ArrayList<Pair<Pair<Integer,Float>,Integer>> tentative;
    // Stores the list of tentative students along with their technical round scores and GPA
    Company(String _name, ArrayList<String> _courses, int _required, ArrayList<Pair<Pair<Integer,Float>,Integer>> _tentative) {
        /*
         Rarameterised contructor to initial and allocate memory to various attributes of the class object.
         It is used to ensure that we don't need modifier functions for all the attributes.
         Most are set at the time of declaration itself
        */
        name = _name;
        courses = _courses;
        required = _required;
        status = true;
        tentative = _tentative;
        selected = new ArrayList<Integer>();
    }
    public void setSelected(ArrayList<Integer> _selected) {
        // modifier function to store the final list of selected students
        selected = _selected;
    }
    public int getRequired() {
        // returns the number of students required by the company
        return required;
    }
    public void setStatus() {
        // changes the application status of the company to closed
        status = false;
    }
    public ArrayList<Pair<Pair<Integer,Float>, Integer>> getTentative() {
        /*
         returns the tentative list of selected students by the company.
         (Has all eligible students in the order of preference by the company)
        */
        return tentative;
    }
    public String getName() {
        // accessor funciton for the name attribute of the company
        return name;
    }
    public boolean isStatus() {
        // accessor function for the application status of the company
        return status;
    }
    public void display() {
        // displays the relevant information about the company
        System.out.println(name);
        System.out.println("Course Criteria");
        for(String i : courses) {
            System.out.println(i);
        }
        System.out.println("Number Of Required Students = " + required);
        System.out.print("Application Status = ");
        if(status)
            System.out.println("OPEN");
        else
            System.out.println("CLOSE");
    }
}

class Sortbyscore implements Comparator<Pair<Pair<Integer,Float>,Integer>> {
    // Class used to sort the list of tentative candidates. A custom comparator is implemented
    public int compare(Pair<Pair<Integer,Float>,Integer> s, Pair<Pair<Integer,Float>,Integer> t) {
        // Sorts the list in descending order first by technical scores and then by GPA
        int k = t.getKey().getKey() - s.getKey().getKey();
        if(k==0)
            if(t.getKey().getValue() - s.getKey().getValue()>0)
                return 1;
            else
                return -1;
        return k;
    }
}

class Lab {
    public static void main(String[] args) throws IOException {
        // main fucntion used to run the code
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer s = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(s.nextToken());
        ArrayList<Student> student = new ArrayList<Student>();
        ArrayList<Company> company = new ArrayList<Company>();
        for(int i=0;i<n;i++) {
            // Used to add student data
            s = new StringTokenizer(br.readLine());
            float gpa = Float.parseFloat(s.nextToken());
            String course = s.nextToken();
            Student sd = new Student(gpa,course);
            student.add(sd);
        }
        while(Student.stop()) {
            // Loops until all the students have been placed. (not removed from the database)
            s = new StringTokenizer(br.readLine());
            int k = Integer.parseInt(s.nextToken());
            switch(k) {
                // different cases depending of the query from 1-9
                case 1: s = new StringTokenizer(br.readLine());
                        String name = s.nextToken();
                        System.out.print("Number Of Eligible Course = ");
                        s = new StringTokenizer(br.readLine());
                        int difcourse = Integer.parseInt(s.nextToken());
                        ArrayList<String> courses = new ArrayList<String>();
                        for(int i=0;i<difcourse;i++) {
                            s = new StringTokenizer(br.readLine());
                            String temp = s.nextToken();
                            courses.add(temp);
                        }
                        System.out.print("Number of Required Students = ");
                        s = new StringTokenizer(br.readLine());
                        int required = Integer.parseInt(s.nextToken());
                        System.out.println("Application Status = OPEN");
                        System.out.println("Enter scores for the technical round");
                        ArrayList<Pair<Pair<Integer,Float>,Integer>> tentative = new ArrayList<Pair<Pair<Integer,Float>,Integer>>();
                        for(String str : courses) {
                            for(int i=0;i<student.size();i++) {
                                if(student.get(i).stream_check(str)) {
                                    System.out.println("Enter score for Roll no. " + student.get(i).getRoll_number());
                                    s = new StringTokenizer(br.readLine());
                                    int sc = Integer.parseInt(s.nextToken());
                                    student.get(i).addScore(sc,name);
                                    Pair<Integer,Float> tempp = new Pair<Integer,Float> (sc,student.get(i).getGpa());
                                    Pair<Pair<Integer,Float>,Integer> pl = new Pair<Pair<Integer,Float>,Integer> (tempp,student.get(i).getRoll_number());
                                    tentative.add(pl);
                                }
                            }
                        }
                        Collections.sort(tentative, new Sortbyscore());
                        Company temp = new Company(name,courses,required,tentative);
                        company.add(temp);
                    break;
                case 2: System.out.println("Accounts removed for");
                        for(int i=0;i<student.size();i++) {
                           if(student.get(i).getPlaced()) {
                                System.out.println(student.get(i).getRoll_number());
                                student.remove(i);
                                i--;
                            }
                        }
                    break;
                case 3: System.out.println("Accounts removed for");
                        for(int i=0;i<company.size();i++) {
                            if(!company.get(i).isStatus()) {
                                System.out.println(company.get(i).getName());
                                company.remove(i);
                                i--;
                            }
                        }
                    break;
                case 4: System.out.println(Student.getUnplaced()+" students left");
                    break;
                case 5: for(int i=0;i<company.size();i++) {
                            if(company.get(i).isStatus()) {
                                System.out.println(company.get(i).getName());
                            }
                        }
                    break;
                case 6: String comp = s.nextToken();
                        for(int i=0;i<company.size();i++) {
                            if(comp.compareTo(company.get(i).getName())==0) {
                                ArrayList<Integer> selected = new ArrayList<Integer>();
                                ArrayList<Pair<Pair<Integer,Float>,Integer>> tent = company.get(i).getTentative();
                                for(int j=0;j<tent.size();j++) {
                                    for(int x=0;x<student.size();x++) {
                                        if(student.get(x).getRoll_number() == tent.get(j).getValue())
                                            if(!student.get(x).getPlaced()) {
                                                student.get(x).setPlaced(company.get(i).getName());
                                                selected.add(tent.get(j).getValue());
                                            }
                                            else
                                                break;
                                    }
                                    if(selected.size()>=company.get(i).getRequired()) {
                                        company.get(i).setSelected(selected);
                                        company.get(i).setStatus();
                                        break;
                                    }
                                }
                                if(selected.size()<company.get(i).getRequired()) {
                                    company.get(i).setSelected(selected);
                                    company.get(i).setStatus();
                                }
                                System.out.println("Roll Number of Selected Students");
                                for(int x: selected)
                                    System.out.println(x);
                            }
                        }
                    break;
                case 7: String compy = s.nextToken();
                        for(int i=0;i<company.size();i++) {
                            if(compy.compareTo(company.get(i).getName())==0) {
                                company.get(i).display();
                                break;
                            }
                        }
                    break;
                case 8: int studd = Integer.parseInt(s.nextToken());
                        for(int i=0;i<student.size();i++)
                            if(studd == student.get(i).getRoll_number())
                                student.get(i).display();
                    break;
                case 9: int check = Integer.parseInt(s.nextToken());
                        boolean flag = true;
                        for(int i=0;i<student.size();i++)
                            if(check == student.get(i).getRoll_number()) {
                                student.get(i).applied();
                                flag = false;
                            }
                        if(flag)
                            System.out.println("No student with the given roll number has an account");
                    break;
                default: System.out.println("Invalid option");
            }
        }
    }
}