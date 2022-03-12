package com.moderngas.repository;

import com.moderngas.jpaentity.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface AddressRepo extends JpaRepository<AddressEntity, Long> {
}
