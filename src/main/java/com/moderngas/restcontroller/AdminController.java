package com.moderngas.restcontroller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.moderngas.constants.Constants;
import com.moderngas.exception.BadRequestException;
import com.moderngas.pojo.ResponseStatus;
import com.moderngas.pojo.admin.*;
import com.moderngas.pojo.user.UserEntityDto;
import com.moderngas.pojo.user.UserSearchDto;
import com.moderngas.service.*;
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

    @Autowired
    private ResourceCentreService resourceCentreService;

    @Autowired
    private AdminService adminService;

    /**
     * Get All Order List By Parameter
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
    @GetMapping("/order")
    public HttpEntity<PagedModel<EntityModel<OrderDto>>> getAllOrderList(PagedResourcesAssembler<OrderDto> assembler,
                                                                         @RequestParam(value = "size", defaultValue = "0") Integer size,
                                                                         @RequestParam(value = "page", defaultValue = "0") Integer page,
                                                                         @RequestParam(value = "status", required = false) String status,
                                                                         @RequestParam(value = "cylinderType", required = false) List<String> cylinderType,
                                                                         @RequestParam(value = "search", required = false) String search,
                                                                         @RequestParam(value = "quantityOrdering", required = false) String quantityOrder) throws JsonProcessingException, BadRequestException {

        log.info("AdminController :: getAllOrderList >>> Start");
        Sort sortOrdering = getSortingOrder(quantityOrder);
        Pageable pageable = PageRequest.of(page, size, sortOrdering);
        Page<OrderDto> orderDtoList = orderService.getAllOrderListForAdmin(pageable, status, cylinderType, search, quantityOrder);
        Link link = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(AdminController.class)
                .getAllOrderList(assembler, size, page, status, cylinderType, search, quantityOrder)).withSelfRel();

        PagedModel<EntityModel<OrderDto>> model = assembler.toModel(orderDtoList, link);
        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    /**
     * Get Sorting Order
     *
     * @param quantityOrder
     * @return
     */
    private Sort getSortingOrder(String quantityOrder) {
        if (quantityOrder.equals(Constants.FILTER_ORDERING_MIN_MAX)) {
            return Sort.by(Sort.Direction.ASC, "createdDate");
        }
        return Sort.by(Sort.Direction.DESC, "createdDate");
    }

    /**
     * Add Cylinder for Specific Admin by Cylinder Code
     *
     * @param adminId
     * @param cylinderCodeStatusDtoList
     * @return
     * @throws BadRequestException
     */
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @PostMapping("/addCylinder/{adminId}")
    public ResponseEntity<ResponseStatus> addCylinder(@PathVariable("adminId") Long adminId,
                                                      @RequestBody List<CylinderCodeStatusDto> cylinderCodeStatusDtoList) throws BadRequestException {
        log.info("AdminController :: addCylinder >>> Start ");
        String response = inventoryService.addCylinder(adminId, cylinderCodeStatusDtoList);
        return new ResponseEntity<>(new ResponseStatus(response), HttpStatus.OK);
    }

    /**
     * Add User By Roles - Admin Only
     *
     * @param userEntityDto
     * @return ResponseEntity
     * @throws BadRequestException
     */
    @Secured("ROLE_ADMIN")
    @PostMapping(value = "/addUser")
    public ResponseEntity<ResponseStatus> addUser(@RequestBody UserEntityDto userEntityDto) throws BadRequestException {
        log.info("AdminController :: userEntityDto >>> Start");
        String response = userService.addUser(userEntityDto);
        return new ResponseEntity<>(new ResponseStatus(response), HttpStatus.OK);
    }


    /**
     * Get Filter for Admin in Order Tab
     *
     * @return
     */
    @Secured("ROLE_ADMIN")
    @GetMapping("/filter")
    public ResponseEntity<?> getFilters() {
        log.info("AdminController :: getFilters >>> Start");
        return new ResponseEntity<>(genericService.getFilterList(), HttpStatus.OK);
    }

    /**
     * Add delivery vehicles by admin to assigned the particular order.
     *
     * @param deliveryVehicleDto
     * @return
     * @throws BadRequestException
     */
    @Secured("ROLE_ADMIN")
    @PostMapping("/vehicle")
    public ResponseEntity<ResponseStatus> addVehicle(@RequestBody DeliveryVehicleDto deliveryVehicleDto) throws BadRequestException {
        log.info("AdminController :: addVehicle >>> Start");
        String response = userService.addVehicle(deliveryVehicleDto);
        return new ResponseEntity<>(new ResponseStatus(response), HttpStatus.OK);
    }

    /**
     * Get Vehicle ()
     *
     * @param userId
     * @return
     */
    @Secured("ROLE_ADMIN")
    @GetMapping("/vehicle/{id}")
    public ResponseEntity<?> getVehicle(@PathVariable("id") Long userId) {
        log.info("AdminController :: getVehicle >>> Start");
        return new ResponseEntity<>(userService.getVehicleNumberList(userId), HttpStatus.OK);
    }

    /**
     * Get Cylinder Inventory List.
     *
     * @param adminId
     * @return
     */
    @Secured("ROLE_ADMIN")
    @GetMapping("/inventory")
    public ResponseEntity<?> getInventoryCylinderForAdmin(@RequestParam("id") Long adminId) {
        log.info("AdminController :: getInventoryCylinderForAdmin >>> Start");
        return new ResponseEntity<>(inventoryService.getInventoryCylinderForAdmin(adminId), HttpStatus.OK);
    }


    @Secured("ROLE_ADMIN")
    @GetMapping("/search")
    public HttpEntity<PagedModel<EntityModel<UserSearchDto>>> searchUser(PagedResourcesAssembler<UserSearchDto> assembler,
                                                                         @RequestParam(value = "size", defaultValue = "0") Integer size,
                                                                         @RequestParam(value = "page", defaultValue = "0") Integer page,
                                                                         @RequestParam(value = "name") String name) throws BadRequestException {

        log.info("AdminController :: searchUser >>> Start");
        Pageable pageable = PageRequest.of(page, size);
        Page<UserSearchDto> userSearchList = userService.searchUserByName(pageable, name);
        Link link = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(AdminController.class)
                .searchUser(assembler, size, page, name)).withSelfRel();

        PagedModel<EntityModel<UserSearchDto>> model = assembler.toModel(userSearchList, link);
        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    /**
     * Get User details for Admin as per specific Admin
     *
     * @param id
     * @return
     * @throws BadRequestException
     */
    @Secured("ROLE_ADMIN")
    @GetMapping("/userDetails")
    public ResponseEntity<?> getUserDetails(@RequestParam("id") Long id) throws BadRequestException {
        log.info("AdminController :: getUserDetails >>> Start");
        return new ResponseEntity<>(userService.getUserDetailsForAdmin(id), HttpStatus.OK);
    }

    /**
     * Get all the Orders as per Admin
     *
     * @param id
     * @return
     * @throws BadRequestException
     */
    @GetMapping("/userOrder")
    public ResponseEntity<List<OrderDto>> getUserOrder(@RequestParam("id") Long id) throws BadRequestException {
        log.info("AdminController :: getUserOrder >>> Start");
        return new ResponseEntity<>(orderService.getUserOrderListForAdminInUserDetails(id), HttpStatus.OK);
    }

    /**
     * Get All Details for Admin Onboarding.
     *
     * @param id
     * @return
     * @throws BadRequestException
     */
    @GetMapping("/onboarding")
    public ResponseEntity<?> getAdminOnboardingDetails(@RequestParam("id") Long id) throws BadRequestException {
        log.info("AdminController :: getAdminOnboardingDetails >>> Start");
        return new ResponseEntity<>(adminService.getOnboardingDetails(id), HttpStatus.OK);
    }

    /**
     * Save the details for Onboarding (Gas Description and Price)
     *
     * @param onboardingDtoList
     * @return
     * @throws BadRequestException
     */
    @PostMapping("/onboarding")
    public ResponseEntity<ResponseStatus> saveAdminOnBoardingDetails(@RequestBody OnboardingDtoList onboardingDtoList) throws BadRequestException {
        log.info("AdminController :: saveAdminOnBoardingDetails >>> Start");
        String response = adminService.saveOnBoardingDetails(onboardingDtoList);
        return new ResponseEntity<>(new ResponseStatus(response), HttpStatus.OK);
    }

    /**
     * Add Or Update Resource Centre by Admin
     *
     * @param resourceCentreDtoList
     * @return
     * @throws BadRequestException
     */
    @Secured("ROLE_ADMIN")
    @PostMapping("/resourceCentre")
    public ResponseEntity<ResponseStatus> addOrUpdateResourceCentre(@RequestBody List<ResourceCentreDto> resourceCentreDtoList) throws BadRequestException {
        log.info("AdminController :: addOrUpdateResourceCentre >>> Start");
        String response = resourceCentreService.addOrUpdateResourceCentre(resourceCentreDtoList);
        return new ResponseEntity<>(new ResponseStatus(response), HttpStatus.OK);
    }

    /**
     * Get the resource centre by Admin
     *
     * @param id
     * @return
     * @throws BadRequestException
     */
    @Secured("ROLE_ADMIN")
    @GetMapping("/resourceCentre")
    public ResponseEntity<?> getResourceCentre(@RequestParam(value = "id", required = false) Long id) throws BadRequestException {
        log.info("AdminController :: getResourceCentre >>> Start");
        return new ResponseEntity<>(resourceCentreService.getResourceCentre(), HttpStatus.OK);
    }

    /**
     * Delete the resource centre for specific admin
     *
     * @param id
     * @return
     * @throws BadRequestException
     */
    @Secured("ROLE_ADMIN")
    @DeleteMapping("/resourceCentre/{id}")
    public ResponseEntity<ResponseStatus> deleteResourceCentre(@PathVariable("id") Long id) throws BadRequestException {
        log.info("AdminController :: deleteResourceCentre >>> Start");
        String response = resourceCentreService.deleteResourceCentre(id);
        return new ResponseEntity<>(new ResponseStatus(response), HttpStatus.OK);
    }

}

