package io.vscale.uniservice.security.rest.filters;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 25.03.2018
 *
 * @author Andrey Romanov
 * @version 1.0
 */
public class TokenAuthenticationFilter extends GenericFilterBean{

    private final AuthenticationProvider authenticationProvider;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final String header;

    public TokenAuthenticationFilter( AuthenticationProvider authenticationProvider,
                                      AuthenticationEntryPoint authenticationEntryPoint, String header) {
        this.authenticationProvider = authenticationProvider;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.header = header;
    }


    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filterChain)
                                                                            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        try{

            String headerValue = request.getHeader(header);

            if(headerValue == null || headerValue.isEmpty()){
                throw new MissingServletRequestPartException(this.header);
            }

            Authentication authentication = this.authenticationProvider.authenticate(new TokenAuthentication(headerValue));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(req, resp);

        } catch (AuthenticationException authenticationException){
            this.authenticationEntryPoint.commence(request, response, authenticationException);
        }

    }
}
