package com.ssd.admin.web.config;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

public class UserUtil {

	public static ShiroUser getCurrentUser() {
		Subject subject = SecurityUtils.getSubject();
		if(subject!=null) {
			ShiroUser user = (ShiroUser)subject.getPrincipal();
			return user;	
		}else {
			return null;
		}
	}
}
