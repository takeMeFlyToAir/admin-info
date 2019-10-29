package com.ssd.admin.business.service;


import com.ssd.admin.business.entity.StatisticBaseInfoEntity;
import com.ssd.admin.business.qo.StatisticBaseInfoQO;
import com.ssd.admin.common.IService;
import com.ssd.admin.common.PagerForDT;
import com.ssd.admin.common.PagerResultForDT;

import java.util.List;

/**
 * @Author: zhaozhirong
 * @Date: 2019/01/08
 * @Description:
 */
public interface StatisticBaseInfoService extends IService<StatisticBaseInfoEntity> {

    /**
     * 分页查询
     * @param pager
     * @return
     */
    PagerResultForDT<StatisticBaseInfoEntity> selectPage(PagerForDT<StatisticBaseInfoQO> pager);

    Boolean validate(Integer id, Integer year, Integer subject);

    List<StatisticBaseInfoEntity> findAll();

    StatisticBaseInfoEntity findByYearAndSubject(Integer year, Integer subject);
}
