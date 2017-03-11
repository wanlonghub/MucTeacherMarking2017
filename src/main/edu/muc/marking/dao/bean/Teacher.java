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
public class Teacher extends Base {
    private int teacher_id;
    private String teacher_name;
    private String teacher_account;
    private int academy_id;

    public int getTeacher_id() {
        return teacher_id;
    }

    public void setTeacher_id(int teacher_id) {
        this.teacher_id = teacher_id;
    }

    public String getTeacher_name() {
        return teacher_name;
    }

    public void setTeacher_name(String teacher_name) {
        this.teacher_name = teacher_name;
    }

    public String getTeacher_account() {
        return teacher_account;
    }

    public void setTeacher_account(String teacher_account) {
        this.teacher_account = teacher_account;
    }

    public int getAcademy_id() {
        return academy_id;
    }

    public void setAcademy_id(int academy_id) {
        this.academy_id = academy_id;
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "teacher_id=" + teacher_id +
                ", teacher_name='" + teacher_name + '\'' +
                ", teacher_account='" + teacher_account + '\'' +
                ", academy_id=" + academy_id +
                '}';
    }
}
