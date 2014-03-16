package cz.literak.oauth.services;

import cz.literak.oauth.utils.OAuthConfiguration;

import javax.ejb.Local;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Date: 11.2.14
 */
@Local
public interface IOAuthProcessor {

    void handleHandshake(HttpServletRequest request, HttpServletResponse response, OAuthConfiguration.Provider providerParams) throws IOException;

    boolean handleCallback(HttpServletRequest request, HttpServletResponse response, OAuthConfiguration.Provider provider) throws IOException, ServletException;
}
