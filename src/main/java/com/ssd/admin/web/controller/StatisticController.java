package com.ssd.admin.web.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.ssd.admin.business.enums.RoleEnum;
import com.ssd.admin.business.qo.ArticleQO;
import com.ssd.admin.business.service.StatisticBaseInfoService;
import com.ssd.admin.business.service.StatisticService;
import com.ssd.admin.common.JsonResp;
import com.ssd.admin.common.PagerForDT;
import com.ssd.admin.common.PagerResultForDT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: zhaozhirong
 * @Date: 2019/01/14
 * @Description:
 */
@Controller
@RequestMapping("statistic")
public class StatisticController {

    private final Logger logger = LoggerFactory.getLogger(StatisticController.class);

    @Autowired
    private StatisticService statisticService;
    @Autowired
    private StatisticBaseInfoService statisticBaseInfoService;

    private ArticleQO initArticleQO(ArticleQO articleQO){
        if(StrUtil.isBlank(articleQO.getStatisticYear())){
            List<String> allYear = statisticBaseInfoService.findAllYear();
            if(CollUtil.isNotEmpty(allYear)){
                articleQO.setStatisticYear(allYear.get(0));
                return articleQO;
            }
            articleQO.setStatisticYear(String.valueOf(DateUtil.thisYear()));
        }
        return articleQO;
    }


    @ResponseBody
    @RequestMapping(value = "/findOrganizationIdAuthorCount", method = RequestMethod.GET)
    public PagerResultForDT findOrganizationIdAuthorCount(HttpServletRequest request, PagerForDT pagerForDataTable, ArticleQO articleQO) {
        pagerForDataTable.setCondition(articleQO);
        PagerResultForDT pagerResult = statisticService.findOrganizationIdAuthorCount(pagerForDataTable);
        return pagerResult.initsEcho(request.getParameter("sEcho"));
    }
    @ResponseBody
    @RequestMapping(value = "findColumnForOrganizationIdAuthorCount", method = RequestMethod.GET)
    public JsonResp findColumnForOrganizationIdAuthorCount() {
        JsonResp resp = new JsonResp();
        try {
            List<Map<String, Object>> columnForOrganizationIdAuthorCount = statisticService.findColumnForOrganizationIdAuthorCount();
            resp.isSuccess().setData(columnForOrganizationIdAuthorCount);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            resp.isFail().setMessage("操作失败");
        }
        return resp;
    }



    @ResponseBody
    @RequestMapping(value = "/findTcRate", method = RequestMethod.GET)
    public PagerResultForDT findPage(HttpServletRequest request, PagerForDT pagerForDataTable, ArticleQO articleQO) {
        initArticleQO(articleQO);
        pagerForDataTable.setCondition(articleQO);
        PagerResultForDT pagerResult  = statisticService.findTcRate(pagerForDataTable);
        return pagerResult.initsEcho(request.getParameter("sEcho"));
    }

    @ResponseBody
    @RequestMapping(value = "/findContributionRate", method = RequestMethod.GET)
    public PagerResultForDT findContributionRate(HttpServletRequest request, PagerForDT pagerForDataTable, ArticleQO articleQO) {
        pagerForDataTable.setCondition(articleQO);
        PagerResultForDT pagerResult  = statisticService.findContributionRate(pagerForDataTable);
        return pagerResult.initsEcho(request.getParameter("sEcho"));
    }
    @ResponseBody
    @RequestMapping(value = "findColumnForContributionRate", method = RequestMethod.GET)
    public JsonResp findColumnForContributionRate() {
        JsonResp resp = new JsonResp();
        try {
            List<Map<String, Object>> columnForContributionRate = statisticService.findColumnForContributionRate();
            resp.isSuccess().setData(columnForContributionRate);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            resp.isFail().setMessage("操作失败");
        }
        return resp;
    }


    @ResponseBody
    @RequestMapping(value = "/findContributionRateForOrganization", method = RequestMethod.GET)
    public PagerResultForDT findContributionRateForOrganization(HttpServletRequest request,  String year) {
        List<Map<String, Object>> contributionRateForOrganization = statisticService.findContributionRateForOrganization(year);
        PagerResultForDT pagerResult = new PagerResultForDT();
        pagerResult.setData(contributionRateForOrganization);
        pagerResult.setRecordsFiltered(contributionRateForOrganization.size());
        pagerResult.setRecordsTotal(contributionRateForOrganization.size());
        return pagerResult.initsEcho(request.getParameter("sEcho"));
    }
    @ResponseBody
    @RequestMapping(value = "findColumnForContributionRateForOrganization", method = RequestMethod.GET)
    public JsonResp findColumnForContributionRateForOrganization() {
        JsonResp resp = new JsonResp();
        try {
            List<Map<String, Object>> columnForContributionRateForOrganization = statisticService.findColumnForContributionRateForOrganization();
            resp.isSuccess().setData(columnForContributionRateForOrganization);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            resp.isFail().setMessage("操作失败");
        }
        return resp;
    }


    @ResponseBody
    @RequestMapping(value = "/findBonusForOrganization", method = RequestMethod.GET)
    public PagerResultForDT findBonusForOrganization(HttpServletRequest request,  String year) {
        List<Map<String, Object>> bonusForOrganization = statisticService.findBonusForOrganization(year);
        PagerResultForDT pagerResult = new PagerResultForDT();
        pagerResult.setData(bonusForOrganization);
        pagerResult.setRecordsFiltered(bonusForOrganization.size());
        pagerResult.setRecordsTotal(bonusForOrganization.size());
        return pagerResult.initsEcho(request.getParameter("sEcho"));
    }


}
