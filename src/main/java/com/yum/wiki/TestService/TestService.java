package com.yum.wiki.TestService;

import com.yum.wiki.domain.Test;
import com.yum.wiki.mapper.TestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Yum
 * @version 1.0
 */
@Service
public class TestService {
    @Autowired
    private TestMapper testMapper;

    public List<Test> list() {
        return testMapper.list();
    }
}
