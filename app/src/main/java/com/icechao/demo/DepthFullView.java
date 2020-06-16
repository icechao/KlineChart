package com.icechao.demo;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.icechao.kline.R;
import com.icechao.klinelib.model.MarketDepthPercentItem;
import com.icechao.klinelib.view.DepthChartView;

import java.util.List;

/*************************************************************************
 * Description   : 封装一个完整的深度图
 *
 * @PackageName  : com.icechao.kline
 * @FileName     : DepthFullView.java
 * @Author       : chao
 * @Date         : 2019/4/11
 * @Email        : icechliu@gmail.com
 * @version      : V1
 *************************************************************************/
public class DepthFullView extends RelativeLayout {

    private DepthRecycleViewAdapter depthAdapter;
    private DepthChartView depthView;

    public DepthFullView(Context context) {
        super(context);
        initView(context);
    }

    public DepthFullView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public DepthFullView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.kline_depth_part, this);
        depthView = inflate.findViewById(R.id.depth_view);

        RecyclerView recyclerView = inflate.findViewById(R.id.recycley_view_horizental_depth);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        depthAdapter = new DepthRecycleViewAdapter(context);
        recyclerView.getItemAnimator().setChangeDuration(0);
        recyclerView.setAdapter(depthAdapter);

    }

    public DepthChartView getDepthView() {
        return depthView;
    }

    public DepthRecycleViewAdapter getDepthADapter() {
        return depthAdapter;
    }


    public void setDepthChartData(List<MarketDepthPercentItem> percentBuyList, List<MarketDepthPercentItem> percentSellList) {
        depthView.setBuyList(percentBuyList);
        depthView.setSellList(percentSellList);
        depthView.postInvalidate();
    }
}
