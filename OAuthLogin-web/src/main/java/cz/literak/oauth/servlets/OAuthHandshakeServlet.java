package cz.literak.oauth.servlets;

import cz.literak.oauth.services.IOAuthProcessor;
import cz.literak.oauth.utils.OAuthConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Date: 9.2.14
 */
public class OAuthHandshakeServlet extends OAuthServlet {
    private Logger log = LogManager.getLogger(OAuthHandshakeServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request, response);
    }

    @Override
    protected void execute(IOAuthProcessor authProcessor, OAuthConfiguration.Provider params, HttpServletRequest request,
                           HttpServletResponse response) throws ServletException, IOException {
        try {
            authProcessor.handleHandshake(request, response, params);
        } catch (Exception e) {
            log.error("Handshake processing failed for " + params.uriKey, e);
            throw new ServletException(e);
        }
    }
}
