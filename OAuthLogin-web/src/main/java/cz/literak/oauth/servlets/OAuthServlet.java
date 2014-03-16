package cz.literak.oauth.servlets;

import cz.literak.oauth.services.IOAuthProcessor;
import cz.literak.oauth.utils.OAuthConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ejb.EJB;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Date: 13.2.14
 */
public abstract class OAuthServlet extends HttpServlet {
    @EJB
    OAuthConfiguration configuration;

    private Logger log = LogManager.getLogger(OAuthServlet.class);

    protected void process(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            String pathInfo = request.getPathInfo();
            if (pathInfo == null || pathInfo.length() < 2) {
                response.sendError(404);
                return;
            }

            pathInfo = pathInfo.substring(1);
            OAuthConfiguration.Provider provider = configuration.get(pathInfo);
            if (provider == null) {
                log.error("Unexpected provider " + pathInfo);
                throw new ServletException("Missing configuration for OAuth provider '" + pathInfo + "'");
            }

            logParameters(request);
            Context ctx = new InitialContext();
            IOAuthProcessor authProcessor = (IOAuthProcessor) ctx.lookup(provider.processor);
            execute(authProcessor, provider, request, response);
        } catch (Exception e) {
            log.error("OAuth processing failed!", e);
            throw new ServletException("OAuth processing failed!", e);
        }
    }

    private void logParameters(HttpServletRequest request) {
        Map<String, String[]> parameters = request.getParameterMap();
        for (String key : parameters.keySet()) {
            String[] values = parameters.get(key);
            for (String value : values) {
                log.debug("HTTP param " + key + " = '" + value + "'");
            }
        }
    }

    protected abstract void execute(IOAuthProcessor authProcessor, OAuthConfiguration.Provider params,
                                    HttpServletRequest request, HttpServletResponse response)
                                    throws ServletException, IOException;
}
