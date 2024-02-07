package control;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import model.Course;
import model.Student;
import model.Table_Manager;

/*
 * Facilitates the deletion of the rows in the tables and their relations
 */
public class Delete_Process {
    public Delete_Process(JTable table) {
        // if the table selected is the student table, else use the data from the course
        // table
        if (table.equals(Table_Manager.getStudentTable()))
            studentDelete();
        else
            courseDelete();
    }

    /*
     * If the row to be deleted is in the student table.
     */
    private void studentDelete() {
        // get the table of the student data and its model
        JTable student_table = Table_Manager.getStudentTable();
        DefaultTableModel student_table_model = (DefaultTableModel) student_table.getModel();

        // get the selected row
        int table_row_selected = student_table.getSelectedRow();

        // traverse the whole course list to find the course of the student to be
        // deleted
        Outer: for (Course course : Data_Manager.coursesList()) {
            // if found, find the student to remove from the block
            if (student_table.getValueAt(table_row_selected, student_table_model.getColumnCount() - 1)
                    .equals(course.getCourseCode())) {

                // traverse the student list
                for (Student student : course.getBlock()) {
                    // check if the student has all the components/data of the selected row, check
                    // every attribute to avoid changing duplicates
                    if (student.getSurname().equals(student_table.getValueAt(table_row_selected, 0))
                            && student.getFirstName().equals(student_table.getValueAt(table_row_selected, 1))
                            && student.getMiddleName().equals(student_table.getValueAt(table_row_selected, 2))
                            && student.getIDNumber().equals(student_table.getValueAt(table_row_selected, 3))
                            && student.getYearLevel().equals(student_table.getValueAt(table_row_selected, 4))
                            && student.getGender().equals(student_table.getValueAt(table_row_selected, 5))
                            && student.getCourseCode().equals(student_table.getValueAt(table_row_selected, 6))) {
                        // ask for confimation
                        int choosen = JOptionPane.showConfirmDialog(null,
                                "Are you sure?", "Confirmation",
                                JOptionPane.OK_CANCEL_OPTION);
                        // if the choosen option is not okay, cancel the action
                        if (choosen != JOptionPane.OK_OPTION) {
                            return;
                        }
                        // if confirmed, remove the student
                        course.getBlock().remove(student);
                        break Outer;
                    }
                }
            }
        }

        // Since JTable and its TableModel doesn't have the same row counting (because
        // of auto sorting), traverse the whole table for course then if the course to
        // edit is found, use the row number.
        // student_row is for the model and table_row_selected is for the table
        for (int student_row = 0; student_row < student_table.getRowCount(); student_row++) {
            if (student_table_model.getValueAt(student_row, 0).equals(student_table.getValueAt(table_row_selected, 0))
                    && student_table_model.getValueAt(student_row, 1)
                            .equals(student_table.getValueAt(table_row_selected, 1))) {
                student_table_model.removeRow(student_row);
                break;
            }
        }
    }

    /*
     * If the row to be deleted is in the course table.
     */
    private void courseDelete() {
        // get the table of the course data and its model
        JTable course_table = Table_Manager.getCourseTable();
        DefaultTableModel course_table_model = (DefaultTableModel) course_table.getModel();

        int table_row_selected = course_table.getSelectedRow();
        // Since deleting a course also affects the students enrolled, traverse the
        // course list to find the course to be deleted
        for (Course course : Data_Manager.coursesList()) {
            // if the course is found, use the method to delete the course, then remove the
            // course from the course list
            if (course.getCourseCode().equals(course_table.getValueAt(table_row_selected, 0))
                    && course.getCourseName().equals(course_table.getValueAt(table_row_selected, 1))) {
                // ask for confimation
                int choosen = JOptionPane.showConfirmDialog(null,
                        "Currently enrolled: " + course.getBlock().size() + " \nAre you sure?", "Confirmation",
                        JOptionPane.OK_CANCEL_OPTION);
                // if the choosen option is not okay, cancel the action
                if (choosen != JOptionPane.OK_OPTION) {
                    return;
                }
                // if confirmed, remove the course
                course.courseDelete();
                Data_Manager.coursesList().remove(course);
                break; // break the loop
            }
        }

        // removing the selected row from the course table
        for (int row_item = 0; row_item < course_table_model.getRowCount(); row_item++) {
            // if the row found, change the data in the student table from the deleted
            // course to unenrolled
            if (course_table_model.getValueAt(row_item, 0).equals(course_table.getValueAt(table_row_selected, 0))
                    && course_table_model.getValueAt(row_item, 1)
                            .equals(course_table.getValueAt(table_row_selected, 1))) {

                JTable student_table = Table_Manager.getStudentTable();
                // traverse the whole student table and change the data of the students enrolled
                // in the deleted course, then record unenrolled
                for (int student_row = 0; student_row < student_table.getRowCount(); student_row++) {
                    if (student_table.getModel().getValueAt(student_row, student_table.getColumnCount() - 1)
                            .equals(course_table_model.getValueAt(row_item, 0))) {
                        student_table.getModel().setValueAt(Data_Manager.notEnrolled().getCourseCode(), student_row,
                                student_table.getColumnCount() - 1);
                    }
                }

                course_table_model.removeRow(row_item); // remove the selected row from the course table
                break;
            }

        }

    }
}
