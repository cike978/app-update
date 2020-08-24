package com.cike978.appupdate;

import android.content.Context;

import com.cike978.appupdate.bean.ApkUpdateBean;
import com.cike978.appupdate.bean.ResUpdateBean;
import com.cike978.appupdate.bean.UpdateBean;

/**
 * 版本号对比
 * Created by yqs97.
 * Date: 2020/8/24
 * Time: 17:29
 */
 public interface IVersionCompare {

    /**
     * 是否具有新版本的apk更新
     * @param updateBean
     * @return
     */
     boolean hasNewVersionApk(Context context,ApkUpdateBean updateBean);

    /**
     * 是否有新的apk版本更新
     * @param updateBean
     * @return
     */
    boolean hasNewVersionRes(Context context,ResUpdateBean updateBean);

}
