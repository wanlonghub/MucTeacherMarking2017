package edu.muc.marking.db;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * version    date      author
 * ──────────────────────────────────
 * 1.0       17-3-4   wanlong.ma
 * Description:
 * Others:
 * Function List:
 * History:
 */
public interface IResultSetCall<T> {

    public T invoke(ResultSet rs) throws SQLException;

}