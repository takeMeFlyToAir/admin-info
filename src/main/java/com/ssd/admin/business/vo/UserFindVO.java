package com.ssd.admin.business.vo;

import com.ssd.admin.business.enums.RoleEnum;
import lombok.Data;

/**
 * Created by zhaozhirong on 2019/9/18.
 */
@Data
public class UserFindVO {

    private Integer id;

    private String userName;

    private String nickName;

    private String roleDisplay;

    private Integer roleCode;

    private Integer organizationId;

    private String  organizationName;

    public String getRoleDisplay(){
        if(this.roleCode != null){
            return RoleEnum.fromCode(roleCode).getDisplay();
        }
        return RoleEnum.USER.getDisplay();
    }
}
