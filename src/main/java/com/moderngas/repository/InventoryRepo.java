package com.moderngas.repository;


import com.moderngas.enums.CylinderStatus;
import com.moderngas.jpaentity.CylinderEntity;
import com.moderngas.pojo.admin.CylinderInventoryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

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

    @Query(" FROM CylinderEntity WHERE code = :code AND (:id IS NULL OR  userEntity.id = :id)")
    Optional<CylinderEntity> checkIfCylinderCodeExist(@Param("code") String code, @Param("id") Long userId);

    @Query("SELECT ce FROM CylinderEntity ce INNER JOIN ce.cylinderInventoryDetailsEntity WHERE ce.code IN :codeList")
    List<CylinderEntity> getCylinderFromCodeList(@Param("codeList") List<String> codeList);


    @Query("SELECT new com.moderngas.pojo.admin.CylinderInventoryDto(ce.id, ce.code, ce.cylinderStatus, ue.id, ue.name, ce.assignedUserId , ce.assignedUserName , ce.cylinderInventoryDetailsEntity.isTransit, ce.cylinderInventoryDetailsEntity.resourceCentreEntity.id, ce.cylinderInventoryDetailsEntity.resourceCentreEntity.name, ce.identifier) FROM CylinderEntity ce " +
            "LEFT JOIN UserEntity ue ON ce.assignedUserId = ue.id ")
    List<CylinderInventoryDto> getInventoryCylinderForAdmin();

    @Query(value = QUERIES.CYLINDER_INVENTORY_DTO + "WHERE ce.assignedUserId=:assignedUserId AND ce.userEntity.id = :adminId")
    List<CylinderInventoryDto> getAssignedCylinderByUserId(@Param("assignedUserId") Long assignedUserId,
                                                           @Param("adminId") Long adminId);

    @Query(value = QUERIES.FETCH_CYLINDER_BY_RESOURCE_CENTRE_AND_STATUS, countQuery = QUERIES.FETCH_CYLINDER_BY_RESOURCE_CENTRE_AND_STATUS_COUNT)
    Page<CylinderInventoryDto> fetchCylinderFromResourceCentreByIdAndStatus(Pageable pageable,
                                                                            @Param("search") String search,
                                                                            @Param("resourceCentreIds") Set<Long> resourceCentreId,
                                                                            @Param("cylinderStatus") CylinderStatus cylinderStatus,
                                                                            @Param("adminId") Long adminId);

    @Query(QUERIES.CYLINDER_INVENTORY_DTO + " WHERE ce.assignedUserId = :id ")
    Set<CylinderInventoryDto> getInventoryCylinderAssignedToUser(@Param("id") Long id);

    @Query(QUERIES.CYLINDER_INVENTORY_DTO + " WHERE ce.userEntity.id = :id ")
    Set<CylinderInventoryDto> getInventoryCylinderOwnedByUser(@Param("id") Long id);

    @Query("FROM CylinderEntity ce where ce.code = :code")
    CylinderEntity getCylinderDetailsByCode(@RequestParam("code") String code);

    class QUERIES {

        private static final String CYLINDER_INVENTORY_DTO = "SELECT new com.moderngas.pojo.admin.CylinderInventoryDto(ce.id, ce.code, ce.cylinderStatus, ue.id, ue.name, ce.assignedUserId , ce.assignedUserName , cid.isTransit, rc.id, rc.name, ce.identifier) FROM UserEntity ue " +
                " JOIN ue.cylinderEntitySet ce ON ue.id = ce.userEntity.id " +
                " LEFT JOIN ce.cylinderInventoryDetailsEntity cid" +
                " LEFT JOIN cid.resourceCentreEntity rc  ";

        private static final String FETCH_CYLINDER_BY_RESOURCE_CENTRE = CYLINDER_INVENTORY_DTO + "WHERE (ue.id = :adminId OR ce.assignedUserId = :adminId) ";

        private static final String FETCH_CYLINDER_BY_RESOURCE_CENTRE_COUNT = "SELECT COUNT(*) FROM UserEntity ue " +
                " JOIN ue.cylinderEntitySet ce ON ue.id = ce.userEntity.id " +
                " LEFT JOIN ce.cylinderInventoryDetailsEntity cid" +
                " LEFT JOIN cid.resourceCentreEntity rc  " +
                "WHERE (ue.id = :adminId OR ce.assignedUserId = :adminId) ";

        private static final String FETCH_CYLINDER_BY_RESOURCE_CENTRE_AND_SEARCH = " AND (:search IS NULL OR ce.code LIKE %:search% ) " +
                " AND rc.id IN :resourceCentreIds " +
                " AND (:cylinderStatus IS NULL OR ce.cylinderStatus = :cylinderStatus) ";



        private static final String FETCH_CYLINDER_BY_RESOURCE_CENTRE_AND_STATUS = FETCH_CYLINDER_BY_RESOURCE_CENTRE + FETCH_CYLINDER_BY_RESOURCE_CENTRE_AND_SEARCH;

        private static final String FETCH_CYLINDER_BY_RESOURCE_CENTRE_AND_STATUS_COUNT = FETCH_CYLINDER_BY_RESOURCE_CENTRE_COUNT  + FETCH_CYLINDER_BY_RESOURCE_CENTRE_AND_SEARCH;

    }

}
