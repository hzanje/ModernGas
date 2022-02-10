package com.moderngas.repository;


import com.moderngas.enums.CylinderStatus;
import com.moderngas.jpaentity.CylinderEntity;
import com.moderngas.pojo.admin.CylinderInventoryDto;
import com.moderngas.pojo.user.InventoryDetailsDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    @Query("UPDATE CylinderEntity SET cylinderStatus =:status, assignedUserId=:assignedUserId, assignedUserName=:assignedUserName WHERE code IN (:codeList) ")
    void updateCylinderToAssigned(@Param("assignedUserId") Long userId,
                                  @Param("assignedUserName") String userName,
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

    @Query("SELECT new com.moderngas.pojo.admin.CylinderInventoryDto(ce.id, ce.code, ce.cylinderStatus, ue.id, ue.name, ce.cylinderInventoryDetailsEntity.isTransit, ce.cylinderInventoryDetailsEntity.resourceCentreEntity.id, ce.cylinderInventoryDetailsEntity.resourceCentreEntity.name) FROM CylinderEntity ce " +
            "LEFT JOIN UserEntity ue ON ce.assignedUserId = ue.id ")
    List<CylinderInventoryDto> getInventoryCylinderForAdmin();

    @Query("SELECT new com.moderngas.pojo.admin.CylinderInventoryDto(ce.id, ce.code, ce.cylinderStatus, ue.id, ue.name, ce.cylinderInventoryDetailsEntity.isTransit, ce.cylinderInventoryDetailsEntity.resourceCentreEntity.id, ce.cylinderInventoryDetailsEntity.resourceCentreEntity.name) FROM CylinderEntity ce " +
            "LEFT JOIN UserEntity ue ON ce.assignedUserId = ue.id " +
            "WHERE ce.assignedUserId=:assignedUserId")
    List<CylinderInventoryDto> getAssignedCylinderByUserId(@Param("assignedUserId") Long assignedUserId);

    @Query(value = QUERIES.FETCH_CYLINDER_BY_RESOURCE_CENTRE_AND_STATUS)
    Page<CylinderInventoryDto> fetchCylinderFromResourceCentreByIdAndStatus(Pageable pageable,
                                                                            @Param("search") String search,
                                                                            @Param("resourceCentreIds") Set<Long> resourceCentreId,
                                                                            @Param("cylinderStatus") CylinderStatus cylinderStatus,
                                                                            @Param("adminId") Long adminId);

    @Query("SELECT new com.moderngas.pojo.user.InventoryDetailsDto(ce.id, ce.code, ue.id, ue.name, ue.name) FROM UserEntity ue INNER JOIN ue.cylinderEntitySet ce WHERE ce.assignedUserId = :id ")
    Set<InventoryDetailsDto> getInventoryCylinderAssignedToUser(@Param("id") Long id);

    @Query("SELECT new com.moderngas.pojo.user.InventoryDetailsDto(ce.id, ce.code, ue.id, ue.name, ue.name) FROM UserEntity ue INNER JOIN ue.cylinderEntitySet ce WHERE ue.id = :id ")
    Set<InventoryDetailsDto> getInventoryCylinderOwnedByUser(@Param("id") Long id);

    class QUERIES {

        private static final String FETCH_CYLINDER_BY_RESOURCE_CENTRE = "SELECT new com.moderngas.pojo.admin.CylinderInventoryDto(ce.id, ce.code, ce.cylinderStatus, ue.id, ue.name, ce.cylinderInventoryDetailsEntity.isTransit, ce.cylinderInventoryDetailsEntity.resourceCentreEntity.id, ce.cylinderInventoryDetailsEntity.resourceCentreEntity.name) FROM UserEntity ue " +
                "INNER JOIN ue.cylinderEntitySet ce " +
                "WHERE ue.id = :adminId " +
                "AND (:search IS NULL OR ce.code LIKE :search%) " +
                "AND ce.cylinderInventoryDetailsEntity.resourceCentreEntity.id IN :resourceCentreIds ";

        private static final String FETCH_CYLINDER_BY_RESOURCE_CENTRE_AND_STATUS = FETCH_CYLINDER_BY_RESOURCE_CENTRE + " AND (:cylinderStatus IS NULL OR ce.cylinderStatus = :cylinderStatus) ";

        private QUERIES() {
        }
    }

}
