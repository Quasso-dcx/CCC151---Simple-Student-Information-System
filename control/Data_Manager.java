package control;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.JTable;

import model.Course;
import model.Student;
import model.StudentKeyMaker;
import model.Table_Manager;

/*
 * Facilitates the process of converting the data from writer files to objects of Course and Student
 */
public class Data_Manager {
    // files
    private static final String student_file = "CSV_Files\\Students.csv";
    private static final String course_file = "CSV_Files\\Courses.csv";

    private static HashMap<String, Course> courses = new HashMap<>(); // store the registered courses
    private static HashMap<String, Student> students = new HashMap<>(); // store the registered students
    private static Course unenrolled_course = new Course("N/A", "Unenrolled"); // default course
    private static String[] course_column;
    private static String[] student_column;

    private BufferedReader reader;
    private String line;

    public Data_Manager() {
        courseFileReader();
        studentFileReader();
    }

    /*
     * Process the saving of the data from the course table to the course writer
     * file
     */
    public static void courseFileSaver() {
        try {
            // setup the table and the writer
            JTable table = Table_Manager.getCourseTable();
            FileWriter writer = new FileWriter(new File(course_file));

            // save the column names first
            for (int column = 0; column < table.getColumnCount(); column++) {
                if (column == table.getColumnCount() - 1)
                    writer.write(table.getColumnName(column)); // to avoid ',' in the end
                else
                    writer.write(table.getColumnName(column) + ",");
            }
            writer.write("\n");

            // save the data from the table
            for (int row = 0; row < table.getRowCount(); row++) {
                for (int column = 0; column < table.getColumnCount(); column++) {
                    if (column == table.getColumnCount() - 1)
                        writer.write(table.getValueAt(row, column).toString()); // to avoid ',' in the end
                    else
                        writer.write(table.getValueAt(row, column).toString() + ",");
                }
                writer.write("\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * Process the saving of the data from the student table to the student writer
     * file
     */
    public static void studentFileSaver() {
        try {
            // setup the table and the writer
            JTable table = Table_Manager.getStudentTable();
            FileWriter writer = new FileWriter(new File(student_file));

            // save the column names first
            for (int column = 0; column < table.getColumnCount(); column++) {
                if (column == table.getColumnCount() - 1)
                    writer.write(table.getColumnName(column)); // to avoid ',' in the end
                else
                    writer.write(table.getColumnName(column) + ",");
            }
            writer.write("\n");

            // save the data from the table
            for (int row = 0; row < table.getRowCount(); row++) {
                for (int column = 0; column < table.getColumnCount(); column++) {
                    if (column == table.getColumnCount() - 1)
                        writer.write(table.getValueAt(row, column).toString()); // to avoid ',' in the end
                    else
                        writer.write(table.getValueAt(row, column).toString() + ",");
                }
                writer.write("\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * Process the reading of the data from the student writer file to the student
     * table
     */
    private void studentFileReader() {
        try {
            // setup the reader
            reader = new BufferedReader(new FileReader(student_file));

            // save the column names
            student_column = reader.readLine().split(",");

            // read the lines from the file
            while ((line = reader.readLine()) != null) {
                // split the lines per column from writer
                String[] row = line.split(",");

                // incase there is not enought split words
                if (row.length != student_column.length) {
                    for (String word : row)
                        System.out.printf("%-15s", word);
                    System.out.println();
                    continue;
                }

                Student new_student = new Student(row[0], row[1], row[2], row[3], row[4], row[5],
                        unenrolled_course.getCourseCode(), unenrolled_course.getCourseName());
                // traverse the course list for the enrolled course

                Course course = unenrolled_course; // incase the course recorded was deleted or something happened
                if (courses.containsKey(row[6])) {
                    course = courses.get(row[6]);
                }

                new_student.setCourseCode(course.getCourseCode());
                new_student.setCourseName(course.getCourseName());

                String new_student_key = new StudentKeyMaker().keyMaker(new_student.getCourseCode(), new_student.getIDNumber());
                students.put(new_student_key, new_student);
                course.getBlockIDs().add(new_student.getIDNumber());
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * Process the reading of the data from the course writer file to the course
     * table
     */
    private void courseFileReader() {
        courses.put(unenrolled_course.getCourseCode(), unenrolled_course);
        try {
            // setup the reader
            reader = new BufferedReader(new FileReader(course_file));
            course_column = reader.readLine().split(",");

            while ((line = reader.readLine()) != null) {
                // split the lines per column from writer
                String[] row = line.replaceAll("\"", "").split(",");

                // incase there is not enought split words
                if (row.length != course_column.length) {
                    for (String word : row)
                        System.out.printf("%-15s", word);
                    System.out.println();
                    continue;
                }
                // add the course in the course list
                courses.put(row[0], new Course(row[0], row[1]));
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * Return the course list.
     */
    public static HashMap<String, Course> coursesList() {
        return courses;
    }

    public static HashMap<String, Student> studentList(){
        return students;
    }

    /*
     * Return the column names for the student column
     */
    public static String[] getStudentColumn() {
        return student_column;
    }

    /*
     * Return the column names for the course column
     */
    public static String[] getCourseColumn() {
        return course_column;
    }

    /*
     * Return the "Course" for unenrolled student
     */
    public static Course notEnrolled() {
        return unenrolled_course;
    }
}
