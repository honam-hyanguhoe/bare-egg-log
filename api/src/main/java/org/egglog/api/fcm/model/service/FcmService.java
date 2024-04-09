package org.egglog.api.fcm.model.service;

public interface FcmService {
    void subscribeMyTokens(Long userId, Long groupId);
    void sendMessageByTopic(String title, String body, String topicName);
    void sendMessageByToken(String title, String body, String token);
    void sendMessageAll(String title, String body);
    void subscribeByTopic(String token, String topicName);
}
