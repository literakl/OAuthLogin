package cz.literak.oauth.services;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import com.nimbusds.jose.JWSObject;
import org.scribe.model.Token;

import java.text.ParseException;
import java.util.HashMap;

/**
 * Date: 24.1.14
 */
public class GoogleToken extends Token {
    int expires;
    HashMap<String, Object> idToken;

    public GoogleToken(String token, JsonObject jsonObject, String rawResponse) {
        super(token, "", rawResponse);

        for (JsonObject.Member member : jsonObject) {
            String name = member.getName();
            JsonValue value = member.getValue();

            switch(name.toLowerCase()) {
                case "expires_in":
                    expires = value.asInt();
                    break;
                case "id_token":
                    String jws = value.asString();
                    try {
                        JWSObject jwsObject = JWSObject.parse(jws);
                        idToken = jwsObject.getPayload().toJSONObject();
                    } catch (ParseException e) {
                        throw new RuntimeException("Failed to parse id_token: " + jws);
                    }
            }
        }
    }
}
