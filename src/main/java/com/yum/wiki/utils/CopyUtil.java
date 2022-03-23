package com.yum.wiki.utils;

import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Yum
 * @version 1.0
 *
 * 封装BeanUtils
 */
public class CopyUtil {

    /**
     * 单体复制
     * @param source 源
     * @param clazz 目标
     * @param <T>
     * @return
     */
    public static <T> T copy(Object source,Class<T> clazz) {
        if(source == null) {
            return null;
        }
        T obj = null;
        try {
            obj = clazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        BeanUtils.copyProperties(source,obj);
        return obj;
    }

    /**
     * 列表复制
     * @param <T>
     * @param source 源
     * @param clazz 目标
     * @return
     */
    public static <T> List<T> copyList(List source, Class<T> clazz) {
        List<T> target = new ArrayList<>();
        if(!source.isEmpty()) {
            source.stream().forEach(item->{
                T obj = copy(item, clazz);
                target.add(obj);
            });
        }
        return target;
    }
}
