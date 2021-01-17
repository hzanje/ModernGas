package com.moderngas.restcontroller;

import com.moderngas.pojo.admin.CylinderCodeListDto;
import com.moderngas.pojo.admin.CylinderCodeStatusDto;
import com.moderngas.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/employee", produces = "application/json")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PutMapping("/assign/{orderId}")
    public String assignCylinderToUser(@PathVariable("orderId") Long orderId,
                                       @RequestBody CylinderCodeListDto codeListDto) {
        return employeeService.assignCylinderToUser(orderId, codeListDto);
    }

    @PutMapping("/receive/{orderId}")
    public String receiveCylinderFromUser(@PathVariable("orderId") Long orderId,
                                          @RequestBody CylinderCodeListDto codeListDto) {
        return employeeService.receiveCylinderFromUser(orderId, codeListDto);
    }

    @GetMapping("/availableCylinder")
    public List<String> getAvailableCylinder() {
        return employeeService.getAvailableCylinder();
    }

    @PutMapping("/fill")
    public String fillCylinder() {
        return null;
    }
}
