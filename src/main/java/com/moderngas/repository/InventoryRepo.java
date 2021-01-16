package com.moderngas.repository;


import com.moderngas.jpaentity.CylinderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface InventoryRepo extends JpaRepository<CylinderEntity, Long> {
}
