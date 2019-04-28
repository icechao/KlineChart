package com.icechao.klinelib.entity;

import java.io.Serializable;

/*************************************************************************
 * Description   :
 *
 * @PackageName  : com.icechao.klinelib.utils
 * @FileName     : IMACD.java
 * @Author       : chao
 * @Date         : 2019/4/8
 * @Email        : icechliu@gmail.com
 * @version      : V1
 *************************************************************************/
@Deprecated
public interface IMACD extends Serializable {


    /**
     * DEA值
     */
    float getDea();

    /**
     * DIF值
     */
    float getDif();

    /**
     * MACD值
     */
    float getMacd();

}
