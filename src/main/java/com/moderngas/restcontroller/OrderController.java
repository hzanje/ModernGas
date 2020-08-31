package com.moderngas.restcontroller;

import com.moderngas.pojo.OrderDto;
import com.moderngas.pojo.ResponseStatus;
import com.moderngas.service.OrderService;
import com.moderngas.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/order", produces = "application/json")
public class OrderController {

    @Autowired
    UserService userService;

    @Autowired
    OrderService orderService;


    @PostMapping("/placeOrder")
    public ResponseEntity<ResponseStatus> placeOrder(@RequestBody OrderDto orderDto) {
        String response = orderService.placeUserOrder(orderDto);
        return new ResponseEntity<>(new ResponseStatus(response), HttpStatus.OK);
    }

    @PostMapping("/addCart")
    public ResponseEntity<ResponseStatus> addCart(@RequestBody OrderDto orderDto) {
        String response = orderService.addCart(orderDto);
        return new ResponseEntity<>(new ResponseStatus(response), HttpStatus.OK);
    }


}
