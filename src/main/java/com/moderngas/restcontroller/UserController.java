package com.moderngas.restcontroller;

import com.moderngas.pojo.ResponseStatus;
import com.moderngas.pojo.UserEntityDto;
import com.moderngas.service.GenericService;
import com.moderngas.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "/client", produces = "application/json")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    GenericService genericService;

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

    @PostMapping(value = "/changePassword")
    public ResponseEntity<ResponseStatus> changePassword(@RequestParam("userName") final Long username,
                                                         @RequestParam("oldPassword") final String oldPassword,
                                                         @RequestParam("newPassword") final String newPassword) {
        String response = userService.changePassword(username, oldPassword, newPassword);
        return new ResponseEntity<>(new ResponseStatus(response), HttpStatus.OK);
    }

}
