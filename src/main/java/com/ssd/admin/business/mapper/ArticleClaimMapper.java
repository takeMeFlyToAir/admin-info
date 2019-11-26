package com.ssd.admin.business.mapper;


import com.ssd.admin.business.entity.ArticleClaimEntity;
import com.ssd.admin.common.MyMapper;

import java.util.List;


public interface ArticleClaimMapper extends MyMapper<ArticleClaimEntity> {
    List<ArticleClaimEntity> findByArticleIdList(List<Integer> articleIdList);

    void finish();

    List<Integer> findArticleIdListByStatus();


}