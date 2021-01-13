package com.moderngas.restcontroller;

import com.moderngas.pojo.admin.OrderDto;
import com.moderngas.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/admin", produces = "application/json")
public class AdminController {

    @Autowired
    OrderService orderService;

    @GetMapping("/order")
    HttpEntity<PagedModel<EntityModel<OrderDto>>> getAllOrderList(PagedResourcesAssembler<OrderDto> assembler,
               @RequestParam(value = "size",defaultValue = "0") Integer size,
               @RequestParam(value = "page", defaultValue = "0") Integer page,
               @RequestParam(value = "status", required = false) String status,
               @RequestParam(value = "cylinder", required = false) Long cylinderId,
               @RequestParam(value = "search", required = false) String search,
               @RequestParam(value = "quantityOrder", required = false) String quantityOrder) {
        Pageable pageable = PageRequest.of(page, size);
        Page<OrderDto> orderDtoList = orderService.getAllOrderListForAdmin(pageable, status, cylinderId, search, quantityOrder);
        Link link = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(AdminController.class)
                .getAllOrderList(assembler, size, page, status, cylinderId, search, quantityOrder)).withSelfRel();

        PagedModel<EntityModel<OrderDto>> model = assembler.toModel(orderDtoList, link);
        return new ResponseEntity<>(model, HttpStatus.OK);
    }

}

