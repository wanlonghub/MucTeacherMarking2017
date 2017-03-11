package edu.muc.marking.dao.bean;

/**
 * version    date      author
 * ──────────────────────────────────
 * 1.0       17-3-6   wanlong.ma
 * Description:
 * Others:
 * Function List:
 * History:
 */
public class StudentCopy extends  Base {
    private int id;
    private String account;
    private String name;
    private String academy_name;
    private String class_name;

    @Override
    public String toString() {
        return "StudentCopy{" +
                "id=" + id +
                ", account='" + account + '\'' +
                ", name='" + name + '\'' +
                ", academy_name='" + academy_name + '\'' +
                ", class_name='" + class_name + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAcademy_name() {
        return academy_name;
    }

    public void setAcademy_name(String academy_name) {
        this.academy_name = academy_name;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }
}
