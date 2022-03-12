package com.moderngas.service;

import com.moderngas.pojo.NameIdDto;

import java.util.List;

public interface MasterService {

    List<NameIdDto> getOrderStatus();

    List<String> getCylinderStatus();

    List<String> getCylinderType();

    List<NameIdDto> getGasList();

    List<String> getPrivilegeList();

}
