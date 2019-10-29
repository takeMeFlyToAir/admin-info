package com.ssd.admin.business.service;


import com.ssd.admin.business.entity.ArticleEntity;
import com.ssd.admin.business.qo.ArticleQO;
import com.ssd.admin.common.PagerForDT;
import com.ssd.admin.common.PagerResultForDT;

import java.util.List;
import java.util.Map;

/**
 * @Author: zhaozhirong
 * @Date: 2019/01/08
 * @Description:
 */
public interface StatisticService{

    /**
     * 统计每篇文章各个学院认领人数
     * @param pager
     * @return
     */
    PagerResultForDT<Map<String,Object>> findOrganizationIdAuthorCount(PagerForDT<ArticleQO> pager);


    /**
     * 统计每篇文章的引用占比
     * @param pager
     * @return
     */
    PagerResultForDT<ArticleEntity> findTcRate(PagerForDT<ArticleQO> pager) throws IllegalAccessException;

    List<Map<String,Object>> findColumnForOrganizationIdAuthorCount();

}
