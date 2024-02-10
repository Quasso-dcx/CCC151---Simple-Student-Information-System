package model;

public class StudentKeyMaker {
    public StudentKeyMaker(){
    }

    public String keyMaker(String course_code, String ID_num){
        return course_code + "()" + ID_num;
    }
}
