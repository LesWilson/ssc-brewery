package guru.sfg.brewery.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;

@Slf4j
public class UrlParametersAuthFilter extends CustomAuthFilterBase {

    public UrlParametersAuthFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
        super(requiresAuthenticationRequestMatcher);
    }

    protected String getUserName(HttpServletRequest request) {
        return request.getParameter("Api-Key");
    }
    protected String getPassword(HttpServletRequest request) {
        return request.getParameter("Api-Secret");
    }
}
