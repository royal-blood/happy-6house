package com.royalblood.happy6house.service;


import com.royalblood.happy6house.domain.User;
import com.royalblood.happy6house.exception.NotFoundException;
import com.royalblood.happy6house.repository.user.UserRepository;
import com.royalblood.happy6house.service.dto.UserCreateDto;
import com.royalblood.happy6house.service.dto.UserDto;
import com.royalblood.happy6house.service.dto.UserUpdateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
//@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired private final UserRepository userRepository;
    public JwtUserDetailsService(UserRepository userRepository) {
            this.userRepository = userRepository;
    }

    /**
     * Spring Security 필수 메소드 구현
     *
     * @param email 이메일
     * @return UserDetails
     * @throws UsernameNotFoundException 유저가 없을 때 예외 발생
     */
    @Override // 기본적인 반환 타입은 UserDetails, UserDetails를 상속받은 UserInfo로 반환 타입 지정 (자동으로 다운 캐스팅됨)
    // 시큐리티에서 지정한 서비스이기 때문에 이 메소드를 필수로 구현
    public User loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));
    }

    /**
     * 회원가입
     * @param createDto 가일할 회원 정보가 들어있는 DTO
     * @return 저장되는 회원의 PK
     */
    @Transactional
    public Long join(UserCreateDto createDto) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        validateDuplicateMember(createDto);

        String encrypted = encoder.encode(createDto.getPassword());
        createDto.setPassword(encrypted);
        createDto.setAuth("ROLE_USER");

        return userRepository.save(createDto.toEntity())
                .getId();
    }

    private void validateDuplicateMember(UserCreateDto create) {
        userRepository.findByEmail(create.getEmail())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
    }

    public List<UserDto> findUsers() {
        return userRepository.findAll().stream()
                .map(UserDto::of)
                .collect(Collectors.toList());
    }

    public UserDto findById(Long userId) {
        return userRepository.findById(userId)
                .map(UserDto::of)
                .orElseThrow(NotFoundException::new);
    }

    public void delete(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(NotFoundException::new);

        userRepository.delete(id);
    }

    @Transactional
    public UserDto update(Long id, UserUpdateDto updateDto) {
        User user = userRepository.findById(id)
                .orElseThrow(NotFoundException::new);

        updateDto.apply(user);

        return UserDto.of(user);
    }
}