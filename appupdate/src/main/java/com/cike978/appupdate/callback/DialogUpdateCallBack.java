package com.cike978.appupdate.callback;

import com.cike978.appupdate.bean.ResVersionInfo;

/**
 * Created by yqs97.
 * Date: 2020/8/21
 * Time: 16:03
 */
public interface DialogUpdateCallBack {

    /**
     * 资源文件下载完成
     *
     * @param resDownloadPath 资源包下载路径
     * @param resVersionInfo  资源文件升级信息
     * @return true表示用户自己处理资源包的解压等操作，false表示用户不处理交给系统默认处理
     */
    boolean downLoadResFinish(String resDownloadPath, ResVersionInfo resVersionInfo);


    /**
     * 资源文件解压完成的回调
     * 只有 downLoadResFinish返回false才会回调
     *
     * @param isSuccess 是否成功
     */
    void resUnzipFinish(boolean isSuccess);

    /**
     * 忽略apk升级
     */
    void ignoreUpdateApk();

    /**
     * 忽略资源文件升级
     */
    void ignoreUpdateRes();
}
