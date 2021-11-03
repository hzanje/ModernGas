package com.moderngas.service;

import com.moderngas.exception.BadRequestException;

import java.util.List;

public interface EmployeeService {

    List<String> getAvailableCylinder();

    String assignCylinderToUser(Long orderId, List<String> cylinderCodes) throws BadRequestException;

    String receiveCylinderFromUser(Long orderId, List<String> cylinderCodes) throws BadRequestException;

    List<String> getAssignedCylinderByUserId(Long userId) throws BadRequestException;

}
