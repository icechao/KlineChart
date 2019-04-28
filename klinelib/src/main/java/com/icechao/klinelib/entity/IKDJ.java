package com.icechao.klinelib.entity;

import java.io.Serializable;

/*************************************************************************
 * Description   :
 *
 * @PackageName  : com.icechao.klinelib.utils
 * @FileName     : IKDJ.java
 * @Author       : chao
 * @Date         : 2019/4/8
 * @Email        : icechliu@gmail.com
 * @version      : V1
 *************************************************************************/
@Deprecated
public interface IKDJ extends Serializable {

    /**
     * K值
     */
    float getK();

    /**
     * D值
     */
    float getD();

    /**
     * J值
     */
    float getJ();

}