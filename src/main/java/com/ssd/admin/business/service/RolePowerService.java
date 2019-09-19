package com.ssd.admin.business.service;



import com.ssd.admin.business.entity.RolePowerEntity;
import com.ssd.admin.common.IService;

import java.util.List;

/**
 * @Author: zhaozhirong
 * @Date: 2019/01/08
 * @Description:
 */
public interface RolePowerService extends IService<RolePowerEntity> {

    Integer deleteByPowerId(Integer powerId);

    Integer deleteByRoleCode(Integer roleCode);

    List<RolePowerEntity> findByRoleCode(Integer roleCode);

    List<Integer> findPowerIdByRoleCode(Integer roleCode);

    List<String> findRolePowerByUserId(Integer id);
}
