package com.yum.wiki.controller;

import com.yum.wiki.result.CommonResult;
import com.yum.wiki.result.StatisticRes;
import com.yum.wiki.service.EbookSnapShotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Yum
 * @version 1.0
 */
@RestController
@RequestMapping("/ebookSnapshot")
public class EbookSnapShotController {
    @Autowired
    private EbookSnapShotService ebookSnapShotService;

    /**
     * 获取首页统计数值：总阅读数、总点赞数、今日阅读数、今日点赞数、今日预计阅读数、今日预计阅读增长
     *
     * @return
     */
    @GetMapping("/getStatistic")
    public CommonResult getStatistic() {
        List<StatisticRes> statisticResList = ebookSnapShotService.getStatistic();
        CommonResult<List<StatisticRes>> result = new CommonResult();
        result.setData(statisticResList);
        return result;
    }
}
