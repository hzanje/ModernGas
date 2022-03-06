package com.moderngas.restcontroller;

import com.moderngas.exception.BadRequestException;
import com.moderngas.pojo.ResponseStatus;
import com.moderngas.pojo.superadmin.AdminEntityDto;
import com.moderngas.service.SuperAdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/super", produces = "application/json")
public class SuperAdminController {

    private static Logger log = LoggerFactory.getLogger(SuperAdminController.class.getName());

    @Autowired
    private SuperAdminService superAdminService;

    @Secured("ROLE_SUPER_ADMIN")
    @PostMapping("/createAdmin/{superId}")
    public ResponseEntity<ResponseStatus> createAdmin(@PathVariable("superId") Long superId,
                                                      @RequestBody AdminEntityDto adminEntityDto) throws BadRequestException {
        log.info("SuperAdminController :: createAdmin >>> SuperAdminId : {}", superId);
        String response = superAdminService.createAdmin(superId, adminEntityDto);
        return new ResponseEntity<>(new ResponseStatus(response), HttpStatus.OK);
    }
}
