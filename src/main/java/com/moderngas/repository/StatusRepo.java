package com.moderngas.repository;

import com.moderngas.jpaentity.StatusMaster;
import com.moderngas.pojo.NameIdDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StatusRepo extends JpaRepository<StatusMaster, Long> {

    @Query("SELECT new com.moderngas.pojo.NameIdDto(id, name) FROM StatusMaster WHERE activeFlag = 1 ORDER BY sequence ASC")
    List<NameIdDto> getAllActiveStatus();

    @Query(" FROM StatusMaster WHERE id = :id")
    StatusMaster getStatusById(@Param("id") Long id);
}
