package edu.muc.marking.dao;

import com.google.common.base.Preconditions;
import edu.muc.marking.bean.SubmitResult;
import edu.muc.marking.dao.bean.Student;
import edu.muc.marking.dao.bean.Token;
import edu.muc.marking.db.DBUtil;
import edu.muc.marking.util.OtherUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * version    date      author
 * ──────────────────────────────────
 * 1.0       17-3-5   wanlong.ma
 * Description:分数相关数据操作
 * Others:
 * Function List:
 * History:
 */
public class ScoreDao {
    private static Logger logger = LoggerFactory.getLogger(ScoreDao.class);

    /**
     * 提交答案
     * @param submitResult
     * @param token
     * @param answer
     *
     * @return 通过参数引用返回
     */
    public static void submitAnswerWithToken(SubmitResult submitResult,String token, String answer){
        Preconditions.checkNotNull(submitResult);
        Preconditions.checkNotNull(token);
        Preconditions.checkNotNull(answer);

        // 验证token
        Token tokenBean = TokenDao.queryTokenByTokenString(token);
        if(tokenBean == null || tokenBean.getStatus() == Token.TOKEN_STATUS_MISSED){
            submitResult.setStatus(SubmitResult.SUBMIT_STATS_TOKEN_MIEESD);
            submitResult.setMessage("非法token");
            return;
        }else if(tokenBean.getStatus() == Token.TOKEN_STATUS_INVALID){
            submitResult.setStatus(SubmitResult.SUBMIT_STATS_TOKEN_TIMEOUT);
            submitResult.setMessage("token过期");
            return;
        }

        // 解析用户信息
        Student student = UserInfoDao.queryStudentByAccount(tokenBean.getUser_account());
        if(student.getStatus() == Student.MARKING_STATUS_NOT_ALLOWED){
            submitResult.setStatus(SubmitResult.SUBMIT_STATS_NOT_ALLOWED);
            submitResult.setMessage("不能重复打分");
            return;
        }

        // 解析答案
        int[] scoreArray = OtherUtil.parseStringAnswerToIntArray(answer);
        int totalScore = OtherUtil.getSumOfArray(scoreArray);

        // 失效token
        Connection connection = DBUtil.openConnection();
        String sql = "update t_token set status = 0 where token_value = ?";
        try{
            int invalidTokenResult = DBUtil.execute(connection,sql,token);
            if(invalidTokenResult == 1){ // token失效成功
                // 提交答案
                sql = "insert into t_result(student_id,mark_result_detail,mark_result_total,mark_time) " +
                        "values(?,?,?,NOW())";
                int insertResult = DBUtil.execute(connection,sql,student.getStudent_id(),answer,totalScore);
                if(insertResult == 1){
                    submitResult.setStatus(SubmitResult.SUBMIT_STATS_SUCC);
                    submitResult.setMessage("提交成功");
                    // 标记用户已打过分
                    sql = "update t_student set status = 0 where student_id = ?";
                    int updateResult = DBUtil.execute(connection,sql,student.getStudent_id());
                } else{
                    submitResult.setStatus(SubmitResult.SUBMIT_STATS_FAIL);
                    submitResult.setMessage("提交失败");
                }
            }else{ // token 失效失败
                submitResult.setStatus(SubmitResult.SUBMIT_STATS_TOKEN_INVALID_FAIL);
                submitResult.setMessage("token操作失败（失效时异常）");
            }
        }catch (SQLException e){
            logger.error("答案提交失败：插入数据失败",e);
            submitResult.setStatus(SubmitResult.SUBMIT_STATS_FAIL);
            submitResult.setMessage("提交失败");
        }finally {
            DBUtil.closeConnection();
        }
    }
}
