package com.cike978.appupdate.bean

import java.io.Serializable

/**
 * 更新的bean
 * Created by yqs97.
 * Date: 2020/8/21
 * Time: 15:02
 */
abstract class UpdateBean : Serializable {
    companion object {
        val TYPE_APP = "apk"
        val TYPE_ZIP = "zip"
    }

    /**
     * 版本号
     */
    var version: String? = null

    /**
     * 版本号
     */
    var versionCode: Int? = null

    /**
     * 更新说明
     */
    var updateNote: String? = null

    /**
     * 文件下载路径(目录)
     */

    var downLoadFilePath: String? = null


    /**
     * 更新标题
     */

    var title: String? = null

    /**
     * 是否强制更新
     */
    var forceUpdate: Boolean = false

    /**
     * 是否是本地文件
     */
    var isLocalFile: Boolean = false

    /**
     * 下载链接
     */
    var downloadUrl: String? = null

    /**
     * 解压目录
     */
    var unzipDir: String? = null

    /**
     * 资源类型，apk 还是压缩包
     */
    abstract fun getType(): String

    /**
     * 是否显示忽略版本的view
     */
    var isShowIgnoreView:Boolean=false

    /**
     * apk或者资源文件大小
     */
    var targetSize:String?=null
}