package com.moderngas.repository;

import com.moderngas.jpaentity.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface CartRepo extends JpaRepository<CartEntity, Long> {

    List<CartEntity> getCartEntitiesByUserIdOrderByUpdatedDate(Long userId);

    List<CartEntity> getCartEntitiesByUserIdAndAdminIdOrderByUpdatedDate(Long userId, Long adminId);

    @Modifying
    @Query("DELETE FROM CartEntity WHERE userId = :userId")
    void deleteByUserId(@Param("userId") Long userId);
}
