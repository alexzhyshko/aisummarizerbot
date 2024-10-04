package io.github.zhyshko.client;

import io.github.zhyshko.model.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "userClient", url = "${backend.url}")
@Component
public interface UserClient {

    @GetMapping(value = "/users/{userId}", consumes = "application/json")
    User getUser(@PathVariable Long userId);

    @PostMapping(value = "/users", produces = "application/json")
    User registerUser(@RequestBody User user);

    @PutMapping(value = "/users", produces = "application/json")
    User updateUser(@RequestBody User user);

}
