package com.ssd.admin.business.entity;


import com.ssd.admin.common.BaseEntity;
import lombok.Data;

import javax.persistence.Table;
import javax.persistence.Transient;

@Data
@Table(name = "sys_power")
public class PowerEntity extends BaseEntity {
    private String code;

    private String remark;

    /**
     * 0代表此权限尚未给某个角色分配过，1反之
     */
    @Transient
    private Integer rolePowerFlag;
}