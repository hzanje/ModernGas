package com.moderngas.service;

import com.moderngas.exception.BadRequestException;
import com.moderngas.pojo.admin.CylinderCodeListDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

public interface EmployeeService {

    List<String> getAvailableCylinder();

    String assignCylinderToUser(Long orderId, CylinderCodeListDto codeListDto) throws BadRequestException;

    String receiveCylinderFromUser(Long orderId, CylinderCodeListDto codeListDto) throws BadRequestException;

    List<String> getAssignedCylinderByUserId(Long userId) throws BadRequestException;
}
