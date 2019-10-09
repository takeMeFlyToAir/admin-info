package com.ssd.admin.business.qo;

import lombok.Data;

@Data
public class ArticleQO {
    private String aut;
    private String ati;
    private String aso;
    private String apy;
    private String ac1;

    /**
     * 是否查找的是我的审核
     */
    private String audit;

    /**
     * 是否查找的是我的认领
     */
    private String claim;
}