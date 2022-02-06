package com.moderngas.restcontroller;

import com.moderngas.exception.BadRequestException;
import com.moderngas.pojo.ResponseStatus;
import com.moderngas.service.ResourceCentreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/operator", produces = "application/json")
public class OperatorController {

    @Autowired
    private ResourceCentreService resourceCentreService;

    /**
     * Add the cylinder to Resource Center
     * The cylinder are been added to RC via one RC to another or
     * user's cylinder fetched while orders for refill
     *
     * @param resourceCentreId
     * @param cylinderCodes
     * @return
     * @throws BadRequestException
     */
    @PutMapping("/addToResourceCentre/{resourceCentreId}")
    public ResponseEntity<ResponseStatus> addCylinderToResourceCentre(@PathVariable("resourceCentreId") Long resourceCentreId,
                                                                      @RequestBody List<String> cylinderCodes) throws BadRequestException {
        log.info("OperatorController :: addCylinderToResourceCentre >>> Start");
        String response = resourceCentreService.addCylinderToResourceCentre(resourceCentreId, cylinderCodes);
        return new ResponseEntity<>(new ResponseStatus(response), HttpStatus.OK);
    }

    /**
     * Remove Cylinder in Resource Centre
     * The cylinder are been removed from RC, are transiting to another RC or for delivery purpose.
     *
     * @param resourceCentreId
     * @param cylinderCodes
     * @return
     * @throws BadRequestException
     */
    @PutMapping("/removeFromResourceCentre/{resourceCentreId}")
    public ResponseEntity<ResponseStatus> removeCylinderToResourceCentre(@PathVariable("resourceCentreId") Long resourceCentreId,
                                                                         @RequestBody List<String> cylinderCodes) throws BadRequestException {
        log.info("OperatorController :: addCylinderToResourceCentre >>> Start");
        String response = resourceCentreService.removeCylinderFromResourceCentre(resourceCentreId, cylinderCodes);
        return new ResponseEntity<>(new ResponseStatus(response), HttpStatus.OK);
    }

    /**
     * @param resourceCentreId
     * @param cylinderCodes
     * @return
     * @throws BadRequestException
     */
    @PutMapping("/public/addToResourceCentre/{resourceCentreId}/{userId}")
    public ResponseEntity<ResponseStatus> addPublicCylinderToResourceCentre(@PathVariable("resourceCentreId") Long resourceCentreId,
                                                                            @PathVariable("userId") Long userId,
                                                                            @RequestBody List<String> cylinderCodes) throws BadRequestException {
        log.info("OperatorController :: (Public) addCylinderToResourceCentre >>> Start");
        String response = resourceCentreService.addPublicCylinderToResourceCentre(resourceCentreId, userId, cylinderCodes);
        return new ResponseEntity<>(new ResponseStatus(response), HttpStatus.OK);
    }

    /**
     * @param resourceCentreId
     * @param cylinderCodes
     * @return
     * @throws BadRequestException
     */
    @PutMapping("/public/removeFromResourceCentre/{resourceCentreId}")
    public ResponseEntity<ResponseStatus> removePublicCylinderToResourceCentre(@PathVariable("resourceCentreId") Long resourceCentreId,
                                                                               @RequestBody List<String> cylinderCodes) throws BadRequestException {
        log.info("OperatorController :: (Public) removeFromResourceCentre >>> Start");
        String response = "";/*resourceCentreService.addPublicCylinderToResourceCentre(resourceCentreId, cylinderCodes);*/
        return new ResponseEntity<>(new ResponseStatus(response), HttpStatus.OK);
    }

    /**
     * Check if cylinder code exit in System (True : If present).
     *
     * @param code
     * @return
     * @throws BadRequestException
     */
    @GetMapping("/checkCylinderCode")
    public ResponseEntity<ResponseStatus> checkCylinderCode(@RequestParam("code") final String code) throws BadRequestException {
        log.info("OperatorController :: checkCylinderCode >>> {}", code);
        String response = resourceCentreService.checkCylinderCode(code);
        return new ResponseEntity<>(new ResponseStatus(response), HttpStatus.OK);
    }

    /**
     * Fetch the cylinder from Resource Centre
     * Get the list of all the Cylinders present in Resource Centre
     *
     * @param resourceCentreId
     * @param cylinderStatus
     * @return
     * @throws BadRequestException
     */
    @GetMapping("/fetchCylinderFromResourceCentre")
    public ResponseEntity<?> fetchCylinderFromResourceCentre(@RequestParam(value = "id", required = false) Long resourceCentreId,
                                                             @RequestParam(value = "cylinderStatus", required = false) String cylinderStatus,
                                                             @RequestParam("adminId") Long adminId) throws BadRequestException {
        return new ResponseEntity<>(resourceCentreService.fetchCylinderFromResourceCentre(resourceCentreId, cylinderStatus, adminId), HttpStatus.OK);
    }

    /**
     * Mark Cylinder As Filled State.
     *
     * @param cylinderCodes
     * @return
     * @throws BadRequestException
     */
    @PutMapping("/fillCylinder")
    public ResponseEntity<?> fillCylinder(@RequestBody List<String> cylinderCodes) throws BadRequestException {
        log.info("EmployeeController :: fillCylinder >>> Start ");
        return new ResponseEntity<>(new ResponseStatus(resourceCentreService.fillCylinder(cylinderCodes)), HttpStatus.OK);
    }

}
