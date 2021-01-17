package com.moderngas.service;

import com.moderngas.pojo.admin.CylinderCodeListDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

public interface EmployeeService {

    List<String> getAvailableCylinder();

    String assignCylinderToUser(Long orderId, CylinderCodeListDto codeListDto);

    String receiveCylinderFromUser(Long orderId, CylinderCodeListDto codeListDto);
}
