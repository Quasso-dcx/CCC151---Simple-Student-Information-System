package model;

/**
 * Create a student key to minimize typo in the code.
 */
public class StudentKeyMaker {
    public StudentKeyMaker(){
    }

    /**
     * Student Key maker.
     * 
     * @param course_code
     * @param ID_num
     * @return student key
     */
    public String keyMaker(String course_code, String ID_num){
        return course_code + "()" + ID_num;
    }
}
