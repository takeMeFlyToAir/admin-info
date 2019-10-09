package com.ssd.admin.business.enums;

/**
 * 是否枚举
 */
public enum BooleanEnum {

    SYS_ADMIN("否",0),
    ADMIN("是",1),
    ;

    private String display;
    private int code;

    // 构造方法
    private BooleanEnum(String display, int code) {
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
    public static BooleanEnum fromCode(int code) {
        BooleanEnum[] codes = BooleanEnum.values();
        for (BooleanEnum state : codes) {
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
