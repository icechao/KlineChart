package com.icechao.klinelib.entity;

import java.io.Serializable;

/*************************************************************************
 * Description   :
 *
 * @PackageName  : com.icechao.klinelib.utils
 * @FileName     : IWR.java
 * @Author       : chao
 * @Date         : 2019/4/8
 * @Email        : icechliu@gmail.com
 * @version      : V1
 *************************************************************************/
@Deprecated
public interface IWR extends Serializable {

    /**
     * %R值
     */
    Float getWrOne();

    Float getWrTwo();

    Float getWrThree();

}