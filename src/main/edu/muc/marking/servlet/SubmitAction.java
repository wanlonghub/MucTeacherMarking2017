package edu.muc.marking.servlet;

import com.google.common.base.Strings;
import edu.muc.marking.bean.SubmitResult;
import edu.muc.marking.bean.UserInfo;
import edu.muc.marking.dao.ScoreDao;
import edu.muc.marking.dao.UserInfoDao;
import edu.muc.marking.util.SoapUtil;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * version    date      author
 * ──────────────────────────────────
 * 1.0       17-3-4   wanlong.ma
 * Description: 提交答案
 * Others:
 * Function List:
 * History:
 */
public class SubmitAction extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public SubmitAction() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // doPost(request, response);
        // do nothing
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/json");

        String token = request.getParameter("token");
        String answer = request.getParameter("answer");

        SubmitResult submitResult = new SubmitResult();
        if(Strings.isNullOrEmpty(token)){
            submitResult.setStatus(SubmitResult.SUBMIT_STATS_TOKEN_MIEESD);
            submitResult.setMessage("token缺失");
        }else if(Strings.isNullOrEmpty(answer)){
            submitResult.setStatus(SubmitResult.SUBMIT_STATS_FAIL);
            submitResult.setMessage("Answer缺失");
        }else{
            ScoreDao.submitAnswerWithToken(submitResult,token,answer);
        }

        PrintWriter printWriter = response.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonGenerator jsonGenerator = objectMapper.getJsonFactory().createJsonGenerator(printWriter);
        jsonGenerator.writeObject(submitResult);
        printWriter.flush();
        printWriter.close();
        jsonGenerator.close();

    }


}
