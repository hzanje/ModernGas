package com.moderngas.repository;

import com.moderngas.jpaentity.AnonymousCylinderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface AnonymousCylinderRepo extends JpaRepository<AnonymousCylinderEntity, Long> {
}
