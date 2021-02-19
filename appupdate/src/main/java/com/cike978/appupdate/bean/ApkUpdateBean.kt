package com.cike978.appupdate.bean

import java.io.Serializable

/**
 * 应用更新
 * Created by yqs97.
 * Date: 2020/8/21
 * Time: 15:18
 */
@Deprecated("弃用")
class ApkUpdateBean :UpdateBean(),Serializable {


    override fun getType(): String {
        return  UpdateBean.TYPE_APP
    }
}