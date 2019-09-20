package com.ssd.admin.business.enums;

/**
 * 文章状态枚举
 */
public enum ArticleStatusEnum {

    WAIT_CLAIM("待认领",0),
    CLAIM_ING("认领中",10),
    CLAIM_ED("已认领",20),
    ;

    private String display;
    private int code;

    // 构造方法
    private ArticleStatusEnum(String display, int code) {
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
    public static ArticleStatusEnum fromCode(int code) {
        ArticleStatusEnum[] codes = ArticleStatusEnum.values();
        for (ArticleStatusEnum state : codes) {
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
