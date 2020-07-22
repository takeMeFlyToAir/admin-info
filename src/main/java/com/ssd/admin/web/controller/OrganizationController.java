package com.ssd.admin.web.controller;

import com.ssd.admin.business.entity.OrganizationEntity;
import com.ssd.admin.business.service.OrganizationService;
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
@RequestMapping("organization")
public class OrganizationController {

    private final Logger logger = LoggerFactory.getLogger(OrganizationController.class);

    @Autowired
    private OrganizationService organizationService;

    @ResponseBody
    @RequestMapping(value = "/findPage", method = RequestMethod.GET)
    public PagerResultForDT findPage(HttpServletRequest request, PagerForDT pagerForDataTable, String name) {
        pagerForDataTable.setCondition(name);
        PagerResultForDT pagerResult = organizationService.selectPage(pagerForDataTable);
        return pagerResult.initsEcho(request.getParameter("sEcho"));
    }

    @ResponseBody
    @RequestMapping(value = "findAll", method = RequestMethod.GET)
    public JsonResp findAll() {
        JsonResp resp = new JsonResp();
        try {
            List<OrganizationEntity> powerEntityList = organizationService.findAll();
            resp.isSuccess().setData(powerEntityList);
        }catch (Exception e){
            logger.error("organization findAll is error", e);
            resp.isFail().setMessage("查询异常");
        }
       return resp;
    }

    @ResponseBody
    @RequestMapping(value = "validateName", method = {RequestMethod.POST,RequestMethod.GET})
    public Boolean checkCode(@RequestParam("name") String name,
                             @RequestParam(value = "id",required = false) Integer id) {
        Boolean result = false;
        try {
            result = organizationService.validateName(id, name.trim());
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "addOrUpdate", method = RequestMethod.POST)
    public JsonResp add(OrganizationEntity organizationEntity) {
        JsonResp resp = new JsonResp();
        try {
            if(organizationEntity.getId() == null){
                organizationService.save(organizationEntity);
                resp.isSuccess().setMessage("保存成功");
            }else {
                organizationService.updateNotNull(organizationEntity);
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
            organizationService.delete(id);
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
            resp.isSuccess().setData(organizationService.selectByKey(id));
        }catch (Exception e){
            resp.isFail().setMessage("查询异常");
            logger.error("get organization info is error,",e);
        }
        return resp;
    }
}
