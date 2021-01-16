package com.moderngas.restcontroller;

import com.moderngas.Security.UserDetailsServiceImpl;
import com.moderngas.pojo.ResponseStatus;
import com.moderngas.pojo.user.UserEntityDto;
import com.moderngas.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/moderngas", produces = "application/json")
public class GenericController {

    @Autowired
    private UserService userService;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @GetMapping(value = "/checkUserExist")
    public ResponseEntity<ResponseStatus> checkUserExist(@RequestParam("mobileNumber") Long mobileNumber) {
        String response = userService.checkUserExist(mobileNumber);
        return new ResponseEntity<>(new ResponseStatus(response), HttpStatus.OK);
    }

    @PostMapping(value = "/addUser")
    public ResponseEntity<ResponseStatus> addUser(@RequestBody UserEntityDto userEntityDto) {
        String response = userService.addUser(userEntityDto);
        return new ResponseEntity<>(new ResponseStatus(response), HttpStatus.OK);
    }

    @GetMapping(value = "/forgetPassword")
    public ResponseEntity<ResponseStatus> forgetPassword(@RequestParam("userName") Long userName) {
        String response = userService.forgetPassword(userName);
        return new ResponseEntity<>(new ResponseStatus(response), HttpStatus.OK);
    }

    @PostMapping(value = "/refreshToken")
    public ResponseEntity<ResponseStatus> refreshToken(@RequestParam("existingToken") String existingToken) {
        String response = userService.refreshToken(existingToken);
        return new ResponseEntity<>(new ResponseStatus(response), HttpStatus.OK);
    }

}
