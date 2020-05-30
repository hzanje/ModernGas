package com.moderngas.restcontroller;

import com.moderngas.pojo.UserEntityDto;
import com.moderngas.service.GenericService;
import com.moderngas.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/client")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    GenericService genericService;


    @PostMapping(value = "/addClient")
    public String addClient(@RequestBody UserEntityDto userEntityDto) {
        return userService.addUser(userEntityDto);
    }

    @GetMapping(value = "/getAllClient")
    public List<UserEntityDto> getAllClient() {
        return userService.getAllUser();
    }

    @GetMapping(value = "/getClientById")
    public UserEntityDto getClientById(@RequestParam("userId") Long userId) {
        return userService.getUserById(userId);
    }

    @GetMapping(value = "/getUser")
    public UserEntityDto getUser(@RequestParam("userName") Long userName) {
        return genericService.convertUserDataToDto(userService.getUserByLoginId(userName));
    }

    @GetMapping(value = "/checkUserExist")
    public String checkUserExist(@RequestParam("mobileNumber") Long mobileNumber) {
        return userService.checkUserExist(mobileNumber);
    }


}
