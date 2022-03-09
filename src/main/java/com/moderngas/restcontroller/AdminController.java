package com.moderngas.restcontroller;

import com.moderngas.exception.BadRequestException;
import com.moderngas.pojo.ResponseStatus;
import com.moderngas.pojo.UserDto;
import com.moderngas.pojo.admin.*;
import com.moderngas.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
@RequestMapping(value = "/admin", produces = "application/json")
public class AdminController {

    private static Logger log = LoggerFactory.getLogger(AdminController.class.getName());

    @Autowired
    private OrderService orderService;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private UserService userService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private GenericService genericService;

    @Autowired
    private ResourceCentreService resourceCentreService;

    @Autowired
    private AdminService adminService;


    /**
     * Add User To System
     *
     * @param userDto
     * @return ResponseEntity
     * @throws BadRequestException
     */
    @Secured("ROLE_ADMIN")
    @PostMapping(value = "/addUser/{adminId}")
    public ResponseEntity<ResponseStatus> addUser(@PathVariable("adminId") Long adminId,
                                                  @RequestBody UserDto userDto) throws BadRequestException, NoSuchAlgorithmException {
        log.info("AdminController :: userEntityDto >>> AdminId : {}", adminId);
        return new ResponseEntity<>(new ResponseStatus(userService.addUser(adminId, userDto)), HttpStatus.OK);
    }

    /**
     * Update User To System.
     *
     * @param userDto
     * @return
     */
    @Secured("ROLE_USER")
    @PutMapping(value = "/updateUser/{adminId}")
    public ResponseEntity<ResponseStatus> updateUser(@PathVariable("adminId") Long adminId,
                                                     @RequestBody UserDto userDto) throws BadRequestException {
        log.info("UserController :: updateUser >>> AdminId : {}", adminId);
        return new ResponseEntity<>(new ResponseStatus(userService.updateUser(adminId, userDto)), HttpStatus.OK);
    }


    /**
     * Add Employee To System
     *
     * @param adminId
     * @param userDto
     * @return
     */
    @Secured("ROLE_ADMIN")
    @PostMapping("/addEmployee/{adminId}")
    public ResponseEntity<?> addEmployee(@PathVariable("adminId") Long adminId,
                                         @RequestBody UserDto userDto) throws BadRequestException, NoSuchAlgorithmException {
        log.info("EmployeeController :: addEmployee >>> AdminId : {}", adminId);
        return new ResponseEntity<>(new ResponseStatus(employeeService.addEmployee(adminId, userDto)), HttpStatus.OK);
    }

    /**
     * Update Employee To System
     *
     * @param adminId
     * @param userDto
     * @return
     */
    @Secured("ROLE_EMPLOYEE")
    @PostMapping("/updateEmployee/{adminId}")
    public ResponseEntity<?> updateEmployee(@PathVariable("adminId") Long adminId,
                                            @RequestBody UserDto userDto) throws BadRequestException {
        log.info("EmployeeController :: addEmployee >>> AdminId : {}", adminId);
        return new ResponseEntity<>(new ResponseStatus(employeeService.updateEmployee(adminId, userDto)), HttpStatus.OK);
    }

    /**
     * Add Cylinder for Specific Admin by Cylinder Code
     *
     * @param adminId
     * @param cylinderDtoList
     * @return
     * @throws BadRequestException
     */
    @Secured("ROLE_ADMIN")
    @PostMapping("/addCylinder/{adminId}")
    public ResponseEntity<ResponseStatus> addCylinder(@PathVariable("adminId") Long adminId,
                                                      @RequestBody List<CylinderDto> cylinderDtoList) throws BadRequestException {
        log.info("AdminController :: addCylinder >>> AdminId : {}", adminId);
        return new ResponseEntity<>(new ResponseStatus(inventoryService.addAdminCylinder(adminId, cylinderDtoList)), HttpStatus.OK);
    }

    /**
     * Get Filter for Admin in Order Tab
     *
     * @return
     */
    @Secured("ROLE_EMPLOYEE")
    @GetMapping("/filter")
    public ResponseEntity<?> getFilters() {
        log.info("AdminController :: getFilters >>> ");
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
        log.info("AdminController :: addVehicle >>> ");
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
        log.info("AdminController :: getVehicle >>> AdminId : {}", userId);
        return new ResponseEntity<>(userService.getVehicleNumberList(userId), HttpStatus.OK);
    }

