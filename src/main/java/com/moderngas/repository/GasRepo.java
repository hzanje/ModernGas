package com.moderngas.repository;


import com.moderngas.jpaentity.CategoryMaster;
import com.moderngas.jpaentity.GasMaster;
import com.moderngas.jpaentity.StatusMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GasRepo extends JpaRepository<GasMaster,Long> {

    @Query(" FROM CategoryMaster")
    List<CategoryMaster> getAllCategory();

    GasMaster getGasMasterByNameEquals(String name);

    List<GasMaster> getGasMasterByCategoryMaster_Id(Long id);

    @Query(" FROM StatusMaster WHERE id = :id")
    StatusMaster getStatusById(@Param("id") Long id);
}
