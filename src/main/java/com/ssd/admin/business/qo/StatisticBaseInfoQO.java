package com.ssd.admin.business.qo;

import lombok.Data;

@Data
public class StatisticBaseInfoQO {

    private Integer year;

    /**
     * 学科：0：材料科学，10：工程学 20：化学，30：农业科学
     * 具体值见---SubjectEnum
     */
    private Integer subject;


}