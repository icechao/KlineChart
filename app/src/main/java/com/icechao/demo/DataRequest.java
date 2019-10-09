package com.icechao.demo;

import android.content.Context;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.icechao.klinelib.entity.KLineEntity;
import com.icechao.klinelib.utils.DataTools;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class DataRequest {
    private static List<KChartBean> datas = null;

    public static String getStringFromAssert(Context context, String fileName) {
        try {
            InputStream in = context.getResources().getAssets().open(fileName);
            int length = in.available();
            byte[] buffer = new byte[length];
            in.read(buffer);
            return new String(buffer, 0, buffer.length, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static List<KChartBean> getALL(Context context) {
        if (datas == null) {
            final List<KChartBean> data = new Gson().fromJson(getStringFromAssert(context, "ibm.json"), new TypeToken<List<KChartBean>>() {
            }.getType());
            datas = data;
        }
        return datas;
    }

    /**
     * 分页查询
     *
     * @param context
     * @param offset  开始的索引
     * @param size    每次查询的条数
     */
    public static List<KLineEntity> getData(Context context, int offset, int size) {
        List<? extends KLineEntity> all = getALL(context);
        List<KLineEntity> data = new ArrayList<>();
        int start = Math.max(0, all.size() - 1 - offset - size);
        int stop = Math.min(all.size(), all.size() - offset);
        for (int i = start; i < stop; i++) {
            data.add(all.get(i));
        }
        return data;
    }

}


