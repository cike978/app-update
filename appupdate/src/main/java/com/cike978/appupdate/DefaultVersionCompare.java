package com.cike978.appupdate;

import android.content.Context;

import com.cike978.appupdate.bean.ApkUpdateBean;
import com.cike978.appupdate.bean.ResUpdateBean;
import com.cike978.appupdate.uitls.AppUpdateUtils;

/**
 * 默认的版本号比对
 * Created by yqs97.
 * Date: 2020/8/24
 * Time: 17:29
 */
public class DefaultVersionCompare implements IVersionCompare {

    /**
     * 是否具有新版本的apk更新
     *
     * @param updateBean
     * @return
     */
    @Override
    public boolean hasNewVersionApk(Context context, ApkUpdateBean updateBean) {
        boolean hasNewVersion = AppUpdateUtils.getVersionCode(context) < updateBean.getVersionCode();

        return hasNewVersion;

    }


    /**
     * 是否有新的apk版本更新
     *
     * @param updateBean
     * @return
     */
    @Override
    public boolean hasNewVersionRes(Context context, ResUpdateBean updateBean) {
        String localResVersion = AppUpdateUtils.getLocalResVersion(context);
        boolean hasNewVersion = "0".equals(localResVersion)
                || updateBean.getVersion().compareTo(localResVersion) > 0;

        return hasNewVersion;

    }


}
