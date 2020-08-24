package com.yqs.appupdate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.cike978.appupdate.AppVersionManager
import com.cike978.appupdate.bean.ApkUpdateBean

class MainActivity : AppCompatActivity() {

    val instance by lazy { this }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


      findViewById<TextView>(R.id.tv_check).setOnClickListener(object : View.OnClickListener {
          override fun onClick(it: View) {

           var apkUpdateBean:ApkUpdateBean = ApkUpdateBean();
              apkUpdateBean.versionCode=2
              apkUpdateBean.version="1.0.2";
              apkUpdateBean.forceUpdate=true
              apkUpdateBean.title="应用更新"
              apkUpdateBean.updateNote="修复了bug\n增加了新功能\n"
              apkUpdateBean.downloadUrl="https://turbochain.openserver.cn/ospstore/OpenPlanet/OpenPlanet.apk"
              apkUpdateBean.downLoadFilePath= getExternalFilesDir("download")?.absolutePath

              AppVersionManager.Builder.anAppVersionManager().withActivity(instance)
                  .withApkUpdateBean(apkUpdateBean)
                  .withCheckType(AppVersionManager.TYPE_MANUAL)
                  .withHttpManager(MyHttpManager())
                  .build().updateApk();


          }
      })
    }
}