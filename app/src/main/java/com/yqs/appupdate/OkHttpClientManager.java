package com.yqs.appupdate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

/**
 * okhttpclinet 管理
 * <p>
 * Created by YQS.
 * Date: 2019/6/27
 * Time: 11:14
 */
public class OkHttpClientManager {
    private static final String TAG = "OkHttpClientManager";
    private static volatile OkHttpClientManager instance;
    private OkHttpClient okHttpClient;
    private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();

    /**
     * 默认请求超时时间为60s
     */
    private static int defaultTimeOut = 60;

    /**
     * 设置超时时间
     *
     * @param second
     */
    public static void setTimeOutSecond(int second) {
        defaultTimeOut = second;
    }

    /**
     * 获取单例管理
     *
     * @return
     */
    public static OkHttpClientManager getInstance() {
        if (instance == null) {
            synchronized (OkHttpClientManager.class) {
                if (instance == null) {
                    instance = new OkHttpClientManager();
                }
            }
        }
        return instance;
    }

    /**
     * 获取OkHttpClient对象
     *
     * @return
     */
    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }


    /**
     * 获取okhttp请求的cookie
     *
     * @return
     */
    public HashMap<String, List<Cookie>> getCookieStore() {
        return cookieStore;
    }


    /**
     * private----------------------------------------------------------------
     */
    private OkHttpClientManager() {
        okHttpClient = getDefaultClient();
    }


    private OkHttpClient getDefaultClient() {
        return new OkHttpClient().newBuilder()
                //todo 设置为不使用代理的模式 ，就可以阻止第三方使用 Fiddler 或 Charles 进行抓包。
//                .proxy(Proxy.NO_PROXY)
                .connectTimeout(defaultTimeOut, TimeUnit.SECONDS)//设置超时时间
                .readTimeout(defaultTimeOut, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(defaultTimeOut, TimeUnit.SECONDS)//设置写入超时时间
                .cookieJar(getCookieJar())
                .build();
    }


    private CookieJar getCookieJar() {
        return new CookieJar() {
            @Override
            public void saveFromResponse(HttpUrl httpUrl, List<Cookie> list) {
                cookieStore.put(httpUrl.host(), list);
            }

            @Override
            public List<Cookie> loadForRequest(HttpUrl httpUrl) {
                List<Cookie> cookies = cookieStore.get(httpUrl.host());
                return cookies != null ? cookies : new ArrayList<Cookie>();
            }
        };
    }


}
