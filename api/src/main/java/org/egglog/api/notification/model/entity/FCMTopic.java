package org.egglog.api.notification.model.entity;


import lombok.Builder;

@Builder
public class FCMTopic {

    private TopicEnum topic;
    private Long topicId;

    public enum TopicEnum{
        group, board, comment, hospital
    }

    public String setTopic(){
        return topic.name()+'-'+topicId;
    }
}
