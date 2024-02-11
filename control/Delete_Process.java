package control;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import model.Course;
import model.StudentKeyMaker;
import model.Table_Manager;

/**
 * Facilitates the deletion of the rows in the tables and their relations.
 */
public class Delete_Process {
    public Delete_Process(JTable table) {
        /*
         * If the table selected is the student table, else use the data from the course
         * table
         */
        if (table.equals(Table_Manager.getStudentTable()))
            studentDelete();
        else
            courseDelete();
    }

    /**
     * If the row to be deleted is in the student table.
     */
    private void studentDelete() {
        // get the table of the student data and its model
        JTable student_table = Table_Manager.getStudentTable();
        DefaultTableModel student_table_model = (DefaultTableModel) student_table.getModel();

        // get the selected row
        int table_row_selected = student_table.getSelectedRow();

        /*
         * Get the enrolled course of the student to be deleted using the course code
         * key.
         */
        String course_key = student_table.getValueAt(table_row_selected, 6).toString();
        Course course = Data_Manager.coursesList().get(course_key);

        // ask for confimation
        int choosen = JOptionPane.showConfirmDialog(student_table, "Are you sure?", "Confirmation",
                JOptionPane.OK_CANCEL_OPTION);

        // if the choosen option is not okay, cancel the action
        if (choosen != JOptionPane.OK_OPTION) {
            return;
        }

        // get the student ID
        String student_ID = student_table.getValueAt(table_row_selected, 3).toString();

        // get the student key using the course code and the student ID
        String remove_student_key = new StudentKeyMaker().keyMaker(course_key, student_ID);

        // remove the student from the hashmap
        Data_Manager.studentList().remove(remove_student_key);

        // remove the student ID of the deleted student from the course list of IDs
        course.getBlockIDs().remove(student_ID);

        /*
         * Removing the selected row from the student table. Need to traverse the whole
         * table because JTable doesn't have its own row remover, and the model needs to
         * rely on JTable's getSelectedRow().
         */
        for (int row_count = 0; row_count < student_table_model.getRowCount(); row_count++) {
            if (student_table_model.getValueAt(row_count, 0).equals(student_table.getValueAt(table_row_selected, 0))
                    && student_table_model.getValueAt(row_count, 1)
                            .equals(student_table.getValueAt(table_row_selected, 1))
                    && student_table_model.getValueAt(row_count, 2)
                            .equals(student_table.getValueAt(table_row_selected, 2))
                    && student_table_model.getValueAt(row_count, 3)
                            .equals(student_table.getValueAt(table_row_selected, 3))) {
                student_table_model.removeRow(row_count);
                break;
            }
        }
    }

    /**
     * If the row to be deleted is in the course table.
     */
    private void courseDelete() {
        // get the table of the course data and its model
        JTable course_table = Table_Manager.getCourseTable();

        // get the selected row
        int table_row_selected = course_table.getSelectedRow();

        // Since deleting a course also affects the students enrolled, get the course
        // code key of the deleted course
        String course_key = course_table.getValueAt(table_row_selected, 0).toString();
        Course course = Data_Manager.coursesList().get(course_key);

        // ask for confimation
        int choosen = JOptionPane.showConfirmDialog(course_table,
                "Currently enrolled: " + course.getBlockIDs().size() + " \nAre you sure?", "Confirmation",
                JOptionPane.OK_CANCEL_OPTION);

        // if the choosen option is not okay, cancel the action
        if (choosen != JOptionPane.OK_OPTION) {
            return;
        }

        /*
         * If confirmed, change the courses of the enrolled students to the deleted
         * course and remove the deleted course from the hashmap.
         */
        course.courseDelete();
        Data_Manager.coursesList().remove(course_key);

        DefaultTableModel course_table_model = (DefaultTableModel) course_table.getModel();

        /*
         * Removing the selected row from the course table. Need to traverse the whole
         * table because JTable doesn't have its own row remover, and the model needs to
         * rely on JTable's getSelectedRow().
         */
        for (int row_count = 0; row_count < course_table_model.getRowCount(); row_count++) {
            /*
             * If the row found, change the data in the student table from the deleted
             * course to unenrolled.
             */
            if (course_table_model.getValueAt(row_count, 0).equals(course_table.getValueAt(table_row_selected, 0))
                    && course_table_model.getValueAt(row_count, 1)
                            .equals(course_table.getValueAt(table_row_selected, 1))) {

                JTable student_table = Table_Manager.getStudentTable();
                /*
                 * Traverse the whole student table and change the data of the students enrolled
                 * in the deleted course, then record unenrolled. Filter to shrink the size.
                 */
                Filter_Data.rowFilter(student_table, course_key, 6);
                for (int student_row = 0; student_row < student_table.getRowCount(); student_row++) {
                    student_table.setValueAt(Data_Manager.notEnrolled().getCourseCode(), student_row, 6);
                }
                Filter_Data.cancelFilter(student_table);

                course_table_model.removeRow(row_count); // remove the selected row from the course table
                break;
            }

        }

    }
}
