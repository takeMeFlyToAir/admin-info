package com.ssd.admin.web.controller;

import com.ssd.admin.business.entity.UserEntity;
import com.ssd.admin.business.enums.RoleEnum;
import com.ssd.admin.business.qo.UserEditQO;
import com.ssd.admin.business.qo.UserModifyPasswordQO;
import com.ssd.admin.business.qo.UserQO;
import com.ssd.admin.business.service.UserService;
import com.ssd.admin.business.vo.UserFindVO;
import com.ssd.admin.common.JsonResp;
import com.ssd.admin.common.PagerForDT;
import com.ssd.admin.common.PagerResultForDT;
import com.ssd.admin.web.config.CyptoUtils;
import com.ssd.admin.web.config.UserUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Controller
@RequestMapping(value = "/user")
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @ResponseBody
    @RequestMapping(value = "findByOrganizationId", method = RequestMethod.GET)
    public JsonResp findByOrganizationId(Integer organizationId,Integer userType) {
        JsonResp resp = new JsonResp();
        try {
            List<UserFindVO> userFindVOList = userService.findByOrganizationIdAndUserType(organizationId,userType);
            resp.isSuccess().setData(userFindVOList);
        }catch (Exception e){
            logger.error("user findAll is error", e);
            resp.isFail().setMessage("查询异常");
        }
        return resp;
    }

    @RequestMapping(value = "/add")
    @ResponseBody
    public JsonResp add(UserEntity userEntity){
        JsonResp resp = new JsonResp();
        try {
            userEntity.setPassword(CyptoUtils.md5(userEntity.getPassword()));
            userEntity.setRoleCode(RoleEnum.USER.getCode());
            userService.save(userEntity);
            resp.isSuccess().setMessage("保存成功");
        }catch (Exception e){
            resp.isFail().setMessage("保存异常");
            logger.error("add user is error,",e);
        }
        return resp;
    }

    @ResponseBody
    @RequestMapping(value = "checkUserName", method = {RequestMethod.POST,RequestMethod.GET})
    public Boolean checkCode(String userName) {
        Boolean result = false;
        try {
            Boolean hasCode = userService.hasUserName(userName.trim());
            result =  !hasCode;
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return result;
    }


    @RequestMapping(value = "/edit")
    @ResponseBody
    public JsonResp edit(UserEditQO userEditQO){
        JsonResp resp = new JsonResp();
        try {
            UserEntity user = userService.selectByKey(userEditQO.getId());
            if(user == null){
                resp.isFail().setMessage("修改异常");
                return resp;
            }
            if(StringUtils.isNotBlank(userEditQO.getNickName())){
                user.setNickName(userEditQO.getNickName());
            }
            if(userEditQO.getOrganizationId() != null){
                user.setOrganizationId(userEditQO.getOrganizationId());
            }
            user.setUserType(userEditQO.getUserType());
            userService.updateNotNull(user);
            resp.isSuccess().setMessage("修改成功");
        }catch (Exception e){
            resp.isFail().setMessage("修改异常");
            logger.error("reset user password is error,",e);
        }
        return resp;
    }

    @RequestMapping(value = "/delete")
    @ResponseBody
    public JsonResp delete(Integer id){
        JsonResp resp = new JsonResp();
        try {
            UserEntity user = userService.selectByKey(id);
            if(user == null){
                resp.isFail().setMessage("删除异常");
                return resp;
            }
            user.setDeleted(1);
            userService.updateNotNull(user);
            resp.isSuccess().setMessage("删除成功");
        }catch (Exception e){
            resp.isFail().setMessage("删除异常");
            logger.error("delete user is error,",e);
        }
        return resp;
    }

    @RequestMapping(value = "/resetPassword")
    @ResponseBody
    public JsonResp resetPassword(Integer userId,String password){
        JsonResp resp = new JsonResp();
        try {
            userService.resetUserPassword(userId,CyptoUtils.md5(password));
            resp.isSuccess().setMessage("重置成功");
        }catch (Exception e){
            resp.isFail().setMessage("重置异常");
            logger.error("reset user password is error,",e);
        }
        return resp;
    }

    @RequestMapping(value = "/modifyPassword")
    @ResponseBody
    public JsonResp modifyPassword(UserModifyPasswordQO userModifyPasswordQO){
        JsonResp resp = new JsonResp();
        try {
            userModifyPasswordQO.setId(UserUtil.getCurrentUser().getId());
            UserEntity user = userService.selectByKey(userModifyPasswordQO.getId());
            if(user == null){
                resp.isFail().setMessage("修改异常");
                return resp;
            }
            if(!user.getPassword().equals(CyptoUtils.md5(userModifyPasswordQO.getOldPassword()))){
                resp.isFail().setMessage("原密码输入错误");
                return resp;
            }
            userModifyPasswordQO.setNewPassword(CyptoUtils.md5(userModifyPasswordQO.getNewPassword()));
            userService.modifyPassword(userModifyPasswordQO);
            resp.isSuccess().setMessage("修改成功");
        }catch (Exception e){
            resp.isFail().setMessage("修改异常");
            logger.error("reset user password is error,",e);
        }
        return resp;
    }


    /**
     * 分页查询
     * @param request
     * @param pagerForDataTable
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/findPage")
    public PagerResultForDT findPage(HttpServletRequest request, PagerForDT pagerForDataTable, UserQO userQO) {
        pagerForDataTable.setCondition(userQO);
        PagerResultForDT pagerResult = userService.selectPage(pagerForDataTable);
        return pagerResult.initsEcho(request.getParameter("sEcho"));
    }

    @ResponseBody
    @RequestMapping(value = "/modifyUserRole", method = {RequestMethod.POST,RequestMethod.GET})
    public JsonResp modifySuperAdmin(Integer userId,Integer roleCode) {
        JsonResp resp = new JsonResp();
        try {
            UserEntity user = userService.selectByKey(userId);
            if(user == null){
                resp.isFail().setMessage("修改异常");
                return resp;
            }
            if(roleCode != null){
                user.setRoleCode(roleCode);
                userService.updateNotNull(user);
            }
            resp.isSuccess().setMessage("修改成功");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            resp.isFail().setMessage("修改异常");
        }
        return resp;
    }


    @ResponseBody
    @RequestMapping(value = "isSysAdmin", method ={ RequestMethod.POST,RequestMethod.GET})
    public JsonResp isSysAdmin() {
        JsonResp resp = new JsonResp();
        try {
            resp.isSuccess().setData(userService.isSysAdmin(UserUtil.getCurrentUser().getId()));
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            resp.isFail().setMessage("查询异常");
        }
        return resp;
    }

    @RequestMapping(value = "/info")
    @ResponseBody
    public JsonResp info(){
        JsonResp resp = new JsonResp();
        try {
            resp.isSuccess().setData(userService.getUserById(UserUtil.getCurrentUser().getId()));
        }catch (Exception e){
            resp.isFail().setMessage("查询异常");
            logger.error("get login user info is error,",e);
        }
        return resp;
    }


    @RequestMapping(value = "/detail")
    @ResponseBody
    public JsonResp detail(Integer id){
        JsonResp resp = new JsonResp();
        try {
            resp.isSuccess().setData(userService.getUserById(id));
        }catch (Exception e){
            resp.isFail().setMessage("查询异常");
            logger.error("get login user info is error,",e);
        }
        return resp;
    }
}
