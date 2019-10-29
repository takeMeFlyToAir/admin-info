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
        PagerResultForDT pagerResult = null;
        try {
            pagerResult = statisticService.findTcRate(pagerForDataTable);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
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
}
