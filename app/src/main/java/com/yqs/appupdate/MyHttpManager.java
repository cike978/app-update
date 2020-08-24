package com.yqs.appupdate;

import androidx.annotation.NonNull;

import com.cike978.appupdate.HttpManager;

import java.io.File;

/**
 * Created by yqs97.
 * Date: 2020/8/24
 * Time: 20:21
 */
public class MyHttpManager implements HttpManager {
    private static final String TAG = "MyHttpManager";

    @Override
    public void download(@NonNull String url, @NonNull String path, @NonNull String fileName, @NonNull final FileCallback callback) {
        callback.onBefore();
        TCommonRequestManager.getInstance().downLoadFileNew(TAG, url, fileName, path, new BaseRequestManager.ReqProgressCallBack<File>() {
            @Override
            public void onProgress(long total, long current) {
                float progress = (float) ((float) current / total);  //下载进度

                callback.onProgress(progress, total);
            }

            @Override
            public void onReqSuccess(File result) {
                callback.onResponse(result);
            }

            @Override
            public void onReqFailed(String errorMsg) {
                callback.onError(errorMsg);

            }
        });
    }
}
