package edu.muc.marking.dao;

import com.google.common.base.Preconditions;
import edu.muc.marking.dao.bean.Academy;
import edu.muc.marking.dao.bean.Admin;
import edu.muc.marking.db.DBUtil;

import java.sql.Connection;

/**
 * version    date      author
 * ──────────────────────────────────
 * 1.0       17-3-7   wanlong.ma
 * Description:
 * Others:
 * Function List:
 * History:
 */
public class AdminDao {
    /**
     * 通过学院id获取学院实体
     * @param account
     * @return
     */
    public static Admin queryAdminByaccount(String account){
        Connection conn = DBUtil.openConnection();
        String sql = "select * from t_admin where admin_account = ?";
        Admin admin = DBUtil.queryObject(conn,sql,Admin.class,account);
        Preconditions.checkNotNull(admin);
        DBUtil.closeConnection();
        return admin;
    }
}
