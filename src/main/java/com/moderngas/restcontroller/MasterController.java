package com.moderngas.restcontroller;

import com.moderngas.service.MasterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping(value = "/master", produces = "application/json")
public class MasterController {

    @Autowired
    private MasterService masterService;

    /**
     * @return
     */
    @GetMapping("/order-status")
    public ResponseEntity<?> getOrderStatus() {
        log.info("MasterController :: getOrderStatus >>> Start");
        return new ResponseEntity<>(masterService.getOrderStatus(), HttpStatus.OK);
    }

    /**
     * @return
     */
    @GetMapping("/cylinder-status")
    public ResponseEntity<?> getCylinderStatus() {
        log.info("MasterController :: getCylinderStatus >>> Start");
        return new ResponseEntity<>(masterService.getCylinderStatus(), HttpStatus.OK);
    }

    /**
     * @return
     */
    @GetMapping("/cylinder-type")
    public ResponseEntity<?> getCylinderType() {
        log.info("MasterController :: getCylinderType >>> Start");
        return new ResponseEntity<>(masterService.getCylinderType(), HttpStatus.OK);
    }

    /**
     * @return
     */
    @GetMapping("/gas")
    public ResponseEntity<?> getGasList() {
        log.info("MasterController :: getGasList >>> Start");
        return new ResponseEntity<>(masterService.getGasList(), HttpStatus.OK);
    }

    /**
     * @return
     */
    @Secured("USER_ADMIN")
    @GetMapping("/privilege")
    public ResponseEntity<?> getPrivilegeList() {
        log.info("MasterController :: getPrivilegeList >>> Start");
        return new ResponseEntity<>(masterService.getPrivilegeList(), HttpStatus.OK);
    }
}
