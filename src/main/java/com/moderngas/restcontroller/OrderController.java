package com.moderngas.restcontroller;

import com.moderngas.pojo.CartDto;
import com.moderngas.pojo.NameIdDto;
import com.moderngas.pojo.OrderDto;
import com.moderngas.pojo.ResponseStatus;
import com.moderngas.service.OrderService;
import com.moderngas.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/order", produces = "application/json")
public class OrderController {

    @Autowired
    UserService userService;

    @Autowired
    OrderService orderService;

    private static final Logger LOG = LoggerFactory.getLogger(OrderController.class);

    @PostMapping("/placeOrder")
    public ResponseEntity<ResponseStatus> placeOrder(@RequestBody OrderDto orderDto) {
        String response = orderService.placeOrder(orderDto);
        return new ResponseEntity<>(new ResponseStatus(response), HttpStatus.OK);
    }

    @GetMapping("/deleteOrder")
    public ResponseEntity<ResponseStatus> deleteOrder(@RequestParam("id") Long orderId) {
        String response = orderService.deleteOrder(orderId);
        return new ResponseEntity<>(new ResponseStatus(response), HttpStatus.OK);
    }

    @GetMapping("/getOrderListByUser")
    public List<OrderDto> getOrderListByUser(@RequestParam("userId") Long userId) {
        return orderService.getOrderListByUser(userId);
    }

    @PostMapping("/addCart")
    public ResponseEntity<ResponseStatus> addCart(@RequestBody CartDto cartDto) {
        String response = orderService.addCart(cartDto);
        return new ResponseEntity<>(new ResponseStatus(response), HttpStatus.OK);
    }

    @GetMapping("/deleteCart")
    public ResponseEntity<ResponseStatus> deleteCart(@RequestParam("id") Long cartId) {
        String response = orderService.deleteCart(cartId);
        return new ResponseEntity<>(new ResponseStatus(response), HttpStatus.OK);
    }

    @GetMapping("/getCartListByUser")
    public List<CartDto> getCartListByUser(@RequestParam("userId") Long userId) {
        return orderService.getCartByUser(userId);
    }

    @GetMapping("/placeOrderFromCart")
    public ResponseEntity<ResponseStatus> placeOrderFromCart(@RequestParam("userId") Long userId) {
        String response = orderService.placeOrderFromCart(userId);
        return new ResponseEntity<>(new ResponseStatus(response), HttpStatus.OK);
    }

    @GetMapping("/getOrderDetailsById")
    public OrderDto getOrderDetailsById(@RequestParam("orderId") Long orderId) {
        return orderService.getOrderDetailsById(orderId);
    }

    @GetMapping("/updateOrderStatus")
    public ResponseEntity<ResponseStatus> updateOrderStatus(@RequestParam("orderId") Long orderId,
                                                            @RequestParam("statusId") Long statusId ) {
        String response = orderService.updateOrderStatus(orderId, statusId);
        return new ResponseEntity<>(new ResponseStatus(response), HttpStatus.OK);
    }

    @GetMapping("/getOrderStatusList")
    public List<NameIdDto> getOrderStatusList() {
        return orderService.getOrderStatusList();
    }
}
