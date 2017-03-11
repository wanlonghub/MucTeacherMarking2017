package edu.muc.marking.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * version    date      author
 * ──────────────────────────────────
 * 1.0       17-3-4   wanlong.ma
 * Description:
 * Others:
 * Function List:
 * History:
 */
public class TestServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public TestServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // doPost(request, response);
        // do nothing
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/json");

        String action = request.getParameter("action");

        if("login_input".equals(action)) {
            request.getRequestDispatcher("login.jsp").forward(request , response);
        } else if("login".equals(action)) {
            String name = request.getParameter("name");
            String password = request.getParameter("password");

            System.out.println("name->" + name + ",password->" + password);
        }


    }


}
