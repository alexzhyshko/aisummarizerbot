package io.github.zhyshko.routes.message;

import io.github.zhyshko.core.annotation.Entrypoint;
import io.github.zhyshko.core.annotation.Message;
import io.github.zhyshko.core.annotation.MessageMapping;
import io.github.zhyshko.core.response.ResponseEntity;
import io.github.zhyshko.core.router.Route;
import io.github.zhyshko.core.util.UpdateWrapper;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;


@Component
@Entrypoint
@Message
public class GreetingRoute implements Route {

    @MessageMapping
    public ResponseEntity handleGreeting(UpdateWrapper wrapper) {
        SendMessage sendMessage = SendMessage.builder()
                .chatId(wrapper.getChatId())
                .text("Hellow World!")
                .build();

        return ResponseEntity.builder()
                .response(sendMessage)
                .build();
    }


}
