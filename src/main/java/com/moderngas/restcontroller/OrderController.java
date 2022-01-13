package com.moderngas.restcontroller;

import com.moderngas.exception.BadRequestException;
import com.moderngas.pojo.ResponseStatus;
import com.moderngas.pojo.user.CartDto;
import com.moderngas.pojo.user.OrderDto;
import com.moderngas.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping(value = "/order", produces = "application/json")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * Place User Order For Specific Admin
     *
     * @param orderDto
     * @return ResponseStatus
     */
    @PostMapping("/order")
    public ResponseEntity<ResponseStatus> placeOrder(@RequestBody OrderDto orderDto) throws BadRequestException {
        log.info("OrderController :: placeOrder >>> Start");
        String response = orderService.placeOrder(orderDto);
        return new ResponseEntity<>(new ResponseStatus(response), HttpStatus.OK);
    }

    /**
     * Delete User Order Record
     *
     * @param orderId
     * @return ResponseStatus
     * @throws BadRequestException
     */
    @DeleteMapping("/order/{id}")
    public ResponseEntity<ResponseStatus> deleteOrder(@PathVariable("id") Long orderId) throws BadRequestException {
        log.info("OrderController :: deleteOrder >>> Start");
        String response = orderService.deleteOrder(orderId);
        return new ResponseEntity<>(new ResponseStatus(response), HttpStatus.OK);
    }

    /**
     * Get User Order List For Specific Admin
     *
     * @param userId
     * @param adminId
     * @return Order List
     */
    @GetMapping("/order")
    public ResponseEntity<?> getOrderListByUser(@RequestParam("id") Long userId, @RequestParam("adminId") Long adminId) throws BadRequestException {
        log.info("OrderController :: getOrderListByUser >>> Start");
        return new ResponseEntity<>(orderService.getOrderListByUser(userId, adminId), HttpStatus.OK);
    }

    /**
     * Add Or Update User Cart Record For Specific Admin
     *
     * @param cartDto
     * @return ResponseStatus
     * @throws BadRequestException
     */
    @PostMapping("/cart")
    public ResponseEntity<ResponseStatus> addOrUpdateCart(@RequestBody CartDto cartDto) throws BadRequestException {
        log.info("OrderController :: addOrUpdateCart >>> Start");
        String response = orderService.addOrUpdateCart(cartDto);
        return new ResponseEntity<>(new ResponseStatus(response), HttpStatus.OK);
    }

    /**
     * Delete Cart Record
     *
     * @param cartId
     * @return ResponseStatus
     * @throws BadRequestException
     */
    @DeleteMapping("/cart/{id}")
    public ResponseEntity<ResponseStatus> deleteCart(@PathVariable("id") Long cartId) throws BadRequestException {
        log.info("OrderController :: deleteCart >>> Start");
        String response = orderService.deleteCart(cartId);
        return new ResponseEntity<>(new ResponseStatus(response), HttpStatus.OK);
    }

    /**
     * Get User Cart For Specific Admin
     *
     * @param userId
     * @return Cart List
     * @throws BadRequestException
     */
    @GetMapping("/cart")
    public ResponseEntity<?> getCartListByUser(@RequestParam("id") Long userId, @RequestParam("adminId") Long adminId) throws BadRequestException {
        log.info("OrderController :: getCartListByUser >>> Start");
        return new ResponseEntity<>(orderService.getCartByUser(userId, adminId), HttpStatus.OK);
    }

    /**
     * Place All The Orders from Cart.
     *
     * @param userId
     * @param adminId
     * @return ResponseStatus
     * @throws BadRequestException
     */
    @GetMapping("/placeOrderFromCart")
    public ResponseEntity<ResponseStatus> placeOrderFromCart(@RequestParam("id") Long userId,
                                                             @RequestParam("adminId") Long adminId,
                                                             @RequestParam("addressId") Long addressId) throws BadRequestException {
        log.info("OrderController :: placeOrderFromCart >>> Start");
        String response = orderService.placeOrderFromCart(userId, adminId, addressId);
        return new ResponseEntity<>(new ResponseStatus(response), HttpStatus.OK);
    }

    /**
     * Get Order Details By Id.
     *
     * @param orderId
     * @return Order Details
     * @throws BadRequestException
     */
    @GetMapping("/getOrderDetailsById")
    public ResponseEntity<?> getOrderDetailsById(@RequestParam("orderId") Long orderId) throws BadRequestException {
        log.info("OrderController :: getOrderDetailsById >>> Start");
        return new ResponseEntity<>(orderService.getOrderDetailsById(orderId), HttpStatus.OK);
    }

    /**
     * Update the Order Status.
     *
     * @param orderId
     * @param orderStatus
     * @param vehicleId
     * @return ResponseStatus
     * @throws BadRequestException
     */
    @PutMapping("/updateOrderStatus")
    public ResponseEntity<ResponseStatus> updateOrderStatus(@RequestParam(value = "orderId") Long orderId,
                                                            @RequestParam(value = "status") String orderStatus,
                                                            @RequestParam(value = "vehicleId", required = false) Long vehicleId) throws BadRequestException {
        log.info("OrderController :: updateOrderStatus >>> Start");
        return new ResponseEntity<>(new ResponseStatus(orderService.updateOrderStatus(orderId, orderStatus, vehicleId)), HttpStatus.OK);
    }

}
