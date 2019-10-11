package com.ssd.admin.business.enums;

/**
 * 用户类型枚举
 */
public enum UserTypeEnum {

    TEACHER("老师",0),
    STUDENT("学生",1)
    ;

    private String display;
    private int code;

    // 构造方法
    private UserTypeEnum(String display, int code) {
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
    public static UserTypeEnum fromCode(int code) {
        UserTypeEnum[] codes = UserTypeEnum.values();
        for (UserTypeEnum state : codes) {
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
