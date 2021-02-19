package com.cike978.appupdate.bean;


import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 应用升级-app安装文件信息表
 * </p>
 *
 * @author yqs
 * @since 2021-01-06
 */

public class AppFileInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;


    private String appId;

    private Long versionId;

    private Long channelId;

    private String md5;
    private String objectName;


    private Long appFileId;
    private String plistUrl;

    //"标记渠道版本的安装包是否已上架（0-未上架；1-已上架
    private Integer deliveryStatus;


    // @ApiModelProperty(value = "创建人id")
    private Long createUserId;

    //@ApiModelProperty(value = "更新人id")
    private Long updateUserId;


    // @ApiModelProperty(value = "安装包文件大小")
    private Long fileSize;

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

    public Long getVersionId() {
        return versionId;
    }

    public void setVersionId(Long versionId) {
        this.versionId = versionId;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public Long getAppFileId() {
        return appFileId;
    }

    public void setAppFileId(Long appFileId) {
        this.appFileId = appFileId;
    }

    public String getPlistUrl() {
        return plistUrl;
    }

    public void setPlistUrl(String plistUrl) {
        this.plistUrl = plistUrl;
    }

    public Integer getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(Integer deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
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

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }
}
