package com.cike978.appupdate.bean;

import androidx.annotation.DrawableRes;

import com.cike978.appupdate.DefaultVersionCompare;
import com.cike978.appupdate.HttpManager;
import com.cike978.appupdate.IVersionCompare;

import java.io.Serializable;

/**
 * 更新配置
 * Created by yqs97.
 * Date: 2020/8/24
 * Time: 19:58
 */
public class UpdateConfig implements Serializable {

    /**
     * 服务器地址
     */
    private String serverUrl;

    /**
     * 文件服务器地址
     */
    private String downloadServerUrl;

    /**
     * 下载器
     */
    private HttpManager httpManager;


    /**
     * 版本比较器
     */
//    private IVersionCompare versionCompare;


    /**
     * 主题颜色
     */
    private int mThemeColor;
    /**
     * 上方图
     */
    @DrawableRes
    private int mTopPic;

    /**
     * 下载文件的文件夹路径
     */
    private String downloadDirPath;

    /**
     * 资源文件目录，请设置为     public static final String APP_ROOT = EXTERNAL_ROOT + File.separator + EnvironmentVariable.getProperty(KEY_SETTING_APPID);
     */
    private String appRootDir;


    public HttpManager getHttpManager() {
        return httpManager;
    }

    public void setHttpManager(HttpManager httpManager) {
        this.httpManager = httpManager;
    }

//    public IVersionCompare getVersionCompare() {
//        if (versionCompare == null) {
//            versionCompare = new DefaultVersionCompare();
//        }
//        return versionCompare;
//    }
//
//    public void setVersionCompare(IVersionCompare versionCompare) {
//        this.versionCompare = versionCompare;
//    }


    public int getmThemeColor() {
        return mThemeColor;
    }

    public void setmThemeColor(int mThemeColor) {
        this.mThemeColor = mThemeColor;
    }

    public int getmTopPic() {
        return mTopPic;
    }

    public void setmTopPic(int mTopPic) {
        this.mTopPic = mTopPic;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public String getDownloadServerUrl() {
        return downloadServerUrl;
    }

    public void setDownloadServerUrl(String downloadServerUrl) {
        this.downloadServerUrl = downloadServerUrl;
    }

    public String getDownloadDirPath() {
        return downloadDirPath;
    }

    public void setDownloadDirPath(String downloadDirPath) {
        this.downloadDirPath = downloadDirPath;
    }



    public String getAppRootDir() {
        return appRootDir;
    }

    public void setAppRootDir(String appRootDir) {
        this.appRootDir = appRootDir;
    }
}
