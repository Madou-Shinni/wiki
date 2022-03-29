package com.yum.wiki.job;

import com.yum.wiki.service.DocService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class DocJob {

    private static final Logger LOG = LoggerFactory.getLogger(DocJob.class);

    @Autowired
    private DocService docService;

    /**
     * 从5秒开始每35秒的时候触发
     *
     */
    @Scheduled(cron = "5/30 * * * * ? ")
    public void cron() {
        LOG.info("定时任务执行：{}",System.currentTimeMillis());
        docService.updateEbookInfo();
    }

}
