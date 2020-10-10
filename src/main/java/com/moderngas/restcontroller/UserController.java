package com.moderngas.restcontroller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moderngas.pojo.*;
import com.moderngas.pojo.ResponseStatus;
import com.moderngas.service.GenericService;
import com.moderngas.service.UserService;

import net.minidev.json.JSONObject;

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

    @GetMapping(value = "/getUserDashboard")
    public List<UserDashboardDto> getUserDashboard(@RequestParam("userId") Long userId) {
        return userService.getUserDashboard(userId);
    }

    @GetMapping(value = "/getListByCategoryId")
    public List<NameIdDto> getListByCategoryId(@RequestParam("id") Long id) {
        return userService.getListByCategoryId(id);
    }

    @GetMapping(value = "/getGasDetailsById")
    public GasDto getGasDetailsById(@RequestParam("id") Long id, @RequestParam("userId") Long userId) {
        return userService.getGasDetailsById(id, userId);
    }
    
    @PostMapping(value = "/updateUser")
    public ResponseEntity<ResponseStatus> updateUser(@RequestBody UserEntityDto userEntityDto) {
    	String response = userService.updateUser(genericService.convertDtoToUserData(userEntityDto));
    	return new ResponseEntity<>(new ResponseStatus(response), HttpStatus.OK);
    }

    @PostMapping(value = "/updateAddress")
    public ResponseEntity<ResponseStatus> updateAddress(@RequestBody AddressDto addressDtoStr,
                                                        @RequestParam("userId") final Long userId) throws JsonProcessingException {
//        AddressDto addressDto = new ObjectMapper().readValue(addressDtoStr, AddressDto.class);
        String response = userService.updateAddress(genericService.convertDtoToAddressEntity(addressDtoStr), userId);
        return new ResponseEntity<>(new ResponseStatus(response), HttpStatus.OK);
    }
    
    @GetMapping(value="/getAddress")
    public ResponseEntity<JSONObject> getAddress(@RequestParam("userId") final Long userId){
    	JSONObject obj=userService.getAddress(userId);
//<<<<<<< Updated upstream
//    	if(obj.getAsString("message").equals("User does not exists")) {
//    		return new ResponseEntity<>(obj, HttpStatus.NO_CONTENT);
//    	} else if(obj.getAsString("message").equals("Address does not exist")){
//    		return new ResponseEntity<>(obj, HttpStatus.NO_CONTENT);
//    	} else {
//=======
    	if(obj.containsKey("message")) {
    		return new ResponseEntity<>(obj, HttpStatus.BAD_REQUEST);
    	}else {
//>>>>>>> Stashed changes
    		return new ResponseEntity<>(obj, HttpStatus.OK);
    	}
//    	if(obj.containsKey("message")&&obj.getAsString("message").equals("User does not exists")) {
//    		return new ResponseEntity<>(obj, HttpStatus.BAD_REQUEST);
//    	}else if(obj.containsKey("message")&&obj.getAsString("message").equals("Address does not exist")){
//    		return new ResponseEntity<>(obj, HttpStatus.BAD_REQUEST);
//    	}else {
//    		return new ResponseEntity<>(obj, HttpStatus.OK);
//    	}
    }



}
