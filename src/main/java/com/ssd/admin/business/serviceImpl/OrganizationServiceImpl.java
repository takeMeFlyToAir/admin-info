package com.ssd.admin.business.serviceImpl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ssd.admin.business.entity.OrganizationEntity;
import com.ssd.admin.business.entity.PowerEntity;
import com.ssd.admin.business.mapper.OrganizationMapper;
import com.ssd.admin.business.service.OrganizationService;
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
public class OrganizationServiceImpl extends BaseService<OrganizationEntity> implements OrganizationService {


    @Autowired
    private OrganizationMapper organizationMapper;

    @Override
    public PagerResultForDT<OrganizationEntity> selectPage(PagerForDT<String> pager) {
        Example example = new Example(OrganizationEntity.class);
        example.createCriteria().andEqualTo("deleted",0);
        List<OrganizationEntity> allList = this.selectByExample(example);

        String name = pager.getCondition();
        example = new Example(OrganizationEntity.class);
        Example.Criteria criteria = example.createCriteria()
                .andEqualTo("deleted",0);
        example.setOrderByClause("id desc");

        if(StringUtils.isNotEmpty(name)){
            criteria.andLike("name", "%"+name+"%");
        }
        PageHelper.offsetPage(pager.getiDisplayStart(),pager.getiDisplayLength());
        List<OrganizationEntity> organizationEntityList = this.selectByExample(example);
        PageInfo<OrganizationEntity> pageInfo = new PageInfo<>(organizationEntityList);
        PagerResultForDT<OrganizationEntity> pagerResult = new PagerResultForDT<OrganizationEntity>(organizationEntityList,allList.size(),pageInfo.getTotal());
        return pagerResult;
    }

    @Override
    public Boolean validateName(Integer id,String name) {

        if(id == null){
            return validateName(name);
        }else {
            OrganizationEntity organizationEntity = organizationMapper.selectByPrimaryKey(id);
            // id不为空代表修改操作，如果转入的code为原来的code，则返回true
            if (name.equals(organizationEntity.getName())) {
                return true;
            } else { // 如果修改了名字，还是要对名字进行校验
                return validateName(name);
            }
        }
    }

    private Boolean validateName(String name) {
        Example example = new Example(OrganizationEntity.class);
        example.createCriteria().andEqualTo("deleted",0).andEqualTo("name",name);
        List<OrganizationEntity> allList = this.selectByExample(example);
        if (CollectionUtils.isEmpty(allList)) {
            return true;
        }
        return false;
    }

    @Override
    public List<OrganizationEntity> findAll() {
        Example example = new Example(OrganizationEntity.class);
        example.createCriteria().andEqualTo("deleted",0);
        List<OrganizationEntity> allList = this.selectByExample(example);
        return allList;
    }

}
