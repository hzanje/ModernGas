package com.moderngas.restcontroller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/order", produces = "application/json")
public class OrderController {

    @RequestMapping("/placeOrder")
    public String placeOrder(@RequestParam("userId") Long userId) {
        return "";
    }

    @RequestMapping("/addCart")
    public String addCart(@RequestParam("userId") Long userId) {
        return "";
    }


}
