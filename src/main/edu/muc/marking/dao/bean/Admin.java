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
public class Admin extends Base {
    private int admin_id;// 大于1000时表示超级管理员
    private String admin_account;
    private String user_password;
    private String admin_name;
    private int academy_id;
    private int status;

    public Admin(){}

    public int getAdmin_id() {
        return admin_id;
    }

    public void setAdmin_id(int admin_id) {
        this.admin_id = admin_id;
    }

    public String getAdmin_account() {
        return admin_account;
    }

    public void setAdmin_account(String admin_acount) {
        this.admin_account = admin_acount;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    public String getAdmin_name() {
        return admin_name;
    }

    public void setAdmin_name(String admin_name) {
        this.admin_name = admin_name;
    }

    public int getAcademy_id() {
        return academy_id;
    }

    public void setAcademy_id(int academy_id) {
        this.academy_id = academy_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "admin_id=" + admin_id +
                ", admin_account='" + admin_account + '\'' +
                ", user_password='" + user_password + '\'' +
                ", admin_name='" + admin_name + '\'' +
                ", academy_id=" + academy_id +
                ", status=" + status +
                '}';
    }
}
