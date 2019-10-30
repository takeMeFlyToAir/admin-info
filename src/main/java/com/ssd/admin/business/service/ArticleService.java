package com.ssd.admin.business.service;


import com.ssd.admin.business.entity.ArticleEntity;
import com.ssd.admin.business.qo.ArticleQO;
import com.ssd.admin.common.IService;
import com.ssd.admin.common.PagerForDT;
import com.ssd.admin.common.PagerResultForDT;

import java.util.List;


/**
 * @Author: zhaozhirong
 * @Date: 2019/01/08
 * @Description:
 */
public interface ArticleService extends IService<ArticleEntity> {

    /**
     * 分页查询
     * @param pager
     * @return
     */
    PagerResultForDT<ArticleEntity> selectPage(PagerForDT<ArticleQO> pager);


    ArticleEntity getByAut(String aut);

    List<String> findAllYear();

    List<ArticleEntity> findByYear(String year);

}
