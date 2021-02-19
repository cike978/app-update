package com.cike978.appupdate.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 资源文件版本
 * </p>
 *
 * @author yqs
 * @since 2021-01-28
 */

public class ResVersionInfo implements Serializable {

    private static final long serialVersionUID = 1L;


    private Long id;


    private String appId;

    //@ApiModelProperty(value = "平台 1 android 2 ios")
    private Integer platform;

    //版本号
    private String resVersion;

    //"更新描述")
    private String versionDescription;

    // @ApiModelProperty(value = "发布状态（-1 已撤回 0 待发布 1已发布）")
    private Integer versionStatus;

    // @ApiModelProperty(value = "灰度发布（0-无；1-白名单发布；2-IP发布）")
    private Integer grayReleased;

    // @ApiModelProperty(value = "白名单ID")
    private String whiteListId;

    // @ApiModelProperty(value = "IP段发布的list ID")
    private String ipListId;



    private Long createUserId;

    private Long updateUserId;


    //   @ApiModelProperty(value = "更新方式0：强制更新 1：一般更新 2：静默更新 3：可忽略更新 4：静默可忽略更新")
    private Integer updateType;

    private List<ResFileInfo> resFileList = new ArrayList<>();

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

    public String getResVersion() {
        return resVersion;
    }

    public void setResVersion(String resVersion) {
        this.resVersion = resVersion;
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

    public Integer getUpdateType() {
        return updateType;
    }

    public void setUpdateType(Integer updateType) {
        this.updateType = updateType;
    }

    public List<ResFileInfo> getResFileList() {
        return resFileList;
    }

    public void setResFileList(List<ResFileInfo> resFileList) {
        this.resFileList = resFileList;
    }
}
