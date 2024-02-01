package view;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import model.Course;
import model.Data_Manager;
import model.Student;
import model.Table_Manager;

/*
 * Display and facilitates the editing row dialog and editing the data of the selected rows
 */
public class Edit_Dialog extends JDialog {
    private final int DIALOG_WIDTH = 400;
    private final int DIALOG_HEIGHT = 300;
    private final Dimension dialog_dimension = new Dimension(DIALOG_WIDTH, DIALOG_HEIGHT);

    private final GridBagLayout grid_bag_layout = new GridBagLayout();
    private final GridBagConstraints layout_Constraints = new GridBagConstraints();

    private JLabel surname;
    private JTextField surname_data;
    private JLabel first_name;
    private JTextField first_name_data;
    private JLabel middle_name;
    private JTextField middle_name_data;
    private JLabel ID_number;
    private JTextField ID_number_data;
    private JLabel year_level;
    private JTextField year_level_data;
    private JLabel gender;
    private JTextField gender_data;
    private JLabel course_code;
    private JComboBox<String> course_code_data;
    private JButton edit_button;
    private JLabel course_code2;
    private JTextField course_code2_data;
    private JLabel course_name;
    private JTextField course_name_data;

    public Edit_Dialog(JTable table) {
        // setup the dialog
        this.setTitle("Edit Item:");
        this.getContentPane().setPreferredSize(dialog_dimension);
        this.setResizable(false);
        this.setLayout(grid_bag_layout);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setModal(true);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        // if the table selected is the student table, else use the data from the course
        // table
        if (table.equals(Table_Manager.getStudentTable()))
            displayEditStudent(Table_Manager.getStudentTable());
        else
            displayEditCourse(Table_Manager.getCourseTable());
    }

