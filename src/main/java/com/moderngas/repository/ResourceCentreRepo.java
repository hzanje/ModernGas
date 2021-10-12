package com.moderngas.repository;

import com.moderngas.jpaentity.ResourceCentreEntity;
import com.moderngas.pojo.admin.ResourceCentreDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface ResourceCentreRepo extends JpaRepository<ResourceCentreEntity, Long> {

    @Query("SELECT new com.moderngas.pojo.admin.ResourceCentreDto(rc.id, rc.name, rc.alias) FROM UserEntity u INNER JOIN u.resourceCentreEntitySet rc WHERE u.id = :id ")
    List<ResourceCentreDto> getResourceCentreByAdminId(@Param("id") Long id);
}
