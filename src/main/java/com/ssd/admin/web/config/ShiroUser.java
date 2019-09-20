package com.ssd.admin.web.config;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.Date;


public class ShiroUser implements Serializable {
	private static final long serialVersionUID = -1373760761780840081L;
	public Integer id;
	public String userName;
	public String nickName;
	public Integer roleCode;
	private Integer organizationId;

	public ShiroUser(Integer id, String userName, String nickName,Integer organizationId,Integer roleCode) {
		this.id = id;
		this.userName = userName;
		this.nickName = nickName;
		this.organizationId = organizationId;
		this.roleCode = roleCode;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public Integer getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(Integer roleCode) {
		this.roleCode = roleCode;
	}

	/**
	 * 本函数输出将作为默认的<shiro:principal/>输出.
	 */
	@Override
	public String toString() {
		return userName;
	}

	/**
	 * 重载equals,只计算name;
	 */
	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}
	
	/**
	 * 重载equals,只比较loginName
	 */
	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

}
