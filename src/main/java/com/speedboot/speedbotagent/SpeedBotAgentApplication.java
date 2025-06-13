package com.speedboot.speedbotagent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class SpeedBotAgentApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpeedBotAgentApplication.class, args);
    }

}
