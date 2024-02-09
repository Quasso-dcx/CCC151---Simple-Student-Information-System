package control;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import model.Course;
import model.Student;
import model.Table_Manager;

/*
 * Facilitates the editing of the rows in the tables and their relations
 */
public class Edit_Process {
    private JTable table;
    private JDialog edit_dialog;
    private boolean theresDuplicate;

    public Edit_Process(JTable table, JDialog edit_dialog) {
        this.table = table;
        this.edit_dialog = edit_dialog;
    }

    /*
     * If the item to be edited is in the student table
     */
    public void studentEdit(String new_surname, String new_first_name, String new_middle_name, String new_ID_number,
            String new_year_level, String new_gender, String new_course) {

        JTable student_table = this.table;
        DefaultTableModel student_table_model = (DefaultTableModel) student_table.getModel();
        int table_row_selected = student_table.getSelectedRow();

        theresDuplicate = false;

        // traverse the whole student list, check if another student has the same unique
        // attribute registered. Also, check if the attribute of the student is not the
        // same with the selected row to avoid checking itself.
        for (Course course : Data_Manager.coursesList().values()) {
            for (Student student : course.getBlock()) {
                // check for the unique name
                if ((student.getSurname().equals(new_surname)
                        && !student.getSurname().equals(student_table.getValueAt(table_row_selected, 0)))
                        && (student.getFirstName().equals(new_first_name)
                                && !student.getFirstName().equals(student_table.getValueAt(table_row_selected, 1)))
                        && (student.getMiddleName().equals(new_middle_name)
                                && !student.getMiddleName().equals(student_table.getValueAt(table_row_selected, 2)))) {
                    JOptionPane.showMessageDialog(table,
                            "Student: " + new_surname + ", " + new_first_name + " " + new_middle_name
                                    + "\nalready exist.",
                            "Duplication of Entry", JOptionPane.ERROR_MESSAGE);
                    theresDuplicate = true;
                    break;
                }
                // check for the unique id number
                else if (student.getIDNumber().equals(new_ID_number)
                        && !student.getIDNumber().equals(student_table.getValueAt(table_row_selected, 3))) {
                    JOptionPane.showMessageDialog(table,
                            "ID Number: " + new_ID_number
                                    + "\nalready belongs to another student.",
                            "Duplication of Entry", JOptionPane.ERROR_MESSAGE);
                    theresDuplicate = true;
                    break;
                } else
                    continue;

            }
        }

        // if there are no duplicates of the unique attributes
        if (!theresDuplicate) {
            String[] student_new_course = new_course.split("-");
            // Since editing a course also affects the students enrolled, traverse the
            // course list to find the course to be edited
            Course course = Data_Manager.coursesList().get(student_table.getValueAt(table_row_selected, student_table.getColumnCount() - 1));
            for (Student student : course.getBlock()) {
                // check if the student has all the unique components/data of the selected row
                if (student.getSurname().equals(student_table.getValueAt(table_row_selected, 0))
                        && student.getFirstName().equals(student_table.getValueAt(table_row_selected, 1))
                        && student.getMiddleName().equals(student_table.getValueAt(table_row_selected, 2))
                        && student.getIDNumber().equals(student_table.getValueAt(table_row_selected, 3))) {

                    // change the atributes
                    student.setSurname(new_surname);
                    student.setFirstName(new_first_name);
                    student.setMiddleName(new_middle_name);
                    student.setIDNumber(new_ID_number);
                    student.setYearLevel(new_year_level);
                    student.setGender(new_gender);
                    student.setCourseCode(student_new_course[0]);
                    student.setCourseName(student_new_course[1]);

                    // if the course of the student has been changed, unenroll from its previous
                    // course then enroll to its new
                    if (!student_table.getValueAt(table_row_selected, 6).equals(student.getCourseCode())) {
                        // for initialization only, and also when the old course is not found
                        Course old_course = Data_Manager.coursesList().get(student_table.getValueAt(table_row_selected, 6));
                        // for initialization only, and also when the new course is not found
                        Course change_course = Data_Manager.coursesList().get(student_new_course[0]);
                        
                        old_course.getBlock().remove(student); // remove student from the old course
                        change_course.getBlock().add(student); // enroll student to the new course
                    }
                    break;
                }
            }

            // Since JTable and its TableModel doesn't have the same row counting (because
            // of auto sorting), traverse the whole table for course then if the course to
            // edit is found, use the row number.
            // student_row is for the model and table_row_selected is for the table
            for (int selected_row = 0; selected_row < student_table_model.getRowCount(); selected_row++) {
                if (student_table_model.getValueAt(selected_row, 0)
                        .equals(student_table.getValueAt(table_row_selected, 0))
                        && student_table_model.getValueAt(selected_row, 1)
                                .equals(student_table.getValueAt(table_row_selected, 1))
                        && student_table_model.getValueAt(selected_row, 2)
                                .equals(student_table.getValueAt(table_row_selected, 2))
                        && student_table_model.getValueAt(selected_row, 3)
                                .equals(student_table.getValueAt(table_row_selected, 3))) {

                    // change the values in the row of the edited course
                    student_table_model.setValueAt(new_surname, selected_row, 0);
                    student_table_model.setValueAt(new_first_name, selected_row, 1);
                    student_table_model.setValueAt(new_middle_name, selected_row, 2);
                    student_table_model.setValueAt(new_ID_number, selected_row, 3);
                    student_table_model.setValueAt(new_year_level, selected_row, 4);
                    student_table_model.setValueAt(new_gender, selected_row, 5);
                    student_table_model.setValueAt(student_new_course[0], selected_row, 6);

                    // for confirmation
                    JOptionPane.showMessageDialog(null, "Edit Success.");
                    edit_dialog.dispose();
                    break;
                }
            }
        }
    }

    /*
     * If the item to be edited is in the course table
     */
    public void courseEdit(String new_course_code, String new_course_name) {
        JTable course_table = this.table;
        DefaultTableModel course_table_model = (DefaultTableModel) course_table.getModel();
        int table_row_selected = course_table.getSelectedRow();

        theresDuplicate = false;

        // traverse the whole course list, check if another course has the same unique
        // attribute registered. Also, check if the attribute of the course is not the
        // same with the selected row to avoid checking itself.
        if (Data_Manager.coursesList().containsKey(new_course_code)) {
            JOptionPane.showMessageDialog(table, "Course Code: " + new_course_code + "\nalready exist.",
                    "Duplication of Entry", JOptionPane.ERROR_MESSAGE);
            theresDuplicate = true;
        }

        for (Course course : Data_Manager.coursesList().values()) {
            if (course.getCourseName().equals(new_course_name)) {
                JOptionPane.showMessageDialog(table, "Course Name: " + new_course_name + "\nalready exist.",
                        "Duplication of Entry", JOptionPane.ERROR_MESSAGE);
                theresDuplicate = true;
            }
        }

        // if there are no duplicates of the unique attributes
        if (!theresDuplicate) {
            // change the attributes for courseCode of every student inside the course
            if (!course_table.getValueAt(table_row_selected, 0).equals(new_course_code)) {
                Course course = Data_Manager.coursesList().get(course_table.getValueAt(table_row_selected, 0));
                course.setCourseCode(new_course_code);

                for (Student student : course.getBlock()) {
                    student.setCourseCode(new_course_code);
                }
            }

            // change the attributes for courseName of every student inside the course
            if (!course_table.getValueAt(table_row_selected, 1).equals(new_course_name)) {
                Course course = Data_Manager.coursesList().get(course_table.getValueAt(table_row_selected, 0));
                course.setCourseCode(new_course_name);

                course.setCourseName(new_course_name);
                for (Student student : course.getBlock()) {
                    student.setCourseName(new_course_name);
                }
            }

            // since JTable and TableModel doesn't have the same row counting, traverse the
            // whole table for course
            // then if the course to edit is found, use the row number
            // selected_row_model is for the model and table_row_selected is for the table
            for (int selected_row_model = 0; selected_row_model < course_table_model
                    .getRowCount(); selected_row_model++) {
                if (course_table_model.getValueAt(selected_row_model, 0)
                        .equals(course_table.getValueAt(table_row_selected, 0))
                        && course_table_model.getValueAt(selected_row_model, 1)
                                .equals(course_table.getValueAt(table_row_selected, 1))) {

                    JTable student_table = Table_Manager.getStudentTable();
                    // traverse the student table since the jtable doesnt update automatically
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
