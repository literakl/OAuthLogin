package cz.literak.oauth.services;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import org.scribe.builder.api.DefaultApi20;
import org.scribe.exceptions.OAuthException;
import org.scribe.extractors.AccessTokenExtractor;
import org.scribe.model.*;
import org.scribe.oauth.OAuth20ServiceImpl;
import org.scribe.oauth.OAuthService;
import org.scribe.utils.OAuthEncoder;
import org.scribe.utils.Preconditions;

/**
 * Google OAuth2.0
 * Released under the same license as scribe (MIT License)
 * https://gist.github.com/yincrash/2465453
 * @author yincrash
 * @author literakl
 */
public class Google2API extends DefaultApi20 {

    private static final String AUTHORIZE_URL = "https://accounts.google.com/o/oauth2/auth?response_type=code";
    private static final String PARAM_REDIRECT_URI = "redirect_uri";
    private static final String PARAM_CLIENT_ID = "client_id";
    private static final String PARAM_SCOPE = "scope";
    private static final String PARAM_STATE = "state";

    @Override
    public String getAccessTokenEndpoint() {
        return "https://accounts.google.com/o/oauth2/token";
    }

    @Override
    public AccessTokenExtractor getAccessTokenExtractor() {
        return new AccessTokenExtractor() {
            public Token extract(String response) {
                Preconditions.checkEmptyString(response, "Response body is incorrect. Can't extract a token from an empty string");
                JsonObject jsonObject = JsonObject.readFrom(response);
                JsonValue tokenValue = jsonObject.get("access_token");
                if (tokenValue == null) {
                    throw new OAuthException("Response body is incorrect. Can't extract a token from this: '" + response + "'", null);
                }

//                String token = OAuthEncoder.decode(tokenValue);
                return new GoogleToken(tokenValue.asString(), jsonObject, response);
            }
        };
    }

    @Override
    public String getAuthorizationUrl(OAuthConfig config) {
        StringBuilder sb = new StringBuilder(AUTHORIZE_URL);
        sb.append("&").append(PARAM_CLIENT_ID).append("=").append(config.getApiKey());
        sb.append("&").append(PARAM_REDIRECT_URI).append("=").append(OAuthEncoder.encode(config.getCallback()));
        // Append scope if present
        if (config.hasScope()) {
            sb.append("&").append(PARAM_SCOPE).append("=")
              .append(OAuthEncoder.encode(config.getScope()));
        }
//        if (config.hasState()) {
//            sb.append("&").append(PARAM_STATE).append("=")
//              .append(OAuthEncoder.encode(config.getState()));
//        }
        return sb.toString();
    }

    @Override
    public Verb getAccessTokenVerb() {
        return Verb.POST;
    }

    @Override
    public OAuthService createService(OAuthConfig config) {
        return new GoogleOAuth2Service(this, config);
    }

    private class GoogleOAuth2Service extends OAuth20ServiceImpl {

        private static final String GRANT_TYPE_AUTHORIZATION_CODE = "authorization_code";
        private static final String GRANT_TYPE = "grant_type";
        private DefaultApi20 api;
        private OAuthConfig config;

        public GoogleOAuth2Service(DefaultApi20 api, OAuthConfig config) {
            super(api, config);
            this.api = api;
            this.config = config;
        }

        @Override
        public Token getAccessToken(Token requestToken, Verifier verifier) {
            OAuthRequest request = new OAuthRequest(api.getAccessTokenVerb(), api.getAccessTokenEndpoint());
            switch (api.getAccessTokenVerb()) {
                case POST:
                    request.addBodyParameter(OAuthConstants.CLIENT_ID, config.getApiKey());
                    request.addBodyParameter(OAuthConstants.CLIENT_SECRET, config.getApiSecret());
                    request.addBodyParameter(OAuthConstants.CODE, verifier.getValue());
                    request.addBodyParameter(OAuthConstants.REDIRECT_URI, config.getCallback());
                    request.addBodyParameter(GRANT_TYPE, GRANT_TYPE_AUTHORIZATION_CODE);
                    break;
                case GET:
                default:
                    request.addQuerystringParameter(OAuthConstants.CLIENT_ID, config.getApiKey());
                    request.addQuerystringParameter(OAuthConstants.CLIENT_SECRET, config.getApiSecret());
                    request.addQuerystringParameter(OAuthConstants.CODE, verifier.getValue());
                    request.addQuerystringParameter(OAuthConstants.REDIRECT_URI, config.getCallback());
                    if (config.hasScope()) request.addQuerystringParameter(OAuthConstants.SCOPE, config.getScope());
            }
            Response response = request.send();
            return api.getAccessTokenExtractor().extract(response.getBody());
        }
    }

}
