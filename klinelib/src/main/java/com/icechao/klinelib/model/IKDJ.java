package com.icechao.klinelib.model;

import java.io.Serializable;

/*************************************************************************
 * Description   :
 *
 * @PackageName  : com.icechao.klinelib.model
 * @FileName     : IKDJ.java
 * @Author       : chao
 * @Date         : 2019/4/8
 * @Email        : icechliu@gmail.com
 * @version      : V1
 *************************************************************************/
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