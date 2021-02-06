package com.moderngas.repository;


import com.moderngas.enums.CylinderStatus;
import com.moderngas.jpaentity.CylinderEntity;
import com.moderngas.pojo.admin.CylinderCodeListDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface InventoryRepo extends JpaRepository<CylinderEntity, Long> {

    @Query("SELECT code FROM CylinderEntity WHERE cylinderStatus =:status")
    List<String> getAvailableCylinder(@Param("status") CylinderStatus status);

    @Query("UPDATE CylinderEntity SET cylinderStatus =:status, userId=:userId WHERE code IN (:codeList) ")
    void updateCylinderToAssigned(@Param("userId") Long userId,
                                  @Param("codeList") List<String> codeList,
                                  @Param("status") CylinderStatus status);

    @Query("UPDATE CylinderEntity SET cylinderStatus =:status, userId = NULL WHERE code IN (:codeList) AND userId=:userId")
    void updateCylinderToEmpty(@Param("userId") Long userId,
                               @Param("codeList") List<String> codeList,
                               @Param("status") CylinderStatus status);

    @Query(" FROM CylinderEntity WHERE code = :code")
    CylinderEntity checkIfCylinderCodeExist(@Param("code") String code);

}
