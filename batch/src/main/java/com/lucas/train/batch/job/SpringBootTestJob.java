package com.lucas.train.batch.job;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class SpringBootTestJob {

//    @Scheduled(cron = "0/5 * * * * ?")
    private void test() {
        System.out.println("Spring Job Test");
    }
}
