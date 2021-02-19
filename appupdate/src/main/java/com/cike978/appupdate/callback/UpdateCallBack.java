package com.cike978.appupdate.callback;

import com.cike978.appupdate.bean.ResVersionInfo;
import com.cike978.appupdate.bean.VersionDTO;

/**
 * Created by yqs97.
 * Date: 2020/8/21
 * Time: 16:03
 */
public interface UpdateCallBack extends  DialogUpdateCallBack {

   void checkVersionResult(boolean isSuccess, VersionDTO versionDTO);

}
