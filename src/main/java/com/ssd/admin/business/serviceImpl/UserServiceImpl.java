package com.ssd.admin.business.serviceImpl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ssd.admin.business.entity.OrganizationEntity;
import com.ssd.admin.business.entity.UserEntity;
import com.ssd.admin.business.enums.RoleEnum;
import com.ssd.admin.business.mapper.UserMapper;
import com.ssd.admin.business.qo.UserModifyPasswordQO;
import com.ssd.admin.business.qo.UserQO;
import com.ssd.admin.business.service.OrganizationService;
import com.ssd.admin.business.service.UserService;
import com.ssd.admin.business.vo.UserFindVO;
import com.ssd.admin.business.vo.UserVO;
import com.ssd.admin.common.BaseService;
import com.ssd.admin.common.PagerForDT;
import com.ssd.admin.common.PagerResultForDT;
import com.ssd.admin.web.config.CyptoUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by zhaozhirong on 2019/3/1.
 */
@Service
public class UserServiceImpl extends BaseService<UserEntity> implements UserService {


    @Autowired
    private UserMapper userMapper;
    @Autowired
    private OrganizationService organizationService;

    @Override
    public PagerResultForDT<UserFindVO> selectPage(PagerForDT<UserQO> pager) {
        Example example = new Example(UserEntity.class);
        example.createCriteria().andEqualTo("deleted",0);
        List<UserEntity> allList = this.selectByExample(example);

        UserQO userQO = pager.getCondition();
        example = new Example(UserEntity.class);
        Example.Criteria criteria = example.createCriteria()
                .andEqualTo("deleted",0);
        if(StringUtils.isNotEmpty(userQO.getUserName())){
            criteria.andLike("userName", "%"+userQO.getUserName()+"%");
        }
        if(StringUtils.isNotEmpty(userQO.getNickName())){
            criteria.andLike("nickName", "%"+userQO.getNickName()+"%");
        }
        PageHelper.offsetPage(pager.getiDisplayStart(),pager.getiDisplayLength());
        List<UserEntity> userEntityList = this.selectByExample(example);
        PageInfo<UserEntity> pageInfo = new PageInfo<>(userEntityList);
        List<UserFindVO> userFindVOList = UserEntity.toVOList(userEntityList);
        for (UserFindVO userFindVO : userFindVOList) {
            OrganizationEntity organizationEntity = organizationService.selectByKey(userFindVO.getOrganizationId());
            if(organizationEntity != null){
                userFindVO.setOrganizationName(organizationEntity.getName());
            }
        }
        PagerResultForDT<UserFindVO> pagerResult = new PagerResultForDT<UserFindVO>(userFindVOList,allList.size(),pageInfo.getTotal());
        return pagerResult;
    }

    @Override
    public UserVO getUserByUserName(String userName) {
        return userMapper.findUserByUserName(userName);
    }

    @Override
    public Boolean isSysAdmin(Integer userId) {
        return userMapper.selectByPrimaryKey(userId) != null && RoleEnum.fromCode(userMapper.selectByPrimaryKey(userId).getRoleCode()) == RoleEnum.SYS_ADMIN;
    }

    @Override
    public void resetUserPassword(Integer userId, String password) {
        UserEntity userEntity = userMapper.selectByPrimaryKey(userId);
        if(userEntity != null){
            userEntity.setPassword(password);
            this.updateNotNull(userEntity);
        }
    }

    @Override
    public void modifyPassword(UserModifyPasswordQO userModifyPasswordQO) {
        UserEntity userEntity = userMapper.selectByPrimaryKey(userModifyPasswordQO.getId());
        if(userEntity != null){
            userEntity.setPassword(userModifyPasswordQO.getNewPassword());
            this.updateNotNull(userEntity);
        }
    }

    @Override
    public Boolean hasUserName(String userName) {
        Example example = new Example(UserEntity.class);
        example.createCriteria().andEqualTo("deleted",0).andEqualTo("userName",userName);
        List<UserEntity> allList = this.selectByExample(example);
        if(allList != null && allList.size() > 0){
            return true;
        }
        return false;
    }

    @Override
    public UserFindVO getUserById(Integer userId) {
        UserEntity userEntity = userMapper.selectByPrimaryKey(userId);
        UserFindVO userFindVO = UserEntity.toVO(userEntity);
        OrganizationEntity organizationEntity = organizationService.selectByKey(userFindVO.getOrganizationId());
        if(organizationEntity != null){
            userFindVO.setOrganizationName(organizationEntity.getName());
        }
        return userFindVO;
    }

    @Override
    public List<UserFindVO> findByOrganizationIdAndUserType(Integer organizationId,Integer userType) {
        Example example = new Example(UserEntity.class);
        example.createCriteria().andEqualTo("deleted",0).andEqualTo("organizationId",organizationId).andEqualTo("userType",userType);
        List<UserEntity> allList = this.selectByExample(example);
        return UserEntity.toVOList(allList);
    }
}
