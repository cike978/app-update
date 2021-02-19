package com.yqs.appupdate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import com.cike978.appupdate.AppVersionManager
import com.cike978.appupdate.bean.*
import com.cike978.appupdate.callback.DialogUpdateCallBack
import com.cike978.appupdate.callback.UpdateCallBack
import com.google.gson.Gson
import org.json.JSONObject
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    val TAG = "MainActivity"
    val instance by lazy { this }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        findViewById<Button>(R.id.tv_check).setOnClickListener(object : View.OnClickListener {
            override fun onClick(it: View) {
                var params = hashMapOf("appId" to "IEFR4hnp")

                TCommonRequestManager.getInstance().requestGetByAsyn(TAG,
                    "http://192.168.137.1:8002/appstore/appversion/checkVersion", params,
                    object : BaseRequestManager.ReqCallBack<String> {
                        override fun onReqSuccess(result: String?) {
                            var jsonObject = JSONObject(result);
                            var dtoString = jsonObject.getJSONObject("data").toString()
                            var versionDTO: VersionDTO =
                                Gson().fromJson(dtoString, VersionDTO::class.java)

                            var updateConfig: UpdateConfig = UpdateConfig();
                            updateConfig.setHttpManager(MyHttpManager())
                            updateConfig.setDownloadServerUrl("http://192.168.137.1.137:8002/system")
                            updateConfig.downloadDirPath =
                                getExternalFilesDir(null)?.absolutePath.plus(".download")
                            AppVersionManager.Builder.anAppVersionManager().withActivity(instance)
                                .withCheckType(AppVersionManager.TYPE_MANUAL)
                                .withUpdateConfig(updateConfig)
                                .withVersionDTO(versionDTO)
                                .withBackgroundDownload(false)
                                .withUpdateCallBack(object :
                                    UpdateCallBack {


                                    override fun ignoreUpdateApk() {
                                    }

                                    override fun resUnzipFinish(
                                        isSuccess: Boolean
                                    ) {

                                    }

                                    override fun ignoreUpdateRes() {
                                    }

                                    override fun downLoadResFinish(
                                        resDownloadPath: String?,
                                        resVersionInfo: ResVersionInfo?
                                    ): Boolean {
                                        return false
                                    }

                                    override fun checkVersionResult(
                                        isSuccess: Boolean,
                                        versionDTO: VersionDTO?
                                    ) {
                                    }
                                })
                                .build()
                                .updateApk()
                                .updateRes()
                                .start();
                        }

                        override fun onReqFailed(errorMsg: String?) {
                            Log.e(TAG, errorMsg)
                        }

                    })


            }
        })
    }


}