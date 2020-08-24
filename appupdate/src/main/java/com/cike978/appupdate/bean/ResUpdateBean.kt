package com.cike978.appupdate.bean

import java.io.Serializable

/**
 * 资源文件更新
 * Created by yqs97.
 * Date: 2020/8/21
 * Time: 15:18
 */
class ResUpdateBean : UpdateBean(), Serializable {


    override fun getType(): String {
        return UpdateBean.TYPE_ZIP
    }
}