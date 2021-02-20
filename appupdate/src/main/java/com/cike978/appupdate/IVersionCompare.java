package com.cike978.appupdate;

import android.content.Context;

import com.cike978.appupdate.bean.AppVersionInfo;
import com.cike978.appupdate.bean.ResVersionInfo;

import java.io.Serializable;

/**
 * 版本号对比
 * Created by yqs97.
 * Date: 2020/8/24
 * Time: 17:29
 */
 public interface IVersionCompare extends Serializable {

    /**
     * 是否具有新版本的apk更新
     * @param appVersionInfo
     * @return
     */
     boolean hasNewVersionApk(Context context, AppVersionInfo appVersionInfo);

    /**
     * 是否有新的apk版本更新
     * @param resVersionInfo
     * @return
     */
    boolean hasNewVersionRes(Context context, ResVersionInfo resVersionInfo);

}
