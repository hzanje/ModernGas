package com.moderngas.restcontroller;

import com.moderngas.pojo.NameIdDto;
import com.moderngas.service.MasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/master", produces = "application/json")
public class MasterController {

    @Autowired
    private MasterService masterService;

    @GetMapping("/order-status")
    public List<NameIdDto> getOrderStatus() {
        return masterService.getOrderStatus();
    }

    @GetMapping("/cylinder-status")
    public List<String> getCylinderStatus() {
        return masterService.getCylinderStatus();
    }

    @GetMapping("/cylinder-type")
    public List<String> getCylinderType() {
        return masterService.getCylinderType();
    }
}
