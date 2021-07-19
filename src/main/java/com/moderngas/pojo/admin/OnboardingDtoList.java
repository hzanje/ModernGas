package com.moderngas.pojo.admin;

import lombok.Data;

import java.util.List;

@Data
public class OnboardingDtoList {

    private List<OnboardingDto> onboardingDtoList;

    private Long id;
}
