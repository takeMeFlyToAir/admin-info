package com.ssd.admin.web.config;

import com.ssd.admin.business.entity.OrganizationEntity;
import com.ssd.admin.business.service.OrganizationService;
import com.ssd.admin.business.service.UserService;
import com.ssd.admin.business.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;



@Slf4j
public class AccountAuthorizationRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    @Autowired
    private OrganizationService organizationService;

    /**
     * 查询获得用户信息 AuthenticationToken 用于收集用户提交的身份（如用户名）及凭据（如密码）
     *
     * AuthenticationInfo有两个作用： 1、如果Realm 是AuthenticatingRealm
     * 子类，则提供给AuthenticatingRealm 内部使用的
     * CredentialsMatcher进行凭据验证；（如果没有继承它需要在自己的Realm中自己实现验证）；
     * 2、提供给SecurityManager来创建Subject（提供身份信息）；
     * @param authcToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
        String userName = token.getUsername();
        String password = new String(token.getPassword());
        UserVO userVO = userService.getUserByUserName(userName);
        if (userVO == null) {
            // 用户不存在
            throw new UnknownAccountException("用户不存在");
        }
        if(!isPasswordRight(userVO.getPassword(),password)){
            //密码错误
            throw new AuthenticationException("用户名或密码错误");
        }
        OrganizationEntity organizationEntity = organizationService.selectByKey(userVO.getOrganizationId());
        ShiroUser shiroUser = new ShiroUser(userVO.getId(), userVO.getUserName(), userVO.getNickName(),userVO.getOrganizationId(),userVO.getRoleCode(),organizationEntity.getName());
        return new SimpleAuthenticationInfo(shiroUser, CyptoUtils.md5(new String(token.getPassword())), getName());
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        return info;
    }

    private Boolean isPasswordRight(String passwordDb,String passwordVO){
        if(passwordDb.equals(CyptoUtils.md5(passwordVO))){
            return true;
        }
        return false;
    }

}
