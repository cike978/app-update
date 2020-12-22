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
     * 下载器
     */
    private HttpManager httpManager;

    /**
     * 版本比较器
     */
    private IVersionCompare versionCompare;


    /**
     * 主题颜色
     */
    private int mThemeColor;
    /**
     * 上方图
     */
    @DrawableRes
    private int mTopPic;


    public HttpManager getHttpManager() {
        return httpManager;
    }

    public void setHttpManager(HttpManager httpManager) {
        this.httpManager = httpManager;
    }

    public IVersionCompare getVersionCompare() {
        if (versionCompare == null) {
            versionCompare = new DefaultVersionCompare();
        }
        return versionCompare;
    }

    public void setVersionCompare(IVersionCompare versionCompare) {
        this.versionCompare = versionCompare;
    }


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
}
