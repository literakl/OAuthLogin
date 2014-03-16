package cz.literak.oauth.servlets;

import javax.servlet.*;
import java.io.IOException;

/**
 * Date: 11.1.14
 */
public class EncodingFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request.getCharacterEncoding() == null) {
            request.setCharacterEncoding("UTF-8");
        }

        if (chain != null) {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {

    }
}
