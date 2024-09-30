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
public class SummarizeMenu implements Route {

    @ViewInitializer
    public ResponseList initView(UpdateWrapper wrapper, I18NLabelsWrapper labelsWrapper) {
        EditMessageReplyMarkup editMessageReplyMarkup = EditMessageReplyMarkup.builder()
                .chatId(wrapper.getChatId())
                .messageId(wrapper.getMessageId())
                .replyMarkup(InlineKeyboardMarkup.builder()
                        .keyboardRow(new InlineKeyboardRow(
                                        InlineKeyboardButton.builder()
                                                .text(labelsWrapper.getLabel("summarize.new.upload.button"))
                                                .callbackData("new_upload")
                                                .build(),
                                        InlineKeyboardButton.builder()
                                                .text(labelsWrapper.getLabel("summarize.upload.history.button"))
                                                .callbackData("upload_history")
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
                .text(labelsWrapper.getLabel("summarize.menu.text"))
                .chatId(wrapper.getChatId())
                .messageId(wrapper.getMessageId())
                .build();


        return ResponseList.builder()
                .response(editMessageText)
                .response(editMessageReplyMarkup)
                .build();
    }

    @CallbackMapping("new_upload")
    public ResponseEntity handleNewUpload(UpdateWrapper wrapper, I18NLabelsWrapper i18NLabelsWrapper) {

        return ResponseEntity.builder()
//                .nextRoute(NewUploadMenu.class)
                .build();
    }

    @CallbackMapping("upload_history")
    public ResponseEntity handleUploadHistory() {
        return ResponseEntity.builder()
//                .nextRoute(UploadHistoryMenu.class)
                .build();
    }

    @CallbackMapping("back")
    public ResponseEntity handleBack() {
        return ResponseEntity.builder()
                .nextRoute(MainMenu.class)
                .build();
    }

}
