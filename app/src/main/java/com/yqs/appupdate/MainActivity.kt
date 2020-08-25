package com.yqs.appupdate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.cike978.appupdate.AppVersionManager
import com.cike978.appupdate.bean.ApkUpdateBean
import com.cike978.appupdate.bean.ResUpdateBean
import com.cike978.appupdate.bean.UpdateConfig
import com.cike978.appupdate.callback.UpdateCallBack

class MainActivity : AppCompatActivity() {

    val instance by lazy { this }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        findViewById<Button>(R.id.tv_check).setOnClickListener(object : View.OnClickListener {
            override fun onClick(it: View) {

                var apkUpdateBean: ApkUpdateBean = ApkUpdateBean();
                apkUpdateBean.versionCode = 1
                apkUpdateBean.version = "1.0";
                apkUpdateBean.forceUpdate = false
                apkUpdateBean.title = "应用更新"
                apkUpdateBean.updateNote = "修复了bug\n增加了新功能\n"
                apkUpdateBean.downloadUrl =
                    "https://turbochain.openserver.cn/ospstore/OpenPlanet/OpenPlanet.apk"
                apkUpdateBean.downLoadFilePath = getExternalFilesDir("download")?.absolutePath


                var resUpdateBean: ResUpdateBean = ResUpdateBean();
                resUpdateBean.version = "1.0.25";
                resUpdateBean.forceUpdate = false
                resUpdateBean.title = "资源文件更新"
                resUpdateBean.updateNote = "修复了bug\n增加了新功能\n"
                resUpdateBean.downloadUrl =
                    "https://turbochain.openserver.cn/ospstore/OpenPlanet/res.zip"
                resUpdateBean.downLoadFilePath = getExternalFilesDir("download")?.absolutePath


                var updateConfig: UpdateConfig = UpdateConfig();
                updateConfig.setHttpManager(MyHttpManager())

                AppVersionManager.Builder.anAppVersionManager().withActivity(instance)
                    .withApkUpdateBean(apkUpdateBean)
                    .withResUpdateBean(resUpdateBean)
                    .withCheckType(AppVersionManager.TYPE_MANUAL)
                    .withUpdateConfig(updateConfig)
                    .withBackgroundDownload(false)
                    .withUpdateCallBack(object : UpdateCallBack {
                        override fun downLoadResFinish(
                            resDownloadPath: String?,
                            resUpdateBean: ResUpdateBean?
                        ) {
                            Log.e("tag", "处理解压资源文件")
                        }

                        override fun ignoreUpdateApk() {
                        }

                        override fun ignoreUpdateRes() {
                        }
                    })
                    .build()
                    .updateApk()
                    .updateRes()
                    .start();


            }
        })
    }
}