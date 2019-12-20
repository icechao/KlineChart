package com.icechao.demo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.icechao.kline.R;
import com.icechao.klinelib.adapter.KLineChartAdapter;
import com.icechao.klinelib.base.BaseKLineChartView;
import com.icechao.klinelib.entity.MarketDepthPercentItem;
import com.icechao.klinelib.entity.MarketTradeItem;
import com.icechao.klinelib.formatter.DateFormatter;
import com.icechao.klinelib.formatter.ValueFormatter;
import com.icechao.klinelib.utils.DateUtil;
import com.icechao.klinelib.utils.LogUtil;
import com.icechao.klinelib.utils.Status;
import com.icechao.klinelib.view.KLineChartView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends Activity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private KLineChartAdapter<KChartBean> adapter;

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
    //    private List<KChartBean> all;
    private RadioGroup radioGroup;
    private Vibrator vibrator;
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
        findViewById(R.id.text_view_show_hide_vol).setOnClickListener(this);


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

        radioGroup = findViewById(R.id.radio_group_defalt_index);
        radioGroup.setOnCheckedChangeListener(this);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        initKline();
        initData();


    }

    private void initKline() {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = 3;
        Bitmap logoBitmap = BitmapFactory.decodeResource(
                getResources(), R.drawable.icechao, opts);
        TextView loadingView = new TextView(this);
        loadingView.setText("正在加载...");
        adapter = new KLineChartAdapter();
        chartView = findViewById(R.id.kLineChartView);


        chartView.setAdapter(adapter)
                //loading anim
                .setChartItemWidth(50)
                .setCandleWidth(30)
                .setSelectedPointRadius(20)
                .setSelectedPointColor(Color.RED)
                .setAnimLoadData(false)
                .setGridColumns(5)
                .setGridRows(5)
                .setLogoBigmap(logoBitmap)
                .setLogoAlpha(100)
                //set right can over range
                .setOverScrollRange(200)
                //show loading View
                .setLoadingView(loadingView)
                //full or stroke
                .setCandleSolid(false)
                .setOnSelectedChangedListener(new BaseKLineChartView.OnSelectedChangedListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onSelectedChanged(BaseKLineChartView view, int index, float... values) {
                        vibrator.vibrate(VibrationEffect.createOneShot(10, 100));

                    }
                })
                .setSelectorInfoBoxPadding(40)
//                .setChartPaddingTop(0)
                .showLoading()
                .setBetterX(true)
                //set slid listener
//                .setSlidListener(new SlidListener() {
//                    @Override
//                    public void onSlidLeft() {
//                        if (!load) {
//                            chartView.postDelayed(() -> {
//                                chartView.showLoading();
//                                LogUtil.e("onSlidLeft");
//                                List<KChartBean> kChartBeans = all.subList(0, 300);
//                                kChartBeans.addAll(adapter.getDatas());
//                                adapter.resetData(kChartBeans, true);
//                                chartView.hideLoading();
//                                load = true;
//                            }, 2000);
//                        }
//                    }
//
//                    @Override
//                    public void onSlidRight() {
//                        LogUtil.e("onSlidRight");
//
//                    }
//                })
                //set Y label formater
                .setValueFormatter(new ValueFormatter() {
                    @Override
                    public String format(float value) {
                        return String.format(Locale.CHINA, "%.03f", value);
                    }
                })
                //set vol y label formater
                .setVolFormatter(new ValueFormatter() {
                    @Override
                    public String format(float value) {
                        return String.format(Locale.CHINA, "%.03f", value);
                    }
                })
                //set date label formater
                .setDateTimeFormatter(new DateFormatter() {
                    @Override
                    public String format(Date date) {
                        return DateUtil.yyyyMMddFormat.format(date);
                    }
                });
