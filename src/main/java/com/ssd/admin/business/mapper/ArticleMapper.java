package com.ssd.admin.business.mapper;


import com.ssd.admin.business.entity.ArticleEntity;
import com.ssd.admin.common.MyMapper;

import java.util.List;


public interface ArticleMapper extends MyMapper<ArticleEntity> {

    List<String> findAllYear();

    void finish();

}