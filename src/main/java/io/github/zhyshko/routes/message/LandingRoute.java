package io.github.zhyshko.routes.message;

import io.github.zhyshko.constants.SessionConstants;
import io.github.zhyshko.core.annotation.Entrypoint;
import io.github.zhyshko.core.annotation.Message;
import io.github.zhyshko.core.annotation.MessageMapping;
import io.github.zhyshko.core.response.ResponseEntity;
import io.github.zhyshko.core.router.Route;
import io.github.zhyshko.core.service.SessionService;
import io.github.zhyshko.core.util.UpdateWrapper;
import io.github.zhyshko.model.User;
import io.github.zhyshko.model.UserRole;
import io.github.zhyshko.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;


@Component
@Entrypoint
@Message
public class LandingRoute implements Route {

    private UserService userService;
    private SessionService sessionService;

    @Override
    public void initView(SendMessage.SendMessageBuilder sendMessageBuilder) {

    }

    @MessageMapping
    public ResponseEntity handleGreeting(UpdateWrapper wrapper) {

        ResponseEntity.ResponseEntityBuilder responseEntityBuilder = ResponseEntity.builder();


        SendMessage.SendMessageBuilder sendMessageBuilder = SendMessage.builder()
                .chatId(wrapper.getChatId());

        this.userService.getUser(wrapper.getUserId())
                .ifPresentOrElse(user -> {
                            sendMessageBuilder.text(String.format("Welcome back, %s", user.getUsername()));
                            sessionService.set(wrapper, SessionConstants.USERNAME_KEY, user.getUsername());
                            sessionService.set(wrapper, SessionConstants.ROLE_KEY, user.getUserRole());
                        },
                        () -> {
                            var message = wrapper.getUpdate().getMessage();

                            User user = User.builder()
                                    .id(message.getFrom().getId())
                                    .username(message.getFrom().getUserName())
                                    .firstName(message.getFrom().getFirstName())
                                    .lastName(message.getFrom().getLastName())
                                    .languageCode(message.getFrom().getLanguageCode())
                                    .userRole(UserRole.USER)
                                    .build();

                            user = userService.createUser(user);

                            sendMessageBuilder.text(String.format("Registration successful, %s", user.getUsername()));

                            sessionService.set(wrapper, SessionConstants.USERNAME_KEY, user.getUsername());
                            sessionService.set(wrapper, SessionConstants.ROLE_KEY, user.getUserRole());
                        });

        return responseEntityBuilder
                .response(DeleteMessage.builder()
                        .chatId(wrapper.getChatId())
                        .messageId(wrapper.getMessageId())
                        .build())
                .response(sendMessageBuilder.build())
                .nextRoute(MenuRoute.class)
                .build();
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setSessionService(SessionService sessionService) {
        this.sessionService = sessionService;
    }
}
