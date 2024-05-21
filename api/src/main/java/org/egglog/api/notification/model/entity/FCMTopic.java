package org.egglog.api.notification.model.entity;


import lombok.Builder;
import org.egglog.api.notification.model.entity.enums.TopicEnum;

@Builder
public class FCMTopic {

    private TopicEnum topic;
    private Long topicId;

    public String getTopic(){
        return topic.name()+'-'+topicId;
    }
}
