package com.moderngas.restcontroller;

import com.moderngas.exception.BadRequestException;
import com.moderngas.pojo.*;
import com.moderngas.pojo.ResponseStatus;
import com.moderngas.pojo.user.AddressDto;
import com.moderngas.pojo.user.GasDto;
import com.moderngas.pojo.user.UserDashboardDto;
import com.moderngas.pojo.user.UserEntityDto;
import com.moderngas.service.GenericService;
import com.moderngas.service.UserService;

import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping(value = "/client", produces = "application/json")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private GenericService genericService;

    /**
     * Get All the Client/User with respect to its Admin.
     *
     * @param adminId
     * @return
     */
    @GetMapping(value = "/getAllClient")
    public List<UserEntityDto> getAllClient(@RequestParam("adminId") Long adminId) throws BadRequestException {
        log.info("UserController :: getAllClient >>> Start");
        return userService.getAllUserByAdmin(adminId);
    }

    /**
     * Get Client/User by Id.
     *
     * @param userId
     * @return
     * @throws BadRequestException
     */
    @GetMapping(value = "/getClientById")
    public UserEntityDto getClientById(@RequestParam("userId") Long userId) throws BadRequestException {
        log.info("UserController :: getClientById >>> Start");
        return userService.getUserById(userId);
    }

    /**
     * Get User By UserName(PhoneNumber)
     *
     * @param userName
     * @return
     */
    @GetMapping(value = "/getUser")
    public UserEntityDto getUser(@RequestParam("userName") Long userName) throws BadRequestException {
        log.info("UserController :: getUser >>> Start");
        return genericService.convertUserDataToDto(userService.getUserByLoginId(userName));
    }

    /**
     * Changes Password for the User in Application
     *
     * @param username
     * @param oldPassword
     * @param newPassword
     * @return
     * @throws BadRequestException
     */
    @PostMapping(value = "/changePassword")
    public ResponseEntity<ResponseStatus> changePassword(@RequestParam("userName") final Long username,
                                                         @RequestParam("oldPassword") final String oldPassword,
                                                         @RequestParam("newPassword") final String newPassword) throws BadRequestException {
        log.info("UserController :: changePassword >>> Start");
        String response = userService.changePassword(username, oldPassword, newPassword);
        return new ResponseEntity<>(new ResponseStatus(response), HttpStatus.OK);
    }

    /**
     * Get UserDashboard By its respected Admin.
     *
     * @param userId
     * @return
     */
    @GetMapping(value = "/getUserDashboard")
    public List<UserDashboardDto> getUserDashboard(@RequestParam("id") Long userId,
                                                   @RequestParam("adminId") Long adminId) throws BadRequestException {
        log.info("UserController :: getUserDashboard >>> Start");
        return userService.getUserDashboard(userId, adminId);
    }

    /**
     * Get List of Gas Master By Its Category Id.
     *
     * @param categoryId
     * @return
     */
    @GetMapping(value = "/getGasListByCategoryId")
    public List<NameIdDto> getGasListByCategoryId(@RequestParam("id") Long categoryId) {
        log.info("UserController :: getGasListByCategoryId >>> Start");
        return userService.getGasListByCategoryId(categoryId);
    }

    /**
     * Get Gas Details By Id For Specific Admin.
     *
     * @param id
     * @param adminId
     * @return
     * @throws BadRequestException
     */
    @GetMapping(value = "/getGasDetailsById")
    public GasDto getGasDetailsById(@RequestParam("id") Long id, @RequestParam("adminId") Long adminId) throws BadRequestException {
        log.info("UserController :: getGasDetailsById >>> Start");
        return userService.getGasDetailsById(id, adminId);
    }

    /**
     * Update User Details.
     *
     * @param userEntityDto
     * @return
     */
    @PostMapping(value = "/updateUser")
    public ResponseEntity<ResponseStatus> updateUser(@RequestBody UserEntityDto userEntityDto) throws BadRequestException {
        log.info("UserController :: updateUser >>> Start");
        String response = userService.updateUser(genericService.convertDtoToUserData(userEntityDto));
    	return new ResponseEntity<>(new ResponseStatus(response), HttpStatus.OK);
    }

    /**
     * Update User Address
     *
     * @param addressDtoStr
     * @param userId
     * @return
     * @throws BadRequestException
     */
    @PostMapping(value = "/updateAddress")
    public ResponseEntity<ResponseStatus> updateAddress(@RequestBody AddressDto addressDtoStr,
                                                        @RequestParam("userId") final Long userId) throws BadRequestException {
        log.info("UserController :: updateAddress >>> Start");
        String response = userService.updateAddress(genericService.convertDtoToAddressEntity(addressDtoStr), userId);
        return new ResponseEntity<>(new ResponseStatus(response), HttpStatus.OK);
    }

    /**
     * Get User Address by User Id.
     *
     * @param userId
     * @return
     * @throws BadRequestException
     */
    @GetMapping(value="/getAddress")
    public ResponseEntity<JSONObject> getAddress(@RequestParam("userId") final Long userId) throws BadRequestException {
        log.info("UserController :: getAddress >>> Start");
        JSONObject obj=userService.getAddress(userId);
    	if(obj.containsKey("message")) {
    		return new ResponseEntity<>(obj, HttpStatus.BAD_REQUEST);
    	} else {
    		return new ResponseEntity<>(obj, HttpStatus.OK);
    	}
    }

}
