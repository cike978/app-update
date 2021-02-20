package com.cike978.appupdate;

import android.content.Context;

import com.cike978.appupdate.bean.AppVersionInfo;
import com.cike978.appupdate.bean.ResVersionInfo;
import com.cike978.appupdate.uitls.AppUpdateUtils;
import com.cike978.appupdate.uitls.VersionCompareUtils;

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
     * @param appVersionInfo
     * @return
     */
    @Override
    public boolean hasNewVersionApk(Context context, AppVersionInfo appVersionInfo) {
        boolean hasNewVersion = AppUpdateUtils.getVersionCode(context) < appVersionInfo.getVersionCode()
                || VersionCompareUtils.compareVersion(appVersionInfo.getAppVersion(), AppUpdateUtils.getPackageInfo(context).versionName) > 0;
        return hasNewVersion;

    }


    /**
     * 是否有新的apk版本更新
     *
     * @param resVersionInfo
     * @return
     */
    @Override
    public boolean hasNewVersionRes(Context context, ResVersionInfo resVersionInfo) {
        String localResVersion = AppUpdateUtils.getLocalResVersion(context);


        boolean hasNewVersion = "0".equals(localResVersion)
                || VersionCompareUtils.compareVersion(resVersionInfo.getResVersion(), localResVersion) > 0;

        return hasNewVersion;

    }


}
