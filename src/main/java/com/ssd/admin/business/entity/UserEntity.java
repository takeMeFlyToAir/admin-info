package com.ssd.admin.business.entity;

import com.ssd.admin.business.vo.UserFindVO;
import com.ssd.admin.common.BaseEntity;
import lombok.Data;

import javax.persistence.Table;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * UserEntity
 * @author zzr
 * @date 2017-7-4 下午2:49:38
 */
@Data
@Table(name = "sys_user")
public class UserEntity extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1972489869581873269L;

    private String userName;

    private String password;

    private Integer roleCode;

    private String nickName;

    private Integer organizationId;

    /**
     * 用户类型：0老师，1学生
     */
    private Integer userType;

    public static List<UserFindVO> toVOList(List<UserEntity> userEntityList){
        ArrayList<UserFindVO> userFindVOArrayList = new ArrayList<UserFindVO>();
        if(userEntityList != null){
            for (UserEntity userEntity : userEntityList){
                userFindVOArrayList.add(toVO(userEntity));
            }
        }
        return userFindVOArrayList;
    }

    public static UserFindVO toVO(UserEntity userEntity){
        UserFindVO userFindVO = new UserFindVO();
        userFindVO.setUserName(userEntity.getUserName());
        userFindVO.setNickName(userEntity.getNickName());
        userFindVO.setRoleCode(userEntity.getRoleCode());
        userFindVO.setId(userEntity.getId());
        userFindVO.setOrganizationId(userEntity.getOrganizationId());
        userFindVO.setUserType(userEntity.getUserType());
        return userFindVO;
    }
}