package com.ssd.admin.common;

import java.io.Serializable;


public class PagerForDT<T> implements Serializable{

	private static final long serialVersionUID = 1L;

	private int iDisplayStart = 0;

	private int iDisplayLength = 10;

	private String sSearch;

	private T condition;

	private String sortName;//排序名称

	private String sortOrder;//排序规则 desc 或者 asc


	public PagerForDT(){
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public int getiDisplayStart() {
		return iDisplayStart;
	}

	public void setiDisplayStart(int iDisplayStart) {
		this.iDisplayStart = iDisplayStart;
	}

	public int getiDisplayLength() {
		return iDisplayLength;
	}

	public void setiDisplayLength(int iDisplayLength) {
		this.iDisplayLength = iDisplayLength;
	}

	public T getCondition() {
		return condition;
	}

	public void setCondition(T condition) {
		this.condition = condition;
	}

	public String getSortName() {
		return sortName;
	}

	public void setSortName(String sortName) {
		this.sortName = sortName;
	}

	public String getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}

	public String getsSearch() {
		if(sSearch == null){
			return "";
		}
		return sSearch;
	}

	public void setsSearch(String sSearch) {
		this.sSearch = sSearch;
	}
}

