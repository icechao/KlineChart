package com.icechao.klinelib.utils;

/*************************************************************************
 * Description   :
 *
 * @PackageName  : com.icechao.klinelib.utils
 * @FileName     : ShowCrossModle.java
 * @Author       : chao
 * @Date         : 2019-07-19
 * @Email        : icechliu@gmail.com
 * @version      : V1
 *************************************************************************/
public enum ShowCrossModle {
    SELECT_TOUCHE(0),//touch modle
    SELECT_PRESS(1),//long press modle
    SELECT_BOTH(2);//both modle
    private int model;

    ShowCrossModle(int model) {
        this.model = model;
    }
}
