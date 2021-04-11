package com.moderngas.restcontroller;

import com.moderngas.Security.JwtProperties;
import com.moderngas.Security.UserDetailsServiceImpl;
import com.moderngas.constants.Constants;
import com.moderngas.exception.BadRequestException;
import com.moderngas.pojo.ResponseStatus;
import com.moderngas.pojo.user.UserEntityDto;
import com.moderngas.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

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
    public ResponseEntity<ResponseStatus> forgetPassword(@RequestParam("userName") Long userName) throws BadRequestException {
        String response = userService.forgetPassword(userName);
        return new ResponseEntity<>(new ResponseStatus(response), HttpStatus.OK);
    }

    @PutMapping(value = "/refreshToken")
    public ResponseEntity<ResponseStatus> refreshToken(@RequestHeader Map<String, String> requestHeader, HttpServletResponse response) throws BadRequestException {
        String refreshToken = userService.refreshToken(requestHeader.get("authorization"));
        response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + refreshToken);
        return new ResponseEntity<>(new ResponseStatus(Constants.SUCCESS_STR), HttpStatus.OK);
    }

    @GetMapping(value = "/logout")
    public ResponseEntity<ResponseStatus> logoutUser(@RequestHeader Map<String, String> requestHeader) throws BadRequestException {
        String response = userService.logout(requestHeader.get("authorization"));
        return new ResponseEntity<>(new ResponseStatus(response), HttpStatus.OK);
    }

}

