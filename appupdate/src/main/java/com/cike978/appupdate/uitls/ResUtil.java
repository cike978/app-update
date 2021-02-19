package com.cike978.appupdate.uitls;

import android.util.Log;

import com.cike978.appupdate.bean.UpdateConfig;

import java.io.File;

import kotlin.SuccessOrFailureKt;

/**
 * 资源文件工具类
 * Created by yqs97.
 * Date: 2021/2/18
 * Time: 16:35
 */
public class ResUtil {
    private static final String TAG = "ResUtil";
    /**
     * 解压前备份你的资源名
     */
    private final static String RES_BACK_NAME = "res_back";

    private static String RES_FILE_NAME = "updateAndroidForWeChat.zip";

    /**
     * 解压下载的资源文件
     *
     * @param filePath  下载的资源文件路径
     * @param targetDir 要解压到的目标目录
     */
    public static  boolean unzipResFile(String filePath, String targetDir, UpdateConfig updateConfig) {

        //1.解压前先重命名之前的资源文件夹
        String resDir = updateConfig.getAppRootDir() + "/res";
        if (FileUtils.createOrExistsDir(resDir)) {
            FileUtils.rename(resDir, RES_BACK_NAME);
        }

        String waitUnzipResFilePath = resDir + File.separator + RES_FILE_NAME;

        try {
            //2.新建res文件夹
            FileUtils.createOrExistsDir(resDir);

            //3.将下载的资源文件拷贝到res目录中
            FileUtils.copyFile(filePath, waitUnzipResFilePath, new FileUtils.OnReplaceListener() {
                @Override
                public boolean onReplace() {
                    return true;
                }
            });
            //4.开始解压updateAndroidForWeChat.zip及解压到unzip_res 文件夹中

            ZipUtil zUtil = new ZipUtil();
            boolean isUnZipSuccess = false;
            //将更新的zip解压到临时目录
            String targetUnzipDir = resDir + File.separator + "unzip_res";
            isUnZipSuccess = zUtil.unZip(waitUnzipResFilePath, targetUnzipDir);
            if (isUnZipSuccess) {

                //删除备份的原始资源文件夹
                FileUtils.deleteDir(updateConfig.getAppRootDir()
                        + File.separator + RES_BACK_NAME);
                //回调解压成功
                return true;
            }
        } catch (Exception e) {
            Log.e(TAG, "解压资源文件失败");
            //解压失败，删除解压的文件夹，备份的资源文件改名回来
            FileUtils.deleteDir(resDir);
            FileUtils.rename(updateConfig.getAppRootDir() + File.separator
                    + RES_BACK_NAME, "res");
            e.printStackTrace();
        } finally {
            //移除copy的 updateAndroidForWeChat.zip
            FileUtils.deleteFile(waitUnzipResFilePath);
        }
        return false;
    }

}
