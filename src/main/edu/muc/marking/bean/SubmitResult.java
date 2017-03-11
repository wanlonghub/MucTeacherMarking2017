package edu.muc.marking.bean;

/**
 * version    date      author
 * ──────────────────────────────────
 * 1.0       17-3-5   wanlong.ma
 * Description:
 * Others:
 * Function List:
 * History:
 */
public class SubmitResult {
    public static final int SUBMIT_STATS_TOKEN_MIEESD = 1; // token缺失
    public static final int SUBMIT_STATS_TOKEN_TIMEOUT = 2; // token过期
    public static final int SUBMIT_STATS_SUCC = 3; // 提交成功
    public static final int SUBMIT_STATS_FAIL = 4; // 提交失败
    public static final int SUBMIT_STATS_TOKEN_INVALID_FAIL = 5; // token操作失败（失效时异常）
    public static final int SUBMIT_STATS_NOT_ALLOWED = 6; // 用户已经打过分

    private int status;
    private String message;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "SubmitResult{" +
                "status=" + status +
                ", message='" + message + '\'' +
                '}';
    }
}
