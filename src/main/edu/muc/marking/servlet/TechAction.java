package edu.muc.marking.servlet;

import com.google.common.base.Strings;
import edu.muc.marking.bean.SubmitResult;
import edu.muc.marking.dao.ScoreDao;
import edu.muc.marking.dao.TechDao;
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
 * Description: 技术支持点击记录
 * Others:
 * Function List:
 * History:
 */
public class TechAction extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public TechAction() {
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
        TechDao.techClickRecord(token);

    }


}
