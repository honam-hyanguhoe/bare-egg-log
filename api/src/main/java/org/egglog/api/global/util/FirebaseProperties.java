package org.egglog.api.global.util;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
@Getter
@ToString
@ConfigurationProperties(prefix = "fcm")
public class FirebaseProperties {
    private Map<String, String> config;

    public void setConfig(Map<String, String> config){
        this.config = config;
        this.loadPrivateKey(config.get("private_key"));
    }
    public void loadPrivateKey(String path){
        Resource resource = new ClassPathResource(path);
        try (InputStream is = resource.getInputStream()) {
            // 리소스 파일 사용
            String content = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            this.config.put("private_key",content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
