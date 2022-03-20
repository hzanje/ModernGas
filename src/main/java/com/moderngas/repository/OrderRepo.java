package com.moderngas.repository;

import com.moderngas.enums.CylinderType;
import com.moderngas.enums.OrderStatus;
import com.moderngas.jpaentity.OrderEntity;
import com.moderngas.pojo.admin.OrderDto;
import com.moderngas.pojo.user.FrequentOrderProductDto;
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
    List<OrderEntity> getOrderEntitiesByUserId(@Param("userId") Long userId,
                                               @Param("adminId") Long adminId);

    @Query(value = QUERIES.ORDER_COUNT_BY_USER_ID)
    int getOrderCountByUserId(@Param("userId") Long userId);

    @Query(" UPDATE OrderEntity SET activeFlag = 0 where id = :id")
    void deleteOrderById(@Param("id") Long orderId);

    @Query(value = QUERIES.ALL_ORDER_LIST_HEADER_FOR_ADMIN, countQuery = QUERIES.ALL_ORDER_LIST_FOR_ADMIN_COUNT)
    Page<OrderDto> getAllOrderListForAdmin(Pageable pageable,
                                           @Param("status") List<OrderStatus> status,
                                           @Param("cylinderType") List<CylinderType> cylinderType,
                                           @Param("adminId") Long adminId,
                                           @Param("search") String search,
                                           @Param("quantityOrder") String quantityOrder);

    @Query(QUERIES.ORDER_LIST_FOR_ADMIN_IN_USER_DETAILS)
    List<OrderDto> getUserOrderListForAdminInUserDetails(@Param("userId") Long userId);

    @Query(QUERIES.FREQUENTLY_ORDER_PRODUCT_LIST)
    Page<FrequentOrderProductDto> getFrequentlyOrderProduct(Pageable pageable,
                                                            @Param("userId") Long userId,
                                                            @Param("adminId") Long adminId);

    class QUERIES {

        private static final String ORDER_ENTRIES_BY_USER_ID = "FROM OrderEntity WHERE userId = :userId AND adminId = :adminId ORDER BY updatedDate ";
        private static final String ORDER_COUNT_BY_USER_ID = "SELECT COUNT(*) FROM OrderEntity WHERE userId = :userId ORDER BY updatedDate ";
        private static final String ALL_ORDER_LIST_FOR_ADMIN = " FROM OrderEntity ord " +
                " LEFT JOIN UserEntity u ON u.id = ord.userId " +
                " WHERE :adminId member of u.adminIdSet" +
                " AND ord.activeFlag = 1 " +
                " AND (COALESCE(:status) IS NULL OR ord.orderStatus IN :status)" +
                " AND (COALESCE(:cylinderType) IS NULL OR ord.cylinderType IN :cylinderType)" +
                " AND (:search IS NULL OR u.name LIKE :search%)" +
                " AND (:quantityOrder IS NULL)" +
                " ORDER BY function('date_format', ord.createdDate, '%Y, %m, %d') DESC, ord.orderStatus ASC ";

        private static final String ALL_ORDER_LIST_HEADER_FOR_ADMIN = "SELECT new com.moderngas.pojo.admin.OrderDto(" +
                "ord.id, ord.cylinderType, ord.isRefill, u.id, u.name, ord.gasMaster.name, ord.gasMaster.categoryMaster.name," +
                " ord.orderStatus, ord.quantity, ord.createdDate) " + ALL_ORDER_LIST_FOR_ADMIN;

        private static final String ALL_ORDER_LIST_FOR_ADMIN_COUNT = "SELECT COUNT(ord.id) " + ALL_ORDER_LIST_FOR_ADMIN;

        private static final String ORDER_LIST_FOR_ADMIN_IN_USER_DETAILS = "SELECT new com.moderngas.pojo.admin.OrderDto(" +
                "ord.id, ord.cylinderType, ord.isRefill, u.id, u.name, ord.gasMaster.name, ord.gasMaster.categoryMaster.name," +
                " ord.orderStatus, ord.quantity, ord.createdDate) FROM OrderEntity ord LEFT JOIN UserEntity u ON u.id = ord.userId " +
                " WHERE ord.userId =:userId ORDER BY ord.createdDate DESC";

        private static final String FREQUENTLY_ORDER_PRODUCT_LIST = "SELECT new com.moderngas.pojo.user.FrequentOrderProductDto(ord.activeFlag, ord.gasMaster.id, ord.gasMaster.name, ord.gasMaster.categoryMaster.name, agm.price) " +
                "FROM OrderEntity ord " +
                "LEFT JOIN AdminGasMapping agm ON agm.gasId = ord.gasMaster.id " +
                "WHERE ord.userId =:userId AND ord.adminId = :adminId " +
                "GROUP BY ord.gasMaster.id ORDER BY COUNT(ord.gasMaster.id) DESC ";

        private QUERIES() {
        }

    }
}
