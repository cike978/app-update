package com.cike978.appupdate;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.cike978.appupdate.bean.ApkUpdateBean;
import com.cike978.appupdate.bean.ResUpdateBean;
import com.cike978.appupdate.bean.UpdateBean;
import com.cike978.appupdate.bean.UpdateConfig;
import com.cike978.appupdate.callback.UpdateCallBack;
import com.cike978.appupdate.workflow.Node;
import com.cike978.appupdate.workflow.WorkFlow;
import com.cike978.appupdate.workflow.WorkNode;
import com.cike978.appupdate.workflow.Worker;

/**
 * 应用版本管理
 * Created by yqs97.
 * Date: 2020/8/21
 * Time: 15:59
 */
public class AppVersionManager {

    final static String INTENT_KEY = "update_dialog_values";

    final static String UPDATECONFIG_KEY = "update_config";
    final static String BACK_DOWN_KEY = "back_down";

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
    private UpdateConfig updateConfig;


    /**
     * 是否后台下载
     */
    private boolean isBackgroundDownload;
    /**
     * 检查类型 静默 手动
     */
    private int checkType;

    private  UpdateCallBack updateCallBack;

    /**
     * 工作流
     */
    private WorkFlow workFlow = new WorkFlow.Builder().create();

    public AppVersionManager() {
    }


    public AppVersionManager updateApk() {
        if (hasNewApkVersion()) {
            workFlow.addNode(new WorkNode(1, new Worker() {
                @Override
                public void doWork(Node current) {
                    showDialogFragment(apkUpdateBean, current);
                }
            }));
        } else {
            workFlow.addNode(new WorkNode(1, new Worker() {
                @Override
                public void doWork(Node current) {
                    current.onCompleted();
                }
            }));
            if (checkType == TYPE_MANUAL) {
                Toast.makeText(activity.getApplicationContext(), R.string.app_update_is_latest_version, Toast.LENGTH_SHORT).show();
            }
        }
        return this;
    }


    public AppVersionManager updateRes() {
        if (hasNewResVersion()) {
            workFlow.addNode(new WorkNode(2, new Worker() {
                @Override
                public void doWork(Node current) {
                    showDialogFragment(resUpdateBean, current);
                }
            }));

        } else {
            if (checkType == TYPE_MANUAL) {
//                Toast.makeText(activity.getApplicationContext(), R.string.app_update_is_latest_version, Toast.LENGTH_SHORT).show();
            }
        }
        return this;
    }

    public void start() {
        workFlow.start();
    }

    /**
     * 跳转到更新页面
     */
    public void showDialogFragment(final UpdateBean updateBean, final Node current) {
        if (activity != null && !activity.isFinishing()) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(INTENT_KEY, updateBean);

            if (updateConfig == null) {
                throw new RuntimeException("未设置updateConfig");
            }
            bundle.putSerializable(UPDATECONFIG_KEY, updateConfig);
            bundle.putBoolean(BACK_DOWN_KEY, isBackgroundDownload);


            UpdateDialogFragment updateDialogFragment = UpdateDialogFragment
                    .newInstance(bundle);

            updateDialogFragment.setUpdateCallBack(new UpdateCallBack() {
                @Override
                public void downLoadResFinish(String resDownloadPath, ResUpdateBean resUpdateBean) {
                    Log.e("tag", "downLoadResFinish");
                    current.onCompleted();
                    if (updateCallBack!=null){
                        updateCallBack.downLoadResFinish(resDownloadPath,resUpdateBean);
                    }
                }

                @Override
                public void ignoreUpdateApk() {
                    Log.e("tag", "ignoreUpdateApk");
                    if (updateCallBack!=null){
                        updateCallBack.ignoreUpdateApk();
                    }
                    current.onCompleted();

                }

                @Override
                public void ignoreUpdateRes() {
                    Log.e("tag", "ignoreUpdateRes");
                    current.onCompleted();
                    if (updateCallBack!=null){
                        updateCallBack.ignoreUpdateRes();
                    }

                }
            });
            updateDialogFragment.show(((FragmentActivity) activity).getSupportFragmentManager(), "dialog");
        }

    }


    public boolean hasNewApkVersion() {
        return updateConfig.getVersionCompare().hasNewVersionApk(activity.getApplicationContext(), apkUpdateBean);
    }


    public boolean hasNewResVersion() {
        return updateConfig.getVersionCompare().hasNewVersionRes(activity.getApplicationContext(), resUpdateBean);
    }


    public static final class Builder {
        private Activity activity;
        private ApkUpdateBean apkUpdateBean;
        private ResUpdateBean resUpdateBean;
        private UpdateConfig updateConfig;
        private int checkType = TYPE_SILENCE;
        /**
         * 是否后台下载
         */
        private boolean isBackgroundDownload;
        private UpdateCallBack updateCallBack;

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


        public Builder withCheckType(int checkType) {
            this.checkType = checkType;
            return this;
        }

        public Builder withUpdateConfig(UpdateConfig updateConfig) {
            this.updateConfig = updateConfig;
            return this;
        }

        public Builder withBackgroundDownload(boolean isBackgroundDownload) {
            this.isBackgroundDownload = isBackgroundDownload;
            return this;
        }
        public Builder withUpdateCallBack(UpdateCallBack updateCallBack) {
            this.updateCallBack = updateCallBack;
            return this;
        }
        public AppVersionManager build() {
            AppVersionManager appVersionManager = new AppVersionManager();
            appVersionManager.activity = this.activity;
            appVersionManager.apkUpdateBean = this.apkUpdateBean;
            appVersionManager.resUpdateBean = this.resUpdateBean;
            appVersionManager.checkType = this.checkType;
            appVersionManager.isBackgroundDownload = this.isBackgroundDownload;
            appVersionManager.updateConfig = this.updateConfig;
            appVersionManager.updateCallBack = this.updateCallBack;
            return appVersionManager;
        }
    }
}
