package com.moderngas.restcontroller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.moderngas.exception.BadRequestException;
import com.moderngas.pojo.employee.EmployeeSearchDto;
import com.moderngas.pojo.user.UserSearchDto;
import com.moderngas.service.EmployeeService;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/employee", produces = "application/json")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Secured("ROLE_EMPLOYEE")
    @GetMapping("/getAllEmployee")
    public HttpEntity<PagedModel<EntityModel<EmployeeSearchDto>>> getAllEmployee(PagedResourcesAssembler<EmployeeSearchDto> assembler,
                                                                                 @RequestParam(value = "size", defaultValue = "0") Integer size,
                                                                                 @RequestParam(value = "page", defaultValue = "0") Integer page,
                                                                                 @RequestParam(value = "search", required = false) String search,
                                                                                 @RequestParam(value = "adminId") Long adminId) throws JsonProcessingException, BadRequestException {
        log.info("UserController :: searchUser >>> Start ");
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC);
        Page<EmployeeSearchDto> userSearchDtoList = employeeService.getAllEmployeeByAdmin(pageable, search, adminId);
        Link link = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmployeeController.class)
                .getAllEmployee(assembler, size, page, search, adminId)).withSelfRel();
        PagedModel<EntityModel<EmployeeSearchDto>> model = assembler.toModel(userSearchDtoList, link);
        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    /**
     * Assigned Cylinder to Order.
     *
     * @param orderId
     * @param cylinderCodes
     * @return
     * @throws BadRequestException
     */
    @PutMapping("/assign/{orderId}")
    public ResponseEntity<?> assignCylinderToUser(@PathVariable("orderId") Long orderId,
                                                  @RequestBody List<String> cylinderCodes) throws BadRequestException {
        log.info("EmployeeController :: assignCylinderToUser >>> Start ");
        return new ResponseEntity<>(employeeService.assignCylinderToUser(orderId, cylinderCodes), HttpStatus.OK);
    }

    /**
     * Receive Cylinder From User.
     *
     * @param orderId
     * @param cylinderCodes
     * @return
     * @throws BadRequestException
     */
    @PutMapping("/receive/{orderId}")
    public ResponseEntity<?> receiveCylinderFromUser(@PathVariable("orderId") Long orderId,
                                                     @RequestBody List<String> cylinderCodes) throws BadRequestException {
        log.info("EmployeeController :: receiveCylinderFromUser >>> Start ");
        return new ResponseEntity<>(employeeService.receiveCylinderFromUser(orderId, cylinderCodes), HttpStatus.OK);
    }

    /**
     * Get All the Available Cylinder
     *
     * @return
     */
    @GetMapping("/availableCylinder")
    public ResponseEntity<?> getAvailableCylinder() {
        log.info("EmployeeController :: getAvailableCylinder >>> Start ");
        return new ResponseEntity<>(employeeService.getAvailableCylinder(), HttpStatus.OK);
    }

    /**
     * Get List of Assigned Cylinder Assigned To User
     *
     * @param userId
     * @return
     * @throws BadRequestException
     */
    @GetMapping("/getAssignedCylinder")
    public ResponseEntity<?> getAssignedCylinder(@RequestParam(value = "id") Long userId) throws BadRequestException {
        log.info("EmployeeController :: getAssignedCylinder >>> Start ");
        return new ResponseEntity<>(employeeService.getAssignedCylinderByUserId(userId), HttpStatus.OK);
    }
}