    /**
     * Delete Vehicle By Id
     *
     * @param vehicleId
     * @return
     * @throws BadRequestException
     */
    @Secured("ROLE_ADMIN")
    @DeleteMapping("/vehicle/{id}")
    public ResponseEntity<?> deleteVehicle(@PathVariable("id") Long vehicleId) throws BadRequestException {
        log.info("AdminController :: deleteVehicle >>> Id : {}", vehicleId);
        return new ResponseEntity<>(new ResponseStatus(userService.deleteVehicle(vehicleId)), HttpStatus.OK);
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
        log.info("AdminController :: getInventoryCylinderForAdmin >>> AdminId : {}", adminId);
        return new ResponseEntity<>(inventoryService.getInventoryCylinderForAdmin(adminId), HttpStatus.OK);
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
        log.info("AdminController :: getUserDetails >>> AdminId :{}", id);
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
        log.info("AdminController :: getUserOrder >>> AdminId : {}", id);
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
    public ResponseEntity<?> getAdminOnBoardingDetails(@RequestParam("id") Long id) throws BadRequestException {
        log.info("AdminController :: getAdminOnBoardingDetails >>> ");
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
        log.info("AdminController :: saveAdminOnBoardingDetails >>> ");
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
        log.info("AdminController :: addOrUpdateResourceCentre >>> ");
        String response = resourceCentreService.addOrUpdateResourceCentre(resourceCentreDtoList);
        return new ResponseEntity<>(new ResponseStatus(response), HttpStatus.OK);
    }

    /**
     * Get the resource centre by Admin
     *
     * @param adminId
     * @return
     * @throws BadRequestException
     */
    @Secured("ROLE_ADMIN")
    @GetMapping("/resourceCentre")
    public ResponseEntity<?> getResourceCentre(@RequestParam(value = "id", required = false) Long adminId) throws BadRequestException {
        log.info("AdminController :: getResourceCentre >>> AdminId : {}", adminId);
        return new ResponseEntity<>(resourceCentreService.getResourceCentre(adminId), HttpStatus.OK);
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
        log.info("AdminController :: deleteResourceCentre >>> Id :{}", id);
        String response = resourceCentreService.deleteResourceCentre(id);
        return new ResponseEntity<>(new ResponseStatus(response), HttpStatus.OK);
    }

    /**
     * Place Order
     *
     * @param adminId
     * @param openOrderDto
     * @return
     * @throws BadRequestException
     * @throws NoSuchAlgorithmException
     */
    @Secured("ROLE_EMPLOYEE")
    @PostMapping("/placeOrder/{adminId}")
    public ResponseEntity<?> placeOrder(@PathVariable("adminId") Long adminId,
                                        @RequestBody OpenOrderDto openOrderDto) throws BadRequestException, NoSuchAlgorithmException {
        log.info("AdminController :: placeOrder >>> AdminId > {} ", adminId);
        return new ResponseEntity<>(new ResponseStatus(orderService.placeAdminInitiatedOrder(openOrderDto, adminId)), HttpStatus.OK);
    }

    /**
     * Get All Gas/Product List as Respect to Admin
     * API is used to fetch all Gas/Product List while creating order and View in Product Tab
     *
     * @param adminId
     * @return
     * @throws BadRequestException
     */
    @GetMapping("/getAllGasList/{adminId}")
    public ResponseEntity<?> getAllGasList(@PathVariable("adminId") Long adminId) throws BadRequestException {
        log.info("AdminController :: getAllGasList >>>  AdminId > {} ", adminId);
        return new ResponseEntity<>(userService.getAllGasList(adminId), HttpStatus.OK);
    }

    /**
     * Update Gas/Product Price and Description By Admin
     *
     * @param adminId
     * @param productGasDto
     * @return
     * @throws BadRequestException
     */
    @PutMapping("/updateAdminGas/{adminId}")
    public ResponseEntity<?> updateAdminGas(@PathVariable("adminId") Long adminId,
                                            @RequestBody ProductGasDto productGasDto) throws BadRequestException {
        log.info("AdminController :: updateAdminGas >>> AdminId : {}", adminId);
        return new ResponseEntity<>(new ResponseStatus(adminService.updateAdminGas(adminId, productGasDto)), HttpStatus.OK);
    }

}

