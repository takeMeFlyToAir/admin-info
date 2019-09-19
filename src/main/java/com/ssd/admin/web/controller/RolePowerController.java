package com.ssd.admin.web.controller;

import com.ssd.admin.business.entity.RolePowerEntity;
import com.ssd.admin.business.service.RolePowerService;
import com.ssd.admin.common.JsonResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @Author: zhaozhirong
 * @Date: 2019/04/28
 * @Description:
 */
@Controller
@RequestMapping("rolePower")
public class RolePowerController {

    private final Logger logger = LoggerFactory.getLogger(RolePowerController.class);

    @Autowired
    private RolePowerService rolePowerService;

    @ResponseBody
    @RequestMapping(value = "listRolePower", method = RequestMethod.GET)
    public List<Integer> listUser(Integer roleCode) {
        return rolePowerService.findPowerIdByRoleCode(roleCode);
    }

    @ResponseBody
    @RequestMapping(value = "addRolePower", method = RequestMethod.POST)
    public JsonResp addRolePower(@RequestParam(value = "ids") List<Integer> ids, @RequestParam(value = "roleCode")Integer roleCode) {
        JsonResp resp = new JsonResp();
        try {
            if(ids != null && ids.size() > 0){
                rolePowerService.deleteByRoleCode(roleCode);
                for (Integer id : ids){
                    RolePowerEntity rolePowerEntity = new RolePowerEntity();
                    rolePowerEntity.setRoleCode(roleCode);
                    rolePowerEntity.setPowerId(id);
                    rolePowerService.save(rolePowerEntity);
                }
            }
            resp.isSuccess().setMessage("success");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            resp.isFail().setMessage("保存异常");
        }
        return resp;
    }
}
