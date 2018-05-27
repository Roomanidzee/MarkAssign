package io.vscale.uniservice.controllers.general.auth;

import io.vscale.uniservice.domain.RoleType;
import io.vscale.uniservice.domain.User;
import io.vscale.uniservice.security.role.Role;
import io.vscale.uniservice.services.interfaces.auth.AuthenticationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * 01.03.2018
 *
 * @author Andrey Romanov
 * @version 1.0
 */
@Controller
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(@Qualifier("generalAuthenticationService") AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @GetMapping("/login")
    public String login(@ModelAttribute("model") ModelMap model, Authentication authentication,
                        @RequestParam Optional<String> error) {

        if (authentication != null) {
            return "redirect:/authorize";
        }

        model.addAttribute("error", error);

        return "index";

    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, Authentication authentication) {

        if (authentication != null) {
            request.getSession().invalidate();
        }
        return "redirect:/login";

    }

    @GetMapping("/authorize")
    public String root(Authentication authentication, ModelMap map){

        if(authentication != null){

            User user = this.authenticationService.getUserByAuthentication(authentication);

            if(user.getRoles().size() > 1){

                map.put("role_check", true);
                return "index";

            }else{

                RoleType roleType = user.getRoles().iterator().next();

                if(roleType.getRole().equals(Role.ADMIN)){
                    return "redirect:/admin/index";
                }else if(roleType.getRole().equals(Role.STUDENT)){
                    return "redirect:/student/index";
                }else if(roleType.getRole().equals(Role.COOPERATOR)){
                    return "redirect:/cooperator/index";
                }

            }

        }

        return "redirect:/login";

    }

    @GetMapping("/cooperator_auth")
    public String cooperatorAuth(Authentication authentication){

        if(authentication != null){
            return "redirect:/cooperator/index";
        }

        return "redirect:/login";

    }

    @GetMapping("/admin_auth")
    public String adminAuth(Authentication authentication){

        if(authentication != null){
            return "redirect:/admin/index";
        }

        return "redirect:/login";

    }

}
