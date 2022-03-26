package com.moderngas.repository;

import com.moderngas.jpaentity.DeliveryVehicleEntity;
import com.moderngas.pojo.NameIdDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface DeliveryVehicleRepo extends JpaRepository<DeliveryVehicleEntity, Long> {

    @Query("SELECT new com.moderngas.pojo.NameIdDto(id, number) FROM DeliveryVehicleEntity WHERE userId = :userId AND activeFlag = 1")
    List<NameIdDto> getVehicleNumberList(@Param("userId") Long userId);

    @Query("FROM DeliveryVehicleEntity dv WHERE dv.number = :vehicleNumber")
    DeliveryVehicleEntity getVehicleByNumber(@Param("vehicleNumber") String vehicleNumber);

    @Query("FROM DeliveryVehicleEntity dv WHERE dv.id = :id")
    DeliveryVehicleEntity getVehicleById(@Param("id") Long deliveryVehicleId);
}
