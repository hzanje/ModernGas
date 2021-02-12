package com.moderngas.repository;

import com.moderngas.jpaentity.DeliveryVehicle;
import com.moderngas.pojo.NameIdDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface DeliveryVehicleRepo extends JpaRepository<DeliveryVehicle, Long> {

    @Query("SELECT new com.moderngas.pojo.NameIdDto(id, number) FROM DeliveryVehicle WHERE userId = :userId")
    List<NameIdDto> getVehicleNumberList(@Param("userId") Long userId);
}
