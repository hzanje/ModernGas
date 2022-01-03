package com.moderngas.restcontroller;

import com.moderngas.exception.BadRequestException;
import com.moderngas.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/employee", produces = "application/json")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * Assigned Cylinder to Order.
     *
     * @param orderId
     * @param cylinderCodes
     * @return
     * @throws BadRequestException
     */
    @PutMapping("/assign/{orderId}")
    public ResponseEntity<?> assignCylinderToUser(@PathVariable("orderId") Long orderId,
                                                  @RequestBody List<String> cylinderCodes) throws BadRequestException {
        log.info("EmployeeController :: assignCylinderToUser >>> Start ");
        return new ResponseEntity<>(employeeService.assignCylinderToUser(orderId, cylinderCodes), HttpStatus.OK);
    }

    /**
     * Receive Cylinder From User.
     *
     * @param orderId
     * @param cylinderCodes
     * @return
     * @throws BadRequestException
     */
    @PutMapping("/receive/{orderId}")
    public ResponseEntity<?> receiveCylinderFromUser(@PathVariable("orderId") Long orderId,
                                                     @RequestBody List<String> cylinderCodes) throws BadRequestException {
        log.info("EmployeeController :: receiveCylinderFromUser >>> Start ");
        return new ResponseEntity<>(employeeService.receiveCylinderFromUser(orderId, cylinderCodes), HttpStatus.OK);
    }

    /**
     * Get All the Available Cylinder
     *
     * @return
     */
    @GetMapping("/availableCylinder")
    public ResponseEntity<?> getAvailableCylinder() {
        log.info("EmployeeController :: getAvailableCylinder >>> Start ");
        return new ResponseEntity<>(employeeService.getAvailableCylinder(), HttpStatus.OK);
    }

    /**
     * Get List of Assigned Cylinder Assigned To User
     *
     * @param userId
     * @return
     * @throws BadRequestException
     */
    @GetMapping("/getAssignedCylinder")
    public ResponseEntity<?> getAssignedCylinder(@RequestParam(value = "id") Long userId) throws BadRequestException {
        log.info("EmployeeController :: getAssignedCylinder >>> Start ");
        return new ResponseEntity<>(employeeService.getAssignedCylinderByUserId(userId), HttpStatus.OK);
    }
}
