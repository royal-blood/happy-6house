package com.royalblood.happy6house.repository.user;

import com.royalblood.happy6house.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataJpaUserRepository extends JpaRepository<User, Long>, UserRepository {
    @Override
    Optional<User> findByName(String name);
}
