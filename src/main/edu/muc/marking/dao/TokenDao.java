package edu.muc.marking.dao;

import com.google.common.base.Preconditions;
import edu.muc.marking.dao.bean.Token;
import edu.muc.marking.db.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.rmi.runtime.Log;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * version    date      author
 * ──────────────────────────────────
 * 1.0       17-3-5   wanlong.ma
 * Description:
 * Others:
 * Function List:
 * History:
 */
public class TokenDao {

    private static Logger logger = LoggerFactory.getLogger(TokenDao.class);

    /**
     * 检查token有效性：存在&可用
     * @param token
     * @return
     */
    public static int isTokenValid(String token){
        Preconditions.checkNotNull(token);
        Connection connection = DBUtil.openConnection();
        String sql = "select * from t_token where token_value = ?" ;
        Token tokenBean = DBUtil.queryObject(connection,sql,Token.class,token);
        DBUtil.closeConnection();
        if(null == tokenBean)
            return Token.TOKEN_STATUS_MISSED;
        else if(tokenBean.getStatus() == Token.TOKEN_STATUS_INVALID)
            return Token.TOKEN_STATUS_INVALID;
        else
            return Token.TOKEN_STATUS_VALID;
    }

    /**
     * 通过token获取tokenBean
     * @param token
     * @return
     */
    public static Token queryTokenByTokenString(String token){
        Preconditions.checkNotNull(token);
        Connection connection = DBUtil.openConnection();
        String sql = "select * from t_token where token_value = ?" ;
        Token tokenBean = DBUtil.queryObject(connection,sql,Token.class,token);
        DBUtil.closeConnection();
        return  tokenBean;
    }

}
