package com.lucas.train.batch.config;

import com.lucas.train.batch.job.QuartzTestJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfig {

    /**
     * 声明一个任务
     * @return
     */
    @Bean
    public JobDetail jobDetail() {
        return JobBuilder.newJob(QuartzTestJob.class)
                .withIdentity("TestJob", "test")
                .storeDurably()
                .build();
    }

    /**
     * 声明一个触发器，什么时候触发这个任务
     * @return
     */
    @Bean
    public Trigger trigger() {
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail())
                .withIdentity("trigger", "trigger")
                .startNow()
                .withSchedule(CronScheduleBuilder.cronSchedule("*/5 * * * * ?"))
                .build();
    }
}
