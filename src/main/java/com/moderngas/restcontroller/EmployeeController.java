package com.moderngas.restcontroller;

import com.moderngas.exception.BadRequestException;
import com.moderngas.pojo.admin.CylinderCodeListDto;
import com.moderngas.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping(value = "/employee", produces = "application/json")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * Assigned Cylinder to Order.
     *
     * @param orderId
     * @param codeListDto
     * @return
     * @throws BadRequestException
     */
    @PutMapping("/assign/{orderId}")
    public String assignCylinderToUser(@PathVariable("orderId") Long orderId,
                                       @RequestBody CylinderCodeListDto codeListDto) throws BadRequestException {
        log.info("EmployeeController :: assignCylinderToUser >>> Start ");
        return employeeService.assignCylinderToUser(orderId, codeListDto);
    }

    /**
     * Receive Cylinder From User.
     *
     * @param orderId
     * @param codeListDto
     * @return
     * @throws BadRequestException
     */
    @PutMapping("/receive/{orderId}")
    public String receiveCylinderFromUser(@PathVariable("orderId") Long orderId,
                                          @RequestBody CylinderCodeListDto codeListDto) throws BadRequestException {
        log.info("EmployeeController :: receiveCylinderFromUser >>> Start ");
        return employeeService.receiveCylinderFromUser(orderId, codeListDto);
    }

    /**
     *
     * Get All the Available Cylinder
     * @return
     */
    @GetMapping("/availableCylinder")
    public List<String> getAvailableCylinder() {
        log.info("EmployeeController :: getAvailableCylinder >>> Start ");
        return employeeService.getAvailableCylinder();
    }

    /**
     * Get List of Assigned Cylinder Assigned To User
     *
     * @param userId
     * @return
     * @throws BadRequestException
     */
    @GetMapping("/getAssignedCylinder")
    public List<String> getAssignedCylinder(@RequestParam(value = "id") Long userId) throws BadRequestException {
        log.info("EmployeeController :: getAssignedCylinder >>> Start ");
        return employeeService.getAssignedCylinderByUserId(userId);
    }

    /**
     * Mark Cylinder As Filled State.
     *
     * @param cylinderCode
     * @return
     * @throws BadRequestException
     */
    @PutMapping("/fill/{cylinderCode}")
    public String fillCylinder(@PathVariable("cylinderCode") String cylinderCode) throws BadRequestException {
        log.info("EmployeeController :: fillCylinder >>> Start ");
        return employeeService.fillCylinder(cylinderCode);
    }
}
