package io.github.zhyshko.routes.callback;

import io.github.zhyshko.core.annotation.Callback;
import io.github.zhyshko.core.annotation.CallbackMapping;
import io.github.zhyshko.core.annotation.ViewInitializer;
import io.github.zhyshko.core.i18n.impl.I18NLabelsWrapper;
import io.github.zhyshko.core.response.ResponseEntity;
import io.github.zhyshko.core.response.ResponseList;
import io.github.zhyshko.core.router.Route;
import io.github.zhyshko.core.util.UpdateWrapper;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

@Component
@Callback
public class ProfileMenu implements Route {

    @ViewInitializer
    public ResponseList initView(UpdateWrapper wrapper, I18NLabelsWrapper labelsWrapper) {
        EditMessageReplyMarkup editMessageReplyMarkup = EditMessageReplyMarkup.builder()
                .chatId(wrapper.getChatId())
                .messageId(wrapper.getMessageId())
                .replyMarkup(InlineKeyboardMarkup.builder()
                        .keyboardRow(new InlineKeyboardRow(
                                        InlineKeyboardButton.builder()
                                                .text(labelsWrapper.getLabel("profile.token.balance.button.text"))
                                                .callbackData("token_balance")
                                                .build(),
                                        InlineKeyboardButton.builder()
                                                .text(labelsWrapper.getLabel("profile.profile.details.button.text"))
                                                .callbackData("profile_details")
                                                .build(),
                                        InlineKeyboardButton.builder()
                                                .text(labelsWrapper.getLabel("menu.back.button"))
                                                .callbackData("back")
                                                .build()
                                )
                        )
                        .build()
                )
                .build();

        EditMessageText editMessageText = EditMessageText.builder()
                .text(labelsWrapper.getLabel("profile.menu.text"))
                .chatId(wrapper.getChatId())
                .messageId(wrapper.getMessageId())
                .build();


        return ResponseList.builder()
                .response(editMessageText)
                .response(editMessageReplyMarkup)
                .build();
    }

    @CallbackMapping("token_balance")
    public ResponseEntity handleTokenBalance() {

        return ResponseEntity.builder()
//                .nextRoute(TokenBalanceMenu.class)
                .build();
    }

    @CallbackMapping("profile_details")
    public ResponseEntity handleProfileDetails() {
        return ResponseEntity.builder()
//                .nextRoute(ProfileDetailsMenu.class)
                .build();
    }

    @CallbackMapping("back")
    public ResponseEntity handleBack() {
        return ResponseEntity.builder()
                .nextRoute(MainMenu.class)
                .build();
    }

}
