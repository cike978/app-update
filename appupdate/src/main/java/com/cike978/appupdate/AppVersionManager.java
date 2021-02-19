package com.cike978.appupdate;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.cike978.appupdate.bean.ResVersionInfo;
import com.cike978.appupdate.bean.UpdateConfig;
import com.cike978.appupdate.bean.VersionDTO;
import com.cike978.appupdate.callback.DialogUpdateCallBack;
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
    private static final String TAG = "AppVersionManager";

    final static String INTENT_KEY = "update_dialog_values";
    final static String UPDATECONFIG_KEY = "update_config";
    final static String BACK_DOWN_KEY = "back_down";
    final static String UPDAGE_FILE_TYPE = "update_file_type";

    /**
     * 更新文件类型-app
     */
    public final static int TYPE_FILE_APP = 1;
    /**
     * 更新文件类型-资源文件
     */
    public final static int TYPE_FILE_RES = 2;
    /**
     * 静默检查更新 没有loading和toast
     */
    public static final int TYPE_SILENCE = 1;
    /**
     * 手动检查更新
     */
    public static final int TYPE_MANUAL = 2;

    private Activity activity;
    /**
     * 版本信息
     */
    private VersionDTO versionDTO;
    /**
     * 升级配置信息
     */
    private UpdateConfig updateConfig;


    /**
     * 是否后台下载
     */
    private boolean isBackgroundDownload;
    /**
     * 检查类型 静默 手动
     */
    private int checkType;

    /**
     * 升级回调
     */
    private UpdateCallBack updateCallBack;

    /**
     * 工作流
     */
    private WorkFlow workFlow = new WorkFlow.Builder().create();


    /**
     * 是否有应用的更新
     */
    private boolean hasAppUpdate;

    private AppVersionManager() {
    }

    /**
     * 请求版本更新信息
     *
     * @return
     */
    public AppVersionManager requestCheckVersionInfo() {
        //请求检查更新
        workFlow.addNode(new WorkNode(0, new Worker() {
            @Override
            public void doWork(final Node current) {
                //调用检查更新的代码
                if (updateConfig == null) {
                    throw new RuntimeException("未设置updateConfig");
                }

                updateConfig.getHttpManager().checkVersion(activity, new HttpManager.Callback<VersionDTO>() {
                    @Override
                    public void onResponse(VersionDTO versionDTOResult) {
                        if (activity == null || activity.isDestroyed() || activity.isFinishing()) {
                            return;
                        }
                        versionDTO = versionDTOResult;
                        if (updateCallBack != null) {
                            updateCallBack.checkVersionResult(true, versionDTOResult);
                        }
                        current.onCompleted();
                    }

                    @Override
                    public void onError(String error) {
                        Log.e(TAG, error);
                        if (activity == null || activity.isDestroyed() || activity.isFinishing()) {
                            return;
                        }
                        error = TextUtils.isEmpty(error) ?
                                activity.getString(R.string.app_update_checkversion_fail) : error;
                        Toast.makeText(activity.getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                        //不用继续走工作流了
                        if (updateCallBack != null) {
                            updateCallBack.checkVersionResult(true, null);
                        }
                        workFlow.dispose();
                    }
                });
            }
        }));
        return this;
    }


    /**
     * 更新应用
     *
     * @return
     */
    public AppVersionManager updateApk() {
        if (hasNewApkVersion()) {
            hasAppUpdate = true;
            workFlow.addNode(new WorkNode(1, new Worker() {
                @Override
                public void doWork(Node current) {
                    showDialogFragment(TYPE_FILE_APP, current);
                }
            }));
        }
        return this;
    }

    /**
     * 更新资源文件
     *
     * @return
     */
    public AppVersionManager updateRes() {
        if (hasNewResVersion()) {
            workFlow.addNode(new WorkNode(2, new Worker() {
                @Override
                public void doWork(Node current) {
                    showDialogFragment(TYPE_FILE_RES, current);
                }
            }));
        } else {
            if (checkType == TYPE_MANUAL && !hasAppUpdate) {
                //手动检查更新，并且没有应用和资源文件更新的时候，toast提示一下
                Toast.makeText(activity.getApplicationContext(), R.string.app_update_is_latest_version, Toast.LENGTH_SHORT).show();
            }
        }
        return this;
    }

    /**
     * 开始工作流
     */
    public void start() {
        workFlow.start();
    }

    /**
     * 显示更新弹框
     *
     * @param fileType 更新文件类型
     * @param current  工作流节点
     */
    public void showDialogFragment(int fileType, final Node current) {
        if (activity != null && !activity.isFinishing()) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(INTENT_KEY, versionDTO);

            if (updateConfig == null) {
                throw new RuntimeException("未设置updateConfig");
            }
            bundle.putSerializable(UPDATECONFIG_KEY, updateConfig);
            bundle.putBoolean(BACK_DOWN_KEY, isBackgroundDownload);
            bundle.putInt(UPDAGE_FILE_TYPE, fileType);

            UpdateDialogFragment updateDialogFragment = UpdateDialogFragment.newInstance(bundle);
            updateDialogFragment.setDialogUpdateCallBack(new DialogUpdateCallBack() {
                @Override
                public boolean downLoadResFinish(String resDownloadPath, ResVersionInfo resVersionInfo) {
                    Log.e(TAG, "资源文件下载完成" + resDownloadPath);
                    current.onCompleted();
                    if (updateCallBack != null) {
                        return updateCallBack.downLoadResFinish(resDownloadPath, resVersionInfo);
                    }
                    return false;
                }

                @Override
                public void resUnzipFinish(boolean isSuccess) {
                    Log.e(TAG, "resUnzipFinish：" + isSuccess);
                    if (updateCallBack != null) {
                        updateCallBack.resUnzipFinish(isSuccess);
                    }
                    current.onCompleted();
                }

                @Override
                public void ignoreUpdateApk() {
                    Log.e(TAG, "忽略应用升级");
                    if (updateCallBack != null) {
                        updateCallBack.ignoreUpdateApk();
                    }
                    current.onCompleted();
                }

                @Override
                public void ignoreUpdateRes() {
                    Log.e(TAG, "忽略资源文件升级");
                    if (updateCallBack != null) {
                        updateCallBack.ignoreUpdateRes();
                    }
                    current.onCompleted();
                }
            });
            updateDialogFragment.show(((FragmentActivity) activity).getSupportFragmentManager(), "updateDialogFragment");
        }
    }

    /**
     * 是否有应用更新
     *
     * @return
     */
    private boolean hasNewApkVersion() {
        return versionDTO.getAppVersionErrorCode() == 0;
    }

    /**
     * 是否有资源文件更新
     *
     * @return
     */
    private boolean hasNewResVersion() {
        return versionDTO.getResVersionErrorCode() == 0;
    }


    public static final class Builder {
        private Activity activity;
        private VersionDTO versionDTO;
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

        public Builder withVersionDTO(VersionDTO versionDTO) {
            this.versionDTO = versionDTO;
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
            appVersionManager.checkType = this.checkType;
            appVersionManager.isBackgroundDownload = this.isBackgroundDownload;
            appVersionManager.updateConfig = this.updateConfig;
            appVersionManager.updateCallBack = this.updateCallBack;
            appVersionManager.versionDTO = this.versionDTO;
            return appVersionManager;
        }
    }
}
