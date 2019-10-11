package com.ssd.admin.business.service;


import com.ssd.admin.business.entity.ArticleClaimEntity;
import com.ssd.admin.business.enums.ArticleStatusClaimEnum;
import com.ssd.admin.business.qo.ArticleClaimQO;
import com.ssd.admin.common.IService;
import com.ssd.admin.common.PagerForDT;
import com.ssd.admin.common.PagerResultForDT;

import java.lang.reflect.InvocationTargetException;
import java.util.List;


/**
 * @Author: zhaozhirong
 * @Date: 2019/01/08
 * @Description:
 */
public interface ArticleClaimService extends IService<ArticleClaimEntity> {

    /**
     * 分页查询
     * @param pager
     * @return
     */
    PagerResultForDT<ArticleClaimEntity> selectPage(PagerForDT<ArticleClaimQO> pager);



    /**
     * 审核
     * @param id
     * @param articleStatusClaimEnum
     */
    void modifyArticleClaimStatus(Integer id, ArticleStatusClaimEnum articleStatusClaimEnum);

    /**
     * 认领文章
     * @param articleId 文章id
     * @param author 第几作者
     * @param claimUserId 认领作者id
     */
    void claimArticle(Integer articleId, Integer author, Integer claimUserId,Integer authorType) throws InvocationTargetException, IllegalAccessException;

    /**
     * 查看某文章是否存在第几作者的认领记录
     * @param articleId
     * @param author
     * @return
     */
    ArticleClaimEntity getByArticleIdAndAuthor(Integer articleId, Integer author, Integer authorType);

    /**
     * 查询某文章的审核中或者审核通过的认领记录的所有第几作者的集合
     * @param articleId
     * @return
     */
    List<Integer> findByArticleIdAndStatus(Integer articleId,Integer authorType);

    /**
     * 审核
     * @param id
     * @param status
     * @param remark
     */
    void audit(Integer id,Integer status,String remark);

}
