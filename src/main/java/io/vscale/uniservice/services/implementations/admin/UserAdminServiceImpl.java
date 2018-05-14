package io.vscale.uniservice.services.implementations.admin;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.vscale.uniservice.domain.RoleType;
import io.vscale.uniservice.domain.User;
import io.vscale.uniservice.dto.TokenDTO;
import io.vscale.uniservice.forms.rest.NewUserForm;
import io.vscale.uniservice.repositories.data.UserRepository;
import io.vscale.uniservice.repositories.indexing.UserESRepository;

import io.vscale.uniservice.security.states.UserState;
import io.vscale.uniservice.services.interfaces.admin.UserAdminService;
import io.vscale.uniservice.services.interfaces.auth.RoleTypeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

/**
 * 01.03.2018
 *
 * @author Andrey Romanov
 * @version 1.0
 */
@Service
public class UserAdminServiceImpl implements UserAdminService {

    private final UserRepository userRepository;
    private final UserESRepository userESRepository;
    private final RoleTypeService roleTypeService;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Autowired
    public UserAdminServiceImpl(UserRepository userRepository, UserESRepository userESRepository,
                                RoleTypeService roleTypeService) {
        this.userRepository = userRepository;
        this.userESRepository = userESRepository;
        this.roleTypeService = roleTypeService;
    }

    @Override
    public void addNewUserREST(NewUserForm newUserForm) {

        Optional<RoleType> roleType = this.roleTypeService.getRoleTypeByRole(newUserForm.getRole());

        if (!roleType.isPresent()) {
            throw new IllegalArgumentException();
        }

        User user = User.builder()
                        .login(newUserForm.getLogin())
                        .password(newUserForm.getPassword())
                        .state(UserState.CONFIRMED)
                        .build();

        this.userRepository.save(user);
        this.userESRepository.save(user);

    }

    @Override
    public TokenDTO retrieveTokenToUser(Long userId) {

        User user = this.userRepository.findOne(userId);

        String token = Jwts.builder()
                           .claim("login", user.getLogin())
                           .setSubject(user.getId().toString())
                           .signWith(SignatureAlgorithm.RS512, this.jwtSecret)
                           .compact();


        user.setToken(token);
        this.userRepository.save(user);
        this.userESRepository.save(user);

        return TokenDTO.builder()
                       .token(token)
                       .expireTime(LocalDateTime.now(ZoneId.of("Europe/Moscow")))
                       .build();

    }
}
