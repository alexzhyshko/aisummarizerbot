package io.github.zhyshko.routes.callback;

import io.github.zhyshko.core.annotation.Callback;
import io.github.zhyshko.core.annotation.CallbackMapping;
import io.github.zhyshko.core.annotation.ViewInitializer;
import io.github.zhyshko.core.constants.SessionConstants;
import io.github.zhyshko.core.i18n.impl.I18NLabelsWrapper;
import io.github.zhyshko.core.response.ResponseEntity;
import io.github.zhyshko.core.response.ResponseList;
import io.github.zhyshko.core.router.Route;
import io.github.zhyshko.core.util.UpdateWrapper;
import io.github.zhyshko.service.UserService;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.Locale;

@Component
@Callback
public class ChangeLanguageMenu implements Route {

    private UserService userService;

    @ViewInitializer
    public ResponseList initView(UpdateWrapper wrapper, I18NLabelsWrapper labelsWrapper) {

        EditMessageReplyMarkup editMessageReplyMarkup = EditMessageReplyMarkup.builder()
                .chatId(wrapper.getChatId())
                .messageId(wrapper.getMessageId())
                .replyMarkup(InlineKeyboardMarkup.builder()
                        .keyboardRow(new InlineKeyboardRow(prepareLanguageButtons(labelsWrapper))
                        )
                        .build()
                )
                .build();

        EditMessageText editMessageText = EditMessageText.builder()
                .text(labelsWrapper.getLabel("change.lang.menu.text"))
                .chatId(wrapper.getChatId())
                .messageId(wrapper.getMessageId())
                .build();


        return ResponseList.builder()
                .response(editMessageText)
                .response(editMessageReplyMarkup)
                .build();
    }

    @CallbackMapping("uk")
    public ResponseEntity handleUkLang(UpdateWrapper wrapper, I18NLabelsWrapper labelsWrapper) {
        StringBuilder responseTextBuilder = new StringBuilder();

        userService.getUser(wrapper.getUserId()).ifPresentOrElse(user -> {
            responseTextBuilder.append(
                    labelsWrapper.getLabel("change.lang.success.text",
                            ArrayUtils.toArray(labelsWrapper.getLabel("change.lang.name.uk"))));

            user.setLanguageCode("uk");
            userService.saveUser(user);

            wrapper.getSession().set(SessionConstants.SESSION_LOCALE_KEY, Locale.forLanguageTag("uk"));
        }, () -> responseTextBuilder.append(labelsWrapper.getLabel("change.lang.not.exists.text")));

        return ResponseEntity.builder()
                .response(AnswerCallbackQuery.builder()
                        .callbackQueryId(wrapper.getUpdate().getCallbackQuery().getId())
                        .text(responseTextBuilder.toString())
                        .build())
                .nextRoute(ProfileDetailsMenu.class)
                .build();
    }

    @CallbackMapping("en")
    public ResponseEntity handleEnLang(UpdateWrapper wrapper, I18NLabelsWrapper labelsWrapper) {
        StringBuilder responseTextBuilder = new StringBuilder();

        userService.getUser(wrapper.getUserId()).ifPresentOrElse(user -> {
            responseTextBuilder.append(
                    labelsWrapper.getLabel("change.lang.success.text",
                            ArrayUtils.toArray(labelsWrapper.getLabel("change.lang.name.en"))));

            user.setLanguageCode("en");
            userService.saveUser(user);

            wrapper.getSession().set(SessionConstants.SESSION_LOCALE_KEY, Locale.forLanguageTag("en"));
        }, () -> responseTextBuilder.append(labelsWrapper.getLabel("user.not.exists.text")));

        return ResponseEntity.builder()
                .response(AnswerCallbackQuery.builder()
                        .callbackQueryId(wrapper.getUpdate().getCallbackQuery().getId())
                        .text(responseTextBuilder.toString())
                        .build())
                .nextRoute(ProfileDetailsMenu.class)
                .build();
    }

    @CallbackMapping("back")
    public ResponseEntity handleBack() {
        return ResponseEntity.builder()
                .nextRoute(ProfileDetailsMenu.class)
                .build();
    }

    private InlineKeyboardButton[] prepareLanguageButtons(I18NLabelsWrapper labelsWrapper) {
        return ArrayUtils.toArray(
                InlineKeyboardButton.builder()
                        .text("Українська")
                        .callbackData("uk")
                        .build(),
                InlineKeyboardButton.builder()
                        .text("English")
                        .callbackData("en")
                        .build(),
                InlineKeyboardButton.builder()
                        .text(labelsWrapper.getLabel("menu.back.button"))
                        .callbackData("back")
                        .build()
        );
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
