package com.ssd.admin.web.controller;

import com.ssd.admin.business.entity.ArticleEntity;
import com.ssd.admin.business.enums.ArticleStatusEnum;
import com.ssd.admin.business.qo.ArticleQO;
import com.ssd.admin.business.service.ArticleService;
import com.ssd.admin.common.JsonResp;
import com.ssd.admin.common.PagerForDT;
import com.ssd.admin.common.PagerResultForDT;
import com.ssd.admin.util.FileDownloadUtil;
import com.ssd.admin.util.excel.ExcelDataUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: zhaozhirong
 * @Date: 2019/01/14
 * @Description:
 */
@Controller
@RequestMapping("article")
public class ArticleController {

    private final Logger logger = LoggerFactory.getLogger(ArticleController.class);

    @Autowired
    private ArticleService articleService;

    @ResponseBody
    @RequestMapping(value = "/findPage", method = RequestMethod.GET)
    public PagerResultForDT findPage(HttpServletRequest request, PagerForDT pagerForDataTable, ArticleQO articleQO) {
        pagerForDataTable.setCondition(articleQO);
        PagerResultForDT pagerResult = articleService.selectPage(pagerForDataTable);
        return pagerResult.initsEcho(request.getParameter("sEcho"));
    }

    @ResponseBody
    @RequestMapping(value = "addOrUpdate", method = RequestMethod.POST)
    public JsonResp addOrUpdate(ArticleEntity articleEntity) {
        JsonResp resp = new JsonResp();
        try {
            if(articleEntity.getId() == null){
                articleService.save(articleEntity);
                resp.isSuccess().setMessage("保存成功");
            }else {
                articleService.updateNotNull(articleEntity);
                resp.isSuccess().setMessage("更新成功");
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            resp.isFail().setMessage("操作失败");
        }
        return resp;
    }

    @ResponseBody
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public JsonResp delete(Integer id) {
        JsonResp resp = new JsonResp();
        if(id == null){
            resp.isFail().setMessage("删除异常");
            return resp;
        }
        try {
            articleService.delete(id);
            resp.isSuccess().setMessage("删除成功");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            resp.isFail().setMessage(e.getMessage());
        }
        return resp;
    }



    @RequestMapping(value = "/detail")
    @ResponseBody
    public JsonResp detail(Integer id){
        JsonResp resp = new JsonResp();
        try {
            resp.isSuccess().setData(articleService.selectByKey(id));
        }catch (Exception e){
            resp.isFail().setMessage("查询异常");
            logger.error("get article info is error,",e);
        }
        return resp;
    }


    @RequestMapping(value = "/modifyArticleStatus")
    @ResponseBody
    public JsonResp modifyArticleStatus(Integer id,Integer status){
        JsonResp resp = new JsonResp();
        try {
            articleService.modifyArticleStatus(id,ArticleStatusEnum.fromCode(status));
            resp.isSuccess().setMessage("操作成功");
        }catch (Exception e){
            resp.isFail().setMessage("操作异常");
            logger.error("modifyArticleStatus is error,",e);
        }
        return resp;
    }


    @RequestMapping(value = "/importArticle")
    @ResponseBody
    public JsonResp importArticle(@RequestParam("file") MultipartFile file){
        JsonResp resp = new JsonResp();
        try {
            ExcelDataUtil.ExcelData importUser = ExcelDataUtil.readExcel(file, "ImportArticle");
            List<ArticleEntity> list =importUser.getDatas();
            List<ExcelDataUtil.ErrorLine> errorList = importUser.getErrorList();
            if(errorList != null && errorList.size() > 0){
                 resp.isFail().setMessage(errorList.toString());
            }else {
                for (ArticleEntity articleEntity : list) {
                    articleEntity.setStatus(ArticleStatusEnum.WAIT_CLAIM.getCode());
                    articleService.save(articleEntity);
                }
                resp.isSuccess().setMessage("导入成功");
            }
        }catch (Exception e){
            resp.isFail().setMessage("导入异常");
            logger.error("importArticle is error,",e);
        }
        return resp;
    }


    @RequestMapping(value="/downloadTemplate",method = {RequestMethod.POST,RequestMethod.GET})
    public void downloadTemplate(HttpServletRequest request, HttpServletResponse response, Integer materEqpmTypeCde) {
        try {
            String filePath = request.getServletContext().getRealPath("/excel") + "/importArticle.xlsx";
            String fileName = "文章模板.xlsx";
            FileDownloadUtil.downloadFile(filePath, fileName, request, response);
        } catch (Exception e) {
            logger.error("downloadTemplate is error,",e);
        }
    }
}
