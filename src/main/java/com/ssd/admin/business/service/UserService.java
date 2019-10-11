package com.ssd.admin.business.service;


import com.ssd.admin.business.entity.UserEntity;
import com.ssd.admin.business.qo.UserModifyPasswordQO;
import com.ssd.admin.business.qo.UserQO;
import com.ssd.admin.business.vo.UserFindVO;
import com.ssd.admin.business.vo.UserVO;
import com.ssd.admin.common.IService;
import com.ssd.admin.common.PagerForDT;
import com.ssd.admin.common.PagerResultForDT;

import java.util.List;

/**
 * zhaozhirong
 */
public interface UserService extends IService<UserEntity> {

    /**
     * 分页查询
     * @param pager
     * @return
     */
    PagerResultForDT<UserFindVO> selectPage(PagerForDT<UserQO> pager);


    UserVO getUserByUserName(String userName);

    UserFindVO getUserById(Integer userId);

    Boolean isSysAdmin(Integer userId);

    void resetUserPassword(Integer userId, String password);

    void modifyPassword(UserModifyPasswordQO userModifyPasswordQO);

    Boolean hasUserName(String userName);

    List<UserFindVO> findByOrganizationIdAndUserType(Integer organizationId,Integer userType);
}
