package com.ssd.admin.business.service;


import com.ssd.admin.business.entity.ArticleEntity;
import com.ssd.admin.business.enums.ArticleStatusEnum;
import com.ssd.admin.business.qo.ArticleQO;
import com.ssd.admin.common.IService;
import com.ssd.admin.common.PagerForDT;
import com.ssd.admin.common.PagerResultForDT;


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



    /**
     * 审核
     * @param id
     * @param articleStatusEnum
     */
    void modifyArticleStatus(Integer id, ArticleStatusEnum articleStatusEnum);

    ArticleEntity getByAut(String aut);



}
