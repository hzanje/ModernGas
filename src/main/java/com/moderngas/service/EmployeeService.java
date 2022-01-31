package com.moderngas.service;

import com.moderngas.exception.BadRequestException;
import com.moderngas.pojo.admin.CylinderInventoryDto;
import com.moderngas.pojo.user.UserSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EmployeeService {

    Page<UserSearchDto> getAllEmployeeByAdmin(Pageable pageable, String search, Long adminId) throws BadRequestException;

    List<String> getAvailableCylinder();

    String assignCylinderToUser(Long orderId, List<String> cylinderCodes) throws BadRequestException;

    String receiveCylinderFromUser(Long orderId, List<String> cylinderCodes) throws BadRequestException;

    List<CylinderInventoryDto> getAssignedCylinderByUserId(Long userId) throws BadRequestException;

}
