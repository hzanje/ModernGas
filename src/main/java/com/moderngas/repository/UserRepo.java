package com.moderngas.repository;

import com.moderngas.jpaentity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
@Transactional
public interface UserRepo extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByMobileNumber(Long userName);
}
