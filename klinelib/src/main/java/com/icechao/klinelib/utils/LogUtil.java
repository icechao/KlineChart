package com.icechao.klinelib.utils;

import android.util.Log;

import com.icechao.klinelib.BuildConfig;


/*************************************************************************
 * Description   :
 *
 * @PackageName  : com.icechao.klinelib.utils
 * @FileName     : LogUtil.java
 * @Author       : chao
 * @Date         : 2019/4/10
 * @Email        : icechliu@gmail.com
 * @version      : V1
 *************************************************************************/
public class LogUtil {
    private final static String TAG = "CHAO=>";

    public static void e(Object o) {
        if (BuildConfig.DEBUG) {
            Log.e(TAG, String.valueOf(o));
        }
    }

}
