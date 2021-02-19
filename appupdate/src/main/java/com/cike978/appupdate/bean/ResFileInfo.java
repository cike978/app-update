package com.cike978.appupdate.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 资源文件
 * </p>
 *
 * @author yqs
 * @since 2021-01-28
 */

public class ResFileInfo implements Serializable {

    private static final long serialVersionUID = 1L;


    private Long id;


    private String appId;


    private Long versionId;

    //@ApiModelProperty(value = "渠道id(暂时未用到)")
    private Long channelId;

    // @ApiModelProperty(value = "资源文件的MD5值")
    private String md5;

    // @ApiModelProperty(value = "资源文件id")
    private Long resFileId;
    // @ApiModelProperty(value = "资源包文件对象名称")
    private String objectName;


    //@ApiModelProperty(value = "标记渠道版本的安装包是否已上架（0-未上架；1-已上架）")
    private Integer deliveryStatus;



    private Long createUserId;

    private Long updateUserId;


    //@ApiModelProperty(value = "资源文件大小")
    private Long fileSize;

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

    public Long getResFileId() {
        return resFileId;
    }

    public void setResFileId(Long resFileId) {
        this.resFileId = resFileId;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
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

    public List<ResFileInfo> getResFileList() {
        return resFileList;
    }

    public void setResFileList(List<ResFileInfo> resFileList) {
        this.resFileList = resFileList;
    }
}
