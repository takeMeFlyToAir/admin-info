package com.ssd.admin.business.mapper;


import com.ssd.admin.business.entity.UserEntity;
import com.ssd.admin.business.vo.UserVO;
import com.ssd.admin.common.MyMapper;

public interface UserMapper extends MyMapper<UserEntity> {

    UserVO findUserByUserName(String userName);

}