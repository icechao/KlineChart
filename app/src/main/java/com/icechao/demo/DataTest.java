package com.icechao.demo;

import android.content.Context;
import android.content.res.AssetManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/*************************************************************************
 * Description   :
 *
 * @PackageName  : com.icechao.demo
 * @FileName     : DataTest.java
 * @Author       : chao
 * @Date         : 2019-08-09
 * @Email        : icechliu@gmail.com
 * @version      : V1
 *
 *                    .::::.
 *                  .::::::::.
 *                 :::::::::::  
 *             ..:::::::::::'
 *           '::::::::::::'
 *             .::::::::::
 *        '::::::::::::::..
 *             ..::::::::::::.
 *           ``::::::::::::::::
 *            ::::``:::::::::'        .:::.
 *           ::::'   ':::::'       .::::::::.
 *         .::::'      ::::     .:::::::'::::.
 *        .:::'       :::::  .:::::::::' ':::::.
 *       .::'        :::::.:::::::::'      ':::::.
 *      .::'         ::::::::::::::'         ``::::.
 *  ...:::           ::::::::::::'              ``::.
 * ```` ':.          ':::::::::'                  ::::..
 *                    '.:::::'                    ':'````..
 *************************************************************************/
public class DataTest {


    public List<KChartBean> getData(Context context) {
        AssetManager assets = context.getResources().getAssets();
        InputStream in = null;
        try {
            in = assets.open("json.txt");
            int length = in.available();
            byte[] buffer = new byte[length];
            in.read(buffer);
            String json = new String(buffer, 0, buffer.length, "UTF-8");
            List<KChartBean> data = new Gson().fromJson(json, new TypeToken<List<KChartBean>>() {
            }.getType());
            return data;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;


    }


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


    public DepthBean getDepth() {
        return new Gson().fromJson(depth, DepthBean.class);
    }

    public class DepthBean {
        private List<Item> asks;
        private List<Item> bids;

        public List<Item> getAsks() {
            return asks;
        }

        public void setAsks(List<Item> asks) {
            this.asks = asks;
        }

        public List<Item> getBids() {
            return bids;
        }

        public void setBids(List<Item> bids) {
            this.bids = bids;
        }
    }

    public class Item {
        private double price;
        private double quantity;

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public double getQuantity() {
            return quantity;
        }

        public void setQuantity(double quantity) {
            this.quantity = quantity;
        }
    }

    private String depth = "{\n" +
            "    \"asks\": [\n" +
            "        {\n" +
            "            \"price\": \"7378.31\",\n" +
            "            \"quantity\": \"0.148886\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7378.71\",\n" +
            "            \"quantity\": \"0.155027\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7379.58\",\n" +
            "            \"quantity\": \"0.16808\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7380.03\",\n" +
            "            \"quantity\": \"0.175012\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7380.51\",\n" +
            "            \"quantity\": \"0.182231\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7380.69\",\n" +
            "            \"quantity\": \"0.149387\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7380.85\",\n" +
            "            \"quantity\": \"0.125195\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7381.01\",\n" +
            "            \"quantity\": \"0.189748\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7381.1\",\n" +
            "            \"quantity\": \"0.031139\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7381.11\",\n" +
            "            \"quantity\": \"0.011395\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7381.12\",\n" +
            "            \"quantity\": \"0.458747\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7381.16\",\n" +
            "            \"quantity\": \"0.004379\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7381.43\",\n" +
            "            \"quantity\": \"0.025414\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7381.44\",\n" +
            "            \"quantity\": \"0.045911\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7381.45\",\n" +
            "            \"quantity\": \"0.042427\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7381.49\",\n" +
            "            \"quantity\": \"0.00826\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7381.53\",\n" +
            "            \"quantity\": \"0.197574\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7381.55\",\n" +
            "            \"quantity\": \"0.081675\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7381.64\",\n" +
            "            \"quantity\": \"0.007923\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7381.81\",\n" +
            "            \"quantity\": \"0.013222\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7381.82\",\n" +
            "            \"quantity\": \"0.022619\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7381.83\",\n" +
            "            \"quantity\": \"0.269388\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7382.07\",\n" +
            "            \"quantity\": \"0.205724\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7382.1\",\n" +
            "            \"quantity\": \"0.269388\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7382.15\",\n" +
            "            \"quantity\": \"0.064465\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7382.16\",\n" +
            "            \"quantity\": \"0.505389\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7382.31\",\n" +
            "            \"quantity\": \"0.079232\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7382.63\",\n" +
            "            \"quantity\": \"0.214209\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7383\",\n" +
            "            \"quantity\": \"0.206062\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7383.21\",\n" +
            "            \"quantity\": \"0.223045\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7383.82\",\n" +
            "            \"quantity\": \"0.232245\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7383.83\",\n" +
            "            \"quantity\": \"0.02305\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7384.42\",\n" +
            "            \"quantity\": \"0.214726\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7384.45\",\n" +
            "            \"quantity\": \"0.241824\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7385.11\",\n" +
            "            \"quantity\": \"0.251798\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7385.8\",\n" +
            "            \"quantity\": \"0.262184\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7386.52\",\n" +
            "            \"quantity\": \"0.272999\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7387.26\",\n" +
            "            \"quantity\": \"0.284259\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7388.04\",\n" +
            "            \"quantity\": \"0.295984\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7388.85\",\n" +
            "            \"quantity\": \"0.308193\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7389.01\",\n" +
            "            \"quantity\": \"0.273172\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7389.02\",\n" +
            "            \"quantity\": \"0.30816\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7390.57\",\n" +
            "            \"quantity\": \"0.333859\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7390.96\",\n" +
            "            \"quantity\": \"0.333866\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7392.53\",\n" +
            "            \"quantity\": \"0.320825\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7392.74\",\n" +
            "            \"quantity\": \"0.273016\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7394.26\",\n" +
            "            \"quantity\": \"0.333872\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7394.45\",\n" +
            "            \"quantity\": \"0.392776\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7394.83\",\n" +
            "            \"quantity\": \"0.361974\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7396.27\",\n" +
            "            \"quantity\": \"0.40773\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7397.93\",\n" +
            "            \"quantity\": \"0.408405\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7398.07\",\n" +
            "            \"quantity\": \"0.40893\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7399.01\",\n" +
            "            \"quantity\": \"0.424999\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7401.49\",\n" +
            "            \"quantity\": \"0.461403\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7403.62\",\n" +
            "            \"quantity\": \"0.542405\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7405.32\",\n" +
            "            \"quantity\": \"0.519564\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7406.84\",\n" +
            "            \"quantity\": \"0.540191\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7406.86\",\n" +
            "            \"quantity\": \"0.540856\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7699\",\n" +
            "            \"quantity\": \"1.036883\"\n" +
            "        }\n" +
            "    ],\n" +
            "    \"bids\": [\n" +
            "        {\n" +
            "            \"price\": \"7371.64\",\n" +
            "            \"quantity\": \"0.117475\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7371.32\",\n" +
            "            \"quantity\": \"0.122311\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7371.26\",\n" +
            "            \"quantity\": \"0.113098\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7371.16\",\n" +
            "            \"quantity\": \"0.12788\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7369.72\",\n" +
            "            \"quantity\": \"0.420314\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7369.67\",\n" +
            "            \"quantity\": \"0.112545\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7369.37\",\n" +
            "            \"quantity\": \"0.11719\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7369.29\",\n" +
            "            \"quantity\": \"0.132546\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7369.05\",\n" +
            "            \"quantity\": \"0.122026\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7368.83\",\n" +
            "            \"quantity\": \"0.007761\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7368.67\",\n" +
            "            \"quantity\": \"0.014228\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7368.37\",\n" +
            "            \"quantity\": \"0.132304\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7368.22\",\n" +
            "            \"quantity\": \"0.065977\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7368.21\",\n" +
            "            \"quantity\": \"0.391816\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7368.19\",\n" +
            "            \"quantity\": \"0.118267\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7367.99\",\n" +
            "            \"quantity\": \"0.013593\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7367.9\",\n" +
            "            \"quantity\": \"0.131564\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7367.76\",\n" +
            "            \"quantity\": \"0.079773\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7367.63\",\n" +
            "            \"quantity\": \"0.143449\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7367.53\",\n" +
            "            \"quantity\": \"0.022515\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7367.24\",\n" +
            "            \"quantity\": \"0.149369\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7366.84\",\n" +
            "            \"quantity\": \"0.155533\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7366.55\",\n" +
            "            \"quantity\": \"0.314271\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7366.47\",\n" +
            "            \"quantity\": \"0.314271\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7366.41\",\n" +
            "            \"quantity\": \"0.161951\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7365.97\",\n" +
            "            \"quantity\": \"0.168634\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7365.89\",\n" +
            "            \"quantity\": \"0.145337\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7365.88\",\n" +
            "            \"quantity\": \"0.310435\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7365.69\",\n" +
            "            \"quantity\": \"0.015154\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7365.51\",\n" +
            "            \"quantity\": \"0.175593\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7364.7\",\n" +
            "            \"quantity\": \"0.03701\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7364.54\",\n" +
            "            \"quantity\": \"0.190384\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7364.35\",\n" +
            "            \"quantity\": \"0.090926\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7364.31\",\n" +
            "            \"quantity\": \"0.298959\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7364.03\",\n" +
            "            \"quantity\": \"0.243013\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7364.02\",\n" +
            "            \"quantity\": \"0.198241\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7363.43\",\n" +
            "            \"quantity\": \"0.123392\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7362.33\",\n" +
            "            \"quantity\": \"0.223809\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7361.72\",\n" +
            "            \"quantity\": \"0.233045\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7361.35\",\n" +
            "            \"quantity\": \"0.263509\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7361.09\",\n" +
            "            \"quantity\": \"0.242662\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7359.74\",\n" +
            "            \"quantity\": \"0.263102\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7359.03\",\n" +
            "            \"quantity\": \"0.27396\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7358.28\",\n" +
            "            \"quantity\": \"0.285265\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7358.2\",\n" +
            "            \"quantity\": \"0.321994\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7356.7\",\n" +
            "            \"quantity\": \"0.309294\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7355.86\",\n" +
            "            \"quantity\": \"0.322058\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7355.53\",\n" +
            "            \"quantity\": \"0.363127\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7354.01\",\n" +
            "            \"quantity\": \"0.349022\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7353.06\",\n" +
            "            \"quantity\": \"0.363374\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7351.41\",\n" +
            "            \"quantity\": \"0.377852\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7350.48\",\n" +
            "            \"quantity\": \"0.394244\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7348.9\",\n" +
            "            \"quantity\": \"0.462728\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7348.81\",\n" +
            "            \"quantity\": \"0.426881\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7347.67\",\n" +
            "            \"quantity\": \"0.285464\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7345.64\",\n" +
            "            \"quantity\": \"0.543869\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7344.6\",\n" +
            "            \"quantity\": \"0.482178\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7344.15\",\n" +
            "            \"quantity\": \"0.5428\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"price\": \"7000\",\n" +
            "            \"quantity\": \"0.001586\"\n" +
            "        }\n" +
            "    ],\n" +
            "    \"symbol\": \"BTC-USDT\",\n" +
            "    \"timestamp\": 1575617776571,\n" +
            "    \"topic\": \"depth:0:BTC-USDT\"\n" +
            "}";
}
