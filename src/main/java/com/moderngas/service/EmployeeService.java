package com.moderngas.service;

import com.moderngas.exception.BadRequestException;
import com.moderngas.pojo.UserDto;
import com.moderngas.pojo.admin.CylinderInventoryDto;
import com.moderngas.pojo.employee.EmployeeEntityResponseDto;
import com.moderngas.pojo.user.UserSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.mail.MessagingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface EmployeeService {

    Page<UserSearchDto> getAllEmployeeByAdmin(Pageable pageable, String search, Long adminId) throws BadRequestException;

    List<String> getAvailableCylinder();

    String assignCylinderToUser(Long orderId, List<String> cylinderCodes) throws BadRequestException;

    String receiveCylinderFromUser(Long orderId, List<String> cylinderCodes) throws BadRequestException;

    List<CylinderInventoryDto> getAssignedCylinderByUserId(Long userId, Long adminId) throws BadRequestException;

    String addEmployee(Long adminId, UserDto userDto) throws BadRequestException, NoSuchAlgorithmException, MessagingException;

    String updateEmployee(Long adminId, UserDto userDto) throws BadRequestException;

    EmployeeEntityResponseDto getEmployeeById(Long employeeId) throws BadRequestException;
}
