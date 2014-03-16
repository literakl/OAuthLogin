package cz.literak.oauth.servlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Date: 28.2.14
 */
public class FindExistingUserServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String name=null;
//       name = "Hello "+request.getParameter("user");
//       response.setContentType("text/plain");
//       response.setCharacterEncoding("UTF-8");
//       response.getWriter().write(name);
       response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{data: [FB, Google]}");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{data: [FB, Google]}");
    }
}
