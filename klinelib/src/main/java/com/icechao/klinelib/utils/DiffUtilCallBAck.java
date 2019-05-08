package com.icechao.klinelib.utils;

import android.support.v7.util.DiffUtil;
import com.icechao.klinelib.entity.IDepth;

import java.util.List;

/*************************************************************************
 * Description   :
 *
 * @PackageName  : com.icechao.klinelib.utils
 * @FileName     : DiffUtil.java
 * @Author       : chao
 * @Date         : 2019/4/11
 * @Email        : icechliu@gmail.com
 * @version      : V1
 *************************************************************************/
public class DiffUtilCallBAck<T extends IDepth> extends DiffUtil.Callback {

    private List<T> newDatas;
    private List<T> oldDatas;

    public DiffUtilCallBAck(List<T> newDatas, List<T> oldDatas) {
        this.newDatas = newDatas;
        this.oldDatas = oldDatas;
    }

    @Override
    public int getOldListSize() {
        return oldDatas.size();
    }

    @Override
    public int getNewListSize() {
        return newDatas.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return false;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return false;
    }
}
