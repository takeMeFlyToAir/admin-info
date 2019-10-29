package com.ssd.admin.web.controller;

import com.ssd.admin.business.qo.ArticleQO;
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

    @ResponseBody
    @RequestMapping(value = "/findOrganizationIdAuthorCount", method = RequestMethod.GET)
    public PagerResultForDT findOrganizationIdAuthorCount(HttpServletRequest request, PagerForDT pagerForDataTable, ArticleQO articleQO) {
        pagerForDataTable.setCondition(articleQO);
        PagerResultForDT pagerResult = statisticService.findOrganizationIdAuthorCount(pagerForDataTable);
        return pagerResult.initsEcho(request.getParameter("sEcho"));
    }

    @ResponseBody
    @RequestMapping(value = "/findTcRate", method = RequestMethod.GET)
    public PagerResultForDT findPage(HttpServletRequest request, PagerForDT pagerForDataTable, ArticleQO articleQO) {
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
}
