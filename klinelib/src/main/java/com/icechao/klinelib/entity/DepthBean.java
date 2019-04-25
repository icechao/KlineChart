package com.icechao.klinelib.entity;

import java.io.Serializable;
import java.util.List;

/*************************************************************************
 * Description   :
 *
 * @PackageName  : com.icechao.demo.websocket.bean
 * @FileName     : DepthBean.java
 * @Author       : chao
 * @Date         : 2019/4/23
 * @Email        : icechliu@gmail.com
 * @version      : V1
 *************************************************************************/
public class DepthBean implements Serializable {
//    {
//        "ch": "market.btcusdt.depth.percent10",
//            "ts": 1548297435172,
//            "tick": {
//        "asks": [
//			[3672.633737500000000000, 1.282700000000000000]
//		]
//    }
//    }
    /**
     * data : {"asks":[[3578.79,3.5841]],"bids":[[3578.78,0.0015],[3567.01,5.0E-4]]}
     * id : depth
     * rep : market.btcusdt.depth.step0
     * status : ok
     * ts : 1548243211444
     */

    private String ch;
    private DataEntity tick;

    public String getCh() {
        return ch;
    }

    public void setCh(String ch) {
        this.ch = ch;
    }

    public DataEntity getTick() {
        return tick;
    }

    public void setTick(DataEntity tick) {
        this.tick = tick;
    }

    private DataEntity data;
    private String id;
    private String rep;
    private String status;
    private long ts;

    public void setData(DataEntity data) {
        this.data = data;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setRep(String rep) {
        this.rep = rep;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }

    public DataEntity getData() {
        return data;
    }

    public String getId() {
        return id;
    }

    public String getRep() {
        return rep;
    }

    public String getStatus() {
        return status;
    }

    public long getTs() {
        return ts;
    }

    public class DataEntity {
        /**
         * asks : [[3578.79,3.5841]]
         * bids : [[3578.78,0.0015],[3567.01,5.0E-4]]
         */
        //卖
        private List<List<Double>> asks;
        //买
        private List<List<Double>> bids;

        public void setAsks(List<List<Double>> asks) {
            this.asks = asks;
        }

        public void setBids(List<List<Double>> bids) {
            this.bids = bids;
        }

        public List<List<Double>> getAsks() {
            return asks;
        }

        public List<List<Double>> getBids() {
            return bids;
        }
    }
}
