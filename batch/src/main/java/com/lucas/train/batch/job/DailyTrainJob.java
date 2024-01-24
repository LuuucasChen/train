package com.lucas.train.batch.job;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.lucas.common.resp.CommonResp;
import com.lucas.train.batch.feign.BusinessFeign;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

@DisallowConcurrentExecution
public class DailyTrainJob implements Job {

    private static Logger LOG = LoggerFactory.getLogger(DailyTrainJob.class);

    @Autowired
    private BusinessFeign businessFeign;
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        MDC.put("LOG_ID", System.currentTimeMillis() + RandomUtil.randomString(3));
        LOG.info("生成15天后车次数据开始");
        Date date = new Date();
        DateTime dateTime = DateUtil.offsetDay(date, 15);
        Date jdkDate = dateTime.toJdkDate();
        CommonResp<Object> commonResp = businessFeign.genDaily(jdkDate);
        LOG.info("生成15天后车次数据结束,结果{}",commonResp);

    }
}
