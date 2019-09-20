package com.ssd.admin.business.entity;


import com.ssd.admin.business.enums.ArticleStatusEnum;
import com.ssd.admin.common.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Table;
import javax.persistence.Transient;

@Data
@Table(name = "business_article")
public class ArticleEntity extends BaseEntity {

    private Integer status;

    private String title;

    private String author;

    private String organizationName;

    private String source;

    private String page;

    private String publishYear;

    private String subjectArea;

    private String influenceFactor;

    private String statisticsYear;

    private String quoteCount;

    private Integer claimUserId;

    private Integer claimUserOrganizationId;

    @Transient
    private String statusDisplay;

    @Transient
    private String claimUserName;

    @Transient
    private String claimNickName;

    public String getStatusDisplay(){
        String display = ArticleStatusEnum.fromCode(status).getDisplay();
        if(ArticleStatusEnum.fromCode(status) != ArticleStatusEnum.WAIT_CLAIM && claimUserId != null){
            display = String.format(display+"(%s-%s)",claimUserName,claimNickName);
        }
        return display;
    }
}