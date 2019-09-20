package com.ssd.admin.business.service;


import com.ssd.admin.business.entity.OrganizationEntity;
import com.ssd.admin.business.entity.PowerEntity;
import com.ssd.admin.common.IService;
import com.ssd.admin.common.PagerForDT;
import com.ssd.admin.common.PagerResultForDT;

import java.util.List;

/**
 * @Author: zhaozhirong
 * @Date: 2019/01/08
 * @Description:
 */
public interface OrganizationService extends IService<OrganizationEntity> {

    /**
     * 分页查询
     * @param pager
     * @return
     */
    PagerResultForDT<OrganizationEntity> selectPage(PagerForDT<String> pager);


    Boolean validateName(Integer id,String name);

    List<OrganizationEntity> findAll();
}
