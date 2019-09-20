package com.ssd.admin.business.entity;


import com.ssd.admin.common.BaseEntity;
import lombok.Data;

import javax.persistence.Table;
import javax.persistence.Transient;

@Data
@Table(name = "sys_organization")
public class OrganizationEntity extends BaseEntity {
    private String name;

    private String remark;

}