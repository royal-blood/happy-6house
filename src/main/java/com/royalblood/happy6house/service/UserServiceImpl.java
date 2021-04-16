package com.royalblood.happy6house.service;

import com.royalblood.happy6house.domain.User;
import com.royalblood.happy6house.repository.UserRepository;

import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * 회원가입
     */
    public Long join(User user) {
        // 같은 이름이 있는 중복 회원 X
        validateDuplicateMember(user); // 중복 회원 검증

        userRepository.save(user);
        return user.getId();
    }

    private void validateDuplicateMember(User user) {
        userRepository.findByName(user.getName())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
    }

    /**
     * 전체 회원 조회
     */
    public List<User> findUsers() {
        return userRepository.findAll();
    }

    /**
     * 특정 회원 조회
     */
    public Optional<User> findOne(Long userId) {
        return userRepository.findById(userId);
    }
}
