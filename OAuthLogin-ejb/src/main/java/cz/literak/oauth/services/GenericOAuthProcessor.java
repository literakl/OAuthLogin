package cz.literak.oauth.services;

import cz.literak.oauth.model.dao.OAuthLoginDAO;
import cz.literak.oauth.model.entity.OAuthLogin;
import cz.literak.oauth.utils.OAuthConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.scribe.model.Token;
import org.scribe.oauth.OAuthService;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Date: 17.2.14
 */
public abstract class GenericOAuthProcessor {
    public static final String KEY_TOKEN = "OAUTH_TOKEN";
    public static final String KEY_STATE = "OAUTH_STATE";
    public static final String KEY_USER = "USER";

    protected static final Token NULL_TOKEN = null;

    private Logger log = LogManager.getLogger(GenericOAuthProcessor.class);

    @EJB
    OAuthLoginDAO loginDAO;

    protected void handleHandshakeOAuth1(HttpServletRequest request, HttpServletResponse response, OAuthConfiguration.Provider provider) throws IOException {
        OAuthService service = getService(request, provider);
        Token requestToken = service.getRequestToken();
        HttpSession session = request.getSession();
        session.setAttribute(KEY_TOKEN, requestToken);
        String redirectUrl = service.getAuthorizationUrl(requestToken);
        response.sendRedirect(redirectUrl);
    }

    protected void handleHandshakeOAuth2(HttpServletRequest request, HttpServletResponse response, OAuthConfiguration.Provider provider) throws IOException {
        OAuthService service = getService(request, provider);
        String redirectUrl = service.getAuthorizationUrl(NULL_TOKEN);
        response.sendRedirect(redirectUrl);
    }

    protected abstract OAuthService getService(HttpServletRequest request, OAuthConfiguration.Provider provider);

    protected OAuthLogin findExistingUser(String login, String accessToken, OAuthConfiguration.Provider provider) {
        OAuthLogin oAuthLogin;
        if (login != null) {
            oAuthLogin = loginDAO.findByProvider(provider.dbKey, login);
            if (oAuthLogin == null) {
                return null;
            }

            if (accessToken == null || !accessToken.equals(oAuthLogin.getAccessToken())) {
                log.debug("Updating access token, oAuth.id = " + oAuthLogin.getId());
                oAuthLogin.setAccessToken(accessToken);
                loginDAO.merge(oAuthLogin);
            }
            return oAuthLogin;
        }

        if (accessToken != null) {
            return loginDAO.findByAccessToken(provider.dbKey, accessToken);
        }

        return null;
    }

    protected String getCallbackUrl(HttpServletRequest request, String uriKey) {
        StringBuilder sb = new StringBuilder();
        sb.append(request.getScheme()).append("://").append(request.getServerName());
        if ("http".equalsIgnoreCase(request.getScheme())) {
            if (request.getServerPort() != 80) {
                sb.append(":").append(request.getServerPort());
            }
        } else {
            if (request.getServerPort() != 443) {
                sb.append(":").append(request.getServerPort());
            }
        }
        sb.append(request.getContextPath()).append("/callback/").append(uriKey);
        return sb.toString();
    }
}
