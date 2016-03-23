package com.xmas.service.notifiers;

import com.xmas.entity.Message;

import java.util.List;

public interface Notifier {

    void pushMessage(Message message, List<String> tokens);
}
