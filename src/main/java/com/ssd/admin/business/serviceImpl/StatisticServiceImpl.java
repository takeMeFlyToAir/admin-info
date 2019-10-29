package com.ssd.admin.business.serviceImpl;

import com.ssd.admin.business.entity.ArticleClaimEntity;
import com.ssd.admin.business.entity.ArticleEntity;
import com.ssd.admin.business.entity.OrganizationEntity;
import com.ssd.admin.business.entity.StatisticBaseInfoEntity;
import com.ssd.admin.business.qo.ArticleQO;
import com.ssd.admin.business.service.*;
import com.ssd.admin.business.vo.Cons;
import com.ssd.admin.common.PagerForDT;
import com.ssd.admin.common.PagerResultForDT;
import com.ssd.admin.util.BigDecimalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhaozhirong on 2019/3/1.
 */
@Service
public class StatisticServiceImpl implements StatisticService {


    @Autowired
    private ArticleService articleService;

    @Autowired
    private ArticleClaimService articleClaimService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private StatisticBaseInfoService statisticBaseInfoService;
    @Override
    public PagerResultForDT<Map<String,Object>> findOrganizationIdAuthorCount(PagerForDT<ArticleQO> pager) {
        PagerResultForDT count = new PagerResultForDT();
        PagerResultForDT<ArticleEntity> articleEntityPagerResultForDT = articleService.selectPage(pager);
        count.setRecordsFiltered(articleEntityPagerResultForDT.getRecordsFiltered());
        count.setRecordsTotal(articleEntityPagerResultForDT.getRecordsTotal());
        count.setsEcho(articleEntityPagerResultForDT.getsEcho());

        /**
         * 按文章+组织信息为key，存储每个文章对应的每个学院认领的作者数
         */
        List<ArticleEntity> articleEntityList = articleEntityPagerResultForDT.getData();
        Map<String,Integer> articleClaimGroupInfo = articleClaimGroupInfo(articleEntityList);

        List<OrganizationEntity> organizationEntityList = organizationService.findAll();

        List<Map<String,Object>> mapList = new ArrayList<>();
        if(articleEntityList != null && articleEntityList.size() > 0){
            for (ArticleEntity articleEntity : articleEntityList) {
                int authorOrganizationCount = 0;
                Map<String,Object> article = new HashMap<String,Object>();
                article.put("articleId",articleEntity.getId());
                article.put("aut",articleEntity.getAut());
                article.put("apy",articleEntity.getApy());
                for (OrganizationEntity organizationEntity : organizationEntityList) {
                    int articleGroupCount = articleClaimGroupInfo.getOrDefault(articleEntity.getId().toString()+organizationEntity.getId().toString(),0);
                    authorOrganizationCount += articleGroupCount;
                    article.put(organizationEntity.getId().toString(),articleGroupCount);
                }
                article.put("allCount",authorOrganizationCount);
                mapList.add(article);
            }
        }
        count.setData(mapList);
        return count;
    }

    private Map<String,Integer> articleClaimGroupInfo(List<ArticleEntity> articleEntityList ){
        Map<String,Integer> articleClaimGroupInfo = new HashMap<>();
        if(articleEntityList != null && articleEntityList.size() > 0){
            List<Integer> articleIdList = new ArrayList<>();
            for (ArticleEntity articleEntity : articleEntityList) {
                articleIdList.add(articleEntity.getId());
            }
            //查询这些文章的所有认领信息
            List<ArticleClaimEntity> byArticleIdList = articleClaimService.findByArticleIdList(articleIdList);
            if(byArticleIdList != null && byArticleIdList.size() > 0){
                for (ArticleClaimEntity articleClaimEntity : byArticleIdList) {
                    if(articleClaimEntity != null){
                        articleClaimGroupInfo.put(articleClaimEntity.getArticleId().toString()+articleClaimEntity.getClaimUserOrganizationId().toString(), articleClaimEntity.getClaimCount());
                    }
                }
            }
        }
        return articleClaimGroupInfo;
    }


