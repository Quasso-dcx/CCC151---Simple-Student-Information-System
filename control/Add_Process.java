package control;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import model.Course;
import model.Student;

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
        for (Course course : Data_Manager.coursesList()) {
            for (Student student : course.getBlock()) {
                // check for the unique name
                if (student.getSurname().equals(surname_data) && student.getFirstName().equals(first_name_data)
                        && student.getMiddleName().equals(middle_name_data)) {
                    JOptionPane.showMessageDialog(table,
                            "Student: " + surname_data + ", " + first_name_data + " " + middle_name_data
                                    + "\nalready exist.",
                            "Duplication of Entry", JOptionPane.ERROR_MESSAGE);
                    theresDuplicate = true;
                    break;
                }
                // check for the unique id number
                else if (student.getIDNumber().equals(ID_number_data)) {
                    JOptionPane.showMessageDialog(table,
                            "ID Number: " + ID_number_data
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
            // traverse the course list to find the wanted course to enroll
            for (Course course : Data_Manager.coursesList()) {
                // if course is found, enroll the student then add the details of the student to
                // the student table
                if (course.getCourseCode().equals(course_code_data)
                        && course.getCourseName().equals(course_name_data)) {
                    Student new_student = new Student(surname_data, first_name_data,
                            middle_name_data, ID_number_data, year_level_data,
                            gender_data, course_code_data, course_name_data);
                    course.getBlock().add(new_student);

                    table_model.addRow(new Object[] { new_student.getSurname(), new_student.getFirstName(),
                            new_student.getMiddleName(), new_student.getIDNumber(), new_student.getYearLevel(),
                            new_student.getGender(), new_student.getCourseCode() });
                }
            }
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
        for (Course course : Data_Manager.coursesList()) {
            if (course.getCourseCode().equals(course_code_data)) {
                JOptionPane.showMessageDialog(table, "Course Code: " + course_code_data + "\nalready exist.",
                        "Duplication of Entry", JOptionPane.ERROR_MESSAGE);
                theresDuplicate = true;
                break;
            } else if (course.getCourseName().equals(course_name_data)) {
                JOptionPane.showMessageDialog(table, "Course Name: " + course_name_data + "\nalready exist.",
                        "Duplication of Entry", JOptionPane.ERROR_MESSAGE);
                theresDuplicate = true;
                break;
            } else
                continue;
        }

        // if there are no duplicates of the unique attributes
        if (!theresDuplicate) {
            Data_Manager.coursesList().add(new Course(course_code_data, course_name_data));
            table_model.addRow(new Object[] { course_code_data, course_name_data });

            JOptionPane.showMessageDialog(null, "Add Success.");
            add_dialog.dispose();
        }

    }
}
