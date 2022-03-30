package com.yum.wiki.service;

import com.yum.wiki.mapper.EbookSnapshotMapperCust;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
