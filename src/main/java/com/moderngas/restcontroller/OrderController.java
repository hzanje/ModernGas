package com.moderngas.restcontroller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.moderngas.constants.Constants;
import com.moderngas.exception.BadRequestException;
import com.moderngas.pojo.ResponseStatus;
import com.moderngas.pojo.user.CartDto;
import com.moderngas.pojo.user.OrderDto;
import com.moderngas.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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
    @GetMapping("/userOrderList")
    public ResponseEntity<?> getOrderListByUser(@RequestParam("id") Long userId, @RequestParam("adminId") Long adminId) throws BadRequestException {
        log.info("OrderController :: getOrderListByUser >>> Start");
        return new ResponseEntity<>(orderService.getOrderListByUser(userId, adminId), HttpStatus.OK);
    }

    /**
     * Get All Order List By Parameter and Recent Order on Dashboard
     *
     * @param assembler
     * @param size
     * @param page
     * @param status
     * @param cylinderType
     * @param search
     * @param quantityOrder
     * @return
     * @throws JsonProcessingException
     * @throws BadRequestException
     */
    @Secured("ROLE_ADMIN")
    @GetMapping("/adminOrderList")
    public HttpEntity<PagedModel<EntityModel<com.moderngas.pojo.admin.OrderDto>>> getAllOrderList(PagedResourcesAssembler<com.moderngas.pojo.admin.OrderDto> assembler,
                                                                                                  @RequestParam(value = "size", defaultValue = "10") Integer size,
                                                                                                  @RequestParam(value = "page", defaultValue = "0") Integer page,
                                                                                                  @RequestParam(value = "status", required = false) String status,
                                                                                                  @RequestParam(value = "cylinderType", required = false) List<String> cylinderType,
                                                                                                  @RequestParam(value = "search", required = false) String search,
                                                                                                  @RequestParam(value = "id") Long id,
                                                                                                  @RequestParam(value = "quantityOrdering", required = false) String quantityOrder) throws JsonProcessingException, BadRequestException {

        log.info("AdminController :: getAllOrderList >>> Start");
        Sort sortOrdering = getSortingOrder(quantityOrder);
        Pageable pageable = PageRequest.of(page, size, sortOrdering);
        Page<com.moderngas.pojo.admin.OrderDto> orderDtoList = orderService.getAllOrderListForAdmin(pageable, status, cylinderType, search, id, quantityOrder);
        Link link = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(OrderController.class)
                .getAllOrderList(assembler, size, page, status, cylinderType, search, id, quantityOrder)).withSelfRel();

        PagedModel<EntityModel<com.moderngas.pojo.admin.OrderDto>> model = assembler.toModel(orderDtoList, link);
        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    /**
     * Get Sorting Order
     *
     * @param quantityOrder
     * @return
     */
    private Sort getSortingOrder(String quantityOrder) {
        if (!ObjectUtils.isEmpty(quantityOrder) && quantityOrder.equals(Constants.FILTER_ORDERING_MIN_MAX)) {
            return Sort.by(Sort.Direction.ASC, "createdDate");
        }
        return Sort.by(Sort.Direction.DESC, "createdDate");
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
