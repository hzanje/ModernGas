package com.moderngas.repository;

import com.moderngas.jpaentity.AnonymousCylinderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface AnonymousCylinderRepo extends JpaRepository<AnonymousCylinderEntity, Long> {

    @Query("FROM AnonymousCylinderEntity ac WHERE ac.code IN :codes")
    List<AnonymousCylinderEntity> getAllAnonymousCylinderById(@Param("codes") List<String> codes);
}
