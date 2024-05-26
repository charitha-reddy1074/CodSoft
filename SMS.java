import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.*;

// Student Class
class Student {
    private String name;
    private int rollNumber;
    private String grade;
    private String address;

    public Student(String name, int rollNumber, String grade, String address) {
        this.name = name;
        this.rollNumber = rollNumber;
        this.grade = grade;
        this.address = address;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(int rollNumber) {
        this.rollNumber = rollNumber;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Student [name=" + name + ", rollNumber=" + rollNumber + ", grade=" + grade + ", address=" + address + "]";
    }
}

// StudentManagementSystem Class
class StudentManagementSystem {
    private List<Student> students;
    private final String fileName = "students.txt";

    public StudentManagementSystem() {
        this.students = new ArrayList<>();
        loadStudentsFromFile();
    }

    public void addStudent(Student student) {
        students.add(student);
        saveStudentsToFile();
    }

    public void removeStudent(int rollNumber) {
        students.removeIf(student -> student.getRollNumber() == rollNumber);
        saveStudentsToFile();
    }

    public Student searchStudent(int rollNumber) {
        for (Student student : students) {
            if (student.getRollNumber() == rollNumber) {
                return student;
            }
        }
        return null;
    }

    public void displayAllStudents() {
        for (Student student : students) {
            System.out.println(student);
        }
    }

    private void loadStudentsFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String name = parts[0];
                int rollNumber = Integer.parseInt(parts[1]);
                String grade = parts[2];
                String address = parts[3];
                students.add(new Student(name, rollNumber, grade, address));
            }
        } catch (IOException e) {
            System.out.println("Error loading students from file: " + e.getMessage());
        }
    }

    private void saveStudentsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (Student student : students) {
                writer.write(student.getName() + "," + student.getRollNumber() + "," + student.getGrade() + "," + student.getAddress());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving students to file: " + e.getMessage());
        }
    }
}

// Main Class for Console-Based UI
public class SMS {
    private static StudentManagementSystem sms = new StudentManagementSystem();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("1. Add Student");
            System.out.println("2. Remove Student");
            System.out.println("3. Search Student");
            System.out.println("4. Display All Students");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    addStudent();
                    break;
                case 2:
                    removeStudent();
                    break;
                case 3:
                    searchStudent();
                    break;
                case 4:
                    sms.displayAllStudents();
                    break;
                case 5:
                    System.exit(0);
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }

    private static void addStudent() {
        String name = getInput("Enter name: ");
        int rollNumber = getValidatedIntInput("Enter roll number: ");
        String grade = getInput("Enter grade: ");
        String address = getInput("Enter address: ");

        Student student = new Student(name, rollNumber, grade, address);
        sms.addStudent(student);
        System.out.println("Student added successfully!");
    }

    private static void removeStudent() {
        int rollNumber = getValidatedIntInput("Enter roll number to remove: ");
        sms.removeStudent(rollNumber);
        System.out.println("Student removed successfully!");
    }

    private static void searchStudent() {
        int rollNumber = getValidatedIntInput("Enter roll number to search: ");
        Student student = sms.searchStudent(rollNumber);
        if (student != null) {
            System.out.println("Student found: " + student);
        } else {
            System.out.println("Student not found.");
        }
    }

    private static String getInput(String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine();
        while (input.isEmpty()) {
            System.out.println("Input cannot be empty. Please try again.");
            System.out.print(prompt);
            input = scanner.nextLine();
        }
        return input;
    }

    private static int getValidatedIntInput(String prompt) {
        int input = -1;
        boolean valid = false;
        while (!valid) {
            try {
                System.out.print(prompt);
                input = Integer.parseInt(scanner.nextLine());
                valid = true;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }
        return input;
    }
}