    private void displayEditCourse(JTable course_table) {
        // setup the table model to edit the rows
        DefaultTableModel course_table_model = (DefaultTableModel) course_table.getModel();

        // retrieving the data from each cell of the row
        String[] selected_row_data = new String[course_table_model.getColumnCount()];

        // get the selected row and the data in its cells
        int table_row_selected = course_table.getSelectedRow();
        for (int column = 0; column < course_table.getColumnCount(); column++)
            selected_row_data[column] = course_table.getValueAt(table_row_selected, column).toString();

        /*
         * Arranging the displays
         */
        course_code2 = new JLabel("Course Code: ");
        layout_Constraints.fill = GridBagConstraints.HORIZONTAL;
        layout_Constraints.gridx = 0;
        layout_Constraints.gridy = 0;
        layout_Constraints.gridwidth = 2;
        this.add(course_code2, layout_Constraints);

        course_code2_data = new JTextField();
        course_code2_data.setPreferredSize(new Dimension(200, 30));
        course_code2_data.setText(selected_row_data[0]);
        layout_Constraints.gridx = 0;
        layout_Constraints.gridy = 1;
        layout_Constraints.gridwidth = 2;
        this.add(course_code2_data, layout_Constraints);

        course_name = new JLabel("Course Name: ");
        layout_Constraints.gridx = 0;
        layout_Constraints.gridy = 2;
        layout_Constraints.gridwidth = 2;
        this.add(course_name, layout_Constraints);

        course_name_data = new JTextField();
        course_name_data.setPreferredSize(new Dimension(200, 30));
        course_name_data.setText(selected_row_data[1]);
        layout_Constraints.gridx = 0;
        layout_Constraints.gridy = 3;
        layout_Constraints.gridwidth = 2;
        this.add(course_name_data, layout_Constraints);

        // arranging the button and setting its functionality
        edit_button = new JButton("Edit Item");
        layout_Constraints.gridx = 0;
        layout_Constraints.gridy = 9;
        layout_Constraints.gridwidth = 2;
        edit_button.setFocusable(false);
        edit_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // check if there is atleast one field empty
                if (course_code2_data.getText().isEmpty() || course_name_data.getText().isEmpty())
                    JOptionPane.showMessageDialog(edit_button, "Fill all fields.");

                // add the new data to the table then close the dialog
                else {
                    selected_row_data[0] = course_code2_data.getText().toString();
                    selected_row_data[1] = course_name_data.getText().toString();

                    // change the attributes for courseCode of every student inside the course
                    if (!course_table.getValueAt(table_row_selected, 0).equals(selected_row_data[0])) {
                        for (Course course : Data_Manager.coursesList()) {
                            if (course.getCourseCode()
                                    .equals(course_table.getValueAt(table_row_selected, 0))) {
                                course.setCourseCode(selected_row_data[0]);
                                for (Student student : course.getBlock()) {
                                    student.setCourseCode(selected_row_data[0]);
                                }
                            }
                        }
                    }

                    // change the attributes for courseName of every student inside the course
                    if (!course_table.getValueAt(table_row_selected, 1).equals(selected_row_data[1])) {
                        for (Course course : Data_Manager.coursesList()) {
                            if (course.getCourseName()
                                    .equals(course_table.getValueAt(table_row_selected, 1))) {
                                course.setCourseName(selected_row_data[1]);
                                for (Student student : course.getBlock()) {
                                    student.setCourseName(selected_row_data[1]);
                                }
                            }
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
                                    student_table.getModel().setValueAt(selected_row_data[0], student_row_count,
                                            student_table.getColumnCount() - 1);
                                }
                            }

                            // change the values in the row of the edited course
                            for (int column = 0; column < course_table_model.getColumnCount(); column++)
                                course_table_model.setValueAt(selected_row_data[column], selected_row_model, column);

                            // for confirmation
                            JOptionPane.showMessageDialog(edit_button, "Edit Success.");
                            Edit_Dialog.this.dispose();
                            break;
                        }
                    }

                }
            }
        });
        this.add(edit_button, layout_Constraints);
    }

    private void displayEditStudent(JTable student_table) {
        // setup the table model to edit the rows
        DefaultTableModel student_table_model = (DefaultTableModel) student_table.getModel();

        // retrieving the data from each cell of the row
        String[] selected_row_data = new String[student_table.getColumnCount()];

        // get the selected row and the data in its cells
        int table_row_selected = student_table.getSelectedRow();
        for (int column = 0; column < student_table.getColumnCount(); column++)
            selected_row_data[column] = student_table.getValueAt(table_row_selected, column).toString();

        /*
         * Arranging the displays
         */
        surname = new JLabel("Surname: ");
        layout_Constraints.fill = GridBagConstraints.HORIZONTAL;
        layout_Constraints.gridx = 0;
        layout_Constraints.gridy = 0;
        this.add(surname, layout_Constraints);

        surname_data = new JTextField();
        surname_data.setPreferredSize(new Dimension(200, 30));
        surname_data.setText(selected_row_data[0]);
        layout_Constraints.gridx = 1;
        layout_Constraints.gridy = 0;
        this.add(surname_data, layout_Constraints);

        first_name = new JLabel("First Name: ");
        layout_Constraints.fill = GridBagConstraints.HORIZONTAL;
        layout_Constraints.gridx = 0;
        layout_Constraints.gridy = 1;
        this.add(first_name, layout_Constraints);

        first_name_data = new JTextField();
        first_name_data.setPreferredSize(new Dimension(200, 30));
        first_name_data.setText(selected_row_data[1]);
        layout_Constraints.gridx = 1;
        layout_Constraints.gridy = 1;
        this.add(first_name_data, layout_Constraints);

        middle_name = new JLabel("Middle Name: ");
        layout_Constraints.fill = GridBagConstraints.HORIZONTAL;
        layout_Constraints.gridx = 0;
        layout_Constraints.gridy = 2;
        this.add(middle_name, layout_Constraints);

        middle_name_data = new JTextField();
        middle_name_data.setPreferredSize(new Dimension(200, 30));
        middle_name_data.setText(selected_row_data[2]);
        layout_Constraints.gridx = 1;
        layout_Constraints.gridy = 2;
        this.add(middle_name_data, layout_Constraints);

        ID_number = new JLabel("ID Number: ");
        layout_Constraints.fill = GridBagConstraints.HORIZONTAL;
        layout_Constraints.gridx = 0;
        layout_Constraints.gridy = 3;
        this.add(ID_number, layout_Constraints);

        ID_number_data = new JTextField();
        ID_number_data.setPreferredSize(new Dimension(200, 30));
        ID_number_data.setText(selected_row_data[3]);
        layout_Constraints.gridx = 1;
        layout_Constraints.gridy = 3;
        this.add(ID_number_data, layout_Constraints);

        year_level = new JLabel("Year Level: ");
        layout_Constraints.fill = GridBagConstraints.HORIZONTAL;
        layout_Constraints.gridx = 0;
        layout_Constraints.gridy = 4;
        this.add(year_level, layout_Constraints);

        year_level_data = new JTextField();
        year_level_data.setPreferredSize(new Dimension(200, 30));
        year_level_data.setText(selected_row_data[4]);
        layout_Constraints.gridx = 1;
        layout_Constraints.gridy = 4;
        this.add(year_level_data, layout_Constraints);

        gender = new JLabel("Gender: ");
        layout_Constraints.fill = GridBagConstraints.HORIZONTAL;
        layout_Constraints.gridx = 0;
        layout_Constraints.gridy = 5;
        this.add(gender, layout_Constraints);

        gender_data = new JTextField();
        gender_data.setPreferredSize(new Dimension(200, 30));
        gender_data.setText(selected_row_data[5]);
        layout_Constraints.gridx = 1;
        layout_Constraints.gridy = 5;
        this.add(gender_data, layout_Constraints);

        course_code = new JLabel("Course Code: ");
        layout_Constraints.fill = GridBagConstraints.HORIZONTAL;
        layout_Constraints.gridx = 0;
        layout_Constraints.gridy = 6;
        this.add(course_code, layout_Constraints);

        // list the courses available in a readable way
        String[] courses_listed = new String[Data_Manager.coursesList().size()];
        for (int course_count = 0; course_count < Data_Manager.coursesList().size(); course_count++) {
            Course current_course = Data_Manager.coursesList().get(course_count);
            courses_listed[course_count] = current_course.getCourseCode() + "-" + current_course.getCourseName();
        }
        Arrays.sort(courses_listed);

        course_code_data = new JComboBox<>(courses_listed);
        // spliting the text for retrieval of the last column
        for (String course : courses_listed) {
            String[] course_details = course.split("-");
            if (course_details[0].equals(selected_row_data[6])) {
                course_code_data.setSelectedItem(course);
                break;
            }
        }
        layout_Constraints.fill = GridBagConstraints.HORIZONTAL;
        layout_Constraints.gridx = 0;
        layout_Constraints.gridy = 7;
        layout_Constraints.gridwidth = 2;
        this.add(course_code_data, layout_Constraints);

        // arranging the button and setting its functionality
        edit_button = new JButton("Edit Item");
        edit_button.setFocusable(false);
        edit_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // check if there is atleast one field empty
                if (surname_data.getText().isEmpty() || first_name_data.getText().isEmpty() ||
                        middle_name_data.getText().isEmpty() || ID_number_data.getText().isEmpty() ||
                        year_level_data.getText().isEmpty() || gender_data.getText().isEmpty())
                    JOptionPane.showMessageDialog(edit_button, "Fill all fields.");

                // add the new data to the table then close the dialog
                else {
                    // Since editing a course also affects the students enrolled, traverse the
                    // course list to find the course to be edited
                    Outer: for (Course course : Data_Manager.coursesList()) {
                        // traverse the students in the course
                        for (Student student : course.getBlock()) {
                            // check if the student has all the components/data of the selected row, check
                            // every attribute to avoid changing duplicates
                            if (student.getSurname().equals(student_table.getValueAt(table_row_selected, 0))
                                    && student.getFirstName().equals(student_table.getValueAt(table_row_selected, 1))
                                    && student.getMiddleName().equals(student_table.getValueAt(table_row_selected, 2))
                                    && student.getIDNumber().equals(student_table.getValueAt(table_row_selected, 3))
                                    && student.getYearLevel().equals(student_table.getValueAt(table_row_selected, 4))
                                    && student.getGender().equals(student_table.getValueAt(table_row_selected, 5))
                                    && student.getCourseCode()
                                            .equals(student_table.getValueAt(table_row_selected, 6))) {

                                // reuse the previous string array
                                selected_row_data[0] = surname_data.getText().toString();
                                selected_row_data[1] = first_name_data.getText().toString();
                                selected_row_data[2] = middle_name_data.getText().toString();
                                selected_row_data[3] = ID_number_data.getText().toString();
                                selected_row_data[4] = year_level_data.getText().toString();
                                selected_row_data[5] = gender_data.getText().toString();
                                selected_row_data[6] = course_code_data.getSelectedItem().toString().split("-")[0];

                                // change the atributes
                                student.setSurname(selected_row_data[0]);
                                student.setFirstName(selected_row_data[1]);
                                student.setMiddleName(selected_row_data[2]);
                                student.setIDNumber(selected_row_data[3]);
                                student.setYearLevel(selected_row_data[4]);
                                student.setGender(selected_row_data[5]);
                                student.setCourseCode(selected_row_data[6]);
                                student.setCourseName(course_code_data.getSelectedItem().toString().split("-")[1]);

                                // if the course of the student has been changed, unenroll from its previous
                                // course then enroll to its new
                                if (!student_table.getValueAt(table_row_selected, 6).equals(student.getCourseCode())) {
                                    // for initialization only, and also when the new course is not found
                                    Course new_course = Data_Manager.notEnrolled();
                                    for (Course course_find : Data_Manager.coursesList()) {
                                        if (course_find.getCourseCode().equals(selected_row_data[6])) {
                                            new_course = course_find;
                                            break;
                                        }
                                    }
                                    // for initialization only, and also when the old course is not found
                                    Course old_course = Data_Manager.notEnrolled();
                                    for (Course course_find : Data_Manager.coursesList()) {
                                        if (course_find.getCourseCode()
                                                .equals(student_table.getValueAt(table_row_selected, 6))) {
                                            old_course = course_find;
                                            break;
                                        }
                                    }

                                    old_course.getBlock().remove(student); // remove student from the old course
                                    new_course.getBlock().add(student); // enroll student to the new course
                                }
                                break Outer;
                            }
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
                                        .equals(student_table.getValueAt(table_row_selected, 1))) {

                            // change the values in the row of the edited course
                            for (int column = 0; column < student_table_model.getColumnCount(); column++)
                                student_table_model.setValueAt(selected_row_data[column], selected_row, column);

                            // for confirmation
                            JOptionPane.showMessageDialog(edit_button, "Edit Success.");
                            Edit_Dialog.this.dispose();
                            break;
                        }
                    }
                }
            }
        });
        layout_Constraints.fill = GridBagConstraints.HORIZONTAL;
        layout_Constraints.gridx = 0;
        layout_Constraints.gridy = 9;
        layout_Constraints.gridwidth = 2;
        this.add(edit_button, layout_Constraints);
    }
}
