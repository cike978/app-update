package com.yqs.appupdate;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;


import com.cike978.appupdate.uitls.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.Dispatcher;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 通用okhttp请求处理器
 *
 * @desc :
 * @version: 1.0
 */
public abstract class BaseRequestManager {
    private static final String TAG = BaseRequestManager.class.getSimpleName();
    //非response的错误码
    private static final int FAIL_CODE = -1;

    private static final MediaType MEDIA_OBJECT_STREAM = MediaType.parse("application/octet-stream");//mdiatype 这个需要和服务端保持一致
    private static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");//mdiatype 这个需要和服务端保持一致
    private static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");//mdiatype 这个需要和服务端保持一致
    private static final MediaType MEDIA_TYPE_BASE = MediaType.parse("x-www-form-urlencoded; charset=utf-8");//mdiatype 这个需要和服务端保持一致

    public static final int TYPE_GET = 0;//get请求
    public static final int TYPE_POST_JSON = 1;//post请求参数为json
    public static final int TYPE_POST_FORM = 2;//post请求参数为表单
    private Handler okHttpHandler;//全局处理子线程和M主线程通信


    public BaseRequestManager() {
        //初始化Handler
        okHttpHandler = new Handler(Looper.getMainLooper());
    }


    public HashMap<String, List<Cookie>> getCookieStore() {
        return OkHttpClientManager.getInstance().getCookieStore();
    }

    /**
     * 返回OkHttpClient实例
     */
    public OkHttpClient getOkHttpClient() {
        return OkHttpClientManager.getInstance().getOkHttpClient();
    }


//    /**
//     * okHttp同步请求统一入口
//     *
//     * @param actionUrl   接口地址
//     * @param requestType 请求类型
//     * @param paramsMap   请求参数
//     */
//    public Response requestSyn(String TAG, String actionUrl, int requestType, HashMap<String, String> paramsMap) {
//        Response response = null;
//        switch (requestType) {
//            case TYPE_GET:
//                response = requestGetBySyn(TAG, actionUrl, paramsMap);
//                break;
//            case TYPE_POST_JSON:
//                requestPostBySyn(TAG, actionUrl, paramsMap);
//                break;
//            case TYPE_POST_FORM:
//                requestPostBySynWithForm(TAG, actionUrl, paramsMap);
//                break;
//        }
//
//        return response;
//    }


