package com.ssd.admin.business.serviceImpl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ssd.admin.business.entity.ArticleClaimEntity;
import com.ssd.admin.business.entity.ArticleEntity;
import com.ssd.admin.business.entity.UserEntity;
import com.ssd.admin.business.enums.ArticleStatusClaimEnum;
import com.ssd.admin.business.enums.RoleEnum;
import com.ssd.admin.business.mapper.ArticleClaimMapper;
import com.ssd.admin.business.qo.ArticleClaimQO;
import com.ssd.admin.business.service.ArticleClaimService;
import com.ssd.admin.business.service.ArticleService;
import com.ssd.admin.business.service.UserService;
import com.ssd.admin.common.BaseService;
import com.ssd.admin.common.PagerForDT;
import com.ssd.admin.common.PagerResultForDT;
import com.ssd.admin.web.config.ShiroUser;
import com.ssd.admin.web.config.UserUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaozhirong on 2019/3/1.
 */
@Service
public class ArticleClaimServiceImpl extends BaseService<ArticleClaimEntity> implements ArticleClaimService {


    @Autowired
    private ArticleClaimMapper articleClaimMapper;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private UserService userService;

    @Override
    public PagerResultForDT<ArticleClaimEntity> selectPage(PagerForDT<ArticleClaimQO> pager) {
        Example example = new Example(ArticleClaimEntity.class);
        example.createCriteria().andEqualTo("deleted",0);
        List<ArticleClaimEntity> allList = this.selectByExample(example);

        ArticleClaimQO articleClaimQO = pager.getCondition();
        example = new Example(ArticleClaimEntity.class);
        Example.Criteria criteria = example.createCriteria()
                .andEqualTo("deleted",0);
        if(articleClaimQO.getArticleId() != null){
            criteria.andEqualTo("articleId", articleClaimQO.getArticleId());
        }
        if(articleClaimQO.getStatus() != null){
            criteria.andEqualTo("status", articleClaimQO.getStatus());
        }
        if(StringUtils.isNotBlank(articleClaimQO.getAut())){
            criteria.andLike("aut","%"+articleClaimQO.getAut()+"%");
        }
        if(StringUtils.isNotBlank(articleClaimQO.getAti())){
            criteria.andLike("ati","%"+articleClaimQO.getAti()+"%");
        }
        if(StringUtils.isNotBlank(articleClaimQO.getAso())){
            criteria.andLike("aso","%"+articleClaimQO.getAso()+"%");
        }
        if(StringUtils.isNotBlank(articleClaimQO.getApy())){
            criteria.andLike("apy","%"+articleClaimQO.getApy()+"%");
        }
        if(StringUtils.isNotBlank(articleClaimQO.getAc1())){
            criteria.andLike("ac1","%"+articleClaimQO.getAc1()+"%");
        }

        if(StringUtils.isNotBlank(articleClaimQO.getClaimNickName())){
            criteria.andLike("claimNickName","%"+articleClaimQO.getClaimNickName()+"%");
        }
        if(StringUtils.isNotBlank(articleClaimQO.getClaimUserName())){
            criteria.andLike("claimUserName","%"+articleClaimQO.getClaimUserName()+"%");
        }
        if(StringUtils.isNotBlank(articleClaimQO.getOperateNickName())){
            criteria.andLike("operateNickName","%"+articleClaimQO.getOperateNickName()+"%");
        }
        if(StringUtils.isNotBlank(articleClaimQO.getOperateUserName())){
            criteria.andLike("operateUserName","%"+articleClaimQO.getOperateUserName()+"%");
        }

        ShiroUser currentUser = UserUtil.getCurrentUser();
        //查找我的审核
        if(StringUtils.isNotBlank(articleClaimQO.getAudit())){
            //判断是否是超级管理员或者普通管理员：
            //如果是超级管理员，则查所有的审核中的
            //如果是普通管理员，则查找所在组织的审核中的

            RoleEnum roleEnum = RoleEnum.fromCode(currentUser.getRoleCode());
            if(roleEnum == RoleEnum.SYS_ADMIN || roleEnum == RoleEnum.ADMIN){
                criteria.andEqualTo("status", ArticleStatusClaimEnum.AUDIT_ING.getCode());
                if(roleEnum == RoleEnum.ADMIN){
                    criteria.andEqualTo("claimUserOrganizationId", currentUser.getOrganizationId());
                }
            }else{
                criteria.andEqualTo("status", -1);
            }
        }

        //查找我的认领
        if(StringUtils.isNotBlank(articleClaimQO.getClaim())){
            criteria.andEqualTo("claimUserId",currentUser.getId());
        }
        PageHelper.offsetPage(pager.getiDisplayStart(),pager.getiDisplayLength());
        List<ArticleClaimEntity> articleClaimEntityList = this.selectByExample(example);
        PageInfo<ArticleClaimEntity> pageInfo = new PageInfo<>(articleClaimEntityList);
        PagerResultForDT<ArticleClaimEntity> pagerResult = new PagerResultForDT<ArticleClaimEntity>(articleClaimEntityList,allList.size(),pageInfo.getTotal());
        return pagerResult;
    }



