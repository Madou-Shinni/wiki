package com.yum.wiki.job;

import com.yum.wiki.service.EbookSnapShotService;
import com.yum.wiki.utils.SnowFlakeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class EbookSnapShotJob {

    private static final Logger LOG = LoggerFactory.getLogger(EbookSnapShotJob.class);

    @Autowired
    private EbookSnapShotService ebookSnapShotService;
    @Autowired
    private SnowFlakeUtil snowFlakeUtil;


    @Scheduled(cron = "0/5 * * * * ? ")
    public void cron() {
        // 增加日志流水号
        MDC.put("LOG_ID", String.valueOf(snowFlakeUtil.nextId()));
        LOG.info("生成今日知识库快照开始");
        long startTime = System.currentTimeMillis();
        ebookSnapShotService.genSnapshot();
        LOG.info("今日知识库快照结束，耗时：{} 毫秒",System.currentTimeMillis()  - startTime);
    }

}
