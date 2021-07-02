package com.moderngas.restcontroller;

import com.moderngas.exception.BadRequestException;
import com.moderngas.pojo.ResponseStatus;
import com.moderngas.pojo.superadmin.AdminEntityDto;
import com.moderngas.service.SuperAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/super", produces = "application/json")
public class SuperAdminController {

    @Autowired
    private SuperAdminService superAdminService;

    @PostMapping("/createAdmin")
    public ResponseEntity<ResponseStatus> createAdmin(@RequestBody AdminEntityDto adminEntityDto) throws BadRequestException {
        String response = superAdminService.createAdmin(adminEntityDto);
        return new ResponseEntity<>(new ResponseStatus(response), HttpStatus.OK);
    }
}
