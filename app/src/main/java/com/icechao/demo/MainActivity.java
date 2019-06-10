package com.icechao.demo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.icechao.kline.R;
import com.icechao.klinelib.adapter.KLineChartAdapter;
import com.icechao.klinelib.entity.KLineEntity;
import com.icechao.klinelib.entity.MarketDepthPercentItem;
import com.icechao.klinelib.entity.MarketTradeItem;
import com.icechao.klinelib.formatter.DateFormatter;
import com.icechao.klinelib.formatter.ValueFormatter;
import com.icechao.klinelib.utils.ChildStatus;
import com.icechao.klinelib.utils.MainStatus;
import com.icechao.klinelib.utils.SlidListener;
import com.icechao.klinelib.view.KLineChartView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MainActivity extends Activity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private List<KLineEntity> datas;


    private KLineChartAdapter adapter;

    private KLineChartView chartView;

    private Handler handler = new Handler();
    private TextView textViewPriceText;
    private TextView textViewRiseAndFallText;
    private TextView textViewCny;
    private TextView textViewHighPriceText;
    private TextView textViewLowPriceText;
    private TextView textViewVolumeSumText;
    private View attachedOperater;
    private View masterOperater;
    private View moreIndex;
    private View klineOperater;
    //    private ReqBean klineReq;
    private DepthFullView depthFullView;
    private List<KChartBean> all;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chartView = findViewById(R.id.kLineChartView);

        depthFullView = findViewById(R.id.full_depth_view);

        attachedOperater = findViewById(R.id.linear_layout_attached_operater);
        masterOperater = findViewById(R.id.linear_layout_master_operater);
        moreIndex = findViewById(R.id.linear_layout_index_more);
        klineOperater = findViewById(R.id.kline_operater);


        findViewById(R.id.text_view_ma).setOnClickListener(this);
        findViewById(R.id.text_view_boll).setOnClickListener(this);
        findViewById(R.id.text_view_macd).setOnClickListener(this);
        findViewById(R.id.text_view_kdj).setOnClickListener(this);
        findViewById(R.id.text_view_rsi).setOnClickListener(this);
        findViewById(R.id.text_view_wr).setOnClickListener(this);


        findViewById(R.id.text_view_one_minute).setOnClickListener(this);
        findViewById(R.id.text_view_five_minute).setOnClickListener(this);
        findViewById(R.id.text_view_half_hour).setOnClickListener(this);
        findViewById(R.id.text_view_one_week).setOnClickListener(this);
        findViewById(R.id.text_view_one_mounth).setOnClickListener(this);

        findViewById(R.id.text_view_hide_sub).setOnClickListener(this);
        findViewById(R.id.text_view_hide_master).setOnClickListener(this);


        textViewPriceText = findViewById(R.id.text_view_price_text);
        textViewRiseAndFallText = findViewById(R.id.text_view_rise_and_fall_text);
        textViewCny = findViewById(R.id.text_view_cny);
        textViewHighPriceText = findViewById(R.id.high_price_text);
        textViewLowPriceText = findViewById(R.id.low_price_text);
        textViewVolumeSumText = findViewById(R.id.volume_sum_text);

        RadioGroup radioGroup = findViewById(R.id.radio_group_defalt_index);
        radioGroup.setOnCheckedChangeListener(this);

        initKline();
        initData();


    }

    private void initKline() {
        adapter = new KLineChartAdapter();
        chartView = findViewById(R.id.kLineChartView);
        chartView.setAdapter(adapter);
        chartView.setDateTimeFormatter(new DateFormatter());
        chartView.setGridColumns(5);
        chartView.setGridRows(5);
        chartView.setValueFormatter(new ValueFormatter() {
            @Override
            public String format(float value) {
                return String.format("%.2f", value);
            }
        });
        chartView.setVolFormatter(new ValueFormatter() {
            @Override
            public String format(float value) {
                return String.format("%.2f", value);
            }
        });
        chartView.setOverScrollRange(getWindowManager().getDefaultDisplay().getWidth() / 5);
        chartView.showLoading();


        chartView.setSlidListener(new SlidListener() {
            @Override
            public void onSlidLeft() {
                LogUtil.e("onSlidLeft");
            }

            @Override
            public void onSlidRight() {
                LogUtil.e("onSlidRight");
            }
        });

//        chartView.setCrossFollowTouch(true);
    }

    int i = 0;
    Random random = new Random();

    private void initData() {
        new Thread() {
            @Override
            public void run() {
                SystemClock.sleep(1000);
                runOnUiThread(() -> {
                    all = DataRequest.getALL(MainActivity.this);
                    adapter.resetData(all.subList(111, 333));
                    chartView.hideLoading();
                    changeLast();
                });
            }
        }.start();
    }

    private void changeLast() {
        handler.postDelayed(() -> {
            int i = random.nextInt() * 1123 % 400;
            KChartBean kLineEntity = all.get(Math.abs(new Random().nextInt()) % 100);
            KChartBean kLineEntity1 = new KChartBean();
            kLineEntity1.setDate(kLineEntity.date);
            kLineEntity1.setHigh(kLineEntity.getHigh());
            kLineEntity1.setClose(kLineEntity.getClose());
            kLineEntity1.setOpen(kLineEntity.getOpen());
            kLineEntity1.setLow(kLineEntity.getLow());
            kLineEntity1.setVolume(kLineEntity.getVolume());

            textViewPriceText.setText(kLineEntity1.getClosePrice() + "");
            float v = kLineEntity1.getClose() - kLineEntity1.getOpen();
            textViewRiseAndFallText.setText(String.format("%.2f", v * 100 / kLineEntity1.getOpen()));
            textViewCny.setText(String.format("%.2f", 6.5 * kLineEntity1.getClose()));
            textViewHighPriceText.setText(kLineEntity1.getHigh() + "");
            textViewLowPriceText.setText(kLineEntity1.getLow() + "");
            textViewVolumeSumText.setText(kLineEntity1.getVolume() + "");


            if (i++ % 10 == 0) {
                kLineEntity1.setOpen(adapter.getItem(adapter.getCount() - 1).getClosePrice());
                adapter.addLast(kLineEntity1);
            } else {
                kLineEntity1.setOpen(adapter.getItem(adapter.getCount() - 1).getOpenPrice());
                adapter.changeItem(adapter.getCount() - 1, kLineEntity1);


            }
            changeLast();

        }, 2000);
    }


    @Override
    public void onClick(View v) {

        klineOperater.setVisibility(View.GONE);
        masterOperater.setVisibility(View.GONE);
        attachedOperater.setVisibility(View.GONE);
        moreIndex.setVisibility(View.GONE);

        switch (v.getId()) {
            case R.id.text_view_hide_master:
                chartView.hideSelectData();
                chartView.changeMainDrawType(MainStatus.NONE);

                break;
            case R.id.text_view_hide_sub:
                chartView.hideSelectData();
                chartView.hideChildDraw();
                break;
            case R.id.text_view_ma:
                chartView.hideSelectData();
                chartView.changeMainDrawType(MainStatus.MA);
                break;
            case R.id.text_view_boll:
                chartView.hideSelectData();
                chartView.changeMainDrawType(MainStatus.BOLL);
                break;
            case R.id.text_view_macd:
                chartView.hideSelectData();
                chartView.setChildDraw(ChildStatus.MACD);
                break;
            case R.id.text_view_kdj:
                chartView.hideSelectData();
                chartView.setChildDraw(ChildStatus.KDJ);
                break;
            case R.id.text_view_rsi:
                chartView.hideSelectData();
                chartView.setChildDraw(ChildStatus.RSI);
                break;
            case R.id.text_view_wr:
                chartView.hideSelectData();
                chartView.setChildDraw(ChildStatus.WR);
                break;
            case R.id.text_view_one_minute:
                chartView.hideSelectData();
                chartView.setMainDrawLine(false);
                adapter.resetData(all);
                break;
            case R.id.text_view_five_minute:
                chartView.hideSelectData();
                chartView.setMainDrawLine(false);
                adapter.resetData(all);

                break;
            case R.id.text_view_half_hour:
                chartView.hideSelectData();
                chartView.setMainDrawLine(false);
                adapter.resetData(all);
                break;
            case R.id.text_view_one_week:
                chartView.hideSelectData();
                chartView.setMainDrawLine(false);
                adapter.resetData(all);
                break;
            case R.id.text_view_one_mounth:
                chartView.hideSelectData();
                chartView.setMainDrawLine(false);
                adapter.resetData(all);
                break;

        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        klineOperater.setVisibility(View.GONE);
        masterOperater.setVisibility(View.GONE);
        attachedOperater.setVisibility(View.GONE);
        moreIndex.setVisibility(View.GONE);

        switch (checkedId) {
            case R.id.radio_button_time_line:
                chartView.hideSelectData();
                chartView.setMainDrawLine(true);
                adapter.resetData(all);
                break;
            case R.id.radio_button_fifteen:
                chartView.hideSelectData();
                chartView.setMainDrawLine(false);
                adapter.resetData(all);

                break;
            case R.id.radio_button_one_hour:
                chartView.hideSelectData();
                chartView.setMainDrawLine(false);
                adapter.resetData(all.subList(110, 400));
                break;
            case R.id.radio_button_four_hour:
                chartView.hideSelectData();
                chartView.setMainDrawLine(false);
                adapter.resetData(all.subList(150, 450));

                break;
            case R.id.radio_button_one_day:
                chartView.hideSelectData();
                chartView.setMainDrawLine(false);
                adapter.resetData(all.subList(10, 320));
                break;
            case R.id.radio_button_more:
                moreIndex.setVisibility(View.VISIBLE);
                klineOperater.setVisibility(View.VISIBLE);
                break;
            case R.id.radio_button_index_setting:
                masterOperater.setVisibility(View.VISIBLE);
                attachedOperater.setVisibility(View.VISIBLE);
                klineOperater.setVisibility(View.VISIBLE);
                break;


        }
    }


    //-----------------------------------------------------------------------------------------//
    //下面是深度想着的计算
    //-----------------------------------------------------------------------------------------//

    private List<MarketTradeItem> askList = new ArrayList<>();

    private List<MarketTradeItem> bidList = new ArrayList<>();


    /**
     * 买卖数据更新
     *
     * @param symbol
     * @param list
     * @param priceAndAmount
     * @param totalList
     * @param type
     * @param totalAmount
     */
    private void updateBuySellList(String symbol, List<MarketTradeItem> list, List<List<Double>> priceAndAmount, List<Double> totalList, int type, double totalAmount) {
        int loop = Math.min(20, priceAndAmount.size());
        list.clear();
        MarketTradeItem item;

        for (int i = 0; i < loop; i++) {
            item = new MarketTradeItem();
            item.setTradeType(MarketTradeItem.MARKET_TRADE);
            item.setNeedDraw(true);
            item.setSymbol(symbol);
            item.setPrice(priceAndAmount.get(i).get(0));
            item.setAmount(priceAndAmount.get(i).get(1));
            double amount = totalList.get(i);
            int progress = (int) (Double.compare(totalAmount, 0) == 0 ? 1 : amount * 100 / totalAmount);
            if (progress < 1)
                progress = 1;

            item.setProgress(progress);
            item.setType(type);
            list.add(item);
        }

    }

    /**
     * 更新深度数据
     *
     * @param symbol
     * @param list
     * @param priceAndAmount
     * @param totalList
     * @param type
     */
    private void updatePercentBuySellList(String symbol, List<MarketDepthPercentItem> list, List<List<Double>> priceAndAmount, List<Double> totalList, int type) {
        int loop = priceAndAmount.size();
        list.clear();
        MarketDepthPercentItem item;

        //订阅websocket得到的深度数据 进行加工
        //根据不同的需要算法可能需要修改

        for (int i = 0; i < loop; i++) {
            item = new MarketDepthPercentItem();
            item.setSymbol(symbol);
            item.setPrice(priceAndAmount.get(i).get(0));
            double totalAmount = totalList.get(i);
            item.setAmount(totalAmount);
            list.add(item);
        }
        if (type == MarketTradeItem.BUY_TYPE) {
            Collections.reverse(list);
        }

    }

    private List<Double> getPercentTotalAmount(List<List<Double>> list) {
        List<Double> totalList = new ArrayList<>();
        double amount = 0;

        if (list == null)
            return totalList;

        int loop = list.size();

        for (int i = 0; i < loop; i++) {
            if (list.get(i).size() >= 2) {
                amount += list.get(i).get(1);
                totalList.add(amount);
            }
        }
        return totalList;
    }

    @NonNull
    private List<Double> getTotalAmount(List<List<Double>> list) {
        List<Double> totalList = new ArrayList<>();
        double amount = 0;

        if (list == null)
            return totalList;

        int loop = Math.min(20, list.size());

        for (int i = 0; i < loop; i++) {
            if (list.get(i).size() >= 2) {
                amount += list.get(i).get(1);
                totalList.add(amount);
            }
        }
        return totalList;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
