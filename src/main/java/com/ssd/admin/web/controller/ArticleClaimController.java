package com.ssd.admin.web.controller;

import com.ssd.admin.business.entity.ArticleClaimEntity;
import com.ssd.admin.business.enums.ArticleStatusClaimEnum;
import com.ssd.admin.business.qo.ArticleClaimQO;
import com.ssd.admin.business.service.ArticleClaimService;
import com.ssd.admin.common.JsonResp;
import com.ssd.admin.common.PagerForDT;
import com.ssd.admin.common.PagerResultForDT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @Author: zhaozhirong
 * @Date: 2019/01/14
 * @Description:
 */
@Controller
@RequestMapping("articleClaim")
public class ArticleClaimController {

    private final Logger logger = LoggerFactory.getLogger(ArticleClaimController.class);

    @Autowired
    private ArticleClaimService articleClaimService;

    @ResponseBody
    @RequestMapping(value = "/findPage", method = RequestMethod.GET)
    public PagerResultForDT findPage(HttpServletRequest request, PagerForDT pagerForDataTable, ArticleClaimQO articleClaimQO) {
        pagerForDataTable.setCondition(articleClaimQO);
        PagerResultForDT pagerResult = articleClaimService.selectPage(pagerForDataTable);
        return pagerResult.initsEcho(request.getParameter("sEcho"));
    }

    @ResponseBody
    @RequestMapping(value = "claimArticle", method ={ RequestMethod.POST,RequestMethod.GET})
    public JsonResp claimArticle(Integer articleId, Integer author, Integer claimUserId, Integer authorType) {
        JsonResp resp = new JsonResp();
        try {
            ArticleClaimEntity byArticleIdAndAuthor = articleClaimService.getByArticleIdAndAuthor(articleId, author,authorType);
            if(byArticleIdAndAuthor != null){
                resp.isFail().setMessage("已存在认领记录");
            }else {
                articleClaimService.claimArticle(articleId, author,claimUserId,authorType);
                resp.isSuccess().setMessage("认领成功");
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            resp.isFail().setMessage("认领失败");
        }
        return resp;
    }


    @ResponseBody
    @RequestMapping(value = "detail", method ={ RequestMethod.POST,RequestMethod.GET})
    public JsonResp detail(Integer id) {
        JsonResp resp = new JsonResp();
        try {
            resp.isSuccess().setData(articleClaimService.selectByKey(id));
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            resp.isFail().setMessage("查询异常");
        }
        return resp;
    }


    /**
     * 退领
     * @param id
     * @return
     */
    @RequestMapping(value = "/rejectClaim")
    @ResponseBody
    public JsonResp rejectClaim(Integer id, @RequestParam(value = "remark",required = false, defaultValue = "")String remark){
        JsonResp resp = new JsonResp();
        try {
            ArticleClaimEntity articleClaimEntity = articleClaimService.selectByKey(id);
            articleClaimEntity.setStatus(ArticleStatusClaimEnum.REJECT_CLAIM.getCode());
            articleClaimEntity.setRemark(remark);
            articleClaimService.updateNotNull(articleClaimEntity);
            resp.isSuccess().setMessage("退领成功");
        }catch (Exception e){
            resp.isFail().setMessage("操作异常");
            logger.error("rejectClaim is error,",e);
        }
        return resp;
    }

    /**
     * 审核
     * @param id
     * @return
     */
    @RequestMapping(value = "/audit")
    @ResponseBody
    public JsonResp audit(Integer id,Integer status, @RequestParam(value = "remark",required = false, defaultValue = "")String remark){
        JsonResp resp = new JsonResp();
        try {
            articleClaimService.audit(id,status,remark);
            resp.isSuccess().setMessage("审核成功");
        }catch (Exception e){
            resp.isFail().setMessage("操作异常");
            logger.error("audit is error,",e);
        }
        return resp;
    }


    @RequestMapping(value = "/findByArticleIdAndStatus")
    @ResponseBody
    public JsonResp findByArticleIdAndStatus(Integer articleId,Integer authorType){
        JsonResp resp = new JsonResp();
        try {
            List<Integer> allList = new ArrayList<>();
            for (int i = 1; i < 11; i++){
                allList.add(i);
            }
            List<Integer> byArticleIdAndStatus = articleClaimService.findByArticleIdAndStatus(articleId,authorType);
            allList.removeAll(byArticleIdAndStatus);
            resp.isSuccess().setData(allList);
        }catch (Exception e){
            resp.isFail().setMessage("查询异常");
            logger.error("findByArticleIdAndStatus is error,",e);
        }
        return resp;
    }
}
