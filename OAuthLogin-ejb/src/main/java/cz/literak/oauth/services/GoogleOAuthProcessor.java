package cz.literak.oauth.services;

import com.eclipsesource.json.JsonArray;
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
import org.scribe.builder.api.GoogleApi20;
import org.scribe.model.*;
import org.scribe.oauth.OAuthService;
//import ru.hh.oauth.subscribe.api.google.*;

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
public class GoogleOAuthProcessor extends GenericOAuthProcessor implements IOAuthProcessor {
    private static final String RESOURCE_URL = "https://www.googleapis.com/plus/v1/people/me";

    private Logger log = LogManager.getLogger(GoogleOAuthProcessor.class);

    @EJB
    OAuthLoginDAO loginDAO;
    @EJB
    UserDAO userDAO;

    @Override
    public void handleHandshake(HttpServletRequest request, HttpServletResponse response, OAuthConfiguration.Provider provider) throws IOException {
        handleHandshakeOAuth2(request, response, provider);
    }

// http://www.literak.cz/OAuthLogin/callback/google?error=access_denied&state=secret
    @Override
    public boolean handleCallback(HttpServletRequest request, HttpServletResponse response, OAuthConfiguration.Provider provider)
                               throws ServletException, IOException {
        String verifierValue = request.getParameter("code");
        if (verifierValue == null) {
            log.warn("Callback did not receive code parameter!");
            return false;
        }
        Verifier verifier = new Verifier(verifierValue);

        OAuthService service = getService(request, provider);
        Token accessToken = service.getAccessToken(NULL_TOKEN, verifier);
//        GoogleToken accessToken = (GoogleToken) service.getAccessToken(NULL_TOKEN, verifier); TODO subscribe 2.1
//        String screenName = (String) accessToken.idToken.get("sub");
        String screenName = null;

        HttpSession session = request.getSession();
        OAuthLogin oAuthLogin = findExistingUser(screenName, accessToken.getToken(), provider);
        if (oAuthLogin != null) {
            log.info("Access token matched, oAuth.id = " + oAuthLogin.getId());
            session.setAttribute(KEY_USER, oAuthLogin.getUser());
            return true;
        }

        OAuthRequest resourceRequest = new OAuthRequest(Verb.GET, RESOURCE_URL);
        service.signRequest(accessToken, resourceRequest);
        Response resourceResponse = resourceRequest.send();
        JsonObject jsonObject = JsonObject.readFrom(resourceResponse.getBody());

        User user = (User) session.getAttribute(KEY_USER);
        if (user == null) {
            JsonValue value = jsonObject.get("displayName");
            user = new User((value != null) ? value.asString() : null);

            // todo remove when subscribe 2.1
            value = jsonObject.get("id");
            screenName = value.asString();

            JsonObject jsonObjectName = jsonObject.get("name").asObject();
            value = jsonObjectName.get("givenName");
            if (value != null) {
                user.setFirstName(value.asString());
            }

            value = jsonObjectName.get("familyName");
            if (value != null) {
                user.setLastName(value.asString());
            }

            value = jsonObject.get("emails");
            if (value != null) {
                JsonArray emails = value.asArray();
                if (! emails.isEmpty()) {
                    jsonObject = emails.get(0).asObject();
                    user.setEmail(jsonObject.get("value").asString());
                }
            }

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
        String secret = new BigInteger(130, new SecureRandom()).toString(32);
        request.getSession().setAttribute(KEY_STATE, secret);
        return new ServiceBuilder()
//                    .provider(Google2API.class)
                    .provider(GoogleApi20.class)
                    .apiKey(provider.apiKey)
                    .apiSecret(provider.apiSecret)
                    .scope("openid profile email")
//                    .state(secret) TODO uncomment when subscribe 2.1 is releases
                    // Google checks if callback URL is registered in app settings
                    .callback(getCallbackUrl(request, provider.uriKey))
//                    .debug()
                    .build();
    }
}
