package com.ssd.admin.business.entity;


import com.ssd.admin.business.enums.BooleanEnum;
import com.ssd.admin.business.enums.SubjectEnum;
import com.ssd.admin.common.BaseEntity;
import lombok.Data;

import javax.persistence.Table;
import javax.persistence.Transient;

@Data
@Table(name = "business_article")
public class ArticleEntity extends BaseEntity {

    /**
     * 是否高被引：0否，1是
     */
    private Integer highCited;

    /**
     * 是否热点：0否，1是
     */
    private Integer hotSpot;

    /**
     * 学科：0：材料科学，10：工程学 20：化学，30：农业科学
     * 具体值见---SubjectEnum
     */
    private Integer subject;

    private String apt;
    private String aau;
    private String aba;
    private String abe;
    private String agp;
    private String aaf;
    private String abf;
    private String aca;
    /**
     * 文章名
    */
    private String ati;
    /**
     * 刊物名
     */
    private String aso;
    private String ase;
    private String abs;
    private String ala;
    /**
     * 文章类型
     */
    private String adt;
    private String act;
    private String acy;
    private String acl;
    private String asp;
    private String aho;
    private String ade;
    private String aid;
    private String aab;
    /**
     * 著作人及单位
     */
    private String ac1;
    private String arp;
    private String aem;
    private String ari;
    private String aoi;
    private String afu;
    private String afx;
    private String acr;
    private String anr;
    private String atc;
    private String az9;
    private String au1;
    private String au2;
    private String apu;
    private String api;
    private String apa;
    private String asn;
    private String aei;
    private String abn;
    private String aj9;
    private String aji;
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
    private String apn;
    private String asu;
    private String asi;
    private String ama;
    private String abp;
    private String aep;
    private String aar;
    private String adi;
    private String ad2;
    private String aea;
    private String aey;
    private String apg;
    private String awc;
    private String aasc;
    private String aga;
    /**
     * WoS文章编号
     */
    private String aut;
    private String apm;
    private String aoa;
    private String ahc;
    private String ahp;
    private String ada;


    @Transient
    private String claimUserName;

    @Transient
    private String claimNickName;
    @Transient
    private String subjectStr;
    @Transient
    private String highCitedStr;
    @Transient
    private String hotSpotStr;

    //学科某年的总引用数
    @Transient
    private String allAtc;
    //引用占比
    @Transient
    private Double atcRate;

    public String getSubjectStr(){
        return SubjectEnum.fromCode(subject).getDisplay();
    }

    public String getHighCitedStr(){
        return BooleanEnum.fromCode(highCited).getDisplay();
    }

    public String getHotSpotStr(){
        return BooleanEnum.fromCode(hotSpot).getDisplay();
    }
}