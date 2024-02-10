package control;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import model.Course;
import model.Student;
import model.StudentKeyMaker;

/**
 * Facilitates the addition of the rows in the tables and their relations.
 */
public class Add_Process {
    private JTable table;
    private JDialog add_dialog;
    private boolean theresDuplicate;

    public Add_Process(JTable table, JDialog add_dialog) {
        this.table = table;
        this.add_dialog = add_dialog;
    }

    /**
     * If the item to be added is in the student table.
     *
     * @param surname_data
     * @param first_name_data
     * @param middle_name_data
     * @param ID_number_data
     * @param year_level_data
     * @param gender_data
     * @param course_code_data
     * @param course_name_data
     */
    public void studentAdd(String surname_data, String first_name_data, String middle_name_data, String ID_number_data,
            String year_level_data, String gender_data, String course_code_data, String course_name_data) {

        DefaultTableModel table_model = (DefaultTableModel) this.table.getModel();
        theresDuplicate = false;

        /*
         * Filter the table and check if there are rows with the same name as the new
         * student.
         */
        String[] column_data = { surname_data, first_name_data, middle_name_data };
        int[] column_indices = { 0, 1, 2 };
        Filter_Data.multipleFilter(this.table, column_data, column_indices);

        if (this.table.getRowCount() > 0) {
            JOptionPane.showMessageDialog(this.add_dialog,
                    "Student: " + surname_data + ", " + first_name_data + " " + middle_name_data + "\nalready exist.",
                    "Duplication of Entry", JOptionPane.ERROR_MESSAGE);
            theresDuplicate = true;
        }

        /*
         * Filter the table and check if there are rows with the same ID number as the
         * new student. Check only if there is no duplicate with the name to prevent
         * multiple errors popping up.
         */
        if (!theresDuplicate) {
            Filter_Data.rowFilter(this.table, ID_number_data, 3);
            if (this.table.getRowCount() > 0) {
                JOptionPane.showMessageDialog(this.add_dialog,
                        "ID Number: " + ID_number_data + "\nalready belongs to another student.",
                        "Duplication of Entry", JOptionPane.ERROR_MESSAGE);
                theresDuplicate = true;
            }
        }

        Filter_Data.rowFilter(this.table, "", 0); // cancel the filter

        // if there are no duplicates of the unique attributes
        if (!theresDuplicate) {
            // get the course of the new student
            String course_key = course_code_data;
            Course course = Data_Manager.coursesList().get(course_key);

            // create a new student
            Student new_student = new Student(surname_data, first_name_data, middle_name_data, ID_number_data,
                    year_level_data, gender_data, course_code_data, course_name_data);

            // record the ID of the new student to its course
            course.getBlockIDs().add(new_student.getIDNumber());

            // put the new student with its key to the student hashmap
            Data_Manager.studentList().put(new StudentKeyMaker().keyMaker(course_code_data, ID_number_data),
                    new_student);

            // add a new row to the student table
            table_model.addRow(
                    new Object[] { new_student.getSurname(), new_student.getFirstName(), new_student.getMiddleName(),
                            new_student.getIDNumber(), new_student.getYearLevel(), new_student.getGender(),
                            new_student.getCourseCode() });

            // for confirmation
            JOptionPane.showMessageDialog(this.add_dialog, "Add Success.");
            add_dialog.dispose();
        }
    }

    /**
     * If the item to be added is in the course table.
     * 
     * @param course_code_data
     * @param course_name_data
     */
    public void courseAdd(String course_code_data, String course_name_data) {
        DefaultTableModel table_model = (DefaultTableModel) this.table.getModel();
        theresDuplicate = false;

        /*
         * Since the course code is the key for every course, check if there is already
         * the same key in the hashmap.
         */
        if (Data_Manager.coursesList().containsKey(course_code_data)) {
            JOptionPane.showMessageDialog(this.table, "Course Code: " + course_code_data + "\nalready exist.",
                    "Duplication of Entry", JOptionPane.ERROR_MESSAGE);
            theresDuplicate = true;
        }

        /*
         * Filter the table and check if there are rows with the same course name as the
         * new course. Check only if there is no duplicate with the code to prevent
         * multiple errors popping up.
         */
        if (!theresDuplicate) {
            Filter_Data.rowFilter(table, course_name_data, 1);
            if (table.getRowCount() > 0) {
                JOptionPane.showMessageDialog(this.table, "Course Name: " + course_name_data + "\nalready exist.",
                        "Duplication of Entry", JOptionPane.ERROR_MESSAGE);
                theresDuplicate = true;
            }
        }

        Filter_Data.rowFilter(table, "", 0); // cancel the filter

        // if there are no duplicates of the unique attributes
        if (!theresDuplicate) {
            // put the new course and its key to the course hashmap
            Data_Manager.coursesList().put(course_code_data, new Course(course_code_data, course_name_data));

            // add a new row to the course table
            table_model.addRow(new Object[] { course_code_data, course_name_data });

            JOptionPane.showMessageDialog(this.add_dialog, "Add Success.");
            add_dialog.dispose();
        }
    }
}
