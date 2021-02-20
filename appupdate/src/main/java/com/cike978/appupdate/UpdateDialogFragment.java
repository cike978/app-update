package com.cike978.appupdate;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.cike978.appupdate.bean.AppVersionInfo;
import com.cike978.appupdate.bean.ResVersionInfo;
import com.cike978.appupdate.bean.UpdateConfig;
import com.cike978.appupdate.bean.VersionDTO;
import com.cike978.appupdate.callback.DialogUpdateCallBack;
import com.cike978.appupdate.service.DownloadService;
import com.cike978.appupdate.uitls.AppUpdateUtils;
import com.cike978.appupdate.uitls.ColorUtil;
import com.cike978.appupdate.uitls.DrawableUtil;
import com.cike978.appupdate.uitls.FileSizeUtil;
import com.cike978.appupdate.uitls.AppResZipUtil;
import com.cike978.appupdate.uitls.UpdateTypeEnum;
import com.cike978.appupdate.view.NumberProgressBar;

import java.io.File;

/**
 * 应用升级弹框
 *
 * @author yqs
 */
public class UpdateDialogFragment extends DialogFragment implements View.OnClickListener {
    public static boolean isShow = false;

    /**
     * 更新说明
     */
    private TextView mContentTextView;
    //升级按钮
    private Button mUpdateOkButton;
    //进度条
    private NumberProgressBar mNumberProgressBar;
    //关闭按钮
    private ImageView mIvClose;
    //标题
    private TextView mTitleTextView;

    private VersionDTO versionDTO;
    private AppVersionInfo appVersionInfo;
    private ResVersionInfo resVersionInfo;
    private UpdateConfig updateConfig;
    private DialogUpdateCallBack dialogUpdateCallBack;

    /**
     * 是否在后台下载
     */
    private boolean isBackgroundDownload;

    /**
     * 是否静默下载，表示弹框时进度条开始走
     */
    private boolean isSilentDownload;
    /**
     * 升级的文件类型 app或者资源文件
     */
    private int updateFileType;


    /**
     * 升级类型，一般 静默 强制等
     */
    int updateType = UpdateTypeEnum.PUBLIC.getCode();


    /**
     * 回调
     */
    private ServiceConnection conn = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            startDownloadApp((DownloadService.DownloadBinder) service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };
    private LinearLayout mLlClose;
    //默认色
    private int mDefaultColor = 0xffe94339;
    private int mDefaultPicResId = R.drawable.lib_update_app_top_bg;
    private ImageView mTopIv;
    private TextView mIgnoreTextView;
    private DownloadService.DownloadBinder mDownloadBinder;
    private Activity mActivity;


    public void setDialogUpdateCallBack(DialogUpdateCallBack dialogUpdateCallBack) {
        this.dialogUpdateCallBack = dialogUpdateCallBack;
    }


    public static UpdateDialogFragment newInstance(Bundle args) {
        UpdateDialogFragment fragment = new UpdateDialogFragment();
        if (args != null) {
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isShow = true;
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.UpdateAppDialog);
        mActivity = getActivity();
    }

