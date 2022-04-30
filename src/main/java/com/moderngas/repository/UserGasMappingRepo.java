package com.moderngas.repository;

import com.moderngas.jpaentity.UserGasMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface UserGasMappingRepo extends JpaRepository<UserGasMapping, Long> {

    @Query("FROM UserGasMapping ugm where ugm.gasId = :gasId AND ugm.id = :adminId AND ugm.userId = :userId")
    UserGasMapping getGasMappingByGasIdAndAdminIdAndUserId(@Param("gasId") Long gasId,
                                                           @Param("adminId") Long adminId,
                                                           @Param("userId") Long userId);
}
