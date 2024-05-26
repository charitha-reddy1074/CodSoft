import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
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

    public List<Student> getAllStudents() {
        return new ArrayList<>(students);
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

// GUI Class
public class SMS {
    private StudentManagementSystem sms = new StudentManagementSystem();
    private JFrame frame;
    private DefaultTableModel tableModel;
    private JTable studentTable;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SMS().createAndShowGUI());
    }

    private void createAndShowGUI() {
        frame = new JFrame("Student Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        String[] columnNames = {"Name", "Roll Number", "Grade", "Address"};
        tableModel = new DefaultTableModel(columnNames, 0);
        studentTable = new JTable(tableModel);
        frame.add(new JScrollPane(studentTable), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add Student");
        JButton removeButton = new JButton("Remove Student");
        JButton searchButton = new JButton("Search Student");
        JButton displayButton = new JButton("Display All Students");
        JButton exitButton = new JButton("Exit");

        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(displayButton);
        buttonPanel.add(exitButton);

        frame.add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> promptAddStudent());
        removeButton.addActionListener(e -> promptRemoveStudent());
        searchButton.addActionListener(e -> promptSearchStudent());
        displayButton.addActionListener(e -> displayAllStudents());
        exitButton.addActionListener(e -> System.exit(0));

        frame.setVisible(true);
    }

    private void promptAddStudent() {
        JTextField nameField = new JTextField();
        JTextField rollNumberField = new JTextField();
        JTextField gradeField = new JTextField();
        JTextField addressField = new JTextField();

        Object[] message = {
            "Name:", nameField,
            "Roll Number:", rollNumberField,
            "Grade:", gradeField,
            "Address:", addressField
        };

        int option = JOptionPane.showConfirmDialog(frame, message, "Add Student", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                String name = nameField.getText();
                int rollNumber = Integer.parseInt(rollNumberField.getText());
                String grade = gradeField.getText();
                String address = addressField.getText();

                Student student = new Student(name, rollNumber, grade, address);
                sms.addStudent(student);
                displayMessage("Student added successfully!");
                refreshTableData();
            } catch (NumberFormatException ex) {
                displayMessage("Invalid input for roll number. Please enter a valid integer.");
            }
        }
    }

    private void promptRemoveStudent() {
        String rollNumberStr = JOptionPane.showInputDialog(frame, "Enter roll number to remove:");
        if (rollNumberStr != null && !rollNumberStr.isEmpty()) {
            try {
                int rollNumber = Integer.parseInt(rollNumberStr);
                sms.removeStudent(rollNumber);
                displayMessage("Student removed successfully!");
                refreshTableData();
            } catch (NumberFormatException ex) {
                displayMessage("Invalid input for roll number. Please enter a valid integer.");
            }
        }
    }

    private void promptSearchStudent() {
        String rollNumberStr = JOptionPane.showInputDialog(frame, "Enter roll number to search:");
        if (rollNumberStr != null && !rollNumberStr.isEmpty()) {
            try {
                int rollNumber = Integer.parseInt(rollNumberStr);
                Student student = sms.searchStudent(rollNumber);
                if (student != null) {
                    displayMessage("Student found: " + student);
                } else {
                    displayMessage("Student not found.");
                }
            } catch (NumberFormatException ex) {
                displayMessage("Invalid input for roll number. Please enter a valid integer.");
            }
        }
    }

    private void displayAllStudents() {
        refreshTableData();
    }

    private void refreshTableData() {
        tableModel.setRowCount(0); // Clear existing data
        for (Student student : sms.getAllStudents()) {
            tableModel.addRow(new Object[]{student.getName(), student.getRollNumber(), student.getGrade(), student.getAddress()});
        }
    }

    private void displayMessage(String message) {
        JOptionPane.showMessageDialog(frame, message);
    }
}
