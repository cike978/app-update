package com.cike978.appupdate.bean;


import com.cike978.appupdate.uitls.UpdateTypeEnum;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * <p>
 * 应用升级-应用版本
 * </p>
 *
 * @author yqs
 * @since 2021-01-06
 */

public class AppVersionInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String appId;

    // @ApiModelProperty(value = "平台 1 android 2 ios")
    private Integer platform;

    // @ApiModelProperty(value = "版本号")

    private String appVersion;

    // @ApiModelProperty(value = "versioncode")
    private Long versionCode;


    //@ApiModelProperty(value = "更新描述")

    private String versionDescription;

    //   @ApiModelProperty(value = "发布状态（-1 已撤回 0 待发布 1已发布）")
    private Integer versionStatus = 0;

    //  @ApiModelProperty(value = "发布类型（0 普通发布 1定时发布）")
    private Integer releaseType = 0;

    //    @ApiModelProperty(value = "灰度发布（0-无；1-白名单发布；2-IP发布）")
    private Integer grayReleased = 0;

    // @ApiModelProperty(value = "白名单ID")
    private String whiteListId;

    //  @ApiModelProperty(value = "IP段发布的list ID")
    private String ipListId;


    // @ApiModelProperty(value = "创建人id")
    private Long createUserId;

    // @ApiModelProperty(value = "更新人id")
    private Long updateUserId;


    //@ApiModelProperty(value = "定时发布的发布时间")
    private Date planReleaseTime;

    // @ApiModelProperty(value = "升级类型 默认是一般升级")
    private int updateType = UpdateTypeEnum.PUBLIC.getCode();


    private List<AppFileInfo> appFileList = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public Integer getPlatform() {
        return platform;
    }

    public void setPlatform(Integer platform) {
        this.platform = platform;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public Long getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(Long versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionDescription() {
        return versionDescription;
    }

    public void setVersionDescription(String versionDescription) {
        this.versionDescription = versionDescription;
    }

    public Integer getVersionStatus() {
        return versionStatus;
    }

    public void setVersionStatus(Integer versionStatus) {
        this.versionStatus = versionStatus;
    }

    public Integer getReleaseType() {
        return releaseType;
    }

    public void setReleaseType(Integer releaseType) {
        this.releaseType = releaseType;
    }

    public Integer getGrayReleased() {
        return grayReleased;
    }

    public void setGrayReleased(Integer grayReleased) {
        this.grayReleased = grayReleased;
    }

    public String getWhiteListId() {
        return whiteListId;
    }

    public void setWhiteListId(String whiteListId) {
        this.whiteListId = whiteListId;
    }

    public String getIpListId() {
        return ipListId;
    }

    public void setIpListId(String ipListId) {
        this.ipListId = ipListId;
    }



    public Long getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    public Long getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(Long updateUserId) {
        this.updateUserId = updateUserId;
    }

    public Date getPlanReleaseTime() {
        return planReleaseTime;
    }

    public void setPlanReleaseTime(Date planReleaseTime) {
        this.planReleaseTime = planReleaseTime;
    }

    public int getUpdateType() {
        return updateType;
    }

    public void setUpdateType(int updateType) {
        this.updateType = updateType;
    }

    public List<AppFileInfo> getAppFileList() {
        return appFileList;
    }

    public void setAppFileList(List<AppFileInfo> appFileList) {
        this.appFileList = appFileList;
    }
}
