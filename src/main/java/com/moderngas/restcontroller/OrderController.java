package com.moderngas.restcontroller;

import com.moderngas.exception.BadRequestException;
import com.moderngas.pojo.user.CartDto;
import com.moderngas.pojo.user.OrderDto;
import com.moderngas.pojo.ResponseStatus;
import com.moderngas.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/order", produces = "application/json")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/placeOrder")
    public ResponseEntity<ResponseStatus> placeOrder(@RequestBody OrderDto orderDto) {
        String response = orderService.placeOrder(orderDto);
        return new ResponseEntity<>(new ResponseStatus(response), HttpStatus.OK);
    }

    @GetMapping("/deleteOrder")
    public ResponseEntity<ResponseStatus> deleteOrder(@RequestParam("id") Long orderId) throws BadRequestException {
        String response = orderService.deleteOrder(orderId);
        return new ResponseEntity<>(new ResponseStatus(response), HttpStatus.OK);
    }

    @GetMapping("/getOrderListByUser")
    public List<OrderDto> getOrderListByUser(@RequestParam("userId") Long userId) throws BadRequestException {
        return orderService.getOrderListByUser(userId);
    }

    @PostMapping("/addCart")
    public ResponseEntity<ResponseStatus> addCart(@RequestBody CartDto cartDto) {
        String response = orderService.addCart(cartDto);
        return new ResponseEntity<>(new ResponseStatus(response), HttpStatus.OK);
    }

    @GetMapping("/deleteCart")
    public ResponseEntity<ResponseStatus> deleteCart(@RequestParam("id") Long cartId) throws BadRequestException {
        String response = orderService.deleteCart(cartId);
        return new ResponseEntity<>(new ResponseStatus(response), HttpStatus.OK);
    }

    @GetMapping("/getCartListByUser")
    public List<CartDto> getCartListByUser(@RequestParam("userId") Long userId) throws BadRequestException {
        return orderService.getCartByUser(userId);
    }

    @GetMapping("/placeOrderFromCart")
    public ResponseEntity<ResponseStatus> placeOrderFromCart(@RequestParam("userId") Long userId) throws BadRequestException {
        String response = orderService.placeOrderFromCart(userId);
        return new ResponseEntity<>(new ResponseStatus(response), HttpStatus.OK);
    }

    @GetMapping("/getOrderDetailsById")
    public OrderDto getOrderDetailsById(@RequestParam("orderId") Long orderId) throws BadRequestException {
        return orderService.getOrderDetailsById(orderId);
    }

    @PutMapping("/updateOrderStatus")
    public ResponseEntity<ResponseStatus> updateOrderStatus(@RequestParam(value = "orderId") Long orderId,
                                                            @RequestParam(value = "status") String orderStatus,
                                                            @RequestParam(value = "vehicleId", required = false) Long vehicleId) throws BadRequestException {
        String response = orderService.updateOrderStatus(orderId, orderStatus, vehicleId);
        return new ResponseEntity<>(new ResponseStatus(response), HttpStatus.OK);
    }

}
