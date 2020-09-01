package com.icechao.klinelib.model;

import java.io.Serializable;

/*************************************************************************
 * Description   :
 *
 * @PackageName  : com.icechao.klinelib.model
 * @FileName     : IMACD.java
 * @Author       : chao
 * @Date         : 2019/4/8
 * @Email        : icechliu@gmail.com
 * @version      : V1
 *************************************************************************/

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
