package com.cike978.appupdate;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.fragment.app.FragmentActivity;

import com.cike978.appupdate.bean.ApkUpdateBean;
import com.cike978.appupdate.bean.ResUpdateBean;
import com.cike978.appupdate.bean.UpdateBean;

/**
 * Created by yqs97.
 * Date: 2020/8/21
 * Time: 15:59
 */
public class AppVersionManager {

    final static String INTENT_KEY = "update_dialog_values";
    final static String THEME_KEY = "theme_color";
    final static String TOP_IMAGE_KEY = "top_resId";
    final static String HTTP_MANAGER_KEY = "http_manager";

    /**
     * 静默检查更新 没有loading和toast
     */
    public static final int TYPE_SILENCE = 1;
    /**
     * 手动检查更新
     */
    public static final int TYPE_MANUAL = 2;


    private Activity activity;

    private ApkUpdateBean apkUpdateBean;
    private ResUpdateBean resUpdateBean;
    private IVersionCompare versionCompare;
    private HttpManager httpManager;
    private int checkType;

    private int mThemeColor;
    private @DrawableRes
    int mTopPic;


    public IVersionCompare getVersionCompare() {
        if (versionCompare == null) {
            versionCompare = new DefaultVersionCompare();
        }
        return versionCompare;
    }

    public HttpManager getHttpManager() {
        if (httpManager == null) {
            throw new RuntimeException("未设置httpmanager");
        }
        return httpManager;
    }

    public void updateApk() {
        if (hasNewApkVersion()) {

            showDialogFragment(apkUpdateBean);

        } else {
            if (checkType == TYPE_MANUAL) {
                Toast.makeText(activity.getApplicationContext(), R.string.app_update_is_latest_version, Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void updateRes() {
        if (hasNewApkVersion()) {

            showDialogFragment(resUpdateBean);

        } else {
            if (checkType == TYPE_MANUAL) {
//                Toast.makeText(activity.getApplicationContext(), R.string.app_update_is_latest_version, Toast.LENGTH_SHORT).show();
            }
        }
    }


    /**
     * 跳转到更新页面
     */
    public void showDialogFragment(UpdateBean updateBean) {
        if (activity != null && !activity.isFinishing()) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(INTENT_KEY, updateBean);
            if (mThemeColor != 0) {
                bundle.putInt(THEME_KEY, mThemeColor);
            }

            if (mTopPic != 0) {
                bundle.putInt(TOP_IMAGE_KEY, mTopPic);
            }
            bundle.putSerializable(HTTP_MANAGER_KEY, getHttpManager());


            UpdateDialogFragment
                    .newInstance(bundle)
                    .show(((FragmentActivity) activity).getSupportFragmentManager(), "dialog");
        }

    }


    public boolean hasNewApkVersion() {
        return getVersionCompare().hasNewVersionApk(activity.getApplicationContext(), apkUpdateBean);
    }


    public boolean hasNewResVersion() {
        return getVersionCompare().hasNewVersionRes(activity.getApplicationContext(), resUpdateBean);
    }


    public static final class Builder {
        private Activity activity;
        private ApkUpdateBean apkUpdateBean;
        private ResUpdateBean resUpdateBean;
        private IVersionCompare versionCompare;
        private HttpManager httpManager;
        private int checkType = TYPE_SILENCE;


        private Builder() {
        }

        public static Builder anAppVersionManager() {
            return new Builder();
        }

        public Builder withActivity(Activity activity) {
            this.activity = activity;
            return this;
        }

        public Builder withApkUpdateBean(ApkUpdateBean apkUpdateBean) {
            this.apkUpdateBean = apkUpdateBean;
            return this;
        }

        public Builder withResUpdateBean(ResUpdateBean resUpdateBean) {
            this.resUpdateBean = resUpdateBean;
            return this;
        }

        public Builder withVersionCompare(IVersionCompare versionCompare) {
            this.versionCompare = versionCompare;
            return this;
        }

        public Builder withCheckType(int checkType) {
            this.checkType = checkType;
            return this;
        }

        public Builder withHttpManager(HttpManager httpManager) {
            this.httpManager = httpManager;
            return this;
        }

        public AppVersionManager build() {
            AppVersionManager appVersionManager = new AppVersionManager();
            appVersionManager.apkUpdateBean = this.apkUpdateBean;
            appVersionManager.activity = this.activity;
            appVersionManager.resUpdateBean = this.resUpdateBean;
            appVersionManager.versionCompare = this.versionCompare;
            appVersionManager.checkType = this.checkType;
            appVersionManager.httpManager = this.httpManager;
            return appVersionManager;
        }
    }
}
