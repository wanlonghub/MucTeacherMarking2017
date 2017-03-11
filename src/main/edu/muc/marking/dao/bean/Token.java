package edu.muc.marking.dao.bean;

import java.sql.Timestamp;

/**
 * version    date      author
 * ──────────────────────────────────
 * 1.0       17-3-5   wanlong.ma
 * Description:
 * Others:
 * Function List:
 * History:
 */
public class Token {

    public static final int TOKEN_STATUS_MISSED = 2; // 缺失
    public static final int TOKEN_STATUS_VALID = 1; // 有效
    public static final int TOKEN_STATUS_INVALID = 0; // 失效

    private int id;
    private String token_value;
    private Timestamp token_time;
    private String user_account;
    private int status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToken_value() {
        return token_value;
    }

    public void setToken_value(String token_value) {
        this.token_value = token_value;
    }

    public Timestamp getToken_time() {
        return token_time;
    }

    public void setToken_time(Timestamp token_time) {
        this.token_time = token_time;
    }

    public String getUser_account() {
        return user_account;
    }

    public void setUser_account(String user_account) {
        this.user_account = user_account;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Token{" +
                "id=" + id +
                ", token_value='" + token_value + '\'' +
                ", token_time=" + token_time +
                ", user_account='" + user_account + '\'' +
                ", status=" + status +
                '}';
    }
}
