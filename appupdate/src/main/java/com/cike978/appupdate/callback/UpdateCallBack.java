package com.cike978.appupdate.callback;

import com.cike978.appupdate.bean.ResUpdateBean;

/**
 * Created by yqs97.
 * Date: 2020/8/21
 * Time: 16:03
 */
public interface UpdateCallBack {

    /**
     * 资源文件下载完成
     * @param resDownloadPath
     * @param resUpdateBean
     */
    void downLoadResFinish(String resDownloadPath, ResUpdateBean resUpdateBean);

    void ignoreUpdateApk();

    void ignoreUpdateRes();
}
