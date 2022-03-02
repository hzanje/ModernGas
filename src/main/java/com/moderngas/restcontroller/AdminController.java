package com.moderngas.restcontroller;

import com.moderngas.exception.BadRequestException;
import com.moderngas.pojo.ResponseStatus;
import com.moderngas.pojo.UserDto;
import com.moderngas.pojo.admin.*;
import com.moderngas.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
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
        log.info("AdminController :: userEntityDto >>> Start");
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
        log.info("UserController :: updateUser >>> Start");
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
        log.info("EmployeeController >>> addEmployee :: Start");
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
        log.info("EmployeeController >>> addEmployee :: Start");
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
        log.info("AdminController :: addCylinder >>> Start ");
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

    @Secured("ROLE_ADMIN")
    @DeleteMapping("/vehicle/{id}")
    public ResponseEntity<?> deleteVehicle(@PathVariable("id") Long vehicleId) throws BadRequestException {
        log.info("AdminController :: deleteVehicle {} >>> Start", vehicleId);
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
        log.info("AdminController :: getInventoryCylinderForAdmin >>> Start");
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

    @Secured("ROLE_EMPLOYEE")
    @PostMapping("/placeOrder/{adminId}")
    public ResponseEntity<?> placeOrder(@PathVariable("adminId") Long adminId,
                                        @RequestBody OpenOrderDto openOrderDto) throws BadRequestException, NoSuchAlgorithmException {
        log.info("AdminController :: placeOrder :: adminId > {} >>> Start ", adminId);
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
        log.info("AdminController :: getAllGasList :: adminId > {} >>> Start ", adminId);
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
        log.info("AdminController ::  >>> Admin : {}", adminId);
        return new ResponseEntity<>(new ResponseStatus(adminService.updateAdminGas(adminId, productGasDto)), HttpStatus.OK);
    }

}

