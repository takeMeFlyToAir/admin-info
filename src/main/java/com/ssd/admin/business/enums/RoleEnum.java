package com.ssd.admin.business.enums;

/**
 * 角色枚举
 */
public enum RoleEnum {

    SYS_ADMIN("超级管理员",0),
    ADMIN("普通管理员",10),
    USER("用户",20),
    ;

    private String display;
    private int code;

    // 构造方法
    private RoleEnum(String display, int code) {
        this.display = display;
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public String getValue() {
        return name();
    }

    /**
     * 根据code获得名称
     *
     * @param code
     * @return
     */
    public static RoleEnum fromCode(int code) {
        RoleEnum[] codes = RoleEnum.values();
        for (RoleEnum state : codes) {
            if (state.getCode() == code) {
                return state;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return this.display;
    }

    public String getDisplay() {
        return display;
    }
}
