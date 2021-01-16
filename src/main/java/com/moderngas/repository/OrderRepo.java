package com.moderngas.repository;

import com.moderngas.enums.OrderStatus;
import com.moderngas.jpaentity.OrderEntity;
import com.moderngas.pojo.admin.OrderDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Query(value = QUERIES.ALL_ORDER_LIST_HEADER_FOR_ADMIN, countQuery = QUERIES.ALL_ORDER_LIST_FOR_ADMIN_COUNT)
    Page<OrderDto> getAllOrderListForAdmin(Pageable pageable,
                                           @Param("status") OrderStatus status,
                                           @Param("cylinderId") Long cylinderId,
                                           @Param("search") String search,
                                           @Param("quantityOrder") String quantityOrder);

    class QUERIES {

        private static final String ORDER_ENTRIES_BY_USER_ID = "FROM OrderEntity WHERE userId = :userId ORDER BY updatedDate ";

        private static final String ALL_ORDER_LIST_FOR_ADMIN = " FROM OrderEntity ord " +
                " LEFT JOIN UserEntity u ON u.id = ord.userId " +
                " LEFT JOIN CylinderTypeMaster ctm ON ctm.id = ord.cylinderType" +
                " WHERE ord.activeFlag = 1 " +
                " AND (:status IS NULL OR ord.orderStatus = :status)" +
                " AND (:cylinderId IS NULL OR ord.cylinderType = :cylinderId)" +
                " AND (:search IS NULL OR u.name LIKE :search%)" +
                " AND (:quantityOrder IS NULL)" +
                " ORDER BY ord.createdDate DESC, ord.orderStatus ASC ";

        private static final String ALL_ORDER_LIST_HEADER_FOR_ADMIN = "SELECT new com.moderngas.pojo.admin.OrderDto(" +
                "ord.id, ctm.name, ord.isRefill, u.id, u.name, ord.gasMaster.name, ord.gasMaster.categoryMaster.name," +
                " ord.orderStatus, ord.quantity, ord.createdDate) " + ALL_ORDER_LIST_FOR_ADMIN;

        private static final String ALL_ORDER_LIST_FOR_ADMIN_COUNT = "SELECT COUNT(id) " + ALL_ORDER_LIST_FOR_ADMIN;

    }
}
