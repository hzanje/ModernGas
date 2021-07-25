package com.moderngas.repository;

import com.moderngas.jpaentity.UserEntity;
import com.moderngas.pojo.admin.UserDetails;
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

    @Query("SELECT new com.moderngas.pojo.admin.UserDetails(u.id, u.activeFlag, u.name, u.mobileNumber, u.email, u.companyName) " +
            "FROM UserEntity u WHERE u.id = :id")
    UserDetails getUserDetailsForAdmin(@Param("id") Long id);

    @Query("SELECT CASE  WHEN count(u)> 0 THEN true ELSE false END FROM UserEntity u INNER JOIN u.userTokenSet ut " +
            " WHERE ut.token = :token")
    boolean isTokenExist(@Param("token") String token);

    @Query("FROM UserEntity u INNER JOIN u.userTokenSet ut WHERE ut.token = :token")
    UserEntity getUserDetailsByToken(@Param("token") String existingToken);

    class QUERIES {

        private QUERIES() {
        }

        private static final String ALL_USER_BY_NAME = "SELECT new com.moderngas.pojo.user.UserSearchDto(u.id, u.name, u.companyName) FROM UserEntity u WHERE u.name LIKE :name% ORDER BY u.name ASC ";

        private static final String ALL_USER_BY_NAME_COUNT = "SELECT COUNT(u.id) FROM UserEntity u WHERE u.name LIKE :name% ORDER BY u.name ASC ";

    }
}
