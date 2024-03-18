import java.sql.*;
import java.util.Scanner;

public class Main {
    private static Connection connection;

    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/University";
        String user = "postgres";
        String password = "admin";
        boolean check = true;

        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, user, password);

            while (check) {
                System.out.println("1. Get all students");
                System.out.println("2. Add student");
                System.out.println("3. Update student email");
                System.out.println("4. Delete student");
                System.out.println("5. Exit\n");

                Scanner input = new Scanner(System.in);
                System.out.print("Please make a selection: ");

                switch (input.nextInt()) {
                    case 1:
                        getAllStudents();
                        break;
                    case 2:
                        System.out.print("Enter first name: ");
                        String first = input.next();

                        System.out.print("Enter last name: ");
                        String last = input.next();

                        System.out.print("Enter email address: ");
                        String email = input.next();

                        System.out.print("Enter enrollment date (YYYY-MM-DD): ");
                        String date = input.next();

                        addStudent(first, last, email, date);
                        break;
                    case 3:
                        System.out.print("Enter student ID: ");
                        int id = input.nextInt();

                        System.out.print("Enter new email address: ");
                        String newEmail = input.next();

                        updateStudentEmail(id, newEmail);
                        break;
                    case 4:
                        System.out.print("Enter student ID: ");
                        deleteStudent(input.nextInt());
                        break;
                    case 5:
                        check = false;
                        break;
                }
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    private static void getAllStudents() throws SQLException {
        // Retrieve and display all records from the students table
        String query = "SELECT * FROM students ORDER BY student_id ASC";
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();
        System.out.println();
        while (resultSet.next()) {
            System.out.println("Student ID: " + resultSet.getInt("student_id") +
                    ", First Name: " + resultSet.getString("first_name") +
                    ", Last Name: " + resultSet.getString("last_name") +
                    ", Email: " + resultSet.getString("email") +
                    ", Enrollment Date: " + resultSet.getDate("enrollment_date"));
        }
        System.out.println();
    }

    private static void addStudent(String firstName, String lastName, String email, String enrollmentDate) throws SQLException {
        // Insert a new student record into the students table
        String query = "INSERT INTO students (first_name, last_name, email, enrollment_date) VALUES (?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, firstName);
        statement.setString(2, lastName);
        statement.setString(3, email);
        statement.setDate(4, Date.valueOf(enrollmentDate));
        statement.executeUpdate();
        System.out.println("New student added successfully.\n");
    }

    private static void updateStudentEmail(int studentId, String newEmail) throws SQLException {
        // Update the email address for a student with the specified student_id
        String query = "UPDATE students SET email = ? WHERE student_id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, newEmail);
        statement.setInt(2, studentId);
        statement.executeUpdate();
        System.out.println("Email updated successfully.\n");
    }

    private static void deleteStudent(int studentId) throws SQLException {
        // Delete the record of the student with the specified student_id
        String query = "DELETE FROM students WHERE student_id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, studentId);
        statement.executeUpdate();
        System.out.println("Student deleted successfully.\n");
    }
}