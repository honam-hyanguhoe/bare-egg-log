package org.egglog.api.notification.model.entity.enums;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "deeplink")
public class DeepLinkConfig {
    private String base_uri;
    private String main;
    private String community;
    private String group;
    private String calendar;
}
