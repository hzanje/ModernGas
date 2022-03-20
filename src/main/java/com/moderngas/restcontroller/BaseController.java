package com.moderngas.restcontroller;

import com.moderngas.constants.Constants;
import com.moderngas.exception.BadRequestException;
import com.moderngas.pojo.ResponseStatus;
import com.moderngas.security.JwtProperties;
import com.moderngas.security.UserDetailsServiceImpl;
import com.moderngas.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequestMapping(value = "/base", produces = "application/json")
public class BaseController {

    private static Logger log = LoggerFactory.getLogger(BaseController.class.getName());

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    private UserService userService;

    /**
     * Test Connection
     *
     * @return String
     */
    @GetMapping("/test")
    public ResponseEntity<ResponseStatus> testConnection() {
        log.info("BaseController :: testConnection >>>");
        return new ResponseEntity<>(new ResponseStatus("Connected To Reek Application"), HttpStatus.OK);
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
        log.info("BaseController :: forgetPassword >>> {} ", userName);
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
        log.info("BaseController :: refreshToken >>> ");
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
        log.info("BaseController :: logout >>> ");
        String response = userService.logout(requestHeader.get("authorization"));
        return new ResponseEntity<>(new ResponseStatus(response), HttpStatus.OK);
    }

}

