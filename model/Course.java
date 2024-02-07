package model;

import java.util.ArrayList;

import control.Data_Manager;

/*
 * Course object
 */
public class Course {
    private String code;
    private String name;
    private ArrayList<Student> block = new ArrayList<>();

    public Course(String code, String name) {
        this.code = code;
        this.name = name;
    }

    /*
     * Setters and Getters
     */

    public boolean isEmpty() {
        return block.isEmpty();
    }

    public void setCourseCode(String code) {
        this.code = code;
    }

    public void setCourseName(String name) {
        this.name = name;
    }

    public String getCourseCode() {
        return this.code;
    }

    public String getCourseName() {
        return this.name;
    }

    public ArrayList<Student> getBlock() {
        return this.block;
    }

    /*
     * Invoke when deleting the course
     */
    public void courseDelete() {
        /*
         * Transfer all students to unenrolled course then clear the block after
         */
        for (int student_count = 0; student_count < block.size(); student_count++) {
            block.get(student_count).setCourseCode(Data_Manager.notEnrolled().getCourseCode());
            block.get(student_count).setCourseName(Data_Manager.notEnrolled().getCourseName());
            Data_Manager.notEnrolled().getBlock().add(block.get(student_count));
        }
        block.clear();
    }
}
