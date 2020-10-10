package com.moderngas.service;


import com.moderngas.pojo.CartDto;
import com.moderngas.pojo.OrderDto;

import java.text.ParseException;
import java.util.List;

public interface OrderService {

    String placeOrder(OrderDto orderDto);

    List<OrderDto> getOrderListByUser(Long userId);

    String addCart(CartDto cartDto);

    List<CartDto> getCartByUser(Long userId);

    String deleteOrder(Long orderId);

    String deleteCart(Long cartId);

    String placeOrderFromCart(Long userId);

}
