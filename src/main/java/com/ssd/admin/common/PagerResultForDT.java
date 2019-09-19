package com.ssd.admin.common;

import java.io.Serializable;
import java.util.List;

/**
 * Bootstrap Table返回值
 * @author zzr
 *
 */
public class PagerResultForDT<T> implements Serializable {

	private static final long serialVersionUID = 3141067807832984876L;


	private List<T> data;

	/**总条数*/
	private long recordsTotal;

    /**
     * 过滤后的总条数
	 */
	private long recordsFiltered;

	private String sEcho;

	public PagerResultForDT(){
	}

	public PagerResultForDT(List<T> data, long recordsTotal, long recordsFiltered) {
		this.data = data;
		this.recordsTotal = recordsTotal;
		this.recordsFiltered = recordsFiltered;
	}

	public PagerResultForDT initsEcho(String sEcho){
		this.setsEcho(sEcho);
		return this;
	}

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

	public long getRecordsTotal() {
		return recordsTotal;
	}

	public void setRecordsTotal(long recordsTotal) {
		this.recordsTotal = recordsTotal;
	}

	public long getRecordsFiltered() {
		return recordsFiltered;
	}

	public void setRecordsFiltered(long recordsFiltered) {
		this.recordsFiltered = recordsFiltered;
	}

	public String getsEcho() {
		return sEcho;
	}

	public void setsEcho(String sEcho) {
		this.sEcho = sEcho;
	}
}

