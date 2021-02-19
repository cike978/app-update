package com.cike978.appupdate.uitls;

/**
 * 版本状态
 *
 * @Author: yqs
 * @Date: 2021/1/6 17:24
 * <p>
 * 更新方式0：强制更新 1：一般更新 2：静默更新 3：可忽略更新 4：静默可忽略更新
 */
public enum UpdateTypeEnum {

    FORCE(0, "强制更新"),
    PUBLIC(1, "一般更新"),
    SILENCE(2, "静默更新"),
    IGNORE(3, "可忽略更新"),
    SILENCE_IGNORE(4, "静默可忽略更新"),
    UNKNOWN(4, "静默可忽略更新");

    private int code;
    private String note;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    UpdateTypeEnum(int code, String note) {
        this.code = code;
        this.note = note;
    }

    public static UpdateTypeEnum getType(int code) {
        for (UpdateTypeEnum versionStatusEnum : UpdateTypeEnum.values()) {
            if (code == versionStatusEnum.code) {
                return versionStatusEnum;
            }
        }
        return UNKNOWN;

    }
}
