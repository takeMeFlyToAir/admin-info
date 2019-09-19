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
    public Boolean hasCode(String code) {
        Example example = new Example(PowerEntity.class);
        example.createCriteria().andEqualTo("deleted",0).andEqualTo("code",code);
        List<PowerEntity> allList = this.selectByExample(example);
        if(allList != null && allList.size() > 0){
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
