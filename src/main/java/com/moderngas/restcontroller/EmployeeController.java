package com.moderngas.restcontroller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.moderngas.exception.BadRequestException;
import com.moderngas.pojo.ResponseStatus;
import com.moderngas.pojo.user.UserSearchDto;
import com.moderngas.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

@RestController
@RequestMapping(value = "/employee", produces = "application/json")
public class EmployeeController {

    private static Logger log = LoggerFactory.getLogger(EmployeeController.class.getName());

    @Autowired
    private EmployeeService employeeService;

    /**
     * Get All the Employee with respect to its Admin.
     *
     * @param adminId
     * @return
     */
    @Secured({"ROLE_ADMIN", "ROLE_EMPLOYEE"})
    @GetMapping("/getAllEmployee")
    public HttpEntity<PagedModel<EntityModel<UserSearchDto>>> getAllEmployee(PagedResourcesAssembler<UserSearchDto> assembler,
                                                                                 @RequestParam(value = "size", defaultValue = "10") Integer size,
                                                                                 @RequestParam(value = "page", defaultValue = "0") Integer page,
                                                                                 @RequestParam(value = "search", required = false) String search,
                                                                                 @RequestParam(value = "adminId") Long adminId) throws JsonProcessingException, BadRequestException {
        log.info("UserController :: searchUser >>> AdminId : {}, Search : {} ", adminId, search);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "createdDate"));
        Page<UserSearchDto> employeeSearchDtoList = employeeService.getAllEmployeeByAdmin(pageable, search, adminId);
        Link link = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmployeeController.class)
                .getAllEmployee(assembler, size, page, search, adminId)).withSelfRel();
        PagedModel<EntityModel<UserSearchDto>> model = assembler.toModel(employeeSearchDtoList, link);
        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN", "ROLE_EMPLOYEE"})
    @GetMapping("/getEmployeeById/{id}")
    public ResponseEntity<?> getEmployeeById(@PathVariable("id") Long employeeId) throws BadRequestException {
        log.info("EmployeeController :: getEmployeeById >>> EmployeeId : {}", employeeId);
        return new ResponseEntity<>(employeeService.getEmployeeById(employeeId), HttpStatus.OK);
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
        log.info("EmployeeController :: assignCylinderToUser >>> OrderId : {}", orderId);
        return new ResponseEntity<>(new ResponseStatus(employeeService.assignCylinderToUser(orderId, cylinderCodes)), HttpStatus.OK);
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
        log.info("EmployeeController :: receiveCylinderFromUser >>> OrderId : {} ", orderId);
        return new ResponseEntity<>(new ResponseStatus(employeeService.receiveCylinderFromUser(orderId, cylinderCodes)), HttpStatus.OK);
    }

    /**
     * Get All the Available Cylinder
     *
     * @return
     */
    @GetMapping("/availableCylinder")
    public ResponseEntity<?> getAvailableCylinder() {
        log.info("EmployeeController :: getAvailableCylinder >>> ");
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
    public ResponseEntity<?> getAssignedCylinder(@RequestParam(value = "id") Long userId, @RequestParam(value = "adminId") Long adminId) throws BadRequestException {
        log.info("EmployeeController :: getAssignedCylinder >>> UserId :{}, adminId :{} ", userId, adminId);
        return new ResponseEntity<>(employeeService.getAssignedCylinderByUserId(userId, adminId), HttpStatus.OK);
    }
}
