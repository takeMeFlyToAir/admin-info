package com.ssd.admin.web.controller;

import com.ssd.admin.business.service.UserService;
import com.ssd.admin.business.vo.UserVO;
import com.ssd.admin.common.JsonResp;
import com.ssd.admin.web.config.ShiroUser;
import com.ssd.admin.web.config.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Slf4j
@Controller
@RequestMapping(value = "/")
public class IndexController {

    @Autowired
    private UserService userService;

    @RequestMapping("")
    public String index(){
        ShiroUser shiroUser = UserUtil.getCurrentUser();
        System.out.println(shiroUser);
        return "index";
    }

    @RequestMapping("/ping")
    @ResponseBody
    public UserVO ping(String userName){
        return userService.getUserByUserName(userName);
    }

    @RequestMapping("/login")
    public String login(){
        return "login";
    }

    /**
     * 用户登陆
     *
     * @param userName
     * @param password
     * @return
     */
    @RequestMapping(value = "/ajaxLogin", method = RequestMethod.POST)
    @ResponseBody
    public JsonResp ajaxLoginCheck(
            @RequestParam(value = "userName") String userName,
            @RequestParam(value = "password") String password)
    {
        JsonResp resp = new JsonResp();
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(userName, password);
        try {
            subject.login(token);
            resp.setMessage("登录成功");
        } catch (UnknownAccountException e) {
            log.error("用户登录：",e);
            resp.isFail().setMessage(e.getMessage());
            resp = new JsonResp(false, e.getMessage());
        } catch (LockedAccountException e) {
            log.error("用户登录：",e);
            resp.isFail().setMessage(e.getMessage());
        } catch (AuthenticationException e) {
            log.error("用户登录：",e);
            resp.isFail().setMessage("用户名或密码错误");
        } catch(Exception e) {
            log.error("用户登录：",e);
            resp.isFail().setMessage("用户名或密码错误");
        }
        return resp;
    }

    /**
     * @return
     */
    @RequestMapping(value = "/logout",method = RequestMethod.GET)
    @ResponseBody
    public JsonResp logoutAjax() {
        Subject currentUser = SecurityUtils.getSubject();
        currentUser.logout();
        return new JsonResp("退出成功");
    }
}
