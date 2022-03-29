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
        LOG.info("更新知识库的数据开始");
        long startTime = System.currentTimeMillis();
        docService.updateEbookInfo();
        LOG.info("更新知识库数据结束，耗时：{} 毫秒",System.currentTimeMillis()  - startTime);
    }

}
