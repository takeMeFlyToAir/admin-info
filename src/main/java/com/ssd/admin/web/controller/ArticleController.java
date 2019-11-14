package com.ssd.admin.web.controller;

import com.ssd.admin.business.entity.ArticleEntity;
import com.ssd.admin.business.enums.ArticleStatusClaimEnum;
import com.ssd.admin.business.qo.ArticleQO;
import com.ssd.admin.business.service.ArticleService;
import com.ssd.admin.common.JsonResp;
import com.ssd.admin.common.PagerForDT;
import com.ssd.admin.common.PagerResultForDT;
import com.ssd.admin.util.FileDownloadUtil;
import com.ssd.admin.util.excel.ExcelDataUtil;
import org.apache.commons.lang3.StringUtils;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
    @RequestMapping(value = "findAllYear", method = {RequestMethod.GET,RequestMethod.POST})
    public JsonResp findAllYear() {
        JsonResp resp = new JsonResp();
        try {
            resp.isSuccess().setData(articleService.findAllYear());
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


    @ResponseBody
    @RequestMapping(value = "deleteByAut", method = {RequestMethod.GET,RequestMethod.POST})
    public JsonResp deleteByAut(String auts) {
        JsonResp resp = new JsonResp();

        try {
            if(StringUtils.isNotBlank(auts)){
                List<String> stringList = Arrays.asList(auts.split(","));
                for (String aut : stringList) {
                    ArticleEntity byAut = articleService.getByAut(aut);
                    if(byAut != null){
                        articleService.delete(byAut.getId());
                    }
                }
            }
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


    @RequestMapping(value = "/finish")
    @ResponseBody
    public JsonResp finish(){
        JsonResp resp = new JsonResp();
        try {
            articleService.finish();
            resp.isSuccess().setMessage("操作成功");
        }catch (Exception e){
            resp.isFail().setMessage("操作异常");
            logger.error("modifyArticleStatus is error,",e);
        }
        return resp;
    }


    @RequestMapping(value = "/importArticle")
    @ResponseBody
    public JsonResp importArticle(@RequestParam("file") MultipartFile file,@RequestParam(value = "subject",required = true) Integer subject
            ,@RequestParam(value = "inputYear",required = true) String inputYear){
        JsonResp resp = new JsonResp();
        try {
            ExcelDataUtil.ExcelData importArticle = ExcelDataUtil.readExcel(file, "ImportArticle");
            List<ArticleEntity> list =importArticle.getDatas();
            List<ExcelDataUtil.ErrorLine> errorList = importArticle.getErrorList();
            if(errorList != null && errorList.size() > 0){
                 resp.isFail().setMessage(errorList.get(0).error.get(0).getErrorMessage());
            }else {
                for (ArticleEntity articleEntity : list) {
                   try {
                       articleEntity.setHighCited(0);
                       articleEntity.setHotSpot(0);
                       articleEntity.setStatus(0);
                       articleEntity.setSubject(subject);
                       articleEntity.setInputYear(inputYear);
//                    articleEntity.setApd(getDateTime(articleEntity.getApd()));
                       if(StringUtils.isNotBlank(articleEntity.getAut())){
                           articleEntity.setAut(articleEntity.getAut().trim());
                       }
                       if(StringUtils.isBlank(articleEntity.getAtc())){
                           articleEntity.setAtc("0");
                       }
                       articleService.save(articleEntity);
                   }catch (Exception e){
                       e.printStackTrace();
                   }
                }
                resp.isSuccess().setMessage("导入成功");
            }
        }catch (Exception e){
            resp.isFail().setMessage("导入异常");
            logger.error("importArticle is error,",e);
        }
        return resp;
    }

    @RequestMapping(value = "/importHotSpotArticle")
    @ResponseBody
    public JsonResp importHotSpotArticle(@RequestParam("file") MultipartFile file){
        JsonResp resp = new JsonResp();
        try {
            ExcelDataUtil.ExcelData importArticle = ExcelDataUtil.readExcel(file, "ImportArticle");
            List<ArticleEntity> list =importArticle.getDatas();
            List<ExcelDataUtil.ErrorLine> errorList = importArticle.getErrorList();
            if(errorList != null && errorList.size() > 0){
                resp.isFail().setMessage(errorList.get(0).error.get(0).getErrorMessage());
            }else {
                for (ArticleEntity articleEntity : list) {
                    ArticleEntity byAut = articleService.getByAut(articleEntity.getAut());
                    if(byAut != null){
                        byAut.setHotSpot(1);
                        articleService.updateNotNull(byAut);
                    }
                }
                resp.isSuccess().setMessage("导入成功");
            }
        }catch (Exception e){
            resp.isFail().setMessage("导入异常");
            logger.error("importHotSpotArticle is error,",e);
        }
        return resp;
    }

    @RequestMapping(value = "/importHighCitedArticle")
    @ResponseBody
    public JsonResp importHighCitedArticle(@RequestParam("file") MultipartFile file){
        JsonResp resp = new JsonResp();
        try {
            ExcelDataUtil.ExcelData importArticle = ExcelDataUtil.readExcel(file, "ImportArticle");
            List<ArticleEntity> list =importArticle.getDatas();
            List<ExcelDataUtil.ErrorLine> errorList = importArticle.getErrorList();
            if(errorList != null && errorList.size() > 0){
                resp.isFail().setMessage(errorList.get(0).error.get(0).getErrorMessage());
            }else {
                for (ArticleEntity articleEntity : list) {
                    ArticleEntity byAut = articleService.getByAut(articleEntity.getAut());
                    if(byAut != null){
                        byAut.setHighCited(1);
                        articleService.updateNotNull(byAut);
                    }
                }
                resp.isSuccess().setMessage("导入成功");
            }
        }catch (Exception e){
            resp.isFail().setMessage("导入异常");
            logger.error("importHighCitedArticle is error,",e);
        }
        return resp;
    }


    /**
     * 解析时间
     */
    public   String getDateTime(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date d2 = null;
        String format= null;
        try {
            d2 = sdf.parse(time);
            format = formatter.format(d2);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return format;
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
