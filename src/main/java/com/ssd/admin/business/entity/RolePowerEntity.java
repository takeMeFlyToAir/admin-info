package com.ssd.admin.business.entity;



import com.ssd.admin.common.BaseEntity;
import lombok.Data;

import javax.persistence.Table;

@Data
@Table(name = "sys_role_power")
public class RolePowerEntity extends BaseEntity {

    private Integer roleCode;
    private Integer powerId;


}