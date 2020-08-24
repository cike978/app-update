package com.cike978.appupdate.callback;

import com.cike978.appupdate.bean.UpdateBean;

/**
 * Created by yqs97.
 * Date: 2020/8/21
 * Time: 16:03
 */
public interface UpdateCallBack {

    /**
     *
     * @param updateBean
     * @param newVersion 新的版本号
     * @param versionCode 新的版本code
     * @return true 表示用户自己处理升级
     */
    boolean hasNewVersion(UpdateBean updateBean,String newVersion,Integer versionCode);
}
