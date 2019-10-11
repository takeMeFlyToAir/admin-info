package com.ssd.admin.business.entity;


import com.ssd.admin.business.enums.ArticleStatusClaimEnum;
import com.ssd.admin.business.enums.SubjectEnum;
import com.ssd.admin.common.BaseEntity;
import lombok.Data;

import javax.persistence.Table;
import javax.persistence.Transient;

@Data
@Table(name = "business_article_claim")
public class ArticleClaimEntity extends BaseEntity {


    /**
     * 文章id
     */
    private Integer articleId;

    private Integer status;

    /**
     * 备注，用于驳回或者退领说明
     */
    private String remark;

    /**
     * 第几作者
     */
    private Integer author;

    /**
     * 作者类型：0老师，1学生
     */
    private Integer authorType;

    /**
     * 认领作者id
     */
    private Integer claimUserId;

    /**
     * 认领作者所属组织id
     */
    private Integer claimUserOrganizationId;


    /**
     * 认领作者所属组织id
     */
    private Integer claimUserOrganizationName;

    /**
     * 认领人账号
     */
    private String claimUserName;

    /**
     * 认领人昵称
     */
    private String claimNickName;

    /**
     * 操作人id
     */
    private Integer operateUserId;

    /**
     * 操作人所属组织id
     */
    private Integer operateUserOrganizationId;

    /**
     * 操作人所属组织id
     */
    private Integer operateUserOrganizationName;

    /**
     * 操作人账号
     */
    private String operateUserName;

    /**
     * 操作人昵称
     */
    private String operateNickName;


    /**
     * 审批人id
     */
    private Integer auditUserId;

    /**
     * 审批人所属组织id
     */
    private Integer auditUserOrganizationId;

    /**
     * 审批人所属组织id
     */
    private Integer auditUserOrganizationName;

    /**
     * 审批人账号
     */
    private String auditUserName;

    /**
     * 审批人昵称
     */
    private String auditNickName;



    /**
     * 学科：0：材料科学，10：工程学 20：化学，30：农业科学
     * 具体值见---SubjectEnum
     */
    private Integer subject;

    /**
     * 文章名
    */
    private String ati;
    /**
     * 刊物名
     */
    private String aso;
    /**
     * 文章类型
     */
    private String adt;
    /**
     * 著作人及单位
     */
    private String ac1;
    /**
     * 刊登日期
     */
    private String apd;
    /**
     * 刊登年份
     */
    private String apy;
    /**
     * 卷
     */
    private String avl;
    /**
     * 期
    */
    private String ais;
    /**
     * WoS文章编号
     */
    private String aut;



    @Transient
    private String subjectStr;


    @Transient
    private String statusDisplay;

    public String getSubjectStr(){
        return SubjectEnum.fromCode(subject).getDisplay();
    }

    public String getStatusDisplay(){
        String display = ArticleStatusClaimEnum.fromCode(status).getDisplay();
        return display;
    }

}