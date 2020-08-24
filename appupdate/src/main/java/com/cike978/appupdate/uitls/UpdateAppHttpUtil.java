//package com.cike978.appupdate.uitls;
//
//
//
//import android.telecom.Call;
//
//import androidx.annotation.NonNull;
//
//import com.cike978.appupdate.HttpManager;
//
//import java.io.File;
//import java.util.Map;
//
//
//
///**
// * Created by Vector
// * on 2017/6/19 0019.
// */
//
//public class UpdateAppHttpUtil implements HttpManager {
//
//
//    /**
//     * 下载
//     *
//     * @param url      下载地址
//     * @param path     文件保存路径
//     * @param fileName 文件名称
//     * @param callback 回调
//     */
//    @Override
//    public void download(@NonNull String url, @NonNull String path, @NonNull String fileName, @NonNull final FileCallback callback) {
//        OkHttpUtils.get()
//                .url(url)
//                .build()
//                .execute(new FileCallBack(path, fileName) {
//                    @Override
//                    public void inProgress(float progress, long total, int id) {
//                        callback.onProgress(progress, total);
//                    }
//
//                    @Override
//                    public void onError(Call call, Response response, Exception e, int id) {
//                        callback.onError(validateError(e, response));
//                    }
//
//                    @Override
//                    public void onResponse(File response, int id) {
//                        callback.onResponse(response);
//
//                    }
//
//                    @Override
//                    public void onBefore(Request request, int id) {
//                        super.onBefore(request, id);
//                        callback.onBefore();
//                    }
//                });
//
//    }
//}