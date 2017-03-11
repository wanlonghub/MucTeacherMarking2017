package edu.muc.marking.dao;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import edu.muc.marking.bean.UserInfo;
import edu.muc.marking.dao.bean.ClassBean;
import edu.muc.marking.dao.bean.Student;
import edu.muc.marking.dao.bean.Token;
import edu.muc.marking.db.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class TechDao {

    private static Logger logger = LoggerFactory.getLogger(TechDao.class);

    /**
     * 记住点击技术支持的用户信息
     * @param token
     */
    public static void techClickRecord(String token){

        Connection connection = null;
        String sql = null;
        if(Strings.isNullOrEmpty(token)){
            sql = "insert into t_tech(click_time) values(NOW())";
            try{
                connection = DBUtil.openConnection();
                DBUtil.execute(connection,sql);
            }catch (SQLException e){
                logger.error("插入技术点击数据时发生异常",e);
            }finally {
                DBUtil.closeConnection();
            }
        }else{
            Token tokenBean = TokenDao.queryTokenByTokenString(token);
            Student student = UserInfoDao.queryStudentByAccount(tokenBean.getUser_account());
            ClassBean classBean = UserInfoDao.queryClassBeanByClassId(student.getClass_id());
            sql = "insert into t_tech(user_account,user_name,user_class_name,click_time) " +
                    " values(?,?,?,NOW())";
            try{
                connection = DBUtil.openConnection();
                DBUtil.execute(connection,sql,student.getStudent_account(),student.getStudent_name(),classBean.getClass_name());
            }catch (SQLException e){
                logger.error("插入技术点击数据时发生异常",e);
            }finally {
                DBUtil.closeConnection();
            }
        }
    }
}
