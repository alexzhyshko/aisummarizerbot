package io.github.zhyshko.service;

import io.github.zhyshko.model.User;

import java.util.Optional;

public interface UserService {

    Optional<User> getUser(Long userId);

    User createUser(User user);

    boolean exists(Long userId);

}
