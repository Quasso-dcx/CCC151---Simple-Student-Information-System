package control;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import model.Course;
import model.Student;
import model.StudentKeyMaker;

/*
 * Facilitates the addition of the rows in the tables and their relations
 */
public class Add_Process {
    private JTable table;
    private JDialog add_dialog;
    private boolean theresDuplicate;

    public Add_Process(JTable table, JDialog add_dialog) {
        this.table = table;
        this.add_dialog = add_dialog;
    }

    /*
     * If the item to be added is in the student table
     */
    public void studentAdd(String surname_data, String first_name_data,
            String middle_name_data, String ID_number_data, String year_level_data, String gender_data,
            String course_code_data, String course_name_data) {

        DefaultTableModel table_model = (DefaultTableModel) table.getModel();
        theresDuplicate = false;

        // traverse the whole student list, check if another student has the same unique
        // attribute registered
        // TODO: Optimize using the filter function of JTable
        Filter_Data.rowFilter(table, surname_data, 0);
        if (table.getRowCount() > 0) {
            Filter_Data.rowFilter(table, first_name_data, 1);
            if (table.getRowCount() > 0) {
                Filter_Data.rowFilter(table, middle_name_data, 2);
                if (table.getRowCount() > 0) {
                    JOptionPane.showMessageDialog(table,
                            "Student: " + surname_data + ", " + first_name_data + " " + middle_name_data
                                    + "\nalready exist.",
                            "Duplication of Entry", JOptionPane.ERROR_MESSAGE);
                    theresDuplicate = true;
                }
            }
        }

        if (!theresDuplicate) {
            Filter_Data.rowFilter(table, ID_number_data, 3);
            if (table.getRowCount() > 0) {
                JOptionPane.showMessageDialog(table,
                        "ID Number: " + ID_number_data
                                + "\nalready belongs to another student.",
                        "Duplication of Entry", JOptionPane.ERROR_MESSAGE);
                theresDuplicate = true;
            }
        }

        Filter_Data.rowFilter(table, "", 0); // cancel the filter
        // if there are no duplicates of the unique attributes
        if (!theresDuplicate) {
            // traverse the course list to find the wanted course to enroll
            String course_key = course_code_data;
            Course course = Data_Manager.coursesList().get(course_key);

            Student new_student = new Student(surname_data, first_name_data,
                    middle_name_data, ID_number_data, year_level_data,
                    gender_data, course_code_data, course_name_data);

            course.getBlockIDs().add(new_student.getIDNumber());

            Data_Manager.studentList().put(new StudentKeyMaker().keyMaker(course_code_data, ID_number_data),
                    new_student);

            table_model.addRow(new Object[] { new_student.getSurname(), new_student.getFirstName(),
                    new_student.getMiddleName(), new_student.getIDNumber(), new_student.getYearLevel(),
                    new_student.getGender(), new_student.getCourseCode() });

            JOptionPane.showMessageDialog(null, "Add Success.");
            add_dialog.dispose();
        }
    }

    /*
     * If the item to be added is in the course table
     */
    public void courseAdd(String course_code_data, String course_name_data) {
        DefaultTableModel table_model = (DefaultTableModel) table.getModel();
        theresDuplicate = false;

        // traverse the whole course list, check if another course has the same unique
        // attribute registered

        if (Data_Manager.coursesList().containsKey(course_code_data)) {
            JOptionPane.showMessageDialog(table, "Course Code: " + course_code_data + "\nalready exist.",
                    "Duplication of Entry", JOptionPane.ERROR_MESSAGE);
            theresDuplicate = true;
        }

        for (Course course : Data_Manager.coursesList().values()) {
            if (course.getCourseName().equals(course_name_data)) {
                JOptionPane.showMessageDialog(table, "Course Name: " + course_name_data + "\nalready exist.",
                        "Duplication of Entry", JOptionPane.ERROR_MESSAGE);
                theresDuplicate = true;
            }
        }

        // if there are no duplicates of the unique attributes
        if (!theresDuplicate) {
            Data_Manager.coursesList().put(course_code_data, new Course(course_code_data, course_name_data));
            table_model.addRow(new Object[] { course_code_data, course_name_data });

            JOptionPane.showMessageDialog(null, "Add Success.");
            add_dialog.dispose();
        }

    }
}
