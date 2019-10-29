package com.ssd.admin.business.entity;


import com.ssd.admin.business.enums.SubjectEnum;
import com.ssd.admin.common.BaseEntity;
import lombok.Data;

import javax.persistence.Table;
import javax.persistence.Transient;

@Data
@Table(name = "business_statistic_base_info")
public class StatisticBaseInfoEntity extends BaseEntity {
    private Integer year;

    /**
     * 学科：0：材料科学，10：工程学 20：化学，30：农业科学
     * 具体值见---SubjectEnum
     */
    private Integer subject;


    private Integer allTc;

    /**
     * 总奖金
     */
    private Double bonus;

    @Transient
    private String subjectStr;

    public String getSubjectStr(){
        return SubjectEnum.fromCode(subject).getDisplay();
    }
}