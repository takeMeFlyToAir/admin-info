package com.ssd.admin.business.qo;

import lombok.Data;

@Data
public class UserEditQO {
    private Integer id;
    private String userName;
    private String nickName;
    private Integer organizationId;
}