package com.moderngas.restcontroller;

import com.moderngas.exception.BadRequestException;
import com.moderngas.pojo.admin.CylinderCodeListDto;
import com.moderngas.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/employee", produces = "application/json")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PutMapping("/assign/{orderId}")
    public String assignCylinderToUser(@PathVariable("orderId") Long orderId,
                                       @RequestBody CylinderCodeListDto codeListDto) throws BadRequestException {
        log.info("EmployeeController :: assignCylinderToUser >>> Start ");
        return employeeService.assignCylinderToUser(orderId, codeListDto);
    }

    @PutMapping("/receive/{orderId}")
    public String receiveCylinderFromUser(@PathVariable("orderId") Long orderId,
                                          @RequestBody CylinderCodeListDto codeListDto) throws BadRequestException {
        log.info("EmployeeController :: receiveCylinderFromUser >>> Start ");
        return employeeService.receiveCylinderFromUser(orderId, codeListDto);
    }

    @GetMapping("/availableCylinder")
    public List<String> getAvailableCylinder() {
        log.info("EmployeeController :: getAvailableCylinder >>> Start ");
        return employeeService.getAvailableCylinder();
    }

    @GetMapping("/getAssignedCylinder")
    public List<String> getAssignedCylinder(@RequestParam(value = "id") Long userId) throws BadRequestException {
        log.info("EmployeeController :: getAssignedCylinder >>> Start ");
        return employeeService.getAssignedCylinderByUserId(userId);
    }

    @PutMapping("/fill/{cylinderCode}")
    public String fillCylinder(@PathVariable("cylinderCode") String cylinderCode) throws BadRequestException {
        log.info("EmployeeController :: fillCylinder >>> Start ");
        return employeeService.fillCylinder(cylinderCode);
    }
}
