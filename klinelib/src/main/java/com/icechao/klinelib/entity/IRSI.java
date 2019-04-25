package com.icechao.klinelib.entity;

import java.io.Serializable;

/*************************************************************************
 * Description   :
 *
 * @PackageName  : com.icechao.klinelib.utils
 * @FileName     : IRSI.java
 * @Author       : chao
 * @Date         : 2019/4/8
 * @Email        : icechliu@gmail.com
 * @version      : V1
 *************************************************************************/

public interface IRSI extends Serializable {

    /**
     * RSI值
     */
    float getRsiOne();

    float getRsiTwo();

    float getRsiThree();

}
