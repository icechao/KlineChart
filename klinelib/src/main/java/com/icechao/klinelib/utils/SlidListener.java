package com.icechao.klinelib.utils;

/*************************************************************************
 * Description   :
 *
 * @PackageName  : com.icechao.klinelib.utils
 * @FileName     : SlidLisenter.java
 * @Author       : chao
 * @Date         : 2019-06-10
 * @Email        : icechliu@gmail.com
 * @version      : V1
 *************************************************************************/
public interface SlidListener {

    /**
     * the kline slid to left
     */
    void onSlidLeft();

    /**
     * the kline slid to right
     */
    void onSlidRight();
}