    @Override
    public void modifyArticleClaimStatus(Integer id, ArticleStatusClaimEnum articleStatusClaimEnum) {

    }

    @Override
    public void claimArticle(Integer articleId, Integer author, Integer claimUserId) throws InvocationTargetException, IllegalAccessException {
        ArticleEntity articleEntity = articleService.selectByKey(articleId);
        ArticleClaimEntity articleClaimEntity = new ArticleClaimEntity();
        BeanUtils.copyProperties(articleClaimEntity, articleEntity);
        ShiroUser operateUser = UserUtil.getCurrentUser();
        UserEntity claimUser = userService.selectByKey(claimUserId);
        articleClaimEntity.setArticleId(articleId);
        articleClaimEntity.setAuthor(author);
        articleClaimEntity.setId(null);
        //赋值认领信息
        articleClaimEntity.setClaimNickName(claimUser.getNickName());
        articleClaimEntity.setClaimUserName(claimUser.getUserName());
        articleClaimEntity.setClaimUserId(claimUserId);
        articleClaimEntity.setClaimUserOrganizationId(claimUser.getOrganizationId());
        //赋值操作人信息
        articleClaimEntity.setOperateNickName(operateUser.getNickName());
        articleClaimEntity.setOperateUserName(operateUser.getUserName());
        articleClaimEntity.setOperateUserId(operateUser.getId());
        articleClaimEntity.setOperateUserOrganizationId(operateUser.getOrganizationId());
        articleClaimEntity.setStatus(ArticleStatusClaimEnum.AUDIT_ING.getCode());
        articleClaimEntity.setRemark("");
        this.save(articleClaimEntity);
    }

    @Override
    public ArticleClaimEntity getByArticleIdAndAuthor(Integer articleId, Integer author) {
        Example example = new Example(ArticleClaimEntity.class);
        List<Integer> statusList = new ArrayList<>();
        statusList.add(ArticleStatusClaimEnum.AUDIT_ING.getCode());
        statusList.add(ArticleStatusClaimEnum.AUDIT_ED.getCode());
        example.createCriteria().andEqualTo("deleted",0).andEqualTo("articleId",articleId).andEqualTo("author",author).andIn("status",statusList);
        List<ArticleClaimEntity> articleClaimEntityList = this.selectByExample(example);
        if(articleClaimEntityList != null && articleClaimEntityList.size() > 0){
            return articleClaimEntityList.get(0);
        }
        return null;
    }

    @Override
    public List<Integer> findByArticleIdAndStatus(Integer articleId) {
        List<Integer> authorList = new ArrayList<>();
        Example example = new Example(ArticleClaimEntity.class);
        List<Integer> statusList = new ArrayList<>();
        statusList.add(ArticleStatusClaimEnum.AUDIT_ING.getCode());
        statusList.add(ArticleStatusClaimEnum.AUDIT_ED.getCode());
        example.createCriteria().andEqualTo("deleted",0).andEqualTo("articleId",articleId).andIn("status",statusList);
        List<ArticleClaimEntity> articleClaimEntityList = this.selectByExample(example);
        if(articleClaimEntityList != null && articleClaimEntityList.size() > 0){
            for (ArticleClaimEntity articleClaimEntity : articleClaimEntityList) {
                authorList.add(articleClaimEntity.getAuthor());
            }
        }
        return authorList;
    }
}
