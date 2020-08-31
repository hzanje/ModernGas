package com.moderngas.service;


import com.moderngas.pojo.OrderDto;

public interface OrderService {

    String placeUserOrder(OrderDto orderDto);


    String addCart(OrderDto orderDto);
}
