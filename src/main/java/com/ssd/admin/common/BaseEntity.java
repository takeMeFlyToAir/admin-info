package com.ssd.admin.common;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by zhaozhirong on 2019/3/4.
 */
public class BaseEntity {



    /**
     * 数据源ID
     */
    @Id
    @Column(name = "Id")
    @GeneratedValue(generator = "JDBC")
    protected Integer id;

    /**
     * 是否删除 0未删除，1已删除
     */
    protected Integer deleted;

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
