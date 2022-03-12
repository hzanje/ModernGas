package com.moderngas.restcontroller;

import com.moderngas.exception.BadRequestException;
import com.moderngas.pojo.ResponseStatus;
import com.moderngas.pojo.admin.CylinderDto;
import com.moderngas.pojo.user.AddressDto;
import com.moderngas.pojo.user.UserSearchDto;
import com.moderngas.service.GenericService;
import com.moderngas.service.InventoryService;
import com.moderngas.service.UserService;
import net.minidev.json.JSONObject;
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
@RequestMapping(value = "/client", produces = "application/json")
public class UserController {

    private static Logger log = LoggerFactory.getLogger(UserController.class.getName());

    @Autowired
    private UserService userService;

    @Autowired
    private GenericService genericService;

    @Autowired
    private InventoryService inventoryService;

    /**
     * Get All the Client/User with respect to its Admin.
     *
     * @param adminId
     * @return
     */
    @Secured("ROLE_EMPLOYEE")
    @GetMapping("/getAllUser")
    public HttpEntity<PagedModel<EntityModel<UserSearchDto>>> getAllClient(PagedResourcesAssembler<UserSearchDto> assembler,
                                                                           @RequestParam(value = "size", defaultValue = "10") Integer size,
                                                                           @RequestParam(value = "page", defaultValue = "0") Integer page,
                                                                           @RequestParam(value = "search", required = false) String search,
                                                                           @RequestParam(value = "adminId") Long adminId) throws BadRequestException {
        log.info("UserController :: getAllClient >>> Admin Id : {} and search : {}", adminId, search);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "createdDate"));
        Page<UserSearchDto> userSearchDtoList = userService.getAllUserByAdmin(pageable, search, adminId);
        Link link = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                .getAllClient(assembler, size, page, search, adminId)).withSelfRel();
        PagedModel<EntityModel<UserSearchDto>> model = assembler.toModel(userSearchDtoList, link);
        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    /**
     * Get Client/User by Id.
     *
     * @param userId
     * @return
     * @throws BadRequestException
     */
    @GetMapping(value = "/getClientById/{userId}")
    public ResponseEntity<?> getClientById(@PathVariable("userId") Long userId) throws BadRequestException {
        log.info("UserController :: getClientById >>> userId : {}", userId);
        return new ResponseEntity<>(userService.getUserById(userId), HttpStatus.OK);
    }

    /**
     * Get User By UserName(PhoneNumber)
     *
     * @param userName
     * @return
     */
    @Secured("ROLE_ADMIN")
    @GetMapping(value = "/getUser")
    public ResponseEntity<?> getUser(@RequestParam("userName") Long userName) throws BadRequestException {
        log.info("UserController :: getUser >>> userName : {}", userName);
        return new ResponseEntity<>(genericService.convertUserDataToDto(userService.getUserByLoginId(userName)), HttpStatus.OK);
    }

    /**
     * Changes Password for the User in Application
     *
     * @param username
     * @param newPassword
     * @return
     * @throws BadRequestException
     */
    @PostMapping(value = "/changePassword")
    public ResponseEntity<ResponseStatus> changePassword(@RequestParam("userName") final Long username,
                                                         @RequestParam("newPassword") final String newPassword) throws BadRequestException {
        log.info("UserController :: changePassword >>> userName : {}", username);
        String response = userService.changePassword(username, newPassword);
        return new ResponseEntity<>(new ResponseStatus(response), HttpStatus.OK);
    }

    /**
     * Get UserDashboard By its respected Admin.
     * It will fetch the list of products. User will be able to view all categories
     *
     * @param userId
     * @return
     */
    @GetMapping(value = "/getUserDashboard")
    public ResponseEntity<?> getUserDashboard(@RequestParam("id") Long userId,
                                              @RequestParam("adminId") Long adminId) throws BadRequestException {
        log.info("UserController :: getUserDashboard >>> userId : {}, adminId : {}", userId, adminId);
        return new ResponseEntity<>(userService.getUserDashboard(userId, adminId), HttpStatus.OK);
    }

    /**
     * Get List of Gas Master By Its Category Id By its respected Admin.
     *
     * @param categoryId
     * @return
     */
    @GetMapping(value = "/getGasListByCategoryId")
    public ResponseEntity<?> getGasListByCategoryId(@RequestParam("id") Long categoryId,
                                                    @RequestParam("adminId") Long adminId) throws BadRequestException {
        log.info("UserController :: getGasListByCategoryId >>> categoryId : {}, adminId : {}", categoryId, adminId);
        return new ResponseEntity<>(userService.getGasListByCategoryId(categoryId, adminId), HttpStatus.OK);
    }

    /**
     * Get Gas Details By Id For Specific Admin.
     *
     * @param id
     * @param adminId
     * @return
     * @throws BadRequestException
     */
    @GetMapping(value = "/getGasDetailsById")
    public ResponseEntity<?> getGasDetailsById(@RequestParam("id") Long id, @RequestParam("adminId") Long adminId) throws BadRequestException {
        log.info("UserController :: getGasDetailsById >>> GasId : {}, AdminId : {}",id, adminId);
        return new ResponseEntity<>(userService.getGasDetailsById(id, adminId), HttpStatus.OK);
    }


    /**
     * Add or Update User Address
     *
     * @param addressDto
     * @param userId
     * @return
     * @throws BadRequestException
     */
    @PostMapping(value = "/address/{userId}")
    public ResponseEntity<ResponseStatus> addOrUpdateAddress(@RequestBody AddressDto addressDto,
                                                             @PathVariable("userId") final Long userId) throws BadRequestException {
        log.info("UserController :: updateAddress >>> User : {}", userId);
        String response = userService.addOrUpdateAddress(addressDto, userId);
        return new ResponseEntity<>(new ResponseStatus(response), HttpStatus.OK);
    }

    /**
     * Get User Address by User Id.
     *
     * @param userId
     * @return
     * @throws BadRequestException
     */
    @GetMapping(value = "/address/{userId}")
    public ResponseEntity<?> getAddress(@PathVariable("userId") final Long userId) throws BadRequestException {
        log.info("UserController :: getAddress >>> userId : {}", userId);
        JSONObject obj = userService.getAddress(userId);
        if (obj.containsKey("message")) {
            return new ResponseEntity<>(obj, HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(obj, HttpStatus.OK);
        }
    }

    /**
     * Delete User Address By Id
     *
     * @param id
     * @return
     */
    @DeleteMapping(value = "/address/{id}")
    public ResponseEntity<ResponseStatus> deleteUserAddress(@PathVariable("id") Long id) {
        log.info("UserController :: deleteUserAddress >>> Address : {}", id);
        String response = userService.deleteUserAddress(id);
        return new ResponseEntity<>(new ResponseStatus(response), HttpStatus.OK);
    }

    /**
     * Get User Inventory for Specific User's Admin
     *
     * @param id
     * @param adminId
     * @return
     * @throws BadRequestException
     */
    @GetMapping(value = "/inventory")
    public ResponseEntity<?> getUserInventory(@RequestParam("id") Long id,
                                              @RequestParam("adminId") Long adminId) throws BadRequestException {
        log.info("UserController :: getUserInventory >>> UserId : {}, AdminId : {}", id, adminId);
        return new ResponseEntity<>(inventoryService.getUserInventory(id, adminId), HttpStatus.OK);
    }

    /**
     * Add Cylinder for Specific User by Cylinder Code
     *
     * @param userId
     * @param cylinderDtoList
     * @return
     * @throws BadRequestException
     */
    @PostMapping("/addCylinder/{userId}")
    public ResponseEntity<ResponseStatus> addCylinder(@PathVariable("userId") Long userId,
                                                      @RequestBody List<CylinderDto> cylinderDtoList) throws BadRequestException {
        log.info("UserController :: addCylinder >>> UserId :{} ", userId);
        return new ResponseEntity<>(new ResponseStatus(inventoryService.addUserCylinder(userId, cylinderDtoList)), HttpStatus.OK);
    }

    @GetMapping("/frequentlyOrderProduct/{id}")
    public ResponseEntity<?> getFrequentlyOrderProduct(@PathVariable("id") Long userId,
                                                                    @RequestParam("adminId") Long adminId) throws BadRequestException {
        log.info("UserController :: getFrequentlyOrderProduct >>> UserId : {} , AdminId : {}", userId, adminId);
        return new ResponseEntity<>(userService.getFrequentlyOrderProduct(userId, adminId), HttpStatus.OK);
    }
}
