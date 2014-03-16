package cz.literak.oauth.services;

import com.eclipsesource.json.JsonObject;
import cz.literak.oauth.model.dao.OAuthLoginDAO;
import cz.literak.oauth.model.dao.UserDAO;
import cz.literak.oauth.model.entity.OAuthLogin;
import cz.literak.oauth.model.entity.User;
import cz.literak.oauth.utils.OAuthConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.TwitterApi;
import org.scribe.model.*;
import org.scribe.oauth.OAuthService;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Date: 11.2.14
 */
@Stateless
public class TwitterOAuthProcessor extends GenericOAuthProcessor implements IOAuthProcessor {
    private static final String RESOURCE_URL = "https://api.twitter.com/1.1/account/verify_credentials.json";

    private Logger log = LogManager.getLogger(TwitterOAuthProcessor.class);

    @EJB
    OAuthLoginDAO loginDAO;
    @EJB
    UserDAO userDAO;

    @Override
    public void handleHandshake(HttpServletRequest request, HttpServletResponse response, OAuthConfiguration.Provider provider) throws IOException {
        handleHandshakeOAuth1(request, response, provider);
    }

    @Override
    public boolean handleCallback(HttpServletRequest request, HttpServletResponse response, OAuthConfiguration.Provider provider)
                               throws ServletException, IOException {
        HttpSession session = request.getSession();
        Token requestToken = (Token) session.getAttribute(KEY_TOKEN);
        if (requestToken == null) {
            log.warn("Request token is missing, does browser support cookies?");
            return false;
        }
        session.removeAttribute(KEY_TOKEN);

        String verifierValue = request.getParameter("oauth_verifier");
        if (verifierValue == null) {
            log.warn("Callback did not receive oauth_verifier parameter!");
            return false;
        }
        Verifier verifier = new Verifier(verifierValue);

        OAuthService service = getService(request, provider);
        String screenName = request.getParameter("screen_name");
        Token accessToken = service.getAccessToken(requestToken, verifier);

        OAuthLogin oAuthLogin = findExistingUser(screenName, accessToken.getToken(), provider);
        if (oAuthLogin != null) {
            log.debug("Found user, oAuth.id = " + oAuthLogin.getId());
            session.setAttribute(KEY_USER, oAuthLogin.getUser());
            return true;
        }

        OAuthRequest resourceRequest = new OAuthRequest(Verb.GET, RESOURCE_URL);
        service.signRequest(accessToken, resourceRequest);
        Response resourceResponse = resourceRequest.send();
        JsonObject jsonObject = JsonObject.readFrom(resourceResponse.getBody());
        screenName = jsonObject.get("screen_name").asString();

        oAuthLogin = findExistingUser(screenName, null, provider);
        if (oAuthLogin != null) {
            log.debug("Found user, oAuth.id = " + oAuthLogin.getId());
            session.setAttribute(KEY_USER, oAuthLogin.getUser());
            return true;
        }

        User user = (User) session.getAttribute(KEY_USER);
        if (user == null) {
            user = new User(jsonObject.get("name").asString());
            userDAO.persist(user);
            session.setAttribute(KEY_USER, user);
            log.debug("Created user, id " + user.getId());
        }

        oAuthLogin = new OAuthLogin(user, provider.dbKey, screenName);
        oAuthLogin.setAccessToken(accessToken.getToken());
        loginDAO.persist(oAuthLogin);
        log.debug("Created oAuthLogin, id = " + oAuthLogin.getId() + ", user = " + user.getId());
        return true;
    }

    @Override
    protected OAuthService getService(HttpServletRequest request, OAuthConfiguration.Provider provider) {
        return new ServiceBuilder()
                    .provider(TwitterApi.Authenticate.class)
                    .apiKey(provider.apiKey)
                    .apiSecret(provider.apiSecret)
//                    .scope("")
                    // twitter accepts any callback URL
                    .callback(getCallbackUrl(request, provider.uriKey))
//                    .callback("http://www.literak.cz/zaloha/qi.php")
//                    .debug()
                    .build();
    }
}
