package view;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import model.*;

/*
 * Facilitates the functionality and displays the main frame of the SSIS.
 */
public class SSISMainDisplay extends JFrame {
    // frame constants
    private static final int FRAME_HEIGHT = 750;
    private static final int FRAME_WIDTH = 1250;
    private final Dimension MAIN_DIMENSION = new Dimension(FRAME_WIDTH, FRAME_HEIGHT);
    private static final int PADDING_WIDTH = 20;
    private static final int PADDING_HEIGHT = 10;

    // panels for the display
    private JPanel banner_panel;
    private JPanel option_panel;
    private JPanel table_panel;

    // functional buttons
    private JButton add_button;
    private JButton edit_button;
    private JButton delete_button;
    private JButton save_button;
    private JButton students_button;
    private JButton courses_button;

    // data tables
    private JTable student_table;
    private JTable course_table;
    private static JTable display_table;

    public SSISMainDisplay() {
        // basic layouts for the frame display
        this.setTitle("Simple Student Information System (SSIS)");
        this.getContentPane().setPreferredSize(MAIN_DIMENSION);
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.setLayout(null);
        this.pack();
        this.setLocationRelativeTo(null); // must be set after pack()
        this.setResizable(false);
        this.setVisible(true);
        // ask the user if they want to save their actions in the csv file.
        // only save the file after closing the app / clicking the save button
        WindowListener exitListener = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int confirm = JOptionPane.showOptionDialog(
                        null, "Do you want to save your changes?",
                        "Exit Confirmation", JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, null, null);
                if (confirm == JOptionPane.YES_OPTION) {
                    Data_Manager.courseFileSaver();
                    Data_Manager.studentFileSaver();
                    System.exit(0);
                }
                if (confirm == JOptionPane.NO_OPTION)
                    System.exit(0);
            }
        };
        this.addWindowListener(exitListener);

        // create an instance of a Table_Manager where static tables are prepared
        new Table_Manager();
        student_table = Table_Manager.getStudentTable();
        course_table = Table_Manager.getCourseTable();

        bannerAreaDisplay();
        optionAreaDisplay();
        tableAreaDisplay();
    }

    /*
     * Display the top-most part of the frame. Shows the "SSIS" and the buttons to
     * control what table to display.
     */
    private void bannerAreaDisplay() {
        // setup the panel
        banner_panel = new JPanel();
        banner_panel.setBounds(0, 0, this.getWidth(), 75);
        banner_panel.setBackground(this.getBackground());
        banner_panel.setLayout(null);

        // setup the logo/name
        JLabel SSIS_name = new JLabel("SSIS");
        SSIS_name.setBounds(PADDING_WIDTH * 9, PADDING_HEIGHT, 100, 50);
        SSIS_name.setFont(new Font("Courier", Font.BOLD, 32));
        SSIS_name.setHorizontalAlignment(SwingConstants.CENTER);
        banner_panel.add(SSIS_name);

        // setup the button to display the student data table
        students_button = new JButton("Students");
        students_button.setBounds(850, PADDING_HEIGHT * 2, 120, 30);
        students_button.setFont(new Font("Times New Roman", Font.PLAIN, 22));
        students_button.setFocusable(false);
        students_button.addActionListener(e -> {
            // to prevent confusion to what table currently displayed
            students_button.setEnabled(false);
            courses_button.setEnabled(true);

            display_table.getSelectionModel().clearSelection(); // clear the previous table selection
            display_table = student_table; // change the table to be displayed
            refreshTable(); // refresh the display table
        });
        banner_panel.add(students_button);

        // setup the button to display the course data table
        courses_button = new JButton("Courses");
        courses_button.setBounds(1000, PADDING_HEIGHT * 2, 120, 30);
        courses_button.setFont(new Font("Times New Roman", Font.PLAIN, 22));
        courses_button.setFocusable(false);
        courses_button.addActionListener(e -> {
            // to prevent confusion to what table currently displayed
            courses_button.setEnabled(false);
            students_button.setEnabled(true);

            display_table.getSelectionModel().clearSelection(); // clear the previous table selection
            display_table = course_table; // change the table to be displayed
            refreshTable(); // refresh the display table
        });
        banner_panel.add(courses_button);

        this.add(banner_panel);
    }

    /*
     * Display the second part of the frame. Shows the buttons for the functionality
     * of the app.
     * Add, Edit, Delete, and Display the information on the table.
     */
    private void optionAreaDisplay() {
        // setup the panel
        option_panel = new JPanel();
        option_panel.setBounds(0, banner_panel.getHeight(), this.getWidth(), 100);
        option_panel.setBackground(this.getBackground());
        option_panel.setLayout(null);

        // setup and add functionality of the add button
        add_button = new JButton("Add Item");
        add_button.setBounds(PADDING_WIDTH * 1 + 650, PADDING_HEIGHT * 3, 120, 30);
        add_button.setFont(new Font("Times New Roman", Font.PLAIN, 18));
        add_button.setFocusable(false);
        add_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // display the add_dialog
                new Add_Dialog(display_table).setVisible(true);
            }
        });
        option_panel.add(add_button);

        // setup and add functionality of the edit button
        edit_button = new JButton("Edit Item");
        edit_button.setBounds(PADDING_WIDTH * 2 + 650 + 120 * 1, PADDING_HEIGHT * 3, 120, 30);
        edit_button.setFont(new Font("Times New Roman", Font.PLAIN, 18));
        edit_button.setFocusable(false);
        edit_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selected_row = display_table.getSelectedRow();
                if (selected_row < 0)
                    JOptionPane.showMessageDialog(null, "Please select a row.", "Edit Data Error",
                            JOptionPane.DEFAULT_OPTION);
                else {
                    // avoid editing the unenrolled course (the default course)
                    if (display_table.getValueAt(selected_row, 0).equals("N/A")
                            && display_table.getValueAt(selected_row, 1).equals("Unenrolled")) {
                        JOptionPane.showMessageDialog(null, "Cannot be edited.", "Edit Data Error.",
                                JOptionPane.DEFAULT_OPTION);
                    } else {
                        // display the edit_dialog
                        new Edit_Dialog(display_table).setVisible(true);
                    }
                }
            }
        });
        option_panel.add(edit_button);

        // setup and add functionality of the delete button
        delete_button = new JButton("Delete Item");
        delete_button.setBounds(PADDING_WIDTH * 3 + 650 + 120 * 2, PADDING_HEIGHT * 3, 120, 30);
        delete_button.setFont(new Font("Times New Roman", Font.PLAIN, 18));
        delete_button.setFocusable(false);
        delete_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selected_row = display_table.getSelectedRow();
                if (selected_row < 0)
                    JOptionPane.showMessageDialog(null, "Please select a row.", "Delete Data Error",
                            JOptionPane.DEFAULT_OPTION);
                else {
                    // avoid deleting the unenrolled course (the default course)
                    if (display_table.getValueAt(selected_row, 0).equals("N/A")
                            && display_table.getValueAt(selected_row, 1).equals("Unenrolled")) {
                        JOptionPane.showMessageDialog(null, "Cannot be deleted.", "Delete Data Error",
                                JOptionPane.DEFAULT_OPTION);
                    } else {
                        // display the delete_dialog
                        new Delete_Process(display_table);
                    }
                }
            }
        });
        option_panel.add(delete_button);

        // setup and add functionality of the save button
        save_button = new JButton("Save Tables");
        save_button.setBounds(PADDING_WIDTH * 4 + 650 + 120 * 3, PADDING_HEIGHT * 3, 130, 30);
        save_button.setFont(new Font("Times New Roman", Font.PLAIN, 18));
        save_button.setFocusable(false);
        save_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // process the saving of the table data to csv files
                Data_Manager.courseFileSaver();
                Data_Manager.studentFileSaver();
                JOptionPane.showConfirmDialog(null, "Tables Saved.", "Saving Tables", JOptionPane.PLAIN_MESSAGE);
            }
        });
        option_panel.add(save_button);

        this.add(option_panel);
    }

    /*
     * Display the last part of the display. Shows the tables gathered from the csv
     * files.
     */
    private void tableAreaDisplay() {
        // setup the table panel
        table_panel = new JPanel();
        table_panel.setBounds(0, banner_panel.getHeight() + option_panel.getHeight(),
                this.getWidth() - PADDING_WIDTH + 5,
                this.getHeight() - banner_panel.getHeight() - option_panel.getHeight() - 4 * PADDING_HEIGHT);
        table_panel.setLayout(new BoxLayout(table_panel, BoxLayout.Y_AXIS));

        // default table to display
        display_table = student_table;
        students_button.setEnabled(false);

        refreshTable(); // refresh the display in table_panel

        this.add(table_panel);
    }

    /*
     * Refreshes the table displayed.
     */
    private void refreshTable() {
        // setup and manage the display_table
        JScrollPane table_ScrollPane = new JScrollPane(display_table);
        table_ScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        table_ScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        // remove the current objects displayed in the table_panel, then refresh
        table_panel.removeAll();
        table_panel.revalidate();
        table_panel.repaint();
        table_panel.add(table_ScrollPane);
    }
}
