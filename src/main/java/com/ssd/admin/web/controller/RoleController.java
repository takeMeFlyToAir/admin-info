package com.ssd.admin.web.controller;

import com.ssd.admin.business.enums.RoleEnum;
import com.ssd.admin.common.PagerForDT;
import com.ssd.admin.common.PagerResultForDT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: zhaozhirong
 * @Date: 2019/01/14
 * @Description:
 */
@Controller
@RequestMapping("role")
public class RoleController {

    private final Logger logger = LoggerFactory.getLogger(RoleController.class);

    @ResponseBody
    @RequestMapping(value = "/findPage", method = RequestMethod.GET)
    public PagerResultForDT listUser(HttpServletRequest request, PagerForDT pagerForDataTable, String code) {
        List<Map<String,Object>> roleList = new ArrayList<>();
        for (RoleEnum roleEnum : RoleEnum.values()){
            if(roleEnum != RoleEnum.SYS_ADMIN){
                Map<String,Object> map = new HashMap<>();
                map.put("name",roleEnum);
                map.put("display",roleEnum.getDisplay());
                map.put("code",roleEnum.getCode());
                roleList.add(map);
            }
        }
        pagerForDataTable.setCondition(code);
        PagerResultForDT pagerResult = new PagerResultForDT();
        pagerResult.setData(roleList);
        pagerResult.setRecordsFiltered(roleList.size());
        pagerResult.setRecordsTotal(roleList.size());
        return pagerResult.initsEcho(request.getParameter("sEcho"));
    }
}
