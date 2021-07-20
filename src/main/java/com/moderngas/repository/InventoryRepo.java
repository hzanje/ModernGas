package com.moderngas.repository;


import com.moderngas.enums.CylinderStatus;
import com.moderngas.jpaentity.CylinderEntity;
import com.moderngas.pojo.admin.InventoryCylinderDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface InventoryRepo extends JpaRepository<CylinderEntity, Long> {

    @Query("SELECT code FROM CylinderEntity WHERE cylinderStatus =:status")
    List<String> getAvailableCylinder(@Param("status") CylinderStatus status);

    @Modifying
    @Query("UPDATE CylinderEntity SET cylinderStatus =:status, userId=:userId WHERE code IN (:codeList) ")
    void updateCylinderToAssigned(@Param("userId") Long userId,
                                  @Param("codeList") List<String> codeList,
                                  @Param("status") CylinderStatus status);

    @Modifying
    @Query("UPDATE CylinderEntity SET cylinderStatus =:status, userId = NULL WHERE code IN (:codeList) AND userId=:userId")
    void updateCylinderToEmpty(@Param("userId") Long userId,
                               @Param("codeList") List<String> codeList,
                               @Param("status") CylinderStatus status);

    @Query(" FROM CylinderEntity WHERE code = :code")
    Optional<CylinderEntity> checkIfCylinderCodeExist(@Param("code") String code);

    @Query("SELECT new com.moderngas.pojo.admin.InventoryCylinderDto(ce.id, ce.code, ce.cylinderStatus, ue.id, ue.name) FROM CylinderEntity ce " +
            "LEFT JOIN UserEntity ue ON ce.userId = ue.id ")
    List<InventoryCylinderDto> getInventoryCylinderForAdmin();

    @Query("SELECT code FROM CylinderEntity WHERE userId=:userId")
    List<String> getAssignedCylinderByUserId(@Param("userId") Long userId);
}
