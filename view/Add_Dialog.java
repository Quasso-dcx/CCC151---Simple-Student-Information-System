package view;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

import model.Course;
import model.Data_Manager;
import model.Student;
import model.Table_Manager;

/*
 * Display and facilitates the adding row dialog and editing the data of the selected rows
 */
public class Add_Dialog extends JDialog {
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
    private JButton add_button;
    private JLabel course_code2;
    private JTextField course_code2_data;
    private JLabel course_name;
    private JTextField course_name_data;

    public Add_Dialog(JTable table) {
        // setup the dialog
        this.setTitle("Adding Item:");
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
            displayAddStudent(table);
        else
            displayAddCourse(table);
    }

    private void displayAddCourse(JTable course_table) {
        // setup the table model to add the rows
        DefaultTableModel tableModel = (DefaultTableModel) course_table.getModel();

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
        layout_Constraints.gridx = 0;
        layout_Constraints.gridy = 3;
        layout_Constraints.gridwidth = 2;
        this.add(course_name_data, layout_Constraints);

        // arranging the button and setting its functionality
        add_button = new JButton("Add Item");
        layout_Constraints.gridx = 0;
        layout_Constraints.gridy = 9;
        layout_Constraints.gridwidth = 2;
        add_button.setFocusable(false);
        add_button.setToolTipText("Add the item to the table.");
        add_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // check if there is atleast one field empty
                if (course_code2_data.getText().isEmpty() || course_name_data.getText().isEmpty())
                    JOptionPane.showMessageDialog(add_button, "Fill all fields.");

                // add the new data to the table then close the dialog
                else {
                    Data_Manager.coursesList().add(new Course(course_code2_data.getText().toString(),
                            course_name_data.getText().toString()));
                    tableModel.addRow(new Object[] { course_code2_data.getText().toString(),
                            course_name_data.getText().toString() });
                    JOptionPane.showMessageDialog(add_button, "Add Success.");
                    Add_Dialog.this.dispose();
                }
            }

        });
        this.add(add_button, layout_Constraints);
    }

    private void displayAddStudent(JTable student_table) {
        // setup the table model to edit the rows
        DefaultTableModel student_table_model = (DefaultTableModel) student_table.getModel();

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
        layout_Constraints.gridx = 1;
        layout_Constraints.gridy = 5;
        this.add(gender_data, layout_Constraints);

        course_code = new JLabel("Course Code: ");
        layout_Constraints.fill = GridBagConstraints.HORIZONTAL;
        layout_Constraints.gridx = 0;
        layout_Constraints.gridy = 6;
        this.add(course_code, layout_Constraints);

        //list the courses available in a readable way
        String[] courses_listed = new String[Data_Manager.coursesList().size()];
        for (int course_count = 0; course_count < Data_Manager.coursesList().size(); course_count++) {
            Course current_course = Data_Manager.coursesList().get(course_count);
            courses_listed[course_count] = current_course.getCourseCode() + "-" + current_course.getCourseName();
        }
        Arrays.sort(courses_listed);

        course_code_data = new JComboBox<>(courses_listed);
        for (String course : courses_listed){
            //set the default course shown in the combo box to be N/A-Unenrolled
            if (course.equals("N/A-Unenrolled")) {
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
        add_button = new JButton("Add Item");
        add_button.setFocusable(false);
        add_button.setToolTipText("Add the item to the table.");
        add_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // check if there is atleast one field empty
                if (surname_data.getText().isEmpty() || first_name_data.getText().isEmpty() ||
                        middle_name_data.getText().isEmpty() || ID_number_data.getText().isEmpty() ||
                        year_level_data.getText().isEmpty() || gender_data.getText().isEmpty())
                    JOptionPane.showMessageDialog(add_button, "Fill all fields.");

                // add the new data to the table then close the dialog
                else {
                    String[] new_student_course = course_code_data.getSelectedItem().toString().split("-");
                    //traverse the course list to find the wanted course to enroll
                    for (Course course : Data_Manager.coursesList()) {
                        //if course is found, enroll the student then add the details of the student to the student table
                        if (course.getCourseCode().equals(new_student_course[0])) {
                            Student new_student = new Student(surname_data.getText(), first_name_data.getText(),
                                    middle_name_data.getText(), ID_number_data.getText(), year_level_data.getText(),
                                    gender_data.getText(), course.getCourseCode(), course.getCourseName());
                            course.getBlock().add(new_student);

                            student_table_model.addRow(new Object[] { new_student.getSurname(), new_student.getFirstName(),
                                    new_student.getMiddleName(),
                                    new_student.getIDNumber(), new_student.getYearLevel(), new_student.getGender(),
                                    new_student.getCourseCode() });
                        }
                    }
                    JOptionPane.showMessageDialog(add_button, "Add Success.");
                    Add_Dialog.this.dispose();
                }
            }
        });
        layout_Constraints.fill = GridBagConstraints.HORIZONTAL;
        layout_Constraints.gridx = 0;
        layout_Constraints.gridy = 9;
        layout_Constraints.gridwidth = 2;
        this.add(add_button, layout_Constraints);
    }
}
