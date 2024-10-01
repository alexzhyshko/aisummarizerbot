package io.github.zhyshko.routes.callback;

import io.github.zhyshko.core.annotation.Callback;
import io.github.zhyshko.core.annotation.CallbackMapping;
import io.github.zhyshko.core.annotation.ViewInitializer;
import io.github.zhyshko.core.i18n.impl.I18NLabelsWrapper;
import io.github.zhyshko.core.response.ResponseEntity;
import io.github.zhyshko.core.response.ResponseList;
import io.github.zhyshko.core.router.Route;
import io.github.zhyshko.core.util.UpdateWrapper;
import io.github.zhyshko.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;


@Component
@Callback
public class ProfileDetailsMenu implements Route {

    private UserService userService;

    @ViewInitializer
    public ResponseList initView(UpdateWrapper wrapper, I18NLabelsWrapper labelsWrapper) {

        StringBuilder userDetailsTextBuilder = new StringBuilder();
        this.userService.getUser(wrapper.getUserId()).ifPresentOrElse(user -> {
            userDetailsTextBuilder.append(labelsWrapper.getLabel("user.profile.username.label", new String[]{user.getUsername()}));
            userDetailsTextBuilder.append(System.lineSeparator());
            userDetailsTextBuilder.append(labelsWrapper.getLabel("user.profile.firstName.label", new String[]{user.getFirstName()}));
            userDetailsTextBuilder.append(System.lineSeparator());
            if(user.getLastName() != null) {
                userDetailsTextBuilder.append(labelsWrapper.getLabel("user.profile.lastName.label", new String[]{user.getLastName()}));
                userDetailsTextBuilder.append(System.lineSeparator());
            }
            userDetailsTextBuilder.append(labelsWrapper.getLabel("user.profile.language.label", new String[]{user.getLanguageCode()}));
        }, () -> userDetailsTextBuilder.append(labelsWrapper.getLabel("user.not.exists.text")));

        EditMessageReplyMarkup editMessageReplyMarkup = EditMessageReplyMarkup.builder()
                .chatId(wrapper.getChatId())
                .messageId(wrapper.getMessageId())
                .replyMarkup(InlineKeyboardMarkup.builder()
                        .keyboardRow(new InlineKeyboardRow(
                                        InlineKeyboardButton.builder()
                                                .text(labelsWrapper.getLabel("profile.details.change.lang.button.text"))
                                                .callbackData("change_lang")
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
                .text(userDetailsTextBuilder.toString())
                .chatId(wrapper.getChatId())
                .messageId(wrapper.getMessageId())
                .build();


        return ResponseList.builder()
                .response(editMessageText)
                .response(editMessageReplyMarkup)
                .build();
    }

    @CallbackMapping("change_lang")
    public ResponseEntity handleChangeLang() {
        return ResponseEntity.builder()
                .nextRoute(ChangeLanguageMenu.class)
                .build();
    }

    @CallbackMapping("back")
    public ResponseEntity handleBack() {
        return ResponseEntity.builder()
                .nextRoute(ProfileMenu.class)
                .build();
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
