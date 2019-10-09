package com.ssd.admin.business.enums;

/**
 * 学科枚举
 */
public enum SubjectEnum {

    MATERIAL_SCIENCE("材料科学",0),
    ENGINEERING("工程学",10),
    CHEMISTRY("化学",20),
    AGRICULTURAL_SCIENCE("农业科学",30),
    ;

    private String display;
    private int code;

    // 构造方法
    private SubjectEnum(String display, int code) {
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
    public static SubjectEnum fromCode(int code) {
        SubjectEnum[] codes = SubjectEnum.values();
        for (SubjectEnum state : codes) {
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
