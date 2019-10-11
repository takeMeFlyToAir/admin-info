package com.ssd.admin.business.enums;

/**
 * 文章认领状态枚举
 */
public enum ArticleStatusClaimEnum {

    AUDIT_ING("审核中",0),
    REJECT("被驳回",10),
    AUDIT_ED("已通过",20),
    REJECT_CLAIM("已退领",30),
    ;

    private String display;
    private int code;

    // 构造方法
    private ArticleStatusClaimEnum(String display, int code) {
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
    public static ArticleStatusClaimEnum fromCode(int code) {
        ArticleStatusClaimEnum[] codes = ArticleStatusClaimEnum.values();
        for (ArticleStatusClaimEnum state : codes) {
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
