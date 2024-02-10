package model;

import java.util.ArrayList;

import control.Data_Manager;

/*
 * Course object
 */
public class Course {
    private String code;
    private String name;
    private ArrayList<String> block_ID = new ArrayList<>(); //store only ID Numbers

    public Course(String code, String name) {
        this.code = code;
        this.name = name;
    }

    /*
     * Setters and Getters
     */

    public boolean isEmpty() {
        return block_ID.isEmpty();
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

    public ArrayList<String> getBlockIDs() {
        return this.block_ID;
    }

    /*
     * Invoke when deleting the course
     */
    public void courseDelete() {
        /*
         * Transfer all students to unenrolled course then clear the block after
         */
        for (int student_count = 0; student_count < block_ID.size(); student_count++) {
            Student unenrolled_student = Data_Manager.studentList().get(new StudentKeyMaker().keyMaker(this.getCourseCode(), block_ID.get(student_count)));
            
            unenrolled_student.setCourseCode(Data_Manager.notEnrolled().getCourseCode());
            unenrolled_student.setCourseName(Data_Manager.notEnrolled().getCourseName());
            Data_Manager.notEnrolled().getBlockIDs().add(unenrolled_student.getIDNumber());
        }
        
        block_ID.clear();
    }
}
