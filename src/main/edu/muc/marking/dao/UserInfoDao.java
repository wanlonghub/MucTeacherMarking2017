package edu.muc.marking.dao;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import edu.muc.marking.bean.UserInfo;
import edu.muc.marking.dao.bean.*;
import edu.muc.marking.db.DBUtil;
import edu.muc.marking.test.TestApp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;

/**
 * version    date      author
 * ──────────────────────────────────
 * 1.0       17-3-5   wanlong.ma
 * Description:
 * Others:
 * Function List:
 * History:
 */
public class UserInfoDao {

    private static Logger logger = LoggerFactory.getLogger(UserInfoDao.class);

    /**
     * 获取完整的用户信息
     * 先决条件：account、password、name不为null
     * @param userInfo
     */
    public static void queryCompleteUserInfo(UserInfo userInfo) {
        Preconditions.checkNotNull(userInfo);
        Preconditions.checkNotNull(userInfo.getAccount());
        Preconditions.checkNotNull(userInfo.getPassword());
        Preconditions.checkNotNull(userInfo.getName());

        Connection conn = DBUtil.openConnection();

        try{

            // 是否是管理员
            String sql = "select * from t_admin where admin_account = ? and status = 1";
            conn.setAutoCommit(true);
            Admin admin = DBUtil.queryObject(conn, sql, Admin.class, userInfo.getAccount());

            if(admin != null){

                userInfo.setPassword("");

                // token
                String token = UUID.randomUUID().toString().replaceAll("-","");
                String sql2 = "insert into t_token(token_value,token_time,user_account,status) " +
                        " values(?,now(),?,1)";
                int count2 = DBUtil.execute(conn,sql2,token,userInfo.getAccount());
                if(count2 != 1){
                    userInfo.setAuthPassed(false);
                    userInfo.setMessage("token创建失败，请重试");
                }else{
                    userInfo.setToken(token);
                    // 返回管理员信息
                    userInfo.setAuthPassed(true);
                    userInfo.setAdmin(true);
                    if(admin.getAdmin_id() > 1000)// 是否是超级管理员：admin id > 1000   academy_id = 0
                        userInfo.setAcademyId(1); // <-=====
                    else
                        userInfo.setAcademyId(admin.getAcademy_id());
                    return;
                }
            }

            // 是学生
            sql = "select * from t_student where student_account = ?";
            Student student = DBUtil.queryObject(conn,sql,Student.class,userInfo.getAccount());
            System.out.println(student.toString());
            if(student == null){
                // 没有用户信息：“您已成功登录，但未找到您的辅导员数据，请联系管理员”
                userInfo.setAuthPassed(false);
                userInfo.setMessage("登录成功，但未找到您的班级数据，请联系平台管理员");
                userInfo.setPassword("");
            }else{
                userInfo.setPassword("");
                userInfo.setName(student.getStudent_name());
                userInfo.setStatus(student.getStatus());
                if(student.getStatus() == Student.MARKING_STATUS_ALLOWED){
                    userInfo.setMessage("登录成功");
                    // token
                    String token = UUID.randomUUID().toString().replaceAll("-","");
                    userInfo.setToken(token);
                    String sql2 = "insert into t_token(token_value,token_time,user_account,status) " +
                            " values(?,now(),?,1)";
                    int count2 = DBUtil.execute(conn,sql2,token,userInfo.getAccount());
                    if(count2 != 1){
                        userInfo.setAuthPassed(false);
                        userInfo.setMessage("token创建失败，请重试");
                    }
                } else{
                    userInfo.setAuthPassed(false);
                    userInfo.setMessage("您已打过分，感谢参与");
                }
                userInfo.setClassId(student.getClass_id());
            }
        }catch (SQLException e){
            logger.error(e.getMessage(),e);
        }finally {
            DBUtil.closeConnection();
        }

    }

    /**
     * 通过学号获取学生信息
     * @param account
     * @return
     */
    public static Student queryStudentByAccount(String account){
        Connection conn = DBUtil.openConnection();
        String sql = "select * from t_student where student_account = ?";
        Student student = DBUtil.queryObject(conn,sql,Student.class,account);
        Preconditions.checkNotNull(student);
        DBUtil.closeConnection();
        return student;
    }

    /**
     * 通过班级id获取班级实体
     * @param classId
     * @return
     */
    public static ClassBean queryClassBeanByClassId(int classId){
        Connection conn = DBUtil.openConnection();
        String sql = "select * from t_class where class_id = ?";
        ClassBean classBean = DBUtil.queryObject(conn,sql,ClassBean.class,classId);
        Preconditions.checkNotNull(classBean);
        DBUtil.closeConnection();
        return classBean;
    }

    /**
     * 通过学院id获取学院实体
     * @param academyId
     * @return
     */
    public static Academy queryAcademyById(int academyId){
        Connection conn = DBUtil.openConnection();
        String sql = "select * from t_academy where academy_id = ?";
        Academy academy = DBUtil.queryObject(conn,sql,Academy.class,academyId);
        Preconditions.checkNotNull(academy);
        DBUtil.closeConnection();
        return academy;
    }

    /**
     * 通过辅导员id获取辅导员实体
     * @param teacherId
     * @return
     */
    public static Teacher queryTeacherById(int teacherId){
        Connection conn = DBUtil.openConnection();
        String sql = "select * from t_teacher where teacher_id = ?";
        Teacher teacher = DBUtil.queryObject(conn,sql,Teacher.class,teacherId);
        Preconditions.checkNotNull(teacher);
        DBUtil.closeConnection();
        return teacher;
    }

    /**
     * 根据token获取用户信息
     * account
     * name
     * academy_id
     * academy_name
     * class_id
     * class_name
     * teacher_name
     * teacher_id
     *
     * @param token
     * @return
     */
    public static UserInfo queryUserInfoByToken(String token){
        UserInfo userInfo = new UserInfo();
        // token -> tokenBean -> userAccount
        Token tokenBean = TokenDao.queryTokenByTokenString(token);
        if(null == tokenBean){
            return userInfo;
        }
        // userAccount -> student_id student_name class_id
        userInfo.setAuthPassed(true);
        userInfo.setAccount(tokenBean.getUser_account());
        Student student = queryStudentByAccount(userInfo.getAccount());
        userInfo.setStatus(student.getStatus());
        userInfo.setName(student.getStudent_name());
        userInfo.setClassId(student.getClass_id());

        // class_id -> class_name academy_id teacher_id
        ClassBean classBean = queryClassBeanByClassId(userInfo.getClassId());
        userInfo.setClassName(classBean.getClass_name());
        userInfo.setTeacherId(classBean.getTeacher_id());
        userInfo.setAcademyId(classBean.getAcademy_id());
        userInfo.setTeacherId(classBean.getTeacher_id());

        // academy_id -> academy_name
        Academy academy = queryAcademyById(userInfo.getAcademyId());
        userInfo.setAcademyName(academy.getAcademy_name());

        // teacher_id -> teacher_name
        Teacher teacher = queryTeacherById(userInfo.getTeacherId());
        userInfo.setTeacherName(teacher.getTeacher_name());

        return userInfo;
    }




}
