package com.ssd.admin.business.serviceImpl;

import com.ssd.admin.business.entity.ArticleClaimEntity;
import com.ssd.admin.business.entity.ArticleEntity;
import com.ssd.admin.business.entity.OrganizationEntity;
import com.ssd.admin.business.entity.StatisticBaseInfoEntity;
import com.ssd.admin.business.enums.SubjectEnum;
import com.ssd.admin.business.qo.ArticleQO;
import com.ssd.admin.business.service.*;
import com.ssd.admin.business.vo.Cons;
import com.ssd.admin.common.PagerForDT;
import com.ssd.admin.common.PagerResultForDT;
import com.ssd.admin.util.BigDecimalUtil;
import com.ssd.admin.util.ListUtils;
import com.ssd.admin.util.PoolManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

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


    private String getYear(){
        List<String> allYear = articleService.findAllYear();
        if(allYear == null || allYear.isEmpty()){
            return null;
        }
        return allYear.get(0);
    }


    @Override
    public PagerResultForDT<ArticleEntity> findTcRate(PagerForDT<ArticleQO> pager) {
        PagerResultForDT<ArticleEntity> articleEntityPagerResultForDT = articleService.selectPage(pager);
        String statisticYear = pager.getCondition().getStatisticYear();
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
                Integer allTc = articleAllTcByYearAndSubject.get(articleEntity.getSubject().toString()+statisticYear);
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



    @Override
    public PagerResultForDT<Map<String, Object>> findContributionRate(PagerForDT<ArticleQO> pager){
        String statisticYear = pager.getCondition().getStatisticYear();
        PagerResultForDT count = new PagerResultForDT();
        PagerResultForDT<ArticleEntity> articleEntityPagerResultForDT = articleService.selectPage(pager);
        count.setRecordsFiltered(articleEntityPagerResultForDT.getRecordsFiltered());
        count.setRecordsTotal(articleEntityPagerResultForDT.getRecordsTotal());
        count.setsEcho(articleEntityPagerResultForDT.getsEcho());
        List<ArticleEntity> articleEntityList = articleEntityPagerResultForDT.getData();
        /**
         * 按文章+组织信息为key，存储每个文章对应的每个学院认领的作者数
         */
        Map<String,Integer> articleClaimGroupInfo = articleClaimGroupInfo(articleEntityList);
        /**
         * 按年份和学科查询某个学科在某个年份的总引用数
         */
        Map<String,Integer> articleAllTcByYearAndSubject = articleAllTcByYearAndSubject();
        List<OrganizationEntity> organizationEntityList = organizationService.findAll();

        List<Map<String,Object>> mapList = getContributionRateFromArticleList(statisticYear, articleEntityList,organizationEntityList,articleClaimGroupInfo,articleAllTcByYearAndSubject);
        count.setData(mapList);
        return count;
    }



    @Override
    public List<Map<String,Object>> findContributionRateForOrganization(String statisticYear){
        List<Map<String,Object>> resultList = new ArrayList<>();
        if(StringUtils.isBlank(statisticYear)){
            return resultList;
        }

        List<ArticleEntity> articleEntityList = articleService.findByYear(statisticYear);
        List<Map<String,Object>>  contributionRateForOrganization= new ArrayList<>();

        /**
         * 按文章+组织信息为key，存储每个文章对应的每个学院认领的作者数
         */
        Map<String,Integer> articleClaimGroupInfo = articleClaimGroupInfo(articleEntityList);
        /**
         * 按年份和学科查询某个学科在某个年份的总引用数
         */
        Map<String,Integer> articleAllTcByYearAndSubject = articleAllTcByYearAndSubject();
        List<OrganizationEntity> organizationEntityList = organizationService.findAll();

        List<Future<List<Map<String, Object>>>> futureList = new ArrayList<>();
        List<List<ArticleEntity>> groupList = ListUtils.groupList(articleEntityList, Cons.GROUP_LIST_LIMIT);
        for (List<ArticleEntity> articleEntities : groupList) {
            Future<List<Map<String, Object>>> signalContributionRateSubmit = PoolManager.statisticPool.submit(new Callable<List<Map<String, Object>>>() {
                @Override
                public List<Map<String, Object>> call() throws Exception {
                    return getContributionRateFromArticleList(statisticYear,articleEntities,organizationEntityList,articleClaimGroupInfo,articleAllTcByYearAndSubject);
                }
            });
            futureList.add(signalContributionRateSubmit);
        }

        for (Future<List<Map<String, Object>>> listFuture : futureList) {
            try {
                List<Map<String, Object>> mapList = listFuture.get(60, TimeUnit.SECONDS);
                contributionRateForOrganization.addAll(mapList);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        }

        Map<String,Object> materialScience = new HashMap<>();
        Map<String,Object> engineering = new HashMap<>();
        Map<String,Object> chemistry = new HashMap<>();
        Map<String,Object> agriculturalScience = new HashMap<>();
        materialScience.put("subjectStr",SubjectEnum.MATERIAL_SCIENCE.getDisplay());
        materialScience.put("subject",SubjectEnum.MATERIAL_SCIENCE);
        engineering.put("subjectStr",SubjectEnum.ENGINEERING.getDisplay());
        engineering.put("subject",SubjectEnum.ENGINEERING);
        chemistry.put("subjectStr",SubjectEnum.CHEMISTRY.getDisplay());
        chemistry.put("subject",SubjectEnum.CHEMISTRY);
        agriculturalScience.put("subjectStr",SubjectEnum.AGRICULTURAL_SCIENCE.getDisplay());
        agriculturalScience.put("subject",SubjectEnum.AGRICULTURAL_SCIENCE);
        for (OrganizationEntity organizationEntity : organizationEntityList) {
            materialScience.put(organizationEntity.getId().toString(),0);
            engineering.put(organizationEntity.getId().toString(),0);
            chemistry.put(organizationEntity.getId().toString(),0);
            agriculturalScience.put(organizationEntity.getId().toString(),0);
        }

        List<Future<Map<String, Object>>> allSubjectSubmit = new ArrayList<>();
        Future<Map<String, Object>> materialScience_submit = PoolManager.statisticPool.submit(new Callable<Map<String, Object>>() {
            @Override
            public Map<String, Object> call() throws Exception {
                for (Map<String, Object> objectMap : contributionRateForOrganization) {
                    SubjectEnum subject = SubjectEnum.fromCode(Integer.valueOf(objectMap.get("subject").toString()));
                    if(subject == SubjectEnum.MATERIAL_SCIENCE){
                        for (OrganizationEntity organizationEntity : organizationEntityList) {
                            String key = organizationEntity.getId().toString();
                            materialScience.put(key,BigDecimalUtil.add(Double.valueOf(objectMap.getOrDefault(key,0).toString()),Double.valueOf(materialScience.getOrDefault(key,0).toString())));
                        }
                    }
                }
                return materialScience;
            }
        });
        Future<Map<String, Object>> engineering_submit = PoolManager.statisticPool.submit(new Callable<Map<String, Object>>() {
            @Override
            public Map<String, Object> call() throws Exception {
                for (Map<String, Object> objectMap : contributionRateForOrganization) {
                    SubjectEnum subject = SubjectEnum.fromCode(Integer.valueOf(objectMap.get("subject").toString()));
                    if(subject == SubjectEnum.ENGINEERING){
                        for (OrganizationEntity organizationEntity : organizationEntityList) {
                            String key = organizationEntity.getId().toString();
                            engineering.put(key,BigDecimalUtil.add(Double.valueOf(objectMap.getOrDefault(key,0).toString()),Double.valueOf(engineering.getOrDefault(key,0).toString())));
                        }
                    }
                }
                return engineering;
            }
        });
        Future<Map<String, Object>> chemistry_submit = PoolManager.statisticPool.submit(new Callable<Map<String, Object>>() {
            @Override
            public Map<String, Object> call() throws Exception {
                for (Map<String, Object> objectMap : contributionRateForOrganization) {
                    SubjectEnum subject = SubjectEnum.fromCode(Integer.valueOf(objectMap.get("subject").toString()));
                    if(subject == SubjectEnum.CHEMISTRY){
                        for (OrganizationEntity organizationEntity : organizationEntityList) {
                            String key = organizationEntity.getId().toString();
                            chemistry.put(key,BigDecimalUtil.add(Double.valueOf(objectMap.getOrDefault(key,0).toString()),Double.valueOf(chemistry.getOrDefault(key,0).toString())));
                        }
                    }
                }
                return chemistry;
            }
        });
        Future<Map<String, Object>> agriculturalScience_submit = PoolManager.statisticPool.submit(new Callable<Map<String, Object>>() {
            @Override
            public Map<String, Object> call() throws Exception {
                for (Map<String, Object> objectMap : contributionRateForOrganization) {
                    SubjectEnum subject = SubjectEnum.fromCode(Integer.valueOf(objectMap.get("subject").toString()));
                    if(subject == SubjectEnum.AGRICULTURAL_SCIENCE){
                        for (OrganizationEntity organizationEntity : organizationEntityList) {
                            String key = organizationEntity.getId().toString();
                            agriculturalScience.put(key,BigDecimalUtil.add(Double.valueOf(objectMap.getOrDefault(key,0).toString()),Double.valueOf(agriculturalScience.getOrDefault(key,0).toString())));
                        }
                    }
                }
                return agriculturalScience;
            }
        });
        allSubjectSubmit.add(materialScience_submit);
        allSubjectSubmit.add(engineering_submit);
        allSubjectSubmit.add(chemistry_submit);
        allSubjectSubmit.add(agriculturalScience_submit);
        for (Future<Map<String, Object>> mapFuture : allSubjectSubmit) {
            try {
                resultList.add(mapFuture.get(60,TimeUnit.SECONDS));
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        }
        return resultList;
    }


    @Override
    public List<Map<String, Object>> findBonusForOrganization(String statisticYear) {
        List<Map<String, Object>> resultList = new ArrayList<>();
        if(StringUtils.isBlank(statisticYear)){
            return resultList;
        }
        /**
         * 按年份和学科查询某个学科在某个年份的总引用数
         */
        Map<String,Double> articleBonusByYearAndSubject = new HashMap<>();
        List<StatisticBaseInfoEntity> statisticBaseInfoEntityList = statisticBaseInfoService.findAll();
        if(statisticBaseInfoEntityList != null && statisticBaseInfoEntityList.size() > 0){
            for (StatisticBaseInfoEntity statisticBaseInfoEntity : statisticBaseInfoEntityList) {
                articleBonusByYearAndSubject.put(statisticBaseInfoEntity.getSubject().toString()+statisticBaseInfoEntity.getYear().toString(),statisticBaseInfoEntity.getBonus());
            }
        }
        List<OrganizationEntity> organizationEntityList = organizationService.findAll();


        List<Map<String, Object>> contributionRateForOrganization = this.findContributionRateForOrganization(statisticYear);
        for (Map<String, Object> objectMap : contributionRateForOrganization) {
            Map<String, Object> resultMap = new HashMap<>(objectMap);
            SubjectEnum subject = (SubjectEnum) objectMap.get("subject");
            Double bonus = articleBonusByYearAndSubject.getOrDefault(subject.getCode()+statisticYear,0.0);
            for (OrganizationEntity organizationEntity : organizationEntityList) {
                resultMap.put(organizationEntity.getId().toString(),BigDecimalUtil.mul(bonus,Double.valueOf(objectMap.get(organizationEntity.getId().toString()).toString()),Cons.BONUS));
            }
            resultList.add(resultMap);
        }
        Map<String, Object> sum = new HashMap<>();
        sum.put("subjectStr","奖励合计（元）");
        for (OrganizationEntity organizationEntity : organizationEntityList) {
            Double sumMoney = 0.0;
            for (Map<String, Object> objectMap : resultList) {
                Object orDefault = objectMap.getOrDefault(organizationEntity.getId().toString(), 0);
                sumMoney = BigDecimalUtil.add(sumMoney, Double.valueOf(orDefault.toString()),Cons.BONUS);
            }
            sum.put(organizationEntity.getId().toString(),sumMoney);
        }
        resultList.add(sum);
        return resultList;
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

    private List<Map<String,Object>> getContributionRateFromArticleList(String statisticYear, List<ArticleEntity> articleEntityList ,List<OrganizationEntity> organizationEntityList,Map<String,Integer> articleClaimGroupInfo,
                                                                        Map<String,Integer> articleAllTcByYearAndSubject ){
        List<Map<String,Object>> mapList = new ArrayList<>();
        if(articleEntityList != null && articleEntityList.size() > 0){
            for (ArticleEntity articleEntity : articleEntityList) {
                int authorOrganizationCount = 0;
                Map<String,Object> article = new HashMap<String,Object>();
                article.put("articleId",articleEntity.getId());
                article.put("aut",articleEntity.getAut());
                article.put("apy",articleEntity.getApy());
                article.put("subject",articleEntity.getSubject());
                for (OrganizationEntity organizationEntity : organizationEntityList) {
                    int articleGroupCount = articleClaimGroupInfo.getOrDefault(articleEntity.getId().toString()+organizationEntity.getId().toString(),0);
                    authorOrganizationCount += articleGroupCount;
                }
                for (OrganizationEntity organizationEntity : organizationEntityList) {
                    int articleGroupCount = articleClaimGroupInfo.getOrDefault(articleEntity.getId().toString()+organizationEntity.getId().toString(),0);
                    Integer allTc = articleAllTcByYearAndSubject.getOrDefault(articleEntity.getSubject().toString() + statisticYear,0);
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
        return mapList;
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

    @Override
    public List<Map<String,Object>> findColumnForContributionRateForOrganization() {
        List<Map<String,Object>> columnList = new ArrayList<>();
        HashMap<String, Object> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("mData","subjectStr");
        stringStringHashMap.put("title","学科");
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
