package cz.literak.oauth.servlets;

import cz.literak.oauth.utils.OAuthConfiguration;

import javax.ejb.EJB;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.util.StringTokenizer;

/**
 * Date: 11.2.14
 */
public class InitServlet extends HttpServlet {
    @EJB
    OAuthConfiguration configuration;

    @Override
    public void init(ServletConfig config) throws ServletException {
        ServletContext context = config.getServletContext();
        String parameter = context.getInitParameter("oauth.providers");
        StringTokenizer stk = new StringTokenizer(parameter, ",");
        while (stk.hasMoreTokens()) {
            String provider = stk.nextToken().trim();
            addParam(context, provider, "api.key");
            addParam(context, provider, "api.secret");
            addParam(context, provider, "key");
            addParam(context, provider, "processor");
        }
    }

    private void addParam(ServletContext context, String provider, String key) throws ServletException {
        String value = context.getInitParameter(provider + "." + key);
        configuration.addParam(provider, key, value);
    }
}
