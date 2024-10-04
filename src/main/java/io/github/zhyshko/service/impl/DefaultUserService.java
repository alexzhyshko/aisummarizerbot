package io.github.zhyshko.service.impl;

import feign.FeignException;
import io.github.zhyshko.client.UserClient;
import io.github.zhyshko.model.User;
import io.github.zhyshko.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DefaultUserService implements UserService {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultUserService.class);

    private UserClient userClient;

    @Override
    public Optional<User> getUser(Long userId) {
        try {
            return Optional.of(this.userClient.getUser(userId));
        }catch (FeignException.NotFound nfe){
            LOG.warn("User by id {} not found", userId);
            return Optional.empty();
        }
    }

    @Override
    public User registerUser(User user) {
        return this.userClient.registerUser(user);
    }

    @Override
    public User saveUser(User user) {
        return this.userClient.updateUser(user);
    }

    @Autowired
    public void setUserClient(UserClient userClient) {
        this.userClient = userClient;
    }
}
