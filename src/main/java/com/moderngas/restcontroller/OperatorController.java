package com.moderngas.restcontroller;

import com.moderngas.exception.BadRequestException;
import com.moderngas.pojo.ResponseStatus;
import com.moderngas.pojo.admin.CylinderInventoryDto;
import com.moderngas.service.InventoryService;
import com.moderngas.service.ResourceCentreService;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/operator", produces = "application/json")
public class OperatorController {

    private static Logger log = LoggerFactory.getLogger(OperatorController.class.getName());

    @Autowired
    private ResourceCentreService resourceCentreService;

    @Autowired
    private InventoryService inventoryService;

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
    public HttpEntity<PagedModel<EntityModel<CylinderInventoryDto>>> fetchCylinderFromResourceCentre(PagedResourcesAssembler<CylinderInventoryDto> assembler,
                                                                                                     @RequestParam(value = "size", defaultValue = "10") Integer size,
                                                                                                     @RequestParam(value = "page", defaultValue = "0") Integer page,
                                                                                                     @RequestParam(value = "search", required = false) String search,
                                                                                                     @RequestParam(value = "id", required = false) Long resourceCentreId,
                                                                                                     @RequestParam(value = "cylinderStatus", required = false) String cylinderStatus,
                                                                                                     @RequestParam("adminId") Long adminId) throws BadRequestException {
        log.info("OperatorController :: fetchCylinderFromResourceCentre >>> AdminId : {} and Search :{} ", adminId, search);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id"));
        Page<CylinderInventoryDto> cylinderInventoryDtoList = resourceCentreService.fetchCylinderFromResourceCentre(pageable, search, resourceCentreId, cylinderStatus, adminId);
        Link link = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(OperatorController.class)
                .fetchCylinderFromResourceCentre(assembler, size, page, search, resourceCentreId, cylinderStatus, adminId)).withSelfRel();
        PagedModel<EntityModel<CylinderInventoryDto>> model = assembler.toModel(cylinderInventoryDtoList, link);
        return new ResponseEntity<>(model, HttpStatus.OK);
    }

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
        log.info("OperatorController :: addCylinderToResourceCentre >>> Resource Centre : {}", resourceCentreId);
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
        log.info("OperatorController :: addCylinderToResourceCentre >>> Resource Centre : {}", resourceCentreId);
        String response = resourceCentreService.removeCylinderFromResourceCentre(resourceCentreId, cylinderCodes);
        return new ResponseEntity<>(new ResponseStatus(response), HttpStatus.OK);
    }

    /**
     * Add the cylinder to Resource Center (Public)
     *
     * @param resourceCentreId
     * @param cylinderCodes
     * @return
     * @throws BadRequestException
     */
    @PutMapping("/public/addToResourceCentre/{resourceCentreId}/{userId}")
    public ResponseEntity<ResponseStatus> addPublicCylinderToResourceCentre(@PathVariable("resourceCentreId") Long resourceCentreId,
                                                                            @PathVariable("userId") Long userId,
                                                                            @RequestBody List<String> cylinderCodes) throws BadRequestException {
        log.info("OperatorController :: (Public) addCylinderToResourceCentre >>> Resource Centre : {}, UserId : {}", resourceCentreId, userId);
        String response = resourceCentreService.addPublicCylinderToResourceCentre(resourceCentreId, userId, cylinderCodes);
        return new ResponseEntity<>(new ResponseStatus(response), HttpStatus.OK);
    }

    /**
     * @param resourceCentreId
     * @param cylinderCodes
     * @return
     * @throws BadRequestException
     */
    @PutMapping("/public/removeFromResourceCentre/{resourceCentreId}/{userId}")
    public ResponseEntity<ResponseStatus> removePublicCylinderToResourceCentre(@PathVariable("resourceCentreId") Long resourceCentreId,
                                                                               @PathVariable("userId") Long userId,
                                                                               @RequestBody List<String> cylinderCodes) throws BadRequestException {
        log.info("OperatorController :: (Public) removeFromResourceCentre >>> Resource Centre : {}, UserId : {}", resourceCentreId, userId);
        String response = resourceCentreService.removePublicCylinderToResourceCentre(resourceCentreId, userId, cylinderCodes);
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
        log.info("OperatorController :: checkCylinderCode >>> Cylinder Code : {}", code);
        String response = resourceCentreService.checkCylinderCode(code, null);
        return new ResponseEntity<>(new ResponseStatus(response), HttpStatus.OK);
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
        log.info("OperatorController :: fillCylinder >>> ");
        return new ResponseEntity<>(new ResponseStatus(resourceCentreService.fillCylinder(cylinderCodes)), HttpStatus.OK);
    }

    @GetMapping("/getCylinderDetailsByCode")
    public ResponseEntity<?> getCylinderDetailsByCode(@RequestParam("code") String code) throws BadRequestException {
        log.info("OperatorController :: getCylinderDetailsByCode >>> code : {}", code);
        return new ResponseEntity<>(inventoryService.getCylinderDetailsByCode(code), HttpStatus.OK);
    }

}
