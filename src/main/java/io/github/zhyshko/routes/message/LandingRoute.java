package io.github.zhyshko.routes.message;

import io.github.zhyshko.core.annotation.Entrypoint;
import io.github.zhyshko.core.annotation.Message;
import io.github.zhyshko.core.annotation.MessageMapping;
import io.github.zhyshko.core.response.ResponseEntity;
import io.github.zhyshko.core.router.Route;
import io.github.zhyshko.core.util.UpdateWrapper;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;


@Component
@Entrypoint
@Message
public class LandingRoute implements Route {

    @MessageMapping
    public ResponseEntity handleGreeting(UpdateWrapper wrapper) {

        DeleteMessage deleteMessage = DeleteMessage.builder()
                .messageId(wrapper.getMessageId())
                .chatId(wrapper.getChatId())
                .build();

        wrapper.getSession().set("key", "val");

        System.out.println(wrapper.getSession().get("key"));

       return ResponseEntity.builder()
               .response(deleteMessage)
               .nextRoute(AuthenticationRoute.class)
               .build();

    }
}
