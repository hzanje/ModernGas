package com.moderngas.repository;


import com.moderngas.jpaentity.CategoryMaster;
import com.moderngas.jpaentity.GasMaster;
import com.moderngas.pojo.NameIdDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface GasRepo extends JpaRepository<GasMaster,Long> {

    @Query(" FROM CategoryMaster")
    List<CategoryMaster> getAllCategory();

    GasMaster getGasMasterByNameEquals(String name);

    @Query("SELECT new com.moderngas.pojo.NameIdDto(gm.id, gm.name) FROM GasMaster gm WHERE gm.categoryMaster.id = :categoryId AND activeFlag = 1")
    List<NameIdDto> getGasMasterByCategoryId(@Param("categoryId") Long categoryId);

    @Query("FROM GasMaster gm WHERE gm.id IN (:gasIds)")
    List<GasMaster> getGasMasterByIdList(@Param("gasIds") List<Long> gasIds);

    @Modifying
    @Query("SELECT new com.moderngas.pojo.NameIdDto(gm.id, gm.name) FROM GasMaster gm")
    List<NameIdDto> getGasMasterNameIdList();
}
