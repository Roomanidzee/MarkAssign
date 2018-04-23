package io.vscale.uniservice.services.implementations.user;

import io.vscale.uniservice.services.interfaces.storage.StorageService;
import lombok.AllArgsConstructor;

import io.vscale.uniservice.domain.RoleType;
import io.vscale.uniservice.domain.User;
import io.vscale.uniservice.repositories.data.UserRepository;
import io.vscale.uniservice.services.interfaces.user.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 01.03.2018
 *
 * @author Andrey Romanov
 * @version 1.0
 */
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private StorageService storageService;

    @Override
    public List<User> getUsersByRole(RoleType roleType) {

        List<User> currentUsers = this.userRepository.findAll();

        return currentUsers.stream()
                           .filter(user -> user.getRoles().equals(roleType))
                           .collect(Collectors.toList());

    }

    @Override
    public User getUserByLogin(String login) {

        Optional<User> existedUser = this.userRepository.findByLogin(login);

        if(!existedUser.isPresent()){
            throw new NullPointerException("No such user!");
        }

        return existedUser.get();

    }

    @Override
    public User getUserById(Long id) {
        return this.userRepository.findOne(id);
    }

    @Override
    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }

    @Override
    public String savePhoto(MultipartFile file, Authentication authentication) {
        return storageService.savePhoto(file, authentication);
    }

    @Override
    public void requestForAPIToken(Long id) {

        User user = this.userRepository.findOne(id);

        if(user.getToken() != null || !user.getToken().equals("") || !user.getToken().equals("WAITING")){
            throw new IllegalArgumentException("User already waiting for / has a token");
        }else{
            user.setToken("WAITING");
            this.userRepository.save(user);
        }

    }

}
