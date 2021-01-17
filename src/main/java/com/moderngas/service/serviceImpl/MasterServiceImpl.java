package com.moderngas.service.serviceImpl;

import com.moderngas.enums.CylinderStatus;
import com.moderngas.enums.CylinderType;
import com.moderngas.enums.OrderStatus;
import com.moderngas.pojo.NameIdDto;
import com.moderngas.service.MasterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class MasterServiceImpl implements MasterService {

    @Override
    public List<NameIdDto> getOrderStatus() {
        List<NameIdDto> orderStatusList = new ArrayList<>();
        for (OrderStatus orderStatus : OrderStatus.values()) {
            orderStatusList.add(new NameIdDto((long) orderStatus.ordinal(), orderStatus.getName()));
        }
        return orderStatusList;
    }

    @Override
    public List<String> getCylinderStatus() {
        List<String> cylinderStatusList = new ArrayList<>();
        for (CylinderStatus cylinderStatus : CylinderStatus.values()) {
            cylinderStatusList.add(cylinderStatus.getName());
        }
        return cylinderStatusList;
    }

    @Override
    public List<String> getCylinderType() {
        List<String> cylinderTypeList = new ArrayList<>();
        for (CylinderType cylinderType : CylinderType.values()) {
            cylinderTypeList.add(cylinderType.getName());
        }
        return cylinderTypeList;
    }
}
