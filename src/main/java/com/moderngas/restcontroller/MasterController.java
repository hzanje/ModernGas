package com.moderngas.restcontroller;

import com.moderngas.pojo.NameIdDto;
import com.moderngas.service.MasterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping(value = "/master", produces = "application/json")
public class MasterController {

    @Autowired
    private MasterService masterService;

    @GetMapping("/order-status")
    public List<NameIdDto> getOrderStatus() {
        log.info("MasterController :: getOrderStatus >>> Start");
        return masterService.getOrderStatus();
    }

    @GetMapping("/cylinder-status")
    public List<String> getCylinderStatus() {
        log.info("MasterController :: getCylinderStatus >>> Start");
        return masterService.getCylinderStatus();
    }

    @GetMapping("/cylinder-type")
    public List<String> getCylinderType() {
        log.info("MasterController :: getCylinderType >>> Start");
        return masterService.getCylinderType();
    }

    @GetMapping("/gas")
    public List<NameIdDto> getGasList() {
        log.info("MasterController :: getGasList >>> Start");
        return masterService.getGasList();
    }

    @Secured("USER_ADMIN")
    @GetMapping("/privilege")
    public List<String> getPrivilegeList() {
        log.info("MasterController :: getPrivilegeList >>> Start");
        return masterService.getPrivilegeList();
    }
}
