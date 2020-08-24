package com.yqs.appupdate;

import android.content.Context;


/**
 * OKHttp通用请求类
 *
 * @author yqs
 */

public class TCommonRequestManager extends BaseRequestManager {

    private static volatile TCommonRequestManager mInstance;

    @Deprecated
    public static TCommonRequestManager getInstance(Context context) {
        return getInstance();
    }

    public static TCommonRequestManager getInstance() {
        if (mInstance == null) {
            synchronized (TCommonRequestManager.class) {
                if (mInstance == null) {
                    mInstance = new TCommonRequestManager();
                }
            }
        }
        return mInstance;
    }

    protected TCommonRequestManager() {
        super();

    }
}
