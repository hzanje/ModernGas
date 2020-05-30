package com.moderngas.restcontroller;

import com.moderngas.Security.UserDetailsServiceImpl;
import com.moderngas.pojo.UserEntityDto;
import com.moderngas.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/moderngas")
public class GenericController {

    @Autowired
    private UserService userService;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @ResponseBody
    @GetMapping(value = "/checkUserExist")
    public String checkUserExist(@RequestParam("mobileNumber") Long mobileNumber) {
        return userService.checkUserExist(mobileNumber);
    }

    @ResponseBody
    @PostMapping(value = "/addClient")
    public String addClient(@RequestBody UserEntityDto userEntityDto) {
        return userService.addUser(userEntityDto);
    }
}
