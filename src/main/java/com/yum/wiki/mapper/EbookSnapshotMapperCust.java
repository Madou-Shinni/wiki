package com.yum.wiki.mapper;

import com.yum.wiki.result.StatisticRes;

import java.util.List;

/**
 * @author Yum
 * @version 1.0
 */
public interface EbookSnapshotMapperCust {

    void genShapshot();

    /**
     * 获取首页统计数值
     *
     * @return
     */
    List<StatisticRes> getStatistic();

    /**
     * 30天
     *
     * @return
     */
    List<StatisticRes> get30Statistic();
}
