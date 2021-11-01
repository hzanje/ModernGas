package com.moderngas.restcontroller;

import com.moderngas.exception.BadRequestException;
import com.moderngas.pojo.NameIdDto;
import com.moderngas.pojo.ResponseStatus;
import com.moderngas.service.ResourceCentreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping(value = "/operator", produces = "application/json")
public class OperatorController {

    @Autowired
    private ResourceCentreService resourceCentreService;

    @PutMapping("/addToResourceCentre/{resourceCentreId}")
    public ResponseEntity<ResponseStatus> addCylinderToResourceCentre(@PathVariable("resourceCentreId") Long resourceCentreId,
                                                                      @RequestBody List<String> cylinderCodes) throws BadRequestException {
        log.info("OperatorController :: addCylinderToResourceCentre >>> Start");
        String response = resourceCentreService.addCylinderToResourceCentre(resourceCentreId, cylinderCodes);
        return new ResponseEntity<>(new ResponseStatus(response), HttpStatus.OK);
    }

    @GetMapping("/fetchCylinderFromResourceCentre")
    public List<NameIdDto> fetchCylinderFromResourceCentre(@RequestParam("id") Long resourceCentreId,
                                                           @RequestParam(value = "cylinderStatus", required = false) String cylinderStatus) throws BadRequestException {
        return resourceCentreService.fetchCylinderFromResourceCentre(resourceCentreId, cylinderStatus);
    }

}
