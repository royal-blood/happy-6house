package com.royalblood.happy6house.config.auth;

import com.royalblood.happy6house.domain.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor
@EnableWebSecurity // security 설정 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .headers().frameOptions().disable()
            .and()
                // URL별 권한 관리를 설정하는 옵션의 시작점 - antMatchers(권한 관리 대상 지정 옵션) 사용 가능해짐
                .authorizeRequests()
                .antMatchers("/", "/css/**", "/images/**",
                        "/js/**", "/h2-console/**", "/users/new").permitAll()
                //.antMatchers("/api/v1/**").hasRole(Role.USER.name())
                // 설정된 값 외의 요청
                .anyRequest().authenticated()
            .and()
                // 로그아웃 기능에 대한 여러 설정의 진입점
                .logout().logoutSuccessUrl("/")
            .and()
                // OAuth 2 로그인 기능에 대한 여러 설정의 진입점
                .oauth2Login()
                    // OAuth 2 로그인 성공 후 사용자 정보를 가져올 때의 설정들 담당
                    .userInfoEndpoint()
                        // 리소스 서버(social 서비스)에서 사용자 정보를 가져온 후 추가로 진행하고자 하는 기능 명시
                        .userService(customOAuth2UserService);
    }
}
