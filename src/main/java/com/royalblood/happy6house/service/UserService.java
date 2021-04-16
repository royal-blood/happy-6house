package com.royalblood.happy6house.service;

import com.royalblood.happy6house.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    // 회원가입
    Long join(User user);
    // 전체 회원 조회
    List<User> findUsers();
    // 특정 회원 조회
    Optional<User> findOne(Long userId);
}
