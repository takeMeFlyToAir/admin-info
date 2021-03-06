package com.ssd.admin.web.controller;

import com.ssd.admin.business.entity.PowerEntity;
import com.ssd.admin.business.service.PowerService;
import com.ssd.admin.business.service.RolePowerService;
import com.ssd.admin.common.JsonResp;
import com.ssd.admin.common.PagerForDT;
import com.ssd.admin.common.PagerResultForDT;
import com.ssd.admin.web.config.UserUtil;
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

/**
 * @Author: zhaozhirong
 * @Date: 2019/01/14
 * @Description:
 */
@Controller
@RequestMapping("power")
public class PowerController {

    private final Logger logger = LoggerFactory.getLogger(PowerController.class);

    @Autowired
    private PowerService powerService;

    @Autowired
    private RolePowerService rolePowerService;

    @ResponseBody
    @RequestMapping(value = "/findPage", method = RequestMethod.GET)
    public PagerResultForDT findPage(HttpServletRequest request, PagerForDT pagerForDataTable, String code) {
        pagerForDataTable.setCondition(code);
        PagerResultForDT pagerResult = powerService.selectPage(pagerForDataTable);
        return pagerResult.initsEcho(request.getParameter("sEcho"));
    }

    @ResponseBody
    @RequestMapping(value = "findAll", method = RequestMethod.GET)
    public JsonResp findAll() {
        JsonResp resp = new JsonResp();
        try {
            List<PowerEntity> powerEntityList = powerService.findAll();
            resp.isSuccess().setData(powerEntityList);
        }catch (Exception e){
            logger.error("power findAll is error", e);
            resp.isFail().setMessage("查询异常");
        }
       return resp;
    }

    @ResponseBody
    @RequestMapping(value = "findByRoleCode", method = RequestMethod.GET)
    public JsonResp findByRoleCode(Integer roleCode) {
        JsonResp resp = new JsonResp();
        try {
            List<PowerEntity> powerEntityList = powerService.findByRoleCode(roleCode);
            resp.isSuccess().setData(powerEntityList);
        }catch (Exception e){
            logger.error("power findByRoleCode is error", e);
            resp.isFail().setMessage("查询异常");
        }
        return resp;
    }

    @ResponseBody
    @RequestMapping(value = "validateCode", method = {RequestMethod.POST,RequestMethod.GET})
    public Boolean checkCode(@RequestParam("code") String code,
                             @RequestParam(value = "id",required = false) Integer id) {
        Boolean result = false;
        try {
            result = powerService.validateCode(id,code.trim());
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "addOrUpdate", method = RequestMethod.POST)
    public JsonResp addOrUpdate(PowerEntity powerEntity) {
        JsonResp resp = new JsonResp();
        try {
            if(powerEntity.getId() == null){
                powerService.save(powerEntity);
                resp.isSuccess().setMessage("保存成功");
            }else {
                powerService.updateNotNull(powerEntity);
                resp.isSuccess().setMessage("更新成功");
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            resp.isFail().setMessage("操作失败");
        }
        return resp;
    }

    @ResponseBody
    @RequestMapping(value = "deletePower", method = RequestMethod.POST)
    public JsonResp deletePower(Integer id) {
        JsonResp resp = new JsonResp();
        if(id == null){
            resp.isFail().setMessage("删除异常");
            return resp;
        }
        try {
            powerService.delete(id);
            rolePowerService.deleteByPowerId(id);
            resp.isSuccess().setMessage("删除成功");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            resp.isFail().setMessage(e.getMessage());
        }
        return resp;
    }


    @ResponseBody
    @RequestMapping(value = "appPower", method ={ RequestMethod.POST,RequestMethod.GET})
    public JsonResp loadAppPower() {
        JsonResp resp = new JsonResp();
        try {
            resp.isSuccess().setData(rolePowerService.findRolePowerByUserId(UserUtil.getCurrentUser().getId()));
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
            resp.isSuccess().setData(powerService.selectByKey(id));
        }catch (Exception e){
            resp.isFail().setMessage("查询异常");
            logger.error("get power info is error,",e);
        }
        return resp;
    }
}
