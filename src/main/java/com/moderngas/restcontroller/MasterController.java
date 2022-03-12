package com.moderngas.restcontroller;

import com.moderngas.service.MasterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/master", produces = "application/json")
public class MasterController {

    private static Logger log = LoggerFactory.getLogger(MasterController.class.getName());

    @Autowired
    private MasterService masterService;

    /**
     * @return Order Status
     */
    @GetMapping("/order-status")
    public ResponseEntity<?> getOrderStatus() {
        log.info("MasterController :: getOrderStatus >>>");
        return new ResponseEntity<>(masterService.getOrderStatus(), HttpStatus.OK);
    }

    /**
     * @return Cylinder Status
     */
    @GetMapping("/cylinder-status")
    public ResponseEntity<?> getCylinderStatus() {
        log.info("MasterController :: getCylinderStatus >>>");
        return new ResponseEntity<>(masterService.getCylinderStatus(), HttpStatus.OK);
    }

    /**
     * @return Cylinder Type
     */
    @GetMapping("/cylinder-type")
    public ResponseEntity<?> getCylinderType() {
        log.info("MasterController :: getCylinderType >>>");
        return new ResponseEntity<>(masterService.getCylinderType(), HttpStatus.OK);
    }

    /**
     * @return Gas List
     */
    @GetMapping("/gas")
    public ResponseEntity<?> getGasList() {
        log.info("MasterController :: getGasList >>>");
        return new ResponseEntity<>(masterService.getGasList(), HttpStatus.OK);
    }

    /**
     * @return Employee Privilege
     */
    @Secured("ROLE_ADMIN")
    @GetMapping("/privilege")
    public ResponseEntity<?> getPrivilegeList() {
        log.info("MasterController :: getPrivilegeList >>>");
        return new ResponseEntity<>(masterService.getPrivilegeList(), HttpStatus.OK);
    }
}
