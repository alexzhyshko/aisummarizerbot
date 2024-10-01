package io.github.zhyshko.service.impl;

import io.github.zhyshko.dao.UserRepository;
import io.github.zhyshko.model.User;
import io.github.zhyshko.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DefaultUserService implements UserService {

    private UserRepository userRepository;

    @Override
    public Optional<User> getUser(Long userId) {
        return this.userRepository.findById(userId);
    }

    @Override
    public User saveUser(User user) {
        return this.userRepository.save(user);
    }

    @Override
    public boolean exists(Long userId) {
        return this.userRepository.existsById(userId);
    }


    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
