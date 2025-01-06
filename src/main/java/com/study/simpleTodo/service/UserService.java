package com.study.simpleTodo.service;

import com.study.simpleTodo.model.UserEntity;
import com.study.simpleTodo.persistence.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserEntity create(final UserEntity userEntity) {
        if(userEntity == null || userEntity.getUsername() == null) {
            throw new RuntimeException("userEntity is null or empty");
        }
        final String username = userEntity.getUsername();
        if(userRepository.existsByUsername(username)) {
            log.warn("Username already exists", username);
            throw new RuntimeException("Username already exists");
        }
        return userRepository.save(userEntity);
    }

    /**
     * 기존코드
     */
//    public UserEntity getByCredentials(final String username, final String password) {
//        return userRepository.findByUsernameAndPassword(username, password);
//    }

    public UserEntity getByCredentials(final String username, final String password, final PasswordEncoder encoder) {
        final UserEntity userEntity = userRepository.findByUsername(username);

        // matches를 이용해 패스워드가 같은지 확인
        if(userEntity != null && encoder.matches(password, userEntity.getPassword())) {
            return userEntity;
        }
        return null;
    }
}
