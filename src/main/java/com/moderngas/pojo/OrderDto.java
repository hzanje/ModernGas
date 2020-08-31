package com.moderngas.pojo;

import com.moderngas.jpaentity.GasMaster;
import lombok.Data;

@Data
public class OrderDto {

    private String cylinderType;

    private boolean isRefill;

    private Long userId;

    private GasMaster gasMaster;

    private int quantity;

    private int refillCount;


}
