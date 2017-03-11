package edu.muc.marking.dao.bean;

/**
 * version    date      author
 * ──────────────────────────────────
 * 1.0       17-3-5   wanlong.ma
 * Description:
 * Others:
 * Function List:
 * History:
 */
public class Student extends Base {

    public static final int MARKING_STATUS_ALLOWED = 1; // 未打过分，可以打分
    public static final int MARKING_STATUS_NOT_ALLOWED = 0; // 已打过分，不允许打分


    private int student_id;
    private String student_account;
    private String user_password;
    private String student_name;
    private int class_id;
    private int status;

    public Student() {}

    public int getStudent_id() {
        return student_id;
    }

    public void setStudent_id(int student_id) {
        this.student_id = student_id;
    }

    public String getStudent_account() {
        return student_account;
    }

    public void setStudent_account(String student_account) {
        this.student_account = student_account;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_passeword) {
        this.user_password = user_passeword;
    }

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    public int getClass_id() {
        return class_id;
    }

    public void setClass_id(int class_id) {
        this.class_id = class_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Student{" +
                "student_id=" + student_id +
                ", student_account='" + student_account + '\'' +
                ", user_password='" + user_password + '\'' +
                ", student_name='" + student_name + '\'' +
                ", class_id=" + class_id +
                ", status=" + status +
                '}';
    }
}
