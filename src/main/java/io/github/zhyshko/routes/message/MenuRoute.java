package io.github.zhyshko.routes.message;

import com.vdurmont.emoji.EmojiParser;
import io.github.zhyshko.core.annotation.Message;
import io.github.zhyshko.core.router.Route;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

@Component
@Message
public class MenuRoute implements Route {

    @Override
    public void initView(SendMessage.SendMessageBuilder builder) {
        builder
                .text("Main Menu\n" +
                        "You can check your available balance in Profile or proceed with " + EmojiParser.parseToUnicode(":rocket:") + "AI features in Summarize")
                .replyMarkup(ReplyKeyboardMarkup.builder()
                        .oneTimeKeyboard(true)
                        .keyboardRow(new KeyboardRow(
                                        KeyboardButton.builder()
                                                .text("Profile")
                                                .build(),
                                        KeyboardButton.builder()
                                                .text("Summarize")
                                                .build()
                                )
                        )
                        .build()
                );
    }

}
