# app-update
#### 包含应用升级，资源包下载

- 使用方法
```
implementation 'com.cike978:appupdate:0.0.1'

```
- code
```java
 var apkUpdateBean: ApkUpdateBean = ApkUpdateBean();
                apkUpdateBean.versionCode = 12
                apkUpdateBean.version = "1.2";
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
```
