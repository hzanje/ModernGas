package com.moderngas.restcontroller;

import com.moderngas.Security.UserDetailsServiceImpl;
import com.moderngas.jpaentity.UserEntity;
import com.moderngas.repository.UserRepo;
import com.moderngas.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
public class GenericController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @ResponseBody
    @PostMapping(value = "/changePassword")
    public String changePassword(@RequestParam("userName") final Long username,
                                  @RequestParam("oldPassword") final String oldPassword,
                                  @RequestParam("newPassword") final String newPassword, @RequestParam("role") final String role) {

        String result = "Failure";
        UserEntity userEntity = userService.getUserByLoginId(username);
        if (passwordEncoder.matches(oldPassword, userEntity.getPassword())) {
            userEntity.setPassword(passwordEncoder.encode(newPassword));
            /*userService.updateUser(userEntity);*/
            result =  "Success";
        }
        return result;
    }
}
