package com.ssd.admin.business.serviceImpl;

import cn.hutool.core.date.DateUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ssd.admin.business.entity.ArticleEntity;
import com.ssd.admin.business.enums.ArticleStatusClaimEnum;
import com.ssd.admin.business.enums.RoleEnum;
import com.ssd.admin.business.mapper.ArticleClaimMapper;
import com.ssd.admin.business.mapper.ArticleMapper;
import com.ssd.admin.business.qo.ArticleQO;
import com.ssd.admin.business.service.ArticleService;
import com.ssd.admin.business.service.UserService;
import com.ssd.admin.business.vo.Cons;
import com.ssd.admin.common.BaseService;
import com.ssd.admin.common.PagerForDT;
import com.ssd.admin.common.PagerResultForDT;
import com.ssd.admin.web.config.ShiroUser;
import com.ssd.admin.web.config.UserUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by zhaozhirong on 2019/3/1.
 */
@Service
public class ArticleServiceImpl extends BaseService<ArticleEntity> implements ArticleService {


    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ArticleClaimMapper articleClaimMapper;

    @Autowired
    private UserService userService;

    @Override
    public PagerResultForDT<ArticleEntity> selectPage(PagerForDT<ArticleQO> pager) {
        Example example = new Example(ArticleEntity.class);
        example.createCriteria().andEqualTo("deleted",0);
        List<ArticleEntity> allList = this.selectByExample(example);

        ArticleQO articleQO = pager.getCondition();
        example = new Example(ArticleEntity.class);
        Example.Criteria criteria = example.createCriteria()
                .andEqualTo("deleted",0);
        example.setOrderByClause(" apy DESC,subject ASC ");
        if(StringUtils.isNotBlank(articleQO.getAut())){
            criteria.andLike("aut","%"+articleQO.getAut()+"%");
        }
        if(StringUtils.isNotBlank(articleQO.getAti())){
            criteria.andLike("ati","%"+articleQO.getAti()+"%");
        }
        if(StringUtils.isNotBlank(articleQO.getAso())){
            criteria.andLike("aso","%"+articleQO.getAso()+"%");
        }
        if(StringUtils.isNotBlank(articleQO.getApy())){
            criteria.andLike("apy","%"+articleQO.getApy()+"%");
        }
        if(StringUtils.isNotBlank(articleQO.getAc1())){
            criteria.andLike("ac1","%"+articleQO.getAc1()+"%");
        }
        if(articleQO.getStatus() != null){
            criteria.andEqualTo("status", articleQO.getStatus());
        }
        //用于统计
        if(StringUtils.isNotBlank(articleQO.getStatisticYear())){
            String statisticYear = articleQO.getStatisticYear();
            Integer endYear = Integer.parseInt(statisticYear);
            Integer startYear = endYear - Cons.STATISTIC_GAP_YEAR;
            criteria.andLessThanOrEqualTo("inputYear", statisticYear);
            criteria.andBetween("apy", startYear, endYear);
        }
        PageHelper.offsetPage(pager.getiDisplayStart(),pager.getiDisplayLength());
        List<ArticleEntity> articleEntityList = this.selectByExample(example);
        PageInfo<ArticleEntity> pageInfo = new PageInfo<>(articleEntityList);
        PagerResultForDT<ArticleEntity> pagerResult = new PagerResultForDT<ArticleEntity>(articleEntityList,allList.size(),pageInfo.getTotal());
        return pagerResult;
    }



    @Override
    public ArticleEntity getByAut(String aut) {
        Example example = new Example(ArticleEntity.class);
        example.createCriteria().andEqualTo("deleted",0).andEqualTo("aut",aut.trim());
        List<ArticleEntity> articleEntityList = this.selectByExample(example);
        if(articleEntityList != null && articleEntityList.size() > 0){
            return articleEntityList.get(0);
        }
        return null;
    }

    @Override
    public List<String> findAllYear() {
        ArrayList<String> resultList = new ArrayList<>();
        List<String> allYear = articleMapper.findAllYear();
        for (String s : allYear) {
            if(!resultList.contains(s.trim())){
                resultList.add(s.trim());
            }
        }
        return resultList;
    }

    @Override
    public List<ArticleEntity> findByYear(String statisticYear) {
        Example example = new Example(ArticleEntity.class);
        Example.Criteria criteria = example.createCriteria().andEqualTo("deleted", 0);
        Integer endYear = Integer.parseInt(statisticYear);
        Integer startYear = endYear - Cons.STATISTIC_GAP_YEAR;
        criteria.andLessThanOrEqualTo("inputYear", statisticYear);
        criteria.andBetween("apy", startYear, endYear);
        List<ArticleEntity> articleEntityList = this.selectByExample(example);
        return articleEntityList;
    }

    @Override
    public void finish() {
        articleMapper.finish();
        articleClaimMapper.finish();
    }
}
