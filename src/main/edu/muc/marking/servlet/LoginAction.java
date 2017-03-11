package edu.muc.marking.servlet;

import com.google.common.collect.Sets;
import edu.muc.marking.bean.UserInfo;
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
import java.util.Set;

/**
 * version    date      author
 * ──────────────────────────────────
 * 1.0       17-3-4   wanlong.ma
 * Description: 登录验证
 * Others:
 * Function List:
 * History:
 */
public class LoginAction extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public LoginAction() {
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

        String account = request.getParameter("account").trim();
        String password = request.getParameter("password").trim();

        // 门户登录
        UserInfo userInfo = SoapUtil.userLogin(account,password);


        /////////////////////// 测试用：跳过门户验证的管理员登录 //////////////////////

        /* if(account.equals("13052043")){
            userInfo.setAccount("13052043");
            userInfo.setPassword("0000000");
            userInfo.setAuthPassed(true);
            userInfo.setName("wanlong.ma");
        }
        */

        // 管理员特殊处理
        Set<String> adminSet = Sets.newHashSet(
                "9900218",
                "9900417",
                "2005010",
                "9900755",
                "9900792",
                "9900307",
                "2007018",
                "2003016",
                "2003009",
                "2007133",
                "2005006",
                "2001028",
                "2005017",
                "2002038",
                "2006103",
                "2002028",
                "2004005",
                "2001001",
                "2004004",
                "2007071",
                "2002012",
                "9900700",
                "9900300",
                "9900117",
                "2013060"
        );

        if(account.equals(password) && adminSet.contains(account)){
            userInfo.setAuthPassed(true);
            userInfo.setAccount(account);
            userInfo.setPassword(password);
            userInfo.setName(account);
        }

        // 验证成功
        if(userInfo.isAuthPassed()){
            // 获取完整的用户信息
            UserInfoDao.queryCompleteUserInfo(userInfo);
        } else {
            userInfo.setMessage("用户名或密码错误");
        }

        PrintWriter printWriter = response.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonGenerator jsonGenerator = objectMapper.getJsonFactory().createJsonGenerator(printWriter);
        jsonGenerator.writeObject(userInfo);
        printWriter.flush();
        printWriter.close();
        jsonGenerator.close();

    }
}
