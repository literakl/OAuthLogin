package cz.literak.oauth.utils;

import javax.ejb.Singleton;
import javax.servlet.ServletException;
import java.util.HashMap;
import java.util.Map;

/**
 * Date: 11.2.14
 */
@Singleton
public class OAuthConfiguration {
    Map<String, Provider> providers = new HashMap<>();

    public void addParam(String provider, String key, String value) throws ServletException {
        if (value == null) {
            throw new ServletException("Missing key "+ (provider + "." + key));
        }

        Provider map = providers.get(provider);
        if (map == null) {
            map = new Provider(provider);
            providers.put(provider, map);
        }

        switch (key) {
            case "api.key":
                map.apiKey = value; break;
            case "api.secret":
                map.apiSecret = value; break;
            case "key":
                map.dbKey = value; break;
            case "processor":
                map.processor = value;
        }
    }

    public Provider get(String provider) {
        return providers.get(provider);
    }

    public static class Provider {
        public String apiKey, apiSecret, dbKey, processor, uriKey;

        public Provider(String key) {
            uriKey = key;
        }
    }
}
