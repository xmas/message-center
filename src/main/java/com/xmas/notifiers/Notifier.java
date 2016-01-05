package com.xmas.notifiers;

import com.xmas.entity.push.Message;

import java.util.List;

public interface Notifier {

    void pushMessage(Message message, List<String> tokens);
}
