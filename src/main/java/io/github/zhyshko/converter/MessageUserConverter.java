package io.github.zhyshko.converter;

import io.github.zhyshko.model.User;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
public class MessageUserConverter implements Converter<CallbackQuery, User> {

    @Override
    public User convert(CallbackQuery source) {
        return User.builder()
                .id(source.getFrom().getId())
                .username(source.getFrom().getUserName())
                .firstName(source.getFrom().getFirstName())
                .lastName(source.getFrom().getLastName())
                .languageCode(source.getFrom().getLanguageCode())
                .build();
    }
}
