package com.lucas.train.batch.job;

import cn.hutool.core.util.RandomUtil;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

@DisallowConcurrentExecution
public class DailyTrainJob implements Job {

    private static Logger LOG = LoggerFactory.getLogger(DailyTrainJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        MDC.put("LOG_ID", System.currentTimeMillis() + RandomUtil.randomString(3));
        LOG.info("生成每日车次数据开始");
        LOG.info("生成每日车次数据结束");

    }
}
