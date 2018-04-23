package io.vscale.uniservice.services.interfaces.user;

import io.vscale.uniservice.domain.RoleType;
import io.vscale.uniservice.domain.User;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 01.03.2018
 *
 * @author Andrey Romanov
 * @version 1.0
 */
public interface UserService {

    List<User> getUsersByRole(RoleType roleType);
    User getUserByLogin(String login);
    User getUserById(Long id);
    List<User> getAllUsers();

    String savePhoto(MultipartFile file, Authentication authentication);
    void requestForAPIToken(Long id);
}
