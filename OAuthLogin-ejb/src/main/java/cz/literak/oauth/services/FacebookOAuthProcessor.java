package cz.literak.oauth.services;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import cz.literak.oauth.model.dao.OAuthLoginDAO;
import cz.literak.oauth.model.dao.UserDAO;
import cz.literak.oauth.model.entity.OAuthLogin;
import cz.literak.oauth.model.entity.User;
import cz.literak.oauth.utils.OAuthConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.FacebookApi;
import org.scribe.model.*;
import org.scribe.oauth.OAuthService;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Date: 11.2.14
 */
@Stateless
public class FacebookOAuthProcessor extends GenericOAuthProcessor implements IOAuthProcessor {
    private static final String RESOURCE_URL = "https://graph.facebook.com/me";

    private Logger log = LogManager.getLogger(FacebookOAuthProcessor.class);

    @EJB
    OAuthLoginDAO loginDAO;
    @EJB
    UserDAO userDAO;

    @Override
    public void handleHandshake(HttpServletRequest request, HttpServletResponse response, OAuthConfiguration.Provider provider) throws IOException {
        handleHandshakeOAuth2(request, response, provider);
    }

/*
error = 'access_denied'
error_code = '200'
error_reason = 'user_denied'
error_description = 'Permissions error'
 */
    @Override
    public boolean handleCallback(HttpServletRequest request, HttpServletResponse response, OAuthConfiguration.Provider provider)
                               throws ServletException, IOException {
        String error = request.getParameter("error_code");
        if (error != null) {
            return false;
        }

        String verifierValue = request.getParameter("code");
        if (verifierValue == null) {
            log.warn("Callback did not receive code parameter!");
            return false;
        }
        Verifier verifier = new Verifier(verifierValue);

        OAuthService service = getService(request, provider);
        Token accessToken = service.getAccessToken(NULL_TOKEN, verifier);

        HttpSession session = request.getSession();
        OAuthLogin oAuthLogin = findExistingUser(null, accessToken.getToken(), provider);
        if (oAuthLogin != null) {
            log.info("Access token matched, oAuth.id = " + oAuthLogin.getId());
            session.setAttribute(KEY_USER, oAuthLogin.getUser());
            return true;
        }

        OAuthRequest resourceRequest = new OAuthRequest(Verb.GET, RESOURCE_URL);
        service.signRequest(accessToken, resourceRequest);
        Response resourceResponse = resourceRequest.send();

        JsonObject jsonObject = JsonObject.readFrom(resourceResponse.getBody());
        JsonValue value = jsonObject.get("username");
        String screenName = (value != null) ? value.asString() : null;

        oAuthLogin = findExistingUser(screenName, accessToken.getToken(), provider);
        if (oAuthLogin != null) {
            log.debug("Found user, oAuth.id = " + oAuthLogin.getId());
            session.setAttribute(KEY_USER, oAuthLogin.getUser());
            return true;
        }

        User user = (User) session.getAttribute(KEY_USER);
        if (user == null) {
            value = jsonObject.get("name");
            user = new User((value != null) ? value.asString() : null);

            value = jsonObject.get("first_name");
            if (value != null) {
                user.setFirstName(value.asString());
            }

            value = jsonObject.get("last_name");
            if (value != null) {
                user.setLastName(value.asString());
            }

            setEmail(jsonObject, user);

            userDAO.persist(user);
            session.setAttribute(KEY_USER, user);
            log.debug("Created user, id " + user.getId());
        } else {
            if (user.getEmail() == null) {
                setEmail(jsonObject, user);
                userDAO.merge(user);
                log.debug("Set email to user, id " + user.getId());
            }
        }

        oAuthLogin = new OAuthLogin(user, provider.dbKey, screenName);
        oAuthLogin.setAccessToken(accessToken.getToken());
        loginDAO.persist(oAuthLogin);
        log.debug("Created oAuthLogin, id = " + oAuthLogin.getId() + ", user = " + user.getId());
        return true;
    }

    private void setEmail(JsonObject jsonObject, User user) {
        JsonValue value;
        value = jsonObject.get("email");
        if (value != null) {
            user.setEmail(value.asString());
        }
    }

    @Override
    protected OAuthService getService(HttpServletRequest request, OAuthConfiguration.Provider provider) {
        String secret = new BigInteger(130, new SecureRandom()).toString(32);
        request.getSession().setAttribute(KEY_STATE, secret);
        return new ServiceBuilder()
                    .provider(FacebookApi.class)
                    .apiKey(provider.apiKey)
                    .apiSecret(provider.apiSecret)
                    .scope("email")
//                    .state(secret) TODO uncomment when subscribe 2.1 is releases
                    // Facebook checks if callback URL is registered in app settings
                    .callback(getCallbackUrl(request, provider.uriKey))
//                    .debug()
                    .build();
    }
}