    @Override
    public PagerResultForDT<Map<String, Object>> findContributionRate(PagerForDT<ArticleQO> pager){
        PagerResultForDT count = new PagerResultForDT();
        PagerResultForDT<ArticleEntity> articleEntityPagerResultForDT = articleService.selectPage(pager);
        count.setRecordsFiltered(articleEntityPagerResultForDT.getRecordsFiltered());
        count.setRecordsTotal(articleEntityPagerResultForDT.getRecordsTotal());
        count.setsEcho(articleEntityPagerResultForDT.getsEcho());

        /**
         * 按文章+组织信息为key，存储每个文章对应的每个学院认领的作者数
         */
        List<ArticleEntity> articleEntityList = articleEntityPagerResultForDT.getData();
        Map<String,Integer> articleClaimGroupInfo = articleClaimGroupInfo(articleEntityList);
        /**
         * 按年份和学科查询某个学科在某个年份的总引用数
         */
        Map<String,Integer> articleAllTcByYearAndSubject = articleAllTcByYearAndSubject();

        List<OrganizationEntity> organizationEntityList = organizationService.findAll();

        List<Map<String,Object>> mapList = new ArrayList<>();
        if(articleEntityList != null && articleEntityList.size() > 0){
            for (ArticleEntity articleEntity : articleEntityList) {
                int authorOrganizationCount = 0;
                Map<String,Object> article = new HashMap<String,Object>();
                article.put("articleId",articleEntity.getId());
                article.put("aut",articleEntity.getAut());
                article.put("apy",articleEntity.getApy());
                for (OrganizationEntity organizationEntity : organizationEntityList) {
                    int articleGroupCount = articleClaimGroupInfo.getOrDefault(articleEntity.getId().toString()+organizationEntity.getId().toString(),0);
                    authorOrganizationCount += articleGroupCount;
                }
                for (OrganizationEntity organizationEntity : organizationEntityList) {
                    int articleGroupCount = articleClaimGroupInfo.getOrDefault(articleEntity.getId().toString()+organizationEntity.getId().toString(),0);
                    Integer allTc = articleAllTcByYearAndSubject.get(articleEntity.getSubject().toString() + articleEntity.getApy());
                    try {
                        double tcRate=0, contributionRate=0;
                        if(allTc != 0 && authorOrganizationCount != 0){
                            tcRate = BigDecimalUtil.div(Double.valueOf(articleEntity.getAtc().trim()), allTc, Cons.TC_RATE);
                            contributionRate = BigDecimalUtil.mul(BigDecimalUtil.div(articleGroupCount,authorOrganizationCount,Cons.CONTRIBUTION_RATE),tcRate, Cons.CONTRIBUTION_RATE);
                        }
                        article.put(organizationEntity.getId().toString(),contributionRate);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                mapList.add(article);
            }
        }
        count.setData(mapList);
        return count;
    }

    @Override
    public PagerResultForDT<ArticleEntity> findTcRate(PagerForDT<ArticleQO> pager) {
        PagerResultForDT<ArticleEntity> articleEntityPagerResultForDT = articleService.selectPage(pager);
        /**
         * 按年份和学科查询某个学科在某个年份的总引用数
         */
        Map<String,Integer> articleAllTcByYearAndSubject = articleAllTcByYearAndSubject();
        List<StatisticBaseInfoEntity> statisticBaseInfoEntityList = statisticBaseInfoService.findAll();
        if(statisticBaseInfoEntityList != null && statisticBaseInfoEntityList.size() > 0){
            for (StatisticBaseInfoEntity statisticBaseInfoEntity : statisticBaseInfoEntityList) {
                articleAllTcByYearAndSubject.put(statisticBaseInfoEntity.getSubject().toString()+statisticBaseInfoEntity.getYear().toString(),statisticBaseInfoEntity.getAllTc());
            }
        }
        List<ArticleEntity> articleEntityList = articleEntityPagerResultForDT.getData();
        if(articleEntityList != null && articleEntityList.size() > 0){
            for (ArticleEntity articleEntity : articleEntityList) {
                Integer allTc = articleAllTcByYearAndSubject.get(articleEntity.getSubject().toString() + articleEntity.getApy());
                if(allTc == null){
                    articleEntity.setAllAtc("未录入总引用数");
                }else {
                    articleEntity.setAllAtc(allTc.toString());
                    try {
                        articleEntity.setAtcRate(BigDecimalUtil.div(Double.valueOf(articleEntity.getAtc().trim()),allTc, Cons.TC_RATE));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
        return articleEntityPagerResultForDT;
    }

    private Map<String,Integer> articleAllTcByYearAndSubject(){
        Map<String,Integer> articleAllTcByYearAndSubject = new HashMap<>();
        List<StatisticBaseInfoEntity> statisticBaseInfoEntityList = statisticBaseInfoService.findAll();
        if(statisticBaseInfoEntityList != null && statisticBaseInfoEntityList.size() > 0){
            for (StatisticBaseInfoEntity statisticBaseInfoEntity : statisticBaseInfoEntityList) {
                articleAllTcByYearAndSubject.put(statisticBaseInfoEntity.getSubject().toString()+statisticBaseInfoEntity.getYear().toString(),statisticBaseInfoEntity.getAllTc());
            }
        }
        return articleAllTcByYearAndSubject;
    }

    @Override
    public List<Map<String,Object>> findColumnForContributionRate() {
        List<Map<String,Object>> columnList = findBaseColumn();
        return columnList;
    }


    private List<Map<String,Object>> findBaseColumn(){
        List<Map<String,Object>> columnList = new ArrayList<>();
        HashMap<String, Object> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("mData","articleId");
        stringStringHashMap.put("title","文章id");
        columnList.add(stringStringHashMap);
        stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("mData","aut");
        stringStringHashMap.put("title","文章编号");
        columnList.add(stringStringHashMap);
        stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("mData","apy");
        stringStringHashMap.put("title","刊登年份");
        columnList.add(stringStringHashMap);
        List<OrganizationEntity> all = organizationService.findAll();
        for (OrganizationEntity organizationEntity : all) {
            stringStringHashMap = new HashMap<>();
            stringStringHashMap.put("mData",organizationEntity.getId());
            stringStringHashMap.put("title",organizationEntity.getName());
            columnList.add(stringStringHashMap);
        }
        return columnList;
    }

    @Override
    public List<Map<String,Object>> findColumnForOrganizationIdAuthorCount() {
        List<Map<String,Object>> columnList = findBaseColumn();
        HashMap<String, Object> stringStringHashMap = new HashMap<>();
        stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("mData","allCount");
        stringStringHashMap.put("title","作者总数");
        columnList.add(stringStringHashMap);
        return columnList;
    }
}
