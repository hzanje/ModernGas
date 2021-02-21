package com.moderngas.restcontroller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.moderngas.exception.BadRequestException;
import com.moderngas.pojo.ResponseStatus;
import com.moderngas.pojo.NameIdDto;
import com.moderngas.pojo.admin.CylinderCodeStatusDto;
import com.moderngas.pojo.admin.DeliveryVehicleDto;
import com.moderngas.pojo.admin.FilterDto;
import com.moderngas.pojo.admin.InventoryCylinderDto;
import com.moderngas.pojo.admin.OrderDto;
import com.moderngas.pojo.user.UserEntityDto;
import com.moderngas.service.GenericService;
import com.moderngas.service.OrderService;
import com.moderngas.service.UserService;
import com.moderngas.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping(value = "/admin", produces = "application/json")
public class AdminController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private UserService userService;

    @Autowired
    private GenericService genericService;

    @GetMapping("/order")
    public HttpEntity<PagedModel<EntityModel<OrderDto>>> getAllOrderList(PagedResourcesAssembler<OrderDto> assembler,
               @RequestParam(value = "size",defaultValue = "0") Integer size,
               @RequestParam(value = "page", defaultValue = "0") Integer page,
               @RequestParam(value = "status", required = false) String status,
               @RequestParam(value = "cylinderType", required = false) List<String> cylinderType,
               @RequestParam(value = "search", required = false) String search,
               @RequestParam(value = "quantityOrdering", required = false) String quantityOrder) throws JsonProcessingException {
        Pageable pageable = PageRequest.of(page, size);
        Page<OrderDto> orderDtoList = orderService.getAllOrderListForAdmin(pageable, status, cylinderType, search, quantityOrder);
        Link link = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(AdminController.class)
                .getAllOrderList(assembler, size, page, status, cylinderType, search, quantityOrder)).withSelfRel();

        PagedModel<EntityModel<OrderDto>> model = assembler.toModel(orderDtoList, link);
        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    @PostMapping("/addCylinder/{id}")
    public ResponseEntity<ResponseStatus> addCylinder(@PathVariable("id") Long userId,
                                                      @RequestBody CylinderCodeStatusDto cylinderCodeStatusDto) throws BadRequestException {
        String response = inventoryService.addCylinder(userId, cylinderCodeStatusDto);
        return new ResponseEntity<>(new ResponseStatus(response), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/addEmployee")
    public ResponseEntity<ResponseStatus> addEmployee(@RequestBody UserEntityDto userEntityDto) {
        String response = userService.addUser(userEntityDto);
        return new ResponseEntity<>(new ResponseStatus(response), HttpStatus.OK);
    }

    @GetMapping("/filter")
    public FilterDto getFilters() {
        return genericService.getFilterList();
    }

    @PostMapping("/vehicle")
    public ResponseEntity<ResponseStatus> addVehicle(@RequestBody DeliveryVehicleDto deliveryVehicleDto) throws BadRequestException {
        String response = userService.addVehicle(deliveryVehicleDto);
        return new ResponseEntity<>(new ResponseStatus(response), HttpStatus.OK);
    }

    @GetMapping("/vehicle")
    public List<NameIdDto> getVehicle(@RequestParam("id") Long userId) {
        return userService.getVehicleNumberList(userId);
    }

    @GetMapping("/inventory")
    public List<InventoryCylinderDto> getInventoryCylinderForAdmin(@RequestParam("id") Long adminId) {
        return inventoryService.getInventoryCylinderForAdmin(adminId);
    }

}

