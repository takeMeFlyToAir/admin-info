package com.ssd.admin.business.serviceImpl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ssd.admin.business.entity.ArticleEntity;
import com.ssd.admin.business.enums.ArticleStatusEnum;
import com.ssd.admin.business.enums.RoleEnum;
import com.ssd.admin.business.mapper.ArticleMapper;
import com.ssd.admin.business.qo.ArticleQO;
import com.ssd.admin.business.service.ArticleService;
import com.ssd.admin.business.service.UserService;
import com.ssd.admin.business.vo.UserFindVO;
import com.ssd.admin.common.BaseService;
import com.ssd.admin.common.PagerForDT;
import com.ssd.admin.common.PagerResultForDT;
import com.ssd.admin.web.config.ShiroUser;
import com.ssd.admin.web.config.UserUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by zhaozhirong on 2019/3/1.
 */
@Service
public class ArticleServiceImpl extends BaseService<ArticleEntity> implements ArticleService {


    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private UserService userService;

    @Override
    public PagerResultForDT<ArticleEntity> selectPage(PagerForDT<ArticleQO> pager) {
        Example example = new Example(ArticleEntity.class);
        example.createCriteria().andEqualTo("deleted",0);
        List<ArticleEntity> allList = this.selectByExample(example);

        ArticleQO articleQO = pager.getCondition();
        example = new Example(ArticleEntity.class);
        Example.Criteria criteria = example.createCriteria()
                .andEqualTo("deleted",0);
        if(StringUtils.isNotBlank(articleQO.getAut())){
            criteria.andLike("aut","%"+articleQO.getAut()+"%");
        }
        if(StringUtils.isNotBlank(articleQO.getAti())){
            criteria.andLike("ati","%"+articleQO.getAti()+"%");
        }
        if(StringUtils.isNotBlank(articleQO.getAso())){
            criteria.andLike("aso","%"+articleQO.getAso()+"%");
        }
        if(StringUtils.isNotBlank(articleQO.getApy())){
            criteria.andLike("apy","%"+articleQO.getApy()+"%");
        }
        if(StringUtils.isNotBlank(articleQO.getAc1())){
            criteria.andLike("ac1","%"+articleQO.getAc1()+"%");
        }
        ShiroUser currentUser = UserUtil.getCurrentUser();
        //查找审核中，即认领中的
        if(StringUtils.isNotBlank(articleQO.getAudit())){
            //判断是否是超级管理员或者普通管理员：
            //如果是超级管理员，则查所有的待审核，即认领中的
            //如果是普通管理员，则查找所在组织的待审核，即认领中的

            RoleEnum roleEnum = RoleEnum.fromCode(currentUser.getRoleCode());
            if(roleEnum == RoleEnum.SYS_ADMIN || roleEnum == RoleEnum.ADMIN){
                criteria.andEqualTo("status", ArticleStatusEnum.CLAIM_ING.getCode());
                if(roleEnum == RoleEnum.ADMIN){
                    criteria.andEqualTo("claimUserOrganizationId", currentUser.getOrganizationId());
                }
            }else{
                criteria.andEqualTo("status", -1);
            }
        }

        //查找我的认领，即已认领的
        if(StringUtils.isNotBlank(articleQO.getClaim())){
            criteria.andEqualTo("status", ArticleStatusEnum.CLAIM_ED.getCode());
            criteria.andEqualTo("claimUserId",currentUser.getId());
        }

        PageHelper.offsetPage(pager.getiDisplayStart(),pager.getiDisplayLength());
        List<ArticleEntity> articleEntityList = this.selectByExample(example);
        PageInfo<ArticleEntity> pageInfo = new PageInfo<>(articleEntityList);
        PagerResultForDT<ArticleEntity> pagerResult = new PagerResultForDT<ArticleEntity>(articleEntityList,allList.size(),pageInfo.getTotal());
        return pagerResult;
    }



    @Override
    public void modifyArticleStatus(Integer id, ArticleStatusEnum articleStatusEnum) {

    }


}
