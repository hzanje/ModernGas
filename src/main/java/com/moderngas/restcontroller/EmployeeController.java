package com.moderngas.restcontroller;

import com.moderngas.pojo.admin.CylinderCodeListDto;
import com.moderngas.pojo.admin.CylinderCodeStatusDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/employee", produces = "application/json")
public class EmployeeController {

    @PutMapping("/assign/{orderId}")
    public String assignCylinderToUser(@PathVariable("orderId") Long orderId,
                                       @RequestBody CylinderCodeListDto codeListDto) {
        return null;
    }

    @PutMapping("/receive/{orderId}")
    public String receiveCylinderFromUser(@PathVariable("orderId") Long orderId,
                                          @RequestBody CylinderCodeListDto codeListDto) {
        return null;
    }

    @GetMapping("/availableCylinder")
    public String getAvailableCylinder() {
        return null;
    }

    @PutMapping("/fill")
    public String fillCylinder() {
        return null;
    }
}