    @Override
    public void onStart() {
        super.onStart();
        //点击window外的区域 是否消失
        getDialog().setCanceledOnTouchOutside(false);

        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    //禁用
                    if (updateType == UpdateTypeEnum.FORCE.getCode()) {
                        //返回桌面
                        startActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME));
                        return true;
                    } else {
//                        excuteIgnoreCallbackOrBackgroundDownTip();
//                        return false;
                        //fixme 不允许后台下载 以后可能会修改
                        //返回桌面
                        startActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME));
                        return true;
                    }
                }
                return false;
            }
        });

        Window dialogWindow = getDialog().getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        lp.height = (int) (displayMetrics.heightPixels * 0.8f);
        lp.width = (int) (displayMetrics.widthPixels * 0.7f);
        dialogWindow.setAttributes(lp);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.lib_update_app_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view) {
        //提示内容
        mContentTextView = view.findViewById(R.id.tv_update_info);
        //标题
        mTitleTextView = view.findViewById(R.id.tv_title);
        //更新按钮
        mUpdateOkButton = view.findViewById(R.id.btn_ok);
        //进度条
        mNumberProgressBar = view.findViewById(R.id.npb);
        //关闭按钮
        mIvClose = view.findViewById(R.id.iv_close);
        //关闭按钮+线 的整个布局
        mLlClose = view.findViewById(R.id.ll_close);
        //顶部图片
        mTopIv = view.findViewById(R.id.iv_top);
        //忽略此版本  下次再说
        mIgnoreTextView = view.findViewById(R.id.tv_ignore);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    private void initData() {
        versionDTO = (VersionDTO) getArguments().getSerializable(AppVersionManager.INTENT_KEY);
        updateConfig = (UpdateConfig) getArguments().getSerializable(AppVersionManager.UPDATECONFIG_KEY);
        isBackgroundDownload = getArguments().getBoolean(AppVersionManager.BACK_DOWN_KEY, false);
        updateFileType = getArguments().getInt(AppVersionManager.UPDAGE_FILE_TYPE, 0);

        appVersionInfo = versionDTO.getAppVersion();
        resVersionInfo = versionDTO.getResVersion();
        //设置主题色
        initTheme();
        //弹出对话框
        String dialogTitle = "";
        String newVersion = "";
        String targetSize = "";
        String updateLog = "";
        updateType = UpdateTypeEnum.PUBLIC.getCode();
        if (versionDTO != null) {
            if (updateFileType == AppVersionManager.TYPE_FILE_APP) {
                newVersion = appVersionInfo.getAppVersion();
                targetSize = FileSizeUtil.FormetFileSize(appVersionInfo.getAppFileList().get(0).getFileSize()) + "";
                updateLog = appVersionInfo.getVersionDescription();
                updateType = appVersionInfo.getUpdateType();
                dialogTitle = "应用升级";

                isSilentDownload = updateType == UpdateTypeEnum.SILENCE.getCode();
            } else if (updateFileType == AppVersionManager.TYPE_FILE_RES) {
                newVersion = resVersionInfo.getResVersion();
                targetSize = FileSizeUtil.FormetFileSize(resVersionInfo.getResFileList().get(0).getFileSize()) + "";
                updateLog = resVersionInfo.getVersionDescription();
                updateType = resVersionInfo.getUpdateType();
                dialogTitle = "资源文件升级";
                isSilentDownload = updateType == UpdateTypeEnum.SILENCE.getCode();
            }


            String msg = "版本：" + newVersion + "\n";
            if (!TextUtils.isEmpty(targetSize)) {
                msg = "新版本大小：" + targetSize + "\n\n";
            }
            if (!TextUtils.isEmpty(updateLog)) {
                msg += updateLog;
            }
            //更新内容
            mContentTextView.setText(msg);
            //标题
            mTitleTextView.setText(TextUtils.isEmpty(dialogTitle) ? String.format("是否升级到%s版本？", newVersion) : dialogTitle);
            //强制更新
            if (updateType == UpdateTypeEnum.FORCE.getCode()) {
                mLlClose.setVisibility(View.GONE);
            } else {
                //不是强制更新时，才生效
                if (updateType == UpdateTypeEnum.PUBLIC.getCode()) {
                    mIgnoreTextView.setVisibility(View.VISIBLE);
                }
            }

            //静默下载也不显示关闭按钮
            if (isSilentDownload) {
                mLlClose.setVisibility(View.GONE);
            }

            initEvents();
        }
    }

    /**
     * 初始化主题色
     */
    private void initTheme() {


        final int color = updateConfig.getmThemeColor();

        final int topResId = updateConfig.getmTopPic();


        if (0 == topResId) {
            if (0 == color) {
                //默认红色
                setDialogTheme(mDefaultColor, mDefaultPicResId);
            } else {
                setDialogTheme(color, mDefaultPicResId);
            }

        } else {
            if (0 == color) {
                //自动提色
//                Palette.from(AppUpdateUtils.drawableToBitmap(this.getResources().getDrawable(topResId))).generate(new Palette.PaletteAsyncListener() {
//                    @Override
//                    public void onGenerated(Palette palette) {
//                        int mDominantColor = palette.getDominantColor(mDefaultColor);
//                        setDialogTheme(mDominantColor, topResId);
//                    }
//                });
                setDialogTheme(mDefaultColor, topResId);
            } else {
                //更加指定的上色
                setDialogTheme(color, topResId);
            }
        }

    }

    /**
     * 设置
     *
     * @param color    主色
     * @param topResId 图片
     */
    private void setDialogTheme(int color, int topResId) {
        mTopIv.setImageResource(topResId);
        mUpdateOkButton.setBackground(DrawableUtil.getDrawable(AppUpdateUtils.dip2px(4, getActivity()), color));
        mNumberProgressBar.setProgressTextColor(color);
        mNumberProgressBar.setReachedBarColor(color);
        //随背景颜色变化
        mUpdateOkButton.setTextColor(ColorUtil.isTextColorDark(color) ? Color.BLACK : Color.WHITE);
    }

    private void initEvents() {
        mUpdateOkButton.setOnClickListener(this);
        mIvClose.setOnClickListener(this);
        mIgnoreTextView.setOnClickListener(this);

        //静默下载的时候相当于直接点击了下载按钮
        if (isSilentDownload) {
            mUpdateOkButton.performClick();
        }
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.btn_ok) {

            //权限判断是否有访问外部存储空间权限
            int flag = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (flag != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    showTipDialog();
                } else {
                    // 申请授权。
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                }
            } else {
                beforeDownloadFile();
            }

        } else if (i == R.id.iv_close) {
            cancelDownloadService();
            excuteIgnoreCallbackOrBackgroundDownTip();
            dismiss();
        } else if (i == R.id.tv_ignore) {
            excuteIgnoreCallbackOrBackgroundDownTip();
            dismiss();
        }
    }

    private void showTipDialog() {
        // 用户拒绝过这个权限了，应该提示用户，为什么需要这个权限。
        new AlertDialog.Builder(getActivity())
                .setMessage(R.string.app_update_text_storage_rationale)
                .setTitle(R.string.app_update_dialog_title)
                .setNegativeButton(R.string.app_update_text_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                }).setPositiveButton(R.string.app_update_text_confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

            }
        }).show();
    }

    /**
     * 执行忽略版本升级的回调或者后台下载的提示
     */
    public void excuteIgnoreCallbackOrBackgroundDownTip() {
        //fixme 目前没有后台下载
//        if (mDownloadBinder != null) {
//            //表示已经开始下载了
//            Toast.makeText(getActivity().getApplicationContext(), "正在后台下载...", Toast.LENGTH_SHORT).show();
//            return;
//        }

        if (dialogUpdateCallBack != null) {
            if (updateFileType == AppVersionManager.TYPE_FILE_APP) {
                dialogUpdateCallBack.ignoreUpdateApk();
            } else {
                dialogUpdateCallBack.ignoreUpdateRes();
            }
        }
    }


    public void cancelDownloadService() {
        if (mDownloadBinder != null) {
            // 表示用户已经点击了更新，之后点击取消
            mDownloadBinder.stop("取消下载", updateConfig.getHttpManager());
        }
    }

    private void beforeDownloadFile() {
        downloadApp();
        //只有app下载可以放到后台
        if (isBackgroundDownload && updateFileType == AppVersionManager.TYPE_FILE_APP) {
            dismiss();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //升级
                beforeDownloadFile();
            } else {
                //当不允许显示权限申请理由的时候表示被永久拒绝，提示用户打开设置页面
                if (!ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Toast.makeText(getActivity().getApplicationContext(), "请手动打开应用权限管理界面并设置允许读取手机存储", Toast.LENGTH_LONG).show();
                } else {
                    showTipDialog();
                }
            }
        }
    }

    /**
     * 开启后台服务下载
     */
    private void downloadApp() {
        DownloadService.bindService(getActivity(), conn);
        mIgnoreTextView.setVisibility(View.GONE);
    }

    /**
     * 回调监听下载
     */
    private void startDownloadApp(DownloadService.DownloadBinder binder) {
        // 开始下载，监听下载进度，可以用对话框显示
        if (versionDTO != null) {

            this.mDownloadBinder = binder;
            String downloadUrl = "";
            String newVersion = "version";
            if (updateFileType == AppVersionManager.TYPE_FILE_APP) {
                newVersion = appVersionInfo.getAppVersion();
                downloadUrl = updateConfig.getDownloadServerUrl() + "/common/sysFile/download/" + appVersionInfo.getAppFileList().get(0).getObjectName();
            } else if (updateFileType == AppVersionManager.TYPE_FILE_RES) {
                newVersion = resVersionInfo.getResVersion();
                downloadUrl = updateConfig.getDownloadServerUrl() + "/common/sysFile/download/" + resVersionInfo.getResFileList().get(0).getObjectName();

            }


            binder.start(downloadUrl, updateConfig.getDownloadDirPath(), updateFileType, newVersion,
                    updateConfig.getHttpManager(), new DownloadService.DownloadCallback() {
                        @Override
                        public void onStart() {
                            if (!UpdateDialogFragment.this.isRemoving()) {
                                mNumberProgressBar.setVisibility(View.VISIBLE);
                                mUpdateOkButton.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onProgress(float progress, long totalSize) {
                            if (!UpdateDialogFragment.this.isRemoving()) {
                                mNumberProgressBar.setProgress(Math.round(progress * 100));
                                mNumberProgressBar.setMax(100);
                            }
                        }

                        @Override
                        public void setMax(long total) {

                        }

                        //TODO 这里的 onFinish 和 onInstallAppAndAppOnForeground 会有功能上的重合，后期考虑合并优化。
                        @Override
                        public boolean onFinish(final File file) {
                            if (!UpdateDialogFragment.this.isRemoving()) {

                                //资源文件升级
                                if (updateFileType == AppVersionManager.TYPE_FILE_RES) {

                                    if (dialogUpdateCallBack != null) {
                                        boolean isConsume = dialogUpdateCallBack.downLoadResFinish(file.getAbsolutePath(), resVersionInfo);
                                        if (isConsume == false) {
                                            //未被消费，交给代码实现资源包解压缩的相关操作
                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    //fixme targetdir目前还未用到，多资源文件会用到
                                                    boolean isUnzipSuccess = AppResZipUtil
                                                            .unzipResFile(file.getAbsolutePath(), null, updateConfig);
                                                    if (dialogUpdateCallBack != null) {
                                                        if (isUnzipSuccess) {
                                                            dialogUpdateCallBack.resUnzipFinish(true);
                                                        } else {
                                                            dialogUpdateCallBack.resUnzipFinish(false);
                                                        }
                                                    }
                                                }
                                            }).start();
                                        }
                                    }
                                    //关闭弹框
                                    dismissAllowingStateLoss();
                                    return true;
                                }

                                //app升级
                                if (updateType == UpdateTypeEnum.FORCE.getCode()) {
                                    showInstallBtn(file);
                                } else {
                                    dismissAllowingStateLoss();
                                }
                            }
                            //一般返回 true ，当返回 false 时，则下载，不安装，为静默安装使用。
                            return true;
                        }

                        @Override
                        public void onError(String msg) {
                            if (!UpdateDialogFragment.this.isRemoving()) {
                                dismissAllowingStateLoss();
                            }
                        }

                        @Override
                        public boolean onInstallAppAndAppOnForeground(File file) {
                            //这样做的目的是在跳转安装界面，可以监听到用户取消安装的动作;
                            //activity.startActivityForResult(intent, REQ_CODE_INSTALL_APP);
                            //但是如果 由DownloadService 跳转到安装界面，则监听失效。
                            if (updateType != UpdateTypeEnum.FORCE.getCode()) {
                                dismiss();
                            }
                            if (mActivity != null) {
                                AppUpdateUtils.installApp(mActivity, file);
                                //返回 true ，自己处理。
                                return true;
                            } else {
                                //返回 flase ，则由 DownloadService 跳转到安装界面。
                                return false;
                            }
                        }
                    });
        }
    }


    private void showInstallBtn(final File file) {
        mNumberProgressBar.setVisibility(View.GONE);
        mUpdateOkButton.setText("安装");
        mUpdateOkButton.setVisibility(View.VISIBLE);
        mUpdateOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUpdateUtils.installApp(UpdateDialogFragment.this, file);
            }
        });
    }


    @Override
    public void show(FragmentManager manager, String tag) {

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            if (manager.isDestroyed()) {
                return;
            }
        }

        try {
            super.show(manager, tag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        isShow = false;
        super.onDestroyView();
    }
}

