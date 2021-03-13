package com.moderngas.repository;

import com.moderngas.jpaentity.UserEntity;
import com.moderngas.pojo.user.UserSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
@Transactional
public interface UserRepo extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByMobileNumber(Long userName);

    @Query(value = UserRepo.QUERIES.ALL_USER_BY_NAME, countQuery = UserRepo.QUERIES.ALL_USER_BY_NAME_COUNT)
    Page<UserSearchDto> searchUserByName(Pageable pageable,
                                         @Param("name") String name);

    class QUERIES {

        private static final String ALL_USER_BY_NAME = "SELECT new com.moderngas.pojo.user.UserSearchDto(u.id, u.name, u.companyName) FROM UserEntity u WHERE u.name LIKE :name% ORDER BY u.name ASC ";

        private static final String ALL_USER_BY_NAME_COUNT = "SELECT COUNT(u.id) FROM UserEntity u WHERE u.name LIKE :name% ORDER BY u.name ASC ";

    }
}
