package org.egglog.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableJpaAuditing
@EnableScheduling
@SpringBootApplication
@EnableJpaRepositories(basePackages = {
        "org.egglog.api.*.repository.jpa"
})
@EnableRedisRepositories(basePackages = {
        "org.egglog.api.*.repository.redis"
})
public class ApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }

}
