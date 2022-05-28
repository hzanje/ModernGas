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

import javax.mail.MessagingException;
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
                                                  @RequestBody UserDto userDto) throws BadRequestException, NoSuchAlgorithmException, MessagingException {
        log.info("AdminController :: userEntityDto >>> AdminId : {}", adminId);
        return new ResponseEntity<>(new ResponseStatus(userService.addUser(adminId, userDto)), HttpStatus.OK);
    }

    /**
     * Update User To System.
     *
     * @param userDto
     * @return
     */
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @PutMapping(value = "/updateUser/{adminId}")
    public ResponseEntity<ResponseStatus> updateUser(@PathVariable("adminId") Long adminId,
                                                     @RequestBody UserDto userDto) throws BadRequestException {
        log.info("AdminController :: updateUser >>> AdminId : {}", adminId);
        return new ResponseEntity<>(new ResponseStatus(userService.updateUser(adminId, userDto)), HttpStatus.OK);
    }


    /**
     * Add Employee To System
     *
     * @param adminId
     * @param userDto
     * @return
     */
    @Secured({"ROLE_ADMIN", "ROLE_EMPLOYEE"})
    @PostMapping("/addEmployee/{adminId}")
    public ResponseEntity<ResponseStatus> addEmployee(@PathVariable("adminId") Long adminId,
                                         @RequestBody UserDto userDto) throws BadRequestException, NoSuchAlgorithmException, MessagingException {
        log.info("AdminController :: addEmployee >>> AdminId : {}", adminId);
        return new ResponseEntity<>(new ResponseStatus(employeeService.addEmployee(adminId, userDto)), HttpStatus.OK);
    }

    /**
     * Update Employee To System
     *
     * @param adminId
     * @param userDto
     * @return
     */
    @Secured({"ROLE_ADMIN", "ROLE_EMPLOYEE"})
    @PostMapping("/updateEmployee/{adminId}")
    public ResponseEntity<ResponseStatus> updateEmployee(@PathVariable("adminId") Long adminId,
                                            @RequestBody UserDto userDto) throws BadRequestException {
        log.info("AdminController :: addEmployee >>> AdminId : {}", adminId);
        return new ResponseEntity<>(new ResponseStatus(employeeService.updateEmployee(adminId, userDto)), HttpStatus.OK);
    }

    @DeleteMapping("/deleteUser/{adminId}/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable("adminId") Long adminId,
                                        @PathVariable("userId") Long userId) throws BadRequestException {
        log.info("AdminController :: deleteUser >>> AdminId : {}, UserId : {}", adminId, userId);
        return new ResponseEntity<>(userService.deleteUser(adminId, userId), HttpStatus.OK);
    }

    /**
     * Add Cylinder for Specific Admin by Cylinder Code
     *
     * @param adminId
     * @param cylinderDtoList
     * @return
     * @throws BadRequestException
     */
    @Secured({"ROLE_ADMIN", "ROLE_EMPLOYEE"})
    @PostMapping("/addCylinder/{adminId}")
    public ResponseEntity<ResponseStatus> addCylinder(@PathVariable("adminId") Long adminId,
                                                      @RequestBody List<CylinderDto> cylinderDtoList) throws BadRequestException {
        log.info("AdminController :: addCylinder >>> AdminId : {}", adminId);
        return new ResponseEntity<>(new ResponseStatus(inventoryService.addAdminCylinder(adminId, cylinderDtoList)), HttpStatus.OK);
    }

    /**
     * Update Cylinder for Specific Admin by Cylinder Code
     *
     * @param adminId
     * @param cylinderDto
     * @return
     * @throws BadRequestException
     */
    @Secured({"ROLE_ADMIN", "ROLE_EMPLOYEE"})
    @PutMapping("/addCylinder/{adminId}")
    public ResponseEntity<ResponseStatus> updateCylinder(@PathVariable("adminId") Long adminId,
                                                      @RequestBody CylinderDto cylinderDto) throws BadRequestException {
        log.info("AdminController :: updateCylinder >>> AdminId : {}", adminId);
        return new ResponseEntity<>(new ResponseStatus(inventoryService.updateAdminCylinder(adminId, cylinderDto)), HttpStatus.OK);
    }

    /**
     * Get Filter for Admin in Order Tab
     *
     * @return
     */
    @Secured({"ROLE_ADMIN", "ROLE_EMPLOYEE"})
    @GetMapping("/filter")
    public ResponseEntity<FilterDto> getFilters() {
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
    @Secured({"ROLE_ADMIN", "ROLE_EMPLOYEE"})
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
    @Secured({"ROLE_ADMIN", "ROLE_EMPLOYEE"})
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
    @Secured({"ROLE_ADMIN", "ROLE_EMPLOYEE"})
    @DeleteMapping("/vehicle/{id}")
    public ResponseEntity<ResponseStatus> deleteVehicle(@PathVariable("id") Long vehicleId) throws BadRequestException {
        log.info("AdminController :: deleteVehicle >>> Id : {}", vehicleId);
        return new ResponseEntity<>(new ResponseStatus(userService.deleteVehicle(vehicleId)), HttpStatus.OK);
    }

    /**
     * Get Cylinder Inventory List.
     *
     * @param adminId
     * @return
     */
    @Secured({"ROLE_ADMIN", "ROLE_EMPLOYEE"})
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
    @Secured({"ROLE_ADMIN", "ROLE_EMPLOYEE"})
    @GetMapping("/userDetails")
    public ResponseEntity<?> getUserDetails(@RequestParam("id") Long id) throws BadRequestException {
        log.info("AdminController :: getUserDetails >>> AdminId :{}", id);
        return new ResponseEntity<>(userService.getUserDetailsForAdmin(id), HttpStatus.OK);
    }

    /**
     * Get User Order List For Admin In User Details
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
    @Secured({"ROLE_ADMIN", "ROLE_EMPLOYEE"})
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
    @Secured({"ROLE_ADMIN", "ROLE_EMPLOYEE"})
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
    @Secured({"ROLE_ADMIN", "ROLE_EMPLOYEE"})
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
    @Secured({"ROLE_ADMIN", "ROLE_EMPLOYEE"})
    @PostMapping("/placeOrder/{adminId}")
    public ResponseEntity<ResponseStatus> placeOrder(@PathVariable("adminId") Long adminId,
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
    public ResponseEntity<ResponseStatus> updateAdminGas(@PathVariable("adminId") Long adminId,
                                            @RequestParam (value = "userId", required = false) Long userId,
                                            @RequestBody ProductGasDto productGasDto) throws BadRequestException {
        log.info("AdminController :: updateAdminGas >>> AdminId : {}, UserId : {}", adminId, userId);
        return new ResponseEntity<>(new ResponseStatus(adminService.updateAdminGas(adminId, userId, productGasDto)), HttpStatus.OK);
    }

    /**
     * Decrypt the Cylinder Code from Encrypted QR Code Data
     *
     * @param encryptedCode
     * @return
     * @throws BadRequestException
     */
    @GetMapping("/decryptCylinderQR")
    public ResponseEntity<?> decryptCylinderEntity(@RequestParam("encryptedCode") String encryptedCode) throws BadRequestException {
        log.info("AdminController :: decryptCylinderEntity >>> EncryptedCode : {}", encryptedCode);
        return new ResponseEntity<>(inventoryService.decryptCylinderEntity(encryptedCode), HttpStatus.OK);
    }

}

