package com.moderngas.repository;

import com.moderngas.jpaentity.AdminGasMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface AdminGasMappingRepo extends JpaRepository<AdminGasMapping, Long> {

    @Query("SELECT agm FROM UserEntity u INNER JOIN u.adminGasMappings agm WHERE u.id = :adminId AND agm.gasId = :id")
    Optional<AdminGasMapping> getGasMappingByAdminId(@Param("id") Long id,
                                                     @Param("adminId") Long adminId);

    @Query("SELECT agm FROM UserEntity u INNER JOIN u.adminGasMappings agm WHERE u.id = :id")
    List<AdminGasMapping> getAllGasMappingByAdminId(@Param("id") Long id);

    @Query("SELECT agm FROM UserEntity u INNER JOIN u.adminGasMappings agm WHERE u.id = :adminId AND agm.categoryId = :id")
    List<AdminGasMapping> getGasMappingListByCategoryId(@Param("id") Long categoryId,
                                                        @Param("adminId") Long adminId);

    @Query("SELECT agm FROM UserEntity u INNER JOIN u.adminGasMappings agm WHERE u.id = :adminId ORDER BY agm.gasName ASC")
    List<AdminGasMapping> getGasMappingList(@Param("adminId") Long adminId);

    @Query("SELECT agm FROM UserEntity u INNER JOIN u.adminGasMappings agm where agm.gasId = :gasId AND u.id = :adminId ")
    AdminGasMapping getGasMappingByGasIdAndAdminId(@Param("gasId") Long gasId,
                                                   @Param("adminId") Long adminId);
}