    /**
     * okHttp get同步请求
     *
     * @param tag       标记，用于取消请求
     * @param actionUrl 接口地址(例如 http://www.baidu.com)
     * @param paramsMap 请求参数
     */
    public Response requestGetBySyn(String tag, String actionUrl, HashMap<String, String> paramsMap) {
        try {
            String param = getRequstParamFromMap(paramsMap);
            //补全请求地址
            String requestUrl = String.format("%s?%s", actionUrl, param);
            //创建一个请求
            Request request = addHeaders().url(requestUrl).tag(tag).build();
            //创建一个Call
            final Call call = OkHttpClientManager.getInstance().getOkHttpClient().newCall(request);
            //执行请求
            final Response response = call.execute();
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * okHttp post同步请求
     *
     * @param actionUrl 接口地址
     * @param paramsMap 请求参数
     */
    @Deprecated
    private Response requestPostBySyn(String actionUrl, HashMap<String, String> paramsMap) {
        return requestPostBySyn("", actionUrl, paramsMap);
    }

    public Response requestPostBySyn(String tag, String actionUrl, HashMap<String, String> paramsMap) {
        try {
            //补全请求地址
            String requestUrl = actionUrl;
            //创建一个请求实体对象 RequestBody
            FormBody.Builder builder = new FormBody.Builder();
            for (String key : paramsMap.keySet()) {
                builder.add(key, paramsMap.get(key));
            }
            FormBody formBody = builder.build();
            //创建一个请求
            final Request request = addHeaders().url(requestUrl).post(formBody).tag(tag).build();
            //创建一个Call
            final Call call = OkHttpClientManager.getInstance().getOkHttpClient().newCall(request);
            //执行请求
            Response response = call.execute();
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * post同步请求（携带请求头参数）
     *
     * @param tag       标记
     * @param actionUrl url
     * @param paramsMap 参数
     * @param headerMap 请求头
     * @return
     */
    public Response requestPostBySyn(String tag, String actionUrl, HashMap<String, String> paramsMap, HashMap<String, String> headerMap) {
        try {
            //补全请求地址
            String requestUrl = actionUrl;
            //创建一个请求实体对象 RequestBody
            FormBody.Builder builder = new FormBody.Builder();
            for (String key : paramsMap.keySet()) {
                builder.add(key, paramsMap.get(key));
            }
            FormBody formBody = builder.build();
            //创建一个请求
            final Request request = addHeaders(headerMap).url(requestUrl).post(formBody).tag(tag).build();
            //创建一个Call
            final Call call = OkHttpClientManager.getInstance().getOkHttpClient().newCall(request);
            Response response = call.execute();

            return response;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * post -application/json请求
     *
     * @param tag        标记
     * @param requestUrl url
     * @param params     json参数字符串
     * @return
     */
    public Response requestPostByJSONSyn(String tag, String requestUrl, String params) {
        try {
            RequestBody body = RequestBody.create(MEDIA_TYPE_JSON, params);
            //创建一个请求
            final Request request = addHeaders().url(requestUrl).post(body).tag(tag).build();
            //创建一个Call
            final Call call = OkHttpClientManager.getInstance().getOkHttpClient().newCall(request);
            //执行请求
            Response response = call.execute();
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

//    /**
//     * okHttp post同步请求表单提交
//     *
//     * @param actionUrl 接口地址
//     * @param paramsMap 请求参数c
//     */
//    private void requestPostBySynWithForm(String tag, String requestUrl, HashMap<String, String> paramsMap) {
//        try {
//            //创建一个FormBody.Builder
//            FormBody.Builder builder = new FormBody.Builder();
//            for (String key : paramsMap.keySet()) {
//                //追加表单信息
//                builder.add(key, paramsMap.get(key));
//            }
//            //生成表单实体对象
//            RequestBody formBody = builder.build();
//            //创建一个请求
//            final Request request = addHeaders().url(requestUrl).post(formBody).tag(tag).build();
//            //创建一个Call
//            final Call call = OkHttpClientManager.getInstance().getOkHttpClient().newCall(request);
//            //执行请求
//            Response response = call.execute();
//            if (response.isSuccessful()) {
////                Log.e(TAG, "response ----->" + response.body().string());
//            }
//        } catch (Exception e) {
//            Log.e(TAG, e.toString());
//        }
//    }


    /**
     * okHttp异步请求统一入口
     *
     * @param actionUrl   接口地址
     * @param requestType 请求类型
     * @param paramsMap   请求参数
     * @param callBack    请求返回数据回调
     * @param <T>         数据泛型
     **/
    public <T> Call requestAsyn(String tag, String actionUrl, int requestType, HashMap<String, String> paramsMap,
                                TCommonRequestManager.ReqCallBack<T> callBack) {
        Call call = null;
        switch (requestType) {
            case TYPE_GET:
                call = requestGetByAsyn(tag, actionUrl, paramsMap, callBack);
                break;
            case TYPE_POST_JSON:
                call = requestPostByAsyn(tag, actionUrl, paramsMap, callBack);
                break;
            case TYPE_POST_FORM:
                call = requestPostByAsynWithForm(tag, actionUrl, paramsMap, callBack);
                break;
        }
        return call;
    }

    /**
     * okHttp get异步请求
     *
     * @param actionUrl 接口地址
     * @param paramsMap 请求参数
     * @param callBack  请求返回数据回调
     * @param <T>       数据泛型
     * @return
     */
    public <T> Call requestGetByAsyn(String tag, String actionUrl, HashMap<String, String> paramsMap,
                                     final TCommonRequestManager.ReqCallBack<T> callBack) {
        try {
            String param = getRequstParamFromMap(paramsMap);
            String requestUrl = String.format("%s?%s", actionUrl, param);
            final Request request = addHeaders().url(requestUrl).tag(tag).build();
            final Call call = OkHttpClientManager.getInstance().getOkHttpClient().newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
//                    自己主动取消的错误的 java.net.SocketException: Socket closed
//                    超时的错误是 java.net.SocketTimeoutException
//                    网络出错的错误是java.net.ConnectException: Failed to connect to xxxxx
                    if (e.toString().contains("closed") || "java.io.IOException: Canceled".equals(e.toString())) {
                        return;
                    }
                    failedCallBack(FAIL_CODE, "请求失败", callBack);
//                    Log.e(TAG, e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String string = response.body().string();
//                        Log.e(TAG, "response ----->" + string);
                        successCallBack((T) string, callBack);
                    } else {
                        failedCallBack(response.code(), "请求失败", callBack);
                    }
                }
            });
            return call;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * okHttp post异步请求
     *
     * @param actionUrl 接口地址
     * @param paramsMap 请求参数
     * @param callBack  请求返回数据回调
     * @param <T>       数据泛型
     * @return
     */
    public <T> Call requestPostByAsyn(String tag, String actionUrl, HashMap<String, String> paramsMap,
                                      final TCommonRequestManager.ReqCallBack<T> callBack) {
        try {
            FormBody.Builder builder = new FormBody.Builder();
            for (String key : paramsMap.keySet()) {
                //追加表单信息
                builder.add(key, paramsMap.get(key) == null ? "" : paramsMap.get(key));
            }
            //生成表单实体对象
            RequestBody formBody = builder.build();
            String requestUrl = actionUrl;
            final Request request = addHeaders().url(requestUrl).post(formBody).tag(tag).build();
            final Call call = OkHttpClientManager.getInstance().getOkHttpClient().newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    if (e.toString().contains("closed") || "java.io.IOException: Canceled".equals(e.toString())) {
                        return;
                    }
                    failedCallBack(FAIL_CODE, "请求失败", callBack);
//                    Log.e(TAG, e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String string = response.body().string();
//                        LogUtils.d( "response ----->" + string);
                        String cookie = "";
                        Headers headers = response.headers();
                        int requestHeadersLength = headers.size();
                        for (int i = 0; i < requestHeadersLength; i++) {
                            String headerName = headers.name(i);
                            String headerValue = headers.value(i);
                            if (headerValue.contains("UserInfo")) {
                                cookie = headerValue;
                            }
                        }

                        successCallBack((T) string, callBack);
                    } else {
                        failedCallBack(response.code(), "请求失败", callBack);
                    }
                }
            });
            return call;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }


    /**
     * okHttp post异步请求表单提交
     *
     * @param actionUrl 接口地址
     * @param paramsMap 请求参数
     * @param callBack  请求返回数据回调
     * @param <T>       数据泛型
     * @return
     */
    private <T> Call requestPostByAsynWithForm(String tag, final String actionUrl, HashMap<String, String> paramsMap,
                                               final TCommonRequestManager.ReqCallBack<T> callBack) {
        try {
            FormBody.Builder builder = new FormBody.Builder();
            for (String key : paramsMap.keySet()) {
                builder.add(key, paramsMap.get(key) == null ? "" : paramsMap.get(key));
            }
            RequestBody formBody = builder.build();
            String requestUrl = actionUrl;
            final Request request = addHeaders().url(requestUrl).post(formBody).tag(tag).build();
            final Call call = OkHttpClientManager.getInstance().getOkHttpClient().newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    if (e.toString().contains("closed") || "java.io.IOException: Canceled".equals(e.toString())) {
                        return;
                    }
                    failedCallBack(FAIL_CODE, "请求失败", callBack);
//                    Log.e(TAG, e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String string = response.body().string();
                        String cookie = "";
                        Headers headers = response.headers();
                        int requestHeadersLength = headers.size();
                        for (int i = 0; i < requestHeadersLength; i++) {
                            String headerName = headers.name(i);
                            String headerValue = headers.value(i);
                            // Log.i(TAG,"TAG----------->Name:" + headerName + "------------>Value:" + headerValue + "\n");
                            if (headerValue.contains("UserInfo")) {
                                cookie = headerValue;
                            }
                        }


                        Log.e(TAG, "response ----->" + string);
                        Log.e(TAG, "response cookie----->" + cookie);
                        if ("Login".equals(actionUrl)) {
                            Map<String, String> map = new HashMap();
                            map.put("logininfo", string);
                            map.put("cookie", cookie);
                            successCallBack((T) map, callBack);
                            return;
                        }
                        successCallBack((T) string, callBack);
                    } else {
                        failedCallBack(response.code(), "请求失败", callBack);
                    }
                }
            });
            return call;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    /**
     * okHttp post异步请求body提交
     *
     * @param actionUrl 接口地址
     */
    public <T> Call requestPostByAsyncWithRequsetBody(String tag, final String actionUrl, RequestBody requestBody, final TCommonRequestManager.ReqCallBack<T> callBack) {
        try {
            String requestUrl = actionUrl;
            //创建一个请求
            final Request request = addHeaders().url(requestUrl).post(requestBody).tag(tag).build();
            //创建一个Call
            final Call call = OkHttpClientManager.getInstance().getOkHttpClient().newCall(request);
            //执行请求
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
//                    自己主动取消的错误的 java.net.SocketException: Socket closed
//                    超时的错误是 java.net.SocketTimeoutException
//                    网络出错的错误是java.net.ConnectException: Failed to connect to xxxxx
                    if (e.toString().contains("closed") || "java.io.IOException: Canceled".equals(e.toString())) {
                        return;
                    }
                    failedCallBack(FAIL_CODE, "请求失败", callBack);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String string = response.body().string();
                        if (!actionUrl.contains("x.szsing.com")) {
                            Log.d(TAG, "response ----->" + string);
                        }
                        successCallBack((T) string, callBack);
                    } else {
                        failedCallBack(response.code(), "请求失败", callBack);
                    }
                }
            });
            return call;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * okHttp post异步请求JSON提交
     *
     * @param actionUrl 接口地址
     */
    public <T> Call requestPostByAsyncWithJSON(String tag, String actionUrl, String json, final TCommonRequestManager.ReqCallBack<T> callBack) {
        try {
            String requestUrl = actionUrl;
            RequestBody body = RequestBody.create(MEDIA_TYPE_JSON, json);
            //创建一个请求
            final Request request = addHeaders().url(requestUrl).post(body).tag(tag).build();
            //创建一个Call
            final Call call = OkHttpClientManager.getInstance().getOkHttpClient().newCall(request);
            //执行请求

            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
//                    自己主动取消的错误的 java.net.SocketException: Socket closed
//                    超时的错误是 java.net.SocketTimeoutException
//                    网络出错的错误是java.net.ConnectException: Failed to connect to xxxxx
                    if (e.toString().contains("closed") || "java.io.IOException: Canceled".equals(e.toString())) {
                        return;
                    }
                    failedCallBack(FAIL_CODE, "请求失败", callBack);
//                    Log.e(TAG, e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String string = response.body().string();
                        successCallBack((T) string, callBack);
                    } else {
                        failedCallBack(response.code(), "请求失败", callBack);
                    }
                }
            });
            return call;
        } catch (Exception e) {
//            Log.e(TAG, e.toString());
        }
        return null;
    }

    public interface ReqCallBack<T> {
        /**
         * 响应成功
         */
        void onReqSuccess(T result);

        /**
         * 响应失败
         */
        void onReqFailed(String errorMsg);
    }

    /**
     * 带返回错误码的callback
     */
    public static abstract class ReqCodeCallBack<T> implements ReqCallBack<T> {
        public abstract void onReqFailed(int errorCode, String errorMsg);
    }

    /**
     * 统一为请求添加头信息
     *
     * @return
     */
    protected Request.Builder addHeaders() {
        Request.Builder builder = new Request.Builder();
        builder.addHeader("UserAgent", System.getProperty("http.agent"));
//        //中建项目接口需要
//        builder.header("appID",EnvironmentVariable.getProperty("AppToken","F150078D8F3743D2BF37174139839F98"));
//        //这两个是币看接口需要的
//        builder.addHeader("Accept-Language", ResStringUtil.getString(R.string.wechat_bk_header_accept_language));
//        builder.addHeader("Bk-Currency", ResStringUtil.getString(R.string.wechat_bk_header_bk_currency));
        return builder;
    }

    /**
     * 统一为请求添加头信息
     *
     * @return
     */
    protected Request.Builder addHeaders(HashMap<String, String> map) {
        Request.Builder builder = addHeaders();
        for (String key : map.keySet()) {
            builder.addHeader(key, (String) map.get(key));
        }
        return builder;
    }

    /**
     * 统一同意处理成功信息
     *
     * @param result
     * @param callBack
     * @param <T>
     */
    private <T> void successCallBack(final T result, final TCommonRequestManager.ReqCallBack<T> callBack) {
        okHttpHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    callBack.onReqSuccess(result);
                }
            }
        });
    }

    /**
     * 统一处理失败信息
     *
     * @param errorCode 错误码
     * @param errorMsg
     * @param callBack
     * @param <T>
     */
    private <T> void failedCallBack(final int errorCode, final String errorMsg, final TCommonRequestManager.ReqCallBack<T> callBack) {
        okHttpHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    callBack.onReqFailed(errorMsg);
                    if (callBack instanceof ReqCodeCallBack) {
                        ((ReqCodeCallBack) callBack).onReqFailed(errorCode, errorMsg);
                    }
                }
            }
        });
    }

    /**
     * 下载文件 20200125最新版
     * 注意！！！ 文件路径一定要使用回调返回的文件中获取，千万不要手动拼dir+filename
     *
     * @param tag         标记 用于取消下载
     * @param fileUrl     文件url
     * @param fileName    文件名
     * @param destFileDir 存储目标目录
     */
    //TODO 文件名修改
    public void downLoadFileNew(String tag, String fileUrl, String fileName, final String destFileDir,
                                final TCommonRequestManager.ReqProgressCallBack<File> callBack) {

        FileUtils.createOrExistsDir(destFileDir);
        //1.获取文件扩展名
        String fileExtention = FileUtils.getFileExtension(fileName);
        if (TextUtils.isEmpty(fileExtention)) {
            fileExtention = FileUtils.getFileExtension(fileUrl);
        }
        if (TextUtils.isEmpty(fileExtention)) {
            fileExtention = "";
        }

        //要下载的文件名 a.pdf---> a(fileurlmd5).pdf

        String tempDot = fileExtention.equals("") ? "" : ".";
        String name = FileUtils.getFileNameNoExtension(fileName) == null ? "" : FileUtils.getFileNameNoExtension(fileName);
        final String downFileName = name
                + "(" + System.currentTimeMillis() + ")"
                + tempDot
                + fileExtention;
        //临时文件
        String tempdownFileName = "oktemp" + downFileName;

        //看看已下载的文件是否存在
        final File downloadedFile = new File(destFileDir, downFileName);
        if (downloadedFile.exists()) {
            successCallBack(downloadedFile, callBack);
            return;
        }

        final File tempdownFile = new File(destFileDir, tempdownFileName);
        final Request request = new Request.Builder().url(fileUrl).tag(tag).build();
        final Call call = OkHttpClientManager.getInstance().getOkHttpClient().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (e.toString().contains("closed") || "java.io.IOException: Canceled".equals(e.toString())) {
                    return;
                }
                failedCallBack(FAIL_CODE, "下载失败", callBack);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                try {
                    long total = response.body().contentLength();
                    long current = 0;
                    is = response.body().byteStream();
                    fos = new FileOutputStream(tempdownFile);
                    while ((len = is.read(buf)) != -1) {
                        current += len;
                        fos.write(buf, 0, len);
                        progressCallBack(total, current, callBack);
                    }
                    fos.flush();
                    //文件下载完成，重命名
                    FileUtils.rename(tempdownFile, downFileName);
                    successCallBack(downloadedFile, callBack);
                } catch (Exception e) {
                    e.printStackTrace();
                    if (e instanceof SocketException) {
                        if ("Socket is closed".equals(e.getMessage())) {
                            Log.e(TAG, "用户取消了下载");
                            return;
                        }
                    }
                    failedCallBack(response.code(), "下载失败", callBack);
                } finally {
                    try {
                        is.close();
                        fos.close();
                        response.close();
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    public interface ReqProgressCallBack<T> extends TCommonRequestManager.ReqCallBack<T> {
        /**
         * 响应进度更新
         */
        void onProgress(long total, long current);
    }


    /**
     * 统一处理进度信息
     *
     * @param total    总计大小
     * @param current  当前进度
     * @param callBack
     * @param <T>
     */
    private <T> void progressCallBack(final long total, final long current, final TCommonRequestManager.ReqProgressCallBack<T> callBack) {
        okHttpHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    callBack.onProgress(total, current);
                }
            }
        });
    }


    /**
     * 取消请求
     * {@link BaseRequestManager#cannelRequest(String)}
     *
     * @param tag
     */
    @Deprecated
    public void cannelOkHttpRequest(String tag) {
        cannelRequest(tag);
    }

    public static void cannelRequest(String tag) {
        try {
            Dispatcher dispatcher = OkHttpClientManager.getInstance().getOkHttpClient().dispatcher();
            if (dispatcher == null) {
                return;
            }
            synchronized (dispatcher) {
                for (Call call : dispatcher.runningCalls()) {
                    if (tag.equals(call.request().tag())) {
                        call.cancel();
                    }
                }
                for (Call call : dispatcher.queuedCalls()) {
                    if (tag.equals(call.request().tag())) {
                        call.cancel();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 从map中获取请求参数
     *
     * @param map
     * @return
     */
    public static String getRequstParamFromMap(Map<String, String> map) {
        StringBuilder tempParams = new StringBuilder();
        try {
            //处理参数
            int pos = 0;
            for (String key : map.keySet()) {
                if (pos > 0) {
                    tempParams.append("&");
                }
                //对参数进行URLEncoder
                tempParams.append(String.format("%s=%s", key, URLEncoder.encode(map.get(key), "utf-8")));
                pos++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tempParams.toString();
    }
}
