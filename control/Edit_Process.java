package control;

import java.util.Arrays;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import model.Course;
import model.Student;
import model.StudentKeyMaker;
import model.Table_Manager;

/**
 * Facilitates the editing of the rows in the tables and their relations.
 */
public class Edit_Process {
    private JTable table;
    private JDialog edit_dialog;
    private boolean theresDuplicate;

    public Edit_Process(JTable table, JDialog edit_dialog) {
        this.table = table;
        this.edit_dialog = edit_dialog;
    }

    /**
     * If the item to be edited is in the student table.
     * 
     * @param new_surname
     * @param new_first_name
     * @param new_middle_name
     * @param new_ID_number
     * @param new_year_level
     * @param new_gender
     * @param new_course_code
     */
    public void studentEdit(String new_surname, String new_first_name, String new_middle_name, String new_ID_number,
            String new_year_level, String new_gender, String new_course_code) {

        JTable student_table = this.table;
        DefaultTableModel student_table_model = (DefaultTableModel) student_table.getModel();
        int table_row_selected = student_table.getSelectedRow();

        theresDuplicate = false;

        /*
         * Filter the table and check if there are rows with the same name as the edit
         * student. Also, check if the attribute of the student is not the same with the
         * original to avoid checking itself.
         */
        String[] selected_row = new String[student_table.getColumnCount()];
        for (int column = 0; column < student_table.getColumnCount(); column++) {
            selected_row[column] = student_table.getValueAt(table_row_selected, column).toString();
        }

        String[] remaining_row = new String[table.getColumnCount()]; // remaining row after filtering

        String[] column_data = { new_surname, new_first_name, new_middle_name };
        int[] column_indices = { 0, 1, 2 };
        Filter_Data.multipleFilter(student_table, column_data, column_indices);

        if (table.getRowCount() > 0) {
            for (int column = 0; column < student_table.getColumnCount(); column++) {
                remaining_row[column] = student_table.getValueAt(0, column).toString();
            }

            // if the remaining row and selected row are not the same
            if (!Arrays.equals(remaining_row, selected_row)) {
                JOptionPane.showMessageDialog(table,
                        "Student: " + new_surname + ", " + new_first_name + " " + new_middle_name
                                + "\nalready exist.",
                        "Duplication of Entry", JOptionPane.ERROR_MESSAGE);
                theresDuplicate = true;
            }
        }

        /*
         * Filter the table and check if there are rows with the same ID number as the
         * edited ID. Check only if there is no duplicate with the name to prevent
         * multiple errors popping up.
         */
        if (!theresDuplicate) {
            Filter_Data.rowFilter(table, new_ID_number, 3);
            if (table.getRowCount() > 0) {
                for (int column = 0; column < student_table.getColumnCount(); column++) {
                    remaining_row[column] = student_table.getValueAt(0, column).toString();
                }

                // if the remaining row and selected row are not the same
                if (!Arrays.equals(remaining_row, selected_row)) {
                    JOptionPane.showMessageDialog(table,
                            "ID Number: " + new_ID_number
                                    + "\nalready belongs to another student.",
                            "Duplication of Entry", JOptionPane.ERROR_MESSAGE);
                    theresDuplicate = true;
                }
            }
        }

        Filter_Data.rowFilter(table, "", 0); // cancel the filter
        // if there are no duplicates of the unique attributes
        if (!theresDuplicate) {
            // get the old course key of the student and its ID number
            String course_key = student_table.getValueAt(table_row_selected, student_table.getColumnCount() - 1)
                    .toString();
            String ID_num = student_table.getValueAt(table_row_selected, 3).toString();

            // get the student
            String old_student_key = new StudentKeyMaker().keyMaker(course_key, ID_num);
            Student editing_student = Data_Manager.studentList().get(old_student_key);

            // change the atributes
            editing_student.setSurname(new_surname);
            editing_student.setFirstName(new_first_name);
            editing_student.setMiddleName(new_middle_name);
            editing_student.setIDNumber(new_ID_number);
            editing_student.setYearLevel(new_year_level);
            editing_student.setGender(new_gender);
            editing_student.setCourseCode(new_course_code);

            String new_student_key = new StudentKeyMaker().keyMaker(new_course_code, new_ID_number);

            /*
             * If the course of the student has been changed, unenroll from its previous
             * course then enroll to its new.
             */
            if (!student_table.getValueAt(table_row_selected, 6).equals(editing_student.getCourseCode())) {
                // get the old course of the student
                Course old_course = Data_Manager.coursesList().get(course_key);

                // get the new course of the student
                Course new_course = Data_Manager.coursesList().get(new_course_code);

                /*
                 * Update the key of the student by removing it and adding it again with a new
                 * key.
                 */
                Data_Manager.studentList().remove(old_student_key);
                Data_Manager.studentList().put(new_student_key, editing_student);

                old_course.getBlockIDs().remove(ID_num); // remove student from the old course
                new_course.getBlockIDs().add(new_ID_number); // enroll student to the new course
            }

            /*
             * Since JTable and its TableModel doesn't have the same row counting (because
             * of auto sorting), traverse the whole table for course then if the course to
             * edit is found, use the row number. student_row is for the model and
             * table_row_selected is for the table.
             */
            for (int selected_row_count = 0; selected_row_count < student_table_model
                    .getRowCount(); selected_row_count++) {
                if (student_table_model.getValueAt(selected_row_count, 0)
                        .equals(student_table.getValueAt(table_row_selected, 0))
                        && student_table_model.getValueAt(selected_row_count, 1)
                                .equals(student_table.getValueAt(table_row_selected, 1))
                        && student_table_model.getValueAt(selected_row_count, 2)
                                .equals(student_table.getValueAt(table_row_selected, 2))
                        && student_table_model.getValueAt(selected_row_count, 3)
                                .equals(student_table.getValueAt(table_row_selected, 3))) {

                    // change the values in the row of the edited course
                    student_table_model.setValueAt(new_surname, selected_row_count, 0);
                    student_table_model.setValueAt(new_first_name, selected_row_count, 1);
                    student_table_model.setValueAt(new_middle_name, selected_row_count, 2);
                    student_table_model.setValueAt(new_ID_number, selected_row_count, 3);
                    student_table_model.setValueAt(new_year_level, selected_row_count, 4);
                    student_table_model.setValueAt(new_gender, selected_row_count, 5);
                    student_table_model.setValueAt(new_course_code, selected_row_count, 6);

                    // for confirmation
                    JOptionPane.showMessageDialog(null, "Edit Success.");
                    edit_dialog.dispose();
                    break;
                }
            }
        }
    }

    /**
     * If the item to be edited is in the course table.
     * 
     * @param new_course_code
     * @param new_course_name
     */
    public void courseEdit(String new_course_code, String new_course_name) {
        JTable course_table = this.table;
        DefaultTableModel course_table_model = (DefaultTableModel) course_table.getModel();
        int table_row_selected = course_table.getSelectedRow();

        theresDuplicate = false;

        /*
         * Since the course code is the key for every course, check if there is already
         * the same key in the hashmap and if it is changed.
         */
        if (Data_Manager.coursesList().containsKey(new_course_code)
                && !course_table.getValueAt(table_row_selected, 0).equals(new_course_code)) {
            JOptionPane.showMessageDialog(table, "Course Code: " + new_course_code + "\nalready exist.",
                    "Duplication of Entry", JOptionPane.ERROR_MESSAGE);
            theresDuplicate = true;
        }

        /*
         * Filter the table and check if there are rows with the same course name as the
         * new course. Check only if there is no duplicate with the name to prevent
         * multiple errors popping up.
         */
        if (!theresDuplicate) {
            Filter_Data.rowFilter(table, new_course_name, 1);
            if (table.getRowCount() > 0) {
                JOptionPane.showMessageDialog(table, "Course Name: " + new_course_name + "\nalready exist.",
                        "Duplication of Entry", JOptionPane.ERROR_MESSAGE);
                theresDuplicate = true;
            }
        }

        Filter_Data.rowFilter(table, "", 0); // cancel the filter
        // if there are no duplicates of the unique attributes
        if (!theresDuplicate) {
            Course editing_course = Data_Manager.coursesList().get(course_table.getValueAt(table_row_selected, 0));

            // if the course code was edited
            if (!course_table.getValueAt(table_row_selected, 0).equals(new_course_code)) {
                // change the attributes for course code of every student inside the course
                for (String ID_num : editing_course.getBlockIDs()) {
                    // get the student
                    String old_student_key = new StudentKeyMaker().keyMaker(editing_course.getCourseCode(), ID_num);
                    Student student_transfer = Data_Manager.studentList().get(old_student_key);

                    student_transfer.setCourseCode(new_course_code); // change the course code attribute

                    // new student key
                    String new_student_transfer_key = new StudentKeyMaker().keyMaker(new_course_code, ID_num);

                    /*
                     * Update the key of the student by removing it and adding it again with a new
                     * key.
                     */
                    Data_Manager.studentList().remove(old_student_key);
                    Data_Manager.studentList().put(new_student_transfer_key, student_transfer);
                }

                editing_course.setCourseCode(new_course_code); // change the course code
                Data_Manager.coursesList().remove(course_table.getValueAt(table_row_selected, 0)); // remove the old map
                Data_Manager.coursesList().put(new_course_code, editing_course); // add the new map w/ the edited key
            }

            // change the attributes for courseName of every student inside the course
            if (!course_table.getValueAt(table_row_selected, 1).equals(new_course_name)) {
                editing_course = Data_Manager.coursesList().get(new_course_code);

                editing_course.setCourseName(new_course_name); // change the course name
            }

            /*
             * Since JTable and TableModel doesn't have the same row counting (because
             * of auto sorting), traverse the whole table for course then if the course
             * to edit is found, use the row number selected_row_model is for the model
             * and table_row_selected is for the table.
             */
            for (int selected_row_model = 0; selected_row_model < course_table_model
                    .getRowCount(); selected_row_model++) {
                if (course_table_model.getValueAt(selected_row_model, 0)
                        .equals(course_table.getValueAt(table_row_selected, 0))
                        && course_table_model.getValueAt(selected_row_model, 1)
                                .equals(course_table.getValueAt(table_row_selected, 1))) {
                    JTable student_table = Table_Manager.getStudentTable();

                    // traverse the student table since the jtable doesn't update automatically
                    for (int student_row_count = 0; student_row_count < student_table.getModel()
                            .getRowCount(); student_row_count++) {
                        if (student_table.getModel().getValueAt(student_row_count, 6)
                                .equals(course_table_model.getValueAt(selected_row_model, 0))) {
                            student_table.getModel().setValueAt(new_course_code, student_row_count,
                                    student_table.getColumnCount() - 1);
                        }
                    }

                    // change the values in the row of the edited course
                    course_table_model.setValueAt(new_course_code, selected_row_model, 0);
                    course_table_model.setValueAt(new_course_name, selected_row_model, 1);

                    // for confirmation
                    JOptionPane.showMessageDialog(null, "Edit Success.");
                    edit_dialog.dispose();
                    break;
                }
            }
        }
    }
}
