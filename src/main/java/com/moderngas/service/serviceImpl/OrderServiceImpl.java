package com.moderngas.service.serviceImpl;

import com.moderngas.jpaentity.OrderEntity;
import com.moderngas.pojo.OrderDto;
import com.moderngas.repository.OrderRepo;
import com.moderngas.service.GenericService;
import com.moderngas.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    GenericService genericService;

    @Autowired
    OrderRepo orderRepo;

    @Override
    public String placeUserOrder(OrderDto orderDto) {
        String response = "Failure";
        OrderEntity orderEntity = genericService.convertDtoToOrderEntity(orderDto);
        if (null != orderEntity) {
            orderRepo.save(orderEntity);
            response = "Success";
        }
        return response;
    }

    @Override
    public String addCart(OrderDto orderDto) {
        return null;
    }
}