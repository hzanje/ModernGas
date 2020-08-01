package com.moderngas.repository;


import com.moderngas.jpaentity.CategoryMaster;
import com.moderngas.jpaentity.GasMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GasRepo extends JpaRepository<GasMaster,Long> {

    @Query(" FROM CategoryMaster")
    public List<CategoryMaster> getAllCategory();

    public GasMaster getGasMasterByNameEquals(String name);
}
