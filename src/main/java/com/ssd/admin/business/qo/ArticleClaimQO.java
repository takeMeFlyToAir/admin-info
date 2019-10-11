package com.ssd.admin.business.qo;

import lombok.Data;

@Data
public class ArticleClaimQO {

    private Integer articleId;

    private Integer status;

    /**
     * 认领人账号
     */
    private String claimUserName;

    /**
     * 认领人昵称
     */
    private String claimNickName;

    /**
     * 操作人账号
     */
    private String operateUserName;

    /**
     * 操作人昵称
     */
    private String operateNickName;

    /**
     * 是否查找的是我的审核
     */
    private String audit;

    /**
     * 是否查找的是我的认领
     */
    private String claim;

    private String aut;
    private String ati;
    private String aso;
    private String apy;
    private String ac1;
}