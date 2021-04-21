package com.royalblood.happy6house;

import com.royalblood.happy6house.repository.UserRepository;
import com.royalblood.happy6house.service.UserService;
import com.royalblood.happy6house.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {

    private final UserRepository userRepository;

    @Autowired
    public SpringConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    public UserService userService() {
        return new UserServiceImpl(userRepository);
    }

//    @Bean
//    public UserRepository userRepository() {
//        return new JdbcUserRepository(dataSource);
//    }

}