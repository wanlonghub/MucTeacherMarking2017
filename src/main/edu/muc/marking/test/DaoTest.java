package edu.muc.marking.test;

import edu.muc.marking.bean.UserInfo;
import edu.muc.marking.dao.UserInfoDao;
import edu.muc.marking.db.DBUtil;
import junit.framework.TestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
public class DaoTest extends TestCase{

    private static Logger logger = LoggerFactory.getLogger(DaoTest.class);

    public void testqueryCompleteUserInfo(){
        UserInfo userInfo = new UserInfo();
        userInfo.setAuthPassed(true);
        userInfo.setAccount("1001");
        userInfo.setPassword("123456");
        userInfo.setName("小王");
        logger.info(userInfo.toString());
        UserInfoDao.queryCompleteUserInfo(userInfo);
        logger.info(userInfo.toString());
    }

    public void testR(){
        Connection connection = DBUtil.openConnection();
        PreparedStatement preStmt = null;
        ResultSet resultSet = DBUtil.query(connection,preStmt,"select * from t_student where class_id = 0");
        try{
            while(resultSet.next()){
                logger.info(resultSet.getString("student_name"));
            }
        }catch (SQLException e){
            logger.error("",e);
        }finally {
                try{
                    if (null != resultSet)
                        resultSet.close();
                    if (null != preStmt)
                        preStmt.close();
                }catch (SQLException e){
                    logger.error("",e);
                }
        }

    }


}
