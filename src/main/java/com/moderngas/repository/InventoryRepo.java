package com.moderngas.repository;


import com.moderngas.enums.CylinderStatus;
import com.moderngas.jpaentity.CylinderEntity;
import com.moderngas.pojo.admin.InventoryCylinderDto;
import com.moderngas.pojo.user.InventoryDetailsDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@Transactional
public interface InventoryRepo extends JpaRepository<CylinderEntity, Long> {

    @Query("SELECT code FROM CylinderEntity WHERE cylinderStatus =:status")
    List<String> getAvailableCylinder(@Param("status") CylinderStatus status);

    @Modifying
    @Query("UPDATE CylinderEntity SET cylinderStatus =:status, assignedUserId=:assignedUserId WHERE code IN (:codeList) ")
    void updateCylinderToAssigned(@Param("assignedUserId") Long userId,
                                  @Param("codeList") List<String> codeList,
                                  @Param("status") CylinderStatus status);

    @Modifying
    @Query("UPDATE CylinderEntity SET cylinderStatus =:status, assignedUserId = NULL WHERE code IN (:codeList) AND assignedUserId=:assignedUserId")
    void updateCylinderToEmpty(@Param("assignedUserId") Long assignedUserId,
                               @Param("codeList") List<String> codeList,
                               @Param("status") CylinderStatus status);

    @Query(" FROM CylinderEntity WHERE code = :code")
    Optional<CylinderEntity> checkIfCylinderCodeExist(@Param("code") String code);

    @Query(" FROM CylinderEntity WHERE code IN (:codeList)")
    List<CylinderEntity> getCylinderFromCodeList(@Param("codeList") List<String> codeList);

    @Query("SELECT new com.moderngas.pojo.admin.InventoryCylinderDto(ce.id, ce.code, ce.cylinderStatus, ue.id, ue.name) FROM CylinderEntity ce " +
            "LEFT JOIN UserEntity ue ON ce.assignedUserId = ue.id ")
    List<InventoryCylinderDto> getInventoryCylinderForAdmin();

    @Query("SELECT new com.moderngas.pojo.admin.InventoryCylinderDto(ce.id, ce.code, ce.cylinderStatus, ue.id, ue.name) FROM CylinderEntity ce " +
            "LEFT JOIN UserEntity ue ON ce.assignedUserId = ue.id " +
            "WHERE ce.assignedUserId=:assignedUserId")
    List<InventoryCylinderDto> getAssignedCylinderByUserId(@Param("assignedUserId") Long assignedUserId);

    @Query(value = QUERIES.FETCH_CYLINDER_BY_RESOURCE_CENTRE)
    List<InventoryCylinderDto> fetchCylinderFromResourceCentreById(@Param("resourceCentreId") Long resourceCentreId);

    @Query(value = QUERIES.FETCH_CYLINDER_BY_RESOURCE_CENTRE_AND_STATUS)
    List<InventoryCylinderDto> fetchCylinderFromResourceCentreByIdAndStatus(@Param("resourceCentreId") Long resourceCentreId,
                                                                 @Param("cylinderStatus") CylinderStatus cylinderStatus);

    @Query("SELECT new com.moderngas.pojo.user.InventoryDetailsDto(ce.id, ce.code, ue.id, ue.name, ue.name) FROM UserEntity ue INNER JOIN ue.cylinderEntitySet ce WHERE ce.assignedUserId = :id ")
    Set<InventoryDetailsDto> getInventoryCylinderAssignedToUser(@Param("id") Long id);

    @Query("SELECT new com.moderngas.pojo.user.InventoryDetailsDto(ce.id, ce.code, ue.id, ue.name, ue.name) FROM UserEntity ue INNER JOIN ue.cylinderEntitySet ce WHERE ue.id = :id ")
    Set<InventoryDetailsDto> getInventoryCylinderOwnedByUser(@Param("id") Long id);

    class QUERIES {

        private static final String FETCH_CYLINDER_BY_RESOURCE_CENTRE = "SELECT new com.moderngas.pojo.admin.InventoryCylinderDto(ce.id, ce.code, ce.cylinderStatus, ue.id, ue.name) FROM CylinderEntity ce " +
                "LEFT JOIN UserEntity ue ON ce.assignedUserId = ue.id " +
                "WHERE ce.cylinderInventoryDetailsEntity.resourceCentreEntity.id = :resourceCentreId";

        private static final String FETCH_CYLINDER_BY_RESOURCE_CENTRE_AND_STATUS = FETCH_CYLINDER_BY_RESOURCE_CENTRE + " AND ce.cylinderStatus = :cylinderStatus ";

        private QUERIES() {
        }
    }

}
