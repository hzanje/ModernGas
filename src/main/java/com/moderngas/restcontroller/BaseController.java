package com.moderngas.restcontroller;

import com.moderngas.constants.Constants;
import com.moderngas.exception.BadRequestException;
import com.moderngas.pojo.ResponseStatus;
import com.moderngas.security.JwtProperties;
import com.moderngas.security.UserDetailsServiceImpl;
import com.moderngas.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/base", produces = "application/json")
public class BaseController {

    @Autowired
    UserDetailsServiceImpl userDetailsService;
    @Autowired
    private UserService userService;

    /**
     * Check if User Exist in System.
     *
     * @param mobileNumber
     * @return
     */
    @GetMapping(value = "/checkUserExist/{mobileNumber}")
    public ResponseEntity<ResponseStatus> checkUserExist(@PathVariable("mobileNumber") Long mobileNumber) {
        log.info("BaseController :: checkUserExist >>> Start ");
        String response = userService.checkUserExist(mobileNumber);
        return new ResponseEntity<>(new ResponseStatus(response), HttpStatus.OK);
    }

    /**
     * API for Forget Password.
     *
     * @param userName
     * @return
     * @throws BadRequestException
     */
    @GetMapping(value = "/forgetPassword")
    public ResponseEntity<ResponseStatus> forgetPassword(@RequestParam("userName") Long userName) throws BadRequestException {
        log.info("BaseController :: forgetPassword >>> Start ");
        String response = userService.forgetPassword(userName);
        return new ResponseEntity<>(new ResponseStatus(response), HttpStatus.OK);
    }

    /**
     * Refresh Token For The User
     *
     * @param requestHeader
     * @param response
     * @return
     * @throws BadRequestException
     */
    @PutMapping(value = "/refreshToken")
    public ResponseEntity<ResponseStatus> refreshToken(@RequestHeader Map<String, String> requestHeader, HttpServletResponse response) throws BadRequestException {
        log.info("BaseController :: refreshToken >>> Start ");
        String refreshToken = userService.refreshToken(requestHeader.get("authorization"));
        response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + refreshToken);
        return new ResponseEntity<>(new ResponseStatus(Constants.SUCCESS_STR), HttpStatus.OK);
    }

    /**
     * Logout User From The System
     *
     * @param requestHeader
     * @return
     * @throws BadRequestException
     */
    @GetMapping(value = "/logout")
    public ResponseEntity<ResponseStatus> logoutUser(@RequestHeader Map<String, String> requestHeader) throws BadRequestException {
        log.info("BaseController :: logout >>> Start ");
        String response = userService.logout(requestHeader.get("authorization"));
        return new ResponseEntity<>(new ResponseStatus(response), HttpStatus.OK);
    }

}

