package com.ssd.admin.business.serviceImpl;

import com.ssd.admin.business.entity.RolePowerEntity;
import com.ssd.admin.business.mapper.PowerMapper;
import com.ssd.admin.business.mapper.RolePowerMapper;
import com.ssd.admin.business.service.RolePowerService;
import com.ssd.admin.common.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaozhirong on 2019/3/1.
 */
@Service
public class RolePowerServiceImpl extends BaseService<RolePowerEntity> implements RolePowerService {


    @Autowired
    private RolePowerMapper rolePowerMapper;

    @Autowired
    private PowerMapper powerMapper;

    @Override
    public Integer deleteByPowerId(Integer powerId) {
        Example example = new Example(RolePowerEntity.class);
        example.createCriteria().andEqualTo("deleted",0).andEqualTo("powerId",powerId);
        RolePowerEntity rolePowerEntity = new RolePowerEntity();
        rolePowerEntity.setDeleted(1);
        return rolePowerMapper.updateByExampleSelective(rolePowerEntity,example);
    }

    @Override
    public Integer deleteByRoleCode(Integer roleCode) {
        Example example = new Example(RolePowerEntity.class);
        RolePowerEntity rolePowerEntity = new RolePowerEntity();
        rolePowerEntity.setDeleted(1);
        example.createCriteria().andEqualTo("deleted",0).andEqualTo("roleCode",roleCode);
        return rolePowerMapper.updateByExampleSelective(rolePowerEntity,example);
    }

    @Override
    public List<RolePowerEntity> findByRoleCode(Integer roleCode) {
        Example example = new Example(RolePowerEntity.class);
        example.setOrderByClause("id desc");

        example.createCriteria().andEqualTo("deleted",0).andEqualTo("roleCode",roleCode);
        return rolePowerMapper.selectByExample(example);
    }

    @Override
    public List<Integer> findPowerIdByRoleCode(Integer roleCode) {
        List<RolePowerEntity> rolePowerEntityList = this.findByRoleCode(roleCode);
        List<Integer> powerIdList = new ArrayList<>();
        if(rolePowerEntityList != null && rolePowerEntityList.size() > 0){
            for (RolePowerEntity rolePowerEntity : rolePowerEntityList){
                powerIdList.add(rolePowerEntity.getPowerId());
            }
        }
        return powerIdList;
    }

    @Override
    public List<String> findRolePowerByUserId(Integer id) {
        return powerMapper.findRolePowerByUserId(id);
    }
}
