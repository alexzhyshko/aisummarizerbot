package io.github.zhyshko.routes.message;

import io.github.zhyshko.constants.CustomSessionConstants;
import io.github.zhyshko.core.annotation.Callback;
import io.github.zhyshko.core.annotation.CallbackMapping;
import io.github.zhyshko.core.annotation.MessageMapping;
import io.github.zhyshko.core.annotation.ViewInitializer;
import io.github.zhyshko.core.i18n.impl.I18NLabelsWrapper;
import io.github.zhyshko.core.response.ResponseEntity;
import io.github.zhyshko.core.response.ResponseList;
import io.github.zhyshko.core.router.Route;
import io.github.zhyshko.core.util.UpdateWrapper;
import io.github.zhyshko.model.User;
import io.github.zhyshko.routes.callback.MainMenu;
import io.github.zhyshko.service.UserService;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;


@Component
@Callback
public class AuthenticationRoute implements Route {

    private UserService userService;
    private Converter<CallbackQuery, User> callbackQueryUserConverter;

    @ViewInitializer
    public ResponseList initView(UpdateWrapper wrapper, I18NLabelsWrapper labelsWrapper) {
        return ResponseList.builder()
                .response(SendMessage.builder()
                        .chatId(wrapper.getChatId())
                        .text(labelsWrapper.getLabel("registration.text"))
                        .replyMarkup(InlineKeyboardMarkup.builder()
                                .keyboardRow(new InlineKeyboardRow(
                                        InlineKeyboardButton.builder()
                                                .text(labelsWrapper.getLabel("registration.button"))
                                                .callbackData("authenticate")
                                                .build()
                                ))
                                .build())
                        .build())
                .build();
    }

    @CallbackMapping("authenticate")
    public ResponseEntity handleGreeting(UpdateWrapper wrapper, I18NLabelsWrapper labelsWrapper) {

        System.out.println(wrapper.getSession().get("key"));

        ResponseEntity.ResponseEntityBuilder responseEntityBuilder = ResponseEntity.builder();


        AnswerCallbackQuery.AnswerCallbackQueryBuilder answerCallbackQuery = AnswerCallbackQuery.builder()
                .callbackQueryId(wrapper.getUpdate().getCallbackQuery().getId())
                .showAlert(true);

        this.userService.getUser(wrapper.getUserId())
                .ifPresentOrElse(user -> {
                            answerCallbackQuery.text(labelsWrapper.getLabel("welcome.back.text", ArrayUtils.toArray(user.getUsername())));
                            wrapper.getSession().set(CustomSessionConstants.USERNAME_KEY, user.getUsername());
                            wrapper.getSession().set(CustomSessionConstants.ROLE_KEY, user.getUserRole());
                        },
                        () -> {
                            var callback = wrapper.getUpdate().getCallbackQuery();
                            User user = callbackQueryUserConverter.convert(callback);
                            user = userService.createUser(user);
                            answerCallbackQuery.text(labelsWrapper.getLabel("registration.successful.button", ArrayUtils.toArray(user.getUsername())));
                            wrapper.getSession().set(CustomSessionConstants.USERNAME_KEY, user.getUsername());
                            wrapper.getSession().set(CustomSessionConstants.ROLE_KEY, user.getUserRole());
                        });

        return responseEntityBuilder
                .response(answerCallbackQuery.build())
                .nextRoute(MainMenu.class)
                .build();
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setMessageUserConverter(Converter<CallbackQuery, User> callbackQueryUserConverter) {
        this.callbackQueryUserConverter = callbackQueryUserConverter;
    }
}
