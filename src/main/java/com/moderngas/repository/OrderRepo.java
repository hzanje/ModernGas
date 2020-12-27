package com.moderngas.repository;

import com.moderngas.jpaentity.CartEntity;
import com.moderngas.jpaentity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface OrderRepo extends JpaRepository<OrderEntity, Long> {

    @Query(value = QUERIES.ORDER_ENTRIES_BY_USER_ID)
    List<OrderEntity> getOrderEntitiesByUserId(Long userId);

    OrderEntity getOrderEntitiesById(Long id);

    @Query(" UPDATE OrderEntity SET activeFlag = 0 where id = :id")
    void deleteOrderById(@Param("id") Long orderId);

    class QUERIES {

        private static final String ORDER_ENTRIES_BY_USER_ID = "FROM OrderEntity WHERE userId = :userId ORDER BY updatedDate ";

    }
}
