package com.ssd.admin.business.mapper;


import com.ssd.admin.business.entity.PowerEntity;
import com.ssd.admin.common.MyMapper;

import java.util.List;

public interface PowerMapper extends MyMapper<PowerEntity> {

    List<PowerEntity> findByRoleCode(Integer roleCode);

    List<String> findRolePowerByUserId(Integer userId);
}