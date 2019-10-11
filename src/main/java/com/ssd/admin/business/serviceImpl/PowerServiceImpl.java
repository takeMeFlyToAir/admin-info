package com.ssd.admin.business.serviceImpl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ssd.admin.business.entity.PowerEntity;
import com.ssd.admin.business.mapper.PowerMapper;
import com.ssd.admin.business.service.PowerService;
import com.ssd.admin.common.BaseService;
import com.ssd.admin.common.PagerForDT;
import com.ssd.admin.common.PagerResultForDT;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by zhaozhirong on 2019/3/1.
 */
@Service
public class PowerServiceImpl extends BaseService<PowerEntity> implements PowerService {


    @Autowired
    private PowerMapper powerMapper;

    @Override
    public PagerResultForDT<PowerEntity> selectPage(PagerForDT<String> pager) {
        Example example = new Example(PowerEntity.class);
        example.createCriteria().andEqualTo("deleted",0);
        List<PowerEntity> allList = this.selectByExample(example);

        String code = pager.getCondition();
        example = new Example(PowerEntity.class);
        Example.Criteria criteria = example.createCriteria()
                .andEqualTo("deleted",0);
        example.setOrderByClause("id desc");

        if(StringUtils.isNotEmpty(code)){
            criteria.andLike("code", "%"+code+"%");
        }
        PageHelper.offsetPage(pager.getiDisplayStart(),pager.getiDisplayLength());
        List<PowerEntity> powerEntityList = this.selectByExample(example);
        PageInfo<PowerEntity> pageInfo = new PageInfo<>(powerEntityList);
        PagerResultForDT<PowerEntity> pagerResult = new PagerResultForDT<PowerEntity>(powerEntityList,allList.size(),pageInfo.getTotal());
        return pagerResult;
    }

    @Override
    public Boolean validateCode(Integer id,String code) {
        if(id == null){
            return validateCode(code);
        }else {
            PowerEntity powerEntity = powerMapper.selectByPrimaryKey(id);
            // id不为空代表修改操作，如果转入的code为原来的code，则返回true
            if (code.equals(powerEntity.getCode())) {
                return true;
            } else { // 如果修改了名字，还是要对名字进行校验
                return validateCode(code);
            }
        }
    }

    private Boolean validateCode(String code) {
        Example example = new Example(PowerEntity.class);
        example.createCriteria().andEqualTo("deleted",0).andEqualTo("code",code);
        List<PowerEntity> allList = this.selectByExample(example);
        if (CollectionUtils.isEmpty(allList)) {
            return true;
        }
        return false;
    }

    @Override
    public List<PowerEntity> findAll() {
        Example example = new Example(PowerEntity.class);
        example.createCriteria().andEqualTo("deleted",0);
        List<PowerEntity> allList = this.selectByExample(example);
        return allList;
    }

    @Override
    public List<PowerEntity> findByRoleCode(Integer roleCode) {
        return powerMapper.findByRoleCode(roleCode);
    }
}
