package com.cike978.appupdate.bean;


import java.io.Serializable;

/**
 * 应用版本和资源版本信息
 *
 * @Author: yqs
 * @Date: 2021/2/1 16:27
 */
public class VersionDTO implements Serializable {
    private AppVersionInfo appVersion;
    private ResVersionInfo resVersion;

    private Integer appVersionErrorCode;
    private Integer resVersionErrorCode;

    private String appVersionMsg;
    private String resVersionMsg;

    public AppVersionInfo getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(AppVersionInfo appVersion) {
        this.appVersion = appVersion;
    }

    public ResVersionInfo getResVersion() {
        return resVersion;
    }

    public void setResVersion(ResVersionInfo resVersion) {
        this.resVersion = resVersion;
    }

    public Integer getAppVersionErrorCode() {
        return appVersionErrorCode;
    }

    public void setAppVersionErrorCode(Integer appVersionErrorCode) {
        this.appVersionErrorCode = appVersionErrorCode;
    }

    public Integer getResVersionErrorCode() {
        return resVersionErrorCode;
    }

    public void setResVersionErrorCode(Integer resVersionErrorCode) {
        this.resVersionErrorCode = resVersionErrorCode;
    }

    public String getAppVersionMsg() {
        return appVersionMsg;
    }

    public void setAppVersionMsg(String appVersionMsg) {
        this.appVersionMsg = appVersionMsg;
    }

    public String getResVersionMsg() {
        return resVersionMsg;
    }

    public void setResVersionMsg(String resVersionMsg) {
        this.resVersionMsg = resVersionMsg;
    }
}
