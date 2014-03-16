package cz.literak.oauth.servlets;

import cz.literak.oauth.model.entity.User;
import cz.literak.oauth.services.GenericOAuthProcessor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Date: 17.2.14
 */
public class LogoutServlet extends HttpServlet {
    private Logger log = LogManager.getLogger(LogoutServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(GenericOAuthProcessor.KEY_USER);
        if (user != null) {
            log.debug("Logging out user, id = " + user.getId());
            session.removeAttribute(GenericOAuthProcessor.KEY_USER);
        }
        session.invalidate();
        response.sendRedirect(request.getContextPath());
    }
}