//        chartView.setSelectedInfoBoxColors(Color.RED,Color.BLUE,Color.YELLOW);
//        chartView.setChartVolState(false);

        chartView.setScaleXMax(1);
        chartView.setScaleXMin(0.5f);

    }


    Random random = new Random();

    private void initData() {
        new Thread() {
            @Override
            public void run() {
                //使用子线程延迟,防止页面还没有执行SizeChange方法就已经set数据
                //设置数据 adapter会自动切回子线程,所有可以在子线程中操作
                SystemClock.sleep(1000);
                all = DataRequest.getALL(MainActivity.this);
                //两种设置数据的方式
                adapter.resetData(all.subList(0, 380), true);
//                List<KLineEntity> data = new DataTest().getData();
//                adapter.resetData(all);
                chartView.hideLoading();
//                changeLast();
            }
        }.start();
    }

    /**
     * 模拟增量数据
     */
    private void changeLast() {
        handler.postDelayed(() -> {
            int i = random.nextInt() * 1123 % 400;
//            KChartBean kLineEntity = all.get(Math.abs(new Random().nextInt()) % 100);
            KChartBean kLineEntity = (KChartBean) adapter.getDatas().get(adapter.getCount() - 1);
            KChartBean kLineEntity1 = new KChartBean();
            kLineEntity1.setDate(kLineEntity.date);
            kLineEntity1.setHigh(kLineEntity.getHigh() + 10f);
            kLineEntity1.setClose(kLineEntity.getHigh() + 5f);
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
            if (i++ % 3 == 0) {
//                kLineEntity1.setOpen(adapter.getItem(adapter.getCount() - 1).getClosePrice());
                adapter.addLast(kLineEntity1);
            } else {
//                kLineEntity1.setOpen(adapter.getItem(adapter.getCount() - 1).getOpenPrice());
                adapter.changeItem(adapter.getCount() - 1, kLineEntity1);
            }
            changeLast();
            LogUtil.e(kLineEntity1.toString());

        }, 2000);
    }


    @Override
    public void onClick(View v) {

        klineOperater.setVisibility(View.GONE);
        masterOperater.setVisibility(View.GONE);
        attachedOperater.setVisibility(View.GONE);
        moreIndex.setVisibility(View.GONE);

        switch (v.getId()) {
            case R.id.text_view_show_hide_vol:
                chartView.setVolShowState(!chartView.getVolShowState());
                break;
            case R.id.text_view_hide_master:
                chartView.hideSelectData();
                chartView.changeMainDrawType(Status.MainStatus.NONE);

                break;
            case R.id.text_view_hide_sub:
                chartView.hideSelectData();
                chartView.setIndexDraw(Status.IndexStatus.NONE);
                break;
            case R.id.text_view_ma:
                chartView.hideSelectData();
                chartView.changeMainDrawType(Status.MainStatus.MA);
                break;
            case R.id.text_view_boll:
                chartView.hideSelectData();
                chartView.changeMainDrawType(Status.MainStatus.BOLL);
                break;
            case R.id.text_view_macd:
                chartView.hideSelectData();
                chartView.setIndexDraw(Status.IndexStatus.MACD);
                break;
            case R.id.text_view_kdj:
                chartView.hideSelectData();
                chartView.setIndexDraw(Status.IndexStatus.KDJ);
                break;
            case R.id.text_view_rsi:
                chartView.hideSelectData();
                chartView.setIndexDraw(Status.IndexStatus.RSI);
                break;
            case R.id.text_view_wr:
                chartView.hideSelectData();
                chartView.setIndexDraw(Status.IndexStatus.WR);
                break;
            case R.id.text_view_one_minute:
                chartView.hideSelectData();
                chartView.setKlineState(Status.KlineStatus.K_LINE);
//                adapter.resetData(all);
                break;
            case R.id.text_view_five_minute:
                chartView.hideSelectData();
                chartView.setKlineState(Status.KlineStatus.K_LINE);
//                adapter.resetData(all.subList(0, 100));

                break;
            case R.id.text_view_half_hour:
                chartView.hideSelectData();
                chartView.setKlineState(Status.KlineStatus.K_LINE);
//                adapter.resetData(all.subList(0, 150));
                break;
            case R.id.text_view_one_week:
                chartView.hideSelectData();
                chartView.setKlineState(Status.KlineStatus.K_LINE);
//                adapter.resetData(all.subList(0, 155));
                break;
            case R.id.text_view_one_mounth:
                chartView.hideSelectData();
                chartView.setKlineState(Status.KlineStatus.K_LINE);
//                adapter.resetData(all.subList(0, 165));
                break;

        }
        radioGroup.clearCheck();

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        klineOperater.setVisibility(View.GONE);
        masterOperater.setVisibility(View.GONE);
        attachedOperater.setVisibility(View.GONE);
        moreIndex.setVisibility(View.GONE);


        switch (checkedId) {
            case R.id.radio_button_time_line:
                //计算最大最小值包含收盘价与最新收盘价
                chartView.setMaxMinCalcModel(Status.MaxMinCalcModel.CALC_CLOSE_WITH_LAST);
                chartView.hideSelectData();
                chartView.setKlineState(Status.KlineStatus.TIME_LINE);
//                adapter.resetData(all.subList(10, 140));
                break;
            case R.id.radio_button_fifteen:
                //计算最大最小值时包含指标值与最新数据
                chartView.setMaxMinCalcModel(Status.MaxMinCalcModel.CALC_NORMAL_WITH_LAST);
                chartView.hideSelectData();
                chartView.setKlineState(Status.KlineStatus.K_LINE);
//                adapter.resetData(all.subList(10, 300));

                break;
            case R.id.radio_button_one_hour:
                //计算最大最小值时包含指标值与最新数据
                chartView.setMaxMinCalcModel(Status.MaxMinCalcModel.CALC_NORMAL_WITH_LAST);
                chartView.hideSelectData();
                chartView.setKlineState(Status.KlineStatus.K_LINE);
//                adapter.resetData(all.subList(110, 400));
                break;
            case R.id.radio_button_four_hour:
                //计算最大最小值时包含指标值与最新数据
                chartView.setMaxMinCalcModel(Status.MaxMinCalcModel.CALC_NORMAL_WITH_LAST);
                chartView.hideSelectData();
                chartView.setKlineState(Status.KlineStatus.K_LINE);
//                adapter.resetData(all.subList(150, 450));

                break;
            case R.id.radio_button_one_day:
                //计算最大最小值时包含指标值与最新数据
                chartView.setMaxMinCalcModel(Status.MaxMinCalcModel.CALC_NORMAL_WITH_LAST);
                chartView.hideSelectData();
                chartView.setKlineState(Status.KlineStatus.K_LINE);
//                adapter.resetData(all.subList(10, 320));
                break;
            case R.id.radio_button_more:
                //计算最大最小值时包含指标值与最新数据
                chartView.setMaxMinCalcModel(Status.MaxMinCalcModel.CALC_NORMAL_WITH_LAST);
                moreIndex.setVisibility(View.VISIBLE);
                klineOperater.setVisibility(View.VISIBLE);
                break;
            case R.id.radio_button_index_setting:
                //计算最大最小值时包含指标值与最新数据
                chartView.setMaxMinCalcModel(Status.MaxMinCalcModel.CALC_NORMAL_WITH_LAST);
                masterOperater.setVisibility(View.VISIBLE);
                attachedOperater.setVisibility(View.VISIBLE);
                klineOperater.setVisibility(View.VISIBLE);
                break;
        }
    }


    //-----------------------------------------------------------------------------------------//
    //下面是深度相关的计算
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
//            item.setSymbol(symbol);
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
