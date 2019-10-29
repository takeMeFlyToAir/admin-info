package com.ssd.admin.business.serviceImpl;

import com.ssd.admin.business.entity.ArticleClaimEntity;
import com.ssd.admin.business.entity.ArticleEntity;
import com.ssd.admin.business.entity.OrganizationEntity;
import com.ssd.admin.business.qo.ArticleQO;
import com.ssd.admin.business.service.ArticleClaimService;
import com.ssd.admin.business.service.ArticleService;
import com.ssd.admin.business.service.OrganizationService;
import com.ssd.admin.business.service.StatisticService;
import com.ssd.admin.common.PagerForDT;
import com.ssd.admin.common.PagerResultForDT;
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


    @Override
    public PagerResultForDT<Map<String,Object>> findOrganizationIdAuthorCount(PagerForDT<ArticleQO> pager) {
        PagerResultForDT count = new PagerResultForDT();
        PagerResultForDT<ArticleEntity> articleEntityPagerResultForDT = articleService.selectPage(pager);
        count.setRecordsFiltered(articleEntityPagerResultForDT.getRecordsFiltered());
        count.setRecordsTotal(articleEntityPagerResultForDT.getRecordsTotal());
        count.setsEcho(articleEntityPagerResultForDT.getsEcho());

        List<Integer> articleIdList = new ArrayList<>();
        /**
         * 按文章+组织信息为key，存储每个文章对应的每个学院认领的作者数
         */
        Map<String,Integer> articleClaimGroupInfo = new HashMap<>();
        List<ArticleEntity> articleEntityList = articleEntityPagerResultForDT.getData();
        if(articleEntityList != null && articleEntityList.size() > 0){
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

        List<OrganizationEntity> organizationEntityList = organizationService.findAll();

        List<Map<String,Object>> mapList = new ArrayList<>();
        if(articleEntityList != null && articleEntityList.size() > 0){
            for (ArticleEntity articleEntity : articleEntityList) {
                int authorOriganzationCount = 0;
                Map<String,Object> article = new HashMap<String,Object>();
                article.put("articleId",articleEntity.getId());
                article.put("aut",articleEntity.getAut());
                article.put("apy",articleEntity.getApy());
                for (OrganizationEntity organizationEntity : organizationEntityList) {
                    int articleGroupCount = articleClaimGroupInfo.getOrDefault(articleEntity.getId().toString()+organizationEntity.getId().toString(),0);
                    authorOriganzationCount += articleGroupCount;
                    article.put(organizationEntity.getId().toString(),articleGroupCount);
                }
                article.put("allCount",authorOriganzationCount);
                mapList.add(article);
            }
        }
        count.setData(mapList);
        return count;
    }

    @Override
    public List<Map<String,Object>> findColumnForOrganizationIdAuthorCount() {
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
        stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("mData","allCount");
        stringStringHashMap.put("title","作者总数");
        columnList.add(stringStringHashMap);
        return columnList;
    }
}
