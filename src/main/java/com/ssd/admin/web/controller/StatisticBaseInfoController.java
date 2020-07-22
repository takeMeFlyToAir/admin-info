package com.ssd.admin.web.controller;

import com.ssd.admin.business.entity.StatisticBaseInfoEntity;
import com.ssd.admin.business.qo.StatisticBaseInfoQO;
import com.ssd.admin.business.service.StatisticBaseInfoService;
import com.ssd.admin.common.JsonResp;
import com.ssd.admin.common.PagerForDT;
import com.ssd.admin.common.PagerResultForDT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("statisticBaseInfo")
public class StatisticBaseInfoController {

    private final Logger logger = LoggerFactory.getLogger(StatisticBaseInfoController.class);

    @Autowired
    private StatisticBaseInfoService statisticBaseInfoService;


    @ResponseBody
    @RequestMapping(value = "/findPage", method = RequestMethod.GET)
    public PagerResultForDT findPage(HttpServletRequest request, PagerForDT pagerForDataTable, StatisticBaseInfoQO statisticBaseInfoQO) {
        pagerForDataTable.setCondition(statisticBaseInfoQO);
        PagerResultForDT pagerResult = statisticBaseInfoService.selectPage(pagerForDataTable);
        return pagerResult.initsEcho(request.getParameter("sEcho"));
    }

    @ResponseBody
    @RequestMapping(value = "findAll", method = RequestMethod.GET)
    public JsonResp findAll() {
        JsonResp resp = new JsonResp();
        try {
            List<StatisticBaseInfoEntity> statisticBaseInfoEntityList = statisticBaseInfoService.findAll();
            resp.isSuccess().setData(statisticBaseInfoEntityList);
        }catch (Exception e){
            logger.error("statisticBaseInfo findAll is error", e);
            resp.isFail().setMessage("查询异常");
        }
       return resp;
    }

    @ResponseBody
    @RequestMapping(value = "findByRoleCode", method = RequestMethod.GET)
    public JsonResp findByRoleCode(Integer year,Integer subject) {
        JsonResp resp = new JsonResp();
        try {
            StatisticBaseInfoEntity byYearAndSubject = statisticBaseInfoService.findByYearAndSubject(year, subject);
            resp.isSuccess().setData(byYearAndSubject);
        }catch (Exception e){
            logger.error("statisticBaseInfo findByRoleCode is error", e);
            resp.isFail().setMessage("查询异常");
        }
        return resp;
    }

    @ResponseBody
    @RequestMapping(value = "check", method = {RequestMethod.POST,RequestMethod.GET})
    public JsonResp check(@RequestParam("year") Integer year,
                             @RequestParam("subject") Integer subject,
                             @RequestParam(value = "id",required = false) Integer id) {
        JsonResp resp = new JsonResp();
        Boolean result = false;
        try {
            result = statisticBaseInfoService.validate(id,year,subject);
            resp.isSuccess().setData(result);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            resp.isFail();
        }
        return resp;
    }

    @ResponseBody
    @RequestMapping(value = "addOrUpdate", method = RequestMethod.POST)
    public JsonResp addOrUpdate(StatisticBaseInfoEntity statisticBaseInfoEntity) {
        JsonResp resp = new JsonResp();
        try {
            if(statisticBaseInfoEntity.getId() == null){
                statisticBaseInfoService.save(statisticBaseInfoEntity);
                resp.isSuccess().setMessage("保存成功");
            }else {
                statisticBaseInfoService.updateNotNull(statisticBaseInfoEntity);
                resp.isSuccess().setMessage("更新成功");
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            resp.isFail().setMessage("操作失败");
        }
        return resp;
    }

    @ResponseBody
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public JsonResp delete(Integer id) {
        JsonResp resp = new JsonResp();
        if(id == null){
            resp.isFail().setMessage("删除异常");
            return resp;
        }
        try {
            statisticBaseInfoService.delete(id);
            resp.isSuccess().setMessage("删除成功");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            resp.isFail().setMessage(e.getMessage());
        }
        return resp;
    }


    @RequestMapping(value = "/detail")
    @ResponseBody
    public JsonResp detail(Integer id){
        JsonResp resp = new JsonResp();
        try {
            resp.isSuccess().setData(statisticBaseInfoService.selectByKey(id));
        }catch (Exception e){
            resp.isFail().setMessage("查询异常");
            logger.error("get statisticBaseInfo info is error,",e);
        }
        return resp;
    }

    @RequestMapping(value = "/findAllYear")
    @ResponseBody
    public JsonResp findAllYear(){
        JsonResp resp = new JsonResp();
        try {
            resp.isSuccess().setData(statisticBaseInfoService.findAllYear());
        }catch (Exception e){
            resp.isFail().setMessage("查询异常");
            logger.error(" statisticBaseInfo findAllYear is error,",e);
        }
        return resp;
    }
}
