package com.moderngas.service;

import com.moderngas.exception.BadRequestException;
import com.moderngas.pojo.admin.OnboardingDto;
import com.moderngas.pojo.admin.OnboardingDtoList;
import com.moderngas.pojo.admin.ProductGasDto;

import java.util.List;

public interface AdminService {

    List<OnboardingDto> getOnboardingDetails(Long id) throws BadRequestException;

    String saveOnBoardingDetails(OnboardingDtoList onboardingDtoList) throws BadRequestException;

    String updateAdminGas(Long adminId, Long userId,  ProductGasDto productGasDto) throws BadRequestException;
}
