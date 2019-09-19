package com.ssd.admin.business.qo;

import lombok.Data;

@Data
public class UserModifyPasswordQO {
    private Integer id;
    private String oldPassword;
    private String newPassword;
}