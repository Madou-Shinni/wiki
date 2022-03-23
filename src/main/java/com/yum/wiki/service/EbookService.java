package com.yum.wiki.service;

import com.yum.wiki.domain.Ebook;
import com.yum.wiki.mapper.EbookMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Yum
 * @version 1.0
 */
@Service
public class EbookService {
    @Autowired
    private EbookMapper ebookMapper;

    public List<Ebook> list() {
        return ebookMapper.selectByExample(null);
    }
}
