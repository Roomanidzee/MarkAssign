package io.vscale.uniservice.security.providers.rest;

import lombok.SneakyThrows;
import io.vscale.uniservice.domain.User;
import io.vscale.uniservice.repositories.data.UserRepository;
import io.vscale.uniservice.security.rest.filters.TokenAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 25.03.2018
 *
 * @author Andrey Romanov
 * @version 1.0
 */
@Component("restUserAuthentication")
public class UserAuthenticationProvider implements AuthenticationProvider{

    private final UserRepository userRepository;
    private final UserDetailsService userDetailsService;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Autowired
    public UserAuthenticationProvider(UserRepository userRepository,
                                      @Qualifier("restUserDetailsService") UserDetailsService userDetailsService) {
        this.userRepository = userRepository;
        this.userDetailsService = userDetailsService;
    }

    @Override
    @SneakyThrows
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        TokenAuthentication tokenAuthentication = (TokenAuthentication) authentication;
        String token = (String) tokenAuthentication.getCredentials();

        Optional<User> existedUser = this.userRepository.findByToken(token);
        if(!existedUser.isPresent()){
            throw new BadCredentialsException("Wrong password or login");
        }

        UserDetails details = this.userDetailsService.loadUserByUsername(token);
        Collection<? extends GrantedAuthority> authorities = details.getAuthorities();
        TokenAuthentication newTokenAuthentication = new TokenAuthentication(tokenAuthentication.getToken(), authorities);
        newTokenAuthentication.setDetails(details);
        newTokenAuthentication.setAuthenticated(true);
        return newTokenAuthentication;

    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.getName().equals(TokenAuthentication.class.getName());
    }
}
