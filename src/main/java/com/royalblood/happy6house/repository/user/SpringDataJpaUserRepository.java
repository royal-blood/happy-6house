package com.royalblood.happy6house.repository.user;

import com.royalblood.happy6house.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface SpringDataJpaUserRepository extends JpaRepository<User, Long>, UserRepository {
    @Override
    @Query("from User u where u.enabled = true and u.id = :id")
    Optional<User> findById(@Param("id") Long id);

    @Override
    @Query("from User u where u.enabled = true and u.name = :name")
    Optional<User> findByName(@Param("name") String name);

    @Override
    @Query("from User u where u.enabled = true and u.email = :email")
    Optional<User> findByEmail(@Param("email") String email);

    // 변화를 주는 query는 @Modifying과 @Transactional을 선언해주어야 한다.
    @Transactional
    @Modifying
    @Override
    @Query("update User u set u.enabled = false where u.id = :id")
    void delete(@Param("id") Long id);
}
