package com.yum.wiki.service;

import com.yum.wiki.mapper.EbookSnapshotMapperCust;
import com.yum.wiki.result.StatisticRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Yum
 * @version 1.0
 */
@Service
public class EbookSnapShotService {
    @Autowired
    private EbookSnapshotMapperCust ebookSnapshotMapperCust;

    public void genSnapshot() {
        ebookSnapshotMapperCust.genShapshot();
    }

    /**
     * 获取首页统计数值
     * @return
     */
    public List<StatisticRes> getStatistic() {
        return ebookSnapshotMapperCust.getStatistic();
    }

    /**
     * 30天
     *
     * @return
     */
    public List<StatisticRes> get30Statistic() {
        return ebookSnapshotMapperCust.get30Statistic();
    }
}
