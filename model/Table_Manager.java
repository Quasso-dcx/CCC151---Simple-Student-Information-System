package model;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import control.Data_Manager;

/*
 * Facilitates the creation and functionalities of the tables
 */
public class Table_Manager {
    private static JTable students_table;
    private static JTable courses_table;
    private static DefaultTableModel courses_table_model;
    private static DefaultTableModel student_table_model;

    public Table_Manager() {
        new Data_Manager();
        processCourseTable();
        processStudentTable();
    }

    /*
     * Process the initial data and the functionalities of the student table
     */
    private void processStudentTable() {
        // setup the table and its model
        student_table_model = new DefaultTableModel(0, 0) {
            // prevent editing directly in the cell table
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        student_table_model.setColumnIdentifiers(Data_Manager.getStudentColumn());
        students_table = new JTable(student_table_model);
        // sort the table (click the column header)
        students_table.setAutoCreateRowSorter(true);
        // auto sort based on the first column
        students_table.getRowSorter().toggleSortOrder(0);

        // traverse the course list
        for (Course course : Data_Manager.coursesList()) {
            // traverse the students in the course then add their details to the tables
            for (Student student : course.getBlock()) {
                student_table_model.addRow(new Object[] { student.getSurname(), student.getFirstName(),
                        student.getMiddleName(),
                        student.getIDNumber(), student.getYearLevel(), student.getGender(), student.getCourseCode() });
            }
        }
    }

    /*
     * Process the initial data and the functionalities of the course table
     */
    private void processCourseTable() {
        // setup the table and its model
        courses_table_model = new DefaultTableModel(0, 0) {
            // prevent editing directly in the cell table
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        courses_table_model.setColumnIdentifiers(Data_Manager.getCourseColumn());
        courses_table = new JTable(courses_table_model);
        // sort the table (click the column header)
        courses_table.setAutoCreateRowSorter(true);
        // auto sort based on the first column
        courses_table.getRowSorter().toggleSortOrder(0);

        // traverse the course list then add their details to the tables
        for (Course course : Data_Manager.coursesList()) {
            // skip showing in the table the unenrolled course
            if (course.getCourseCode().equals("N/A") && course.getCourseName().equals("Unenrolled"))
                continue;

            courses_table_model.addRow(new Object[] { course.getCourseCode(), course.getCourseName() });
        }
    }

    /*
     * Return the student table
     */
    public static JTable getStudentTable() {
        return Table_Manager.students_table;
    }

    /*
     * Return the course table
     */
    public static JTable getCourseTable() {
        return Table_Manager.courses_table;
    }
}
