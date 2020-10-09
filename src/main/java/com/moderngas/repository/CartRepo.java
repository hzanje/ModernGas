package com.moderngas.repository;

import com.moderngas.jpaentity.CartEntity;
import com.moderngas.jpaentity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepo extends JpaRepository<CartEntity, Long> {

    List<CartEntity> getCartEntitiesByUserIdOrderByUpdatedDate(Long userId);
}
