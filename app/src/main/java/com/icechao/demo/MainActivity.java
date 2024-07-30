package com.icechao.demo;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.icechao.kline.R;
import com.icechao.klinelib.adapter.KLineChartAdapter;
import com.icechao.klinelib.base.BaseKChartView;
import com.icechao.klinelib.callback.OnSelectedChangedListener;
import com.icechao.klinelib.callback.SlidListener;
import com.icechao.klinelib.formatter.DateFormatter;
import com.icechao.klinelib.formatter.ValueFormatter;
import com.icechao.klinelib.model.MarketDepthPercentItem;
import com.icechao.klinelib.model.MarketTradeItem;
import com.icechao.klinelib.utils.DateUtil;
import com.icechao.klinelib.utils.LogUtil;
import com.icechao.klinelib.utils.Status;
import com.icechao.klinelib.view.KChartView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

public class MainActivity extends Activity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private KLineChartAdapter<KChartBean> adapter;

    private KChartView chartView;

    private Handler handler = new Handler();
    private View attachedOperater;
    private View masterOperater;
    private View moreIndex;
    private View klineOperater;
    //    private ReqBean klineReq;
//    private DepthFullView depthFullView;
    private List<KChartBean> all;
    private RadioGroup radioGroup;
    private Vibrator vibrator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTheme(R.style.AppTheme);

        chartView = findViewById(R.id.kLineChartView);
//        depthFullView = findViewById(R.id.full_depth_view);
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
        findViewById(R.id.text_view_change_label_state).setOnClickListener(this);
        findViewById(R.id.text_view_change_theme).setOnClickListener(this);


        findViewById(R.id.text_view_one_minute).setOnClickListener(this);
        findViewById(R.id.text_view_five_minute).setOnClickListener(this);
        findViewById(R.id.text_view_half_hour).setOnClickListener(this);
        findViewById(R.id.text_view_one_week).setOnClickListener(this);
        findViewById(R.id.text_view_one_mounth).setOnClickListener(this);

        findViewById(R.id.text_view_hide_sub).setOnClickListener(this);
        findViewById(R.id.text_view_hide_master).setOnClickListener(this);
        findViewById(R.id.text_view_change_time_color).setOnClickListener(this);


        radioGroup = findViewById(R.id.radio_group_defalt_index);
        radioGroup.setOnCheckedChangeListener(this);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        initKline();
        initData();


    }


    /**
     * K线属性设置
     */
    private void initKline() {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = 3;
        Bitmap logoBitmap = BitmapFactory.decodeResource(
                getResources(), R.drawable.icechao, opts);
        TextView loadingView = new TextView(this);
        loadingView.setText("正在加载...");
        adapter = new KLineChartAdapter();
        chartView = findViewById(R.id.kLineChartView);
        chartView.setTimeLineEndRadius(20);

        chartView.setAdapter(adapter)
                //loading anim
//                .setChartItemWidth(50)
//                .setCandleWidth(30)
                .setSelectedPointRadius(20)
                .setSelectedPointColor(Color.RED)
                .setAnimLoadData(false)
                .setGridColumns(5)
                .setGridRows(5)
                .setMacdHollow(Status.INCREASE_HOLLOW)
                .setPriceLabelInLineClickable(true)
                .setLabelSpace(130)
                .setLogoBitmap(logoBitmap)
                .setLogoAlpha(100)
                //set right can over range
                .setOverScrollRange(getWindow().getWindowManager().getDefaultDisplay().getWidth() / 5)
                //show loading View
                .setLoadingView(loadingView)
                //full or stroke
                .setCandleHollow(Status.INCREASE_HOLLOW)
                .setOnSelectedChangedListener(new OnSelectedChangedListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onSelectedChanged(BaseKChartView view, int index, float... values) {
                        vibrator.vibrate(VibrationEffect.createOneShot(10, 100));

                    }
                })
                .setPriceLabelInLineMarginRight(200)
                .setYLabelBackgroundColor(Color.DKGRAY, true)
                .setSelectInfoBoxPadding(40)
                .showLoading()
                .setBetterX(true)
                //set slid listener
                .setSlidListener(new SlidListener() {
                    @Override
                    public void onSlidLeft() {
                        LogUtil.e("onSlidLeft");
                    }

                    @Override
                    public void onSlidRight() {
                        LogUtil.e("onSlidRight");
                    }
                })
                //set main  formater
                .setMainValueFormatter(new ValueFormatter() {
                    @Override
                    public String format(double value) {
                        return String.format(Locale.CHINA, "%.03f", value);
                    }
                })
                //set vol value  formater
                .setVolFormatter(new ValueFormatter() {
                    @Override
                    public String format(double value) {
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
//        chartView.setAutoFixScrollEnable(false);
//        chartView.setScrollEnable(false);
//        chartView.setScaleEnable(false);
//        chartView.setSelectedInfoBoxColors(Color.RED,Color.BLUE,Color.YELLOW);
//        chartView.setChartVolState(false);

        chartView.setScaleXMax(3);
        chartView.setScaleXMin(0.5f);
    }


    private void initData() {
        //开启线程模拟获取数据
        //设置数据 adapter会自动切回子线程,所有可以在子线程中操作
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    SystemClock.sleep(1000);
                    //1.定义类 KChartBean 继承KLineEntity
                    //2.使用自定义KChartBean 解析数据集合
                    if (null == all) {
                        all = new Gson().fromJson(new DataTest().getData(MainActivity.this),
                                new TypeToken<List<KChartBean>>() {
                                }.getType());
                        //3.设置K线数据  建议直接在子线程设置 KLineChartView 会在 绘制时自动回归主线程
                        adapter.resetData(all.subList(0, 100), false);
//                        chartView.addDrawShape(new ParallelLineShape(chartView));
                    } else {
                        //4.绝大多数是通过最后一根线的时间判断是add还是update
//                        adapter.addLast(all.get(new Random().nextInt(all.size() - 1))); // 尾部追加数据
                        KChartBean data = adapter.getDataSource().get(adapter.getCount() - 1);
                        data.close = data.close + 100;
                        adapter.changeItem(adapter.getCount() - 1, data); // 更新数据
                    }
                    chartView.hideLoading();
                }
            }
        }.start();
    }

    public static HostnameVerifier getHostnameVerifier() {
        HostnameVerifier hostnameVerifier = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                // 校验证书域名


            }
        };
        return hostnameVerifier;
    }



    private boolean changeTheme;

    @Override
    public void onClick(View v) {

        klineOperater.setVisibility(View.GONE);
        masterOperater.setVisibility(View.GONE);
        attachedOperater.setVisibility(View.GONE);
        moreIndex.setVisibility(View.GONE);

        switch (v.getId()) {
            case R.id.text_view_change_time_color:
                chartView.setTimeLineColor(Color.BLUE);
                break;
            case R.id.text_view_change_theme:
                TypedArray typedArray = obtainStyledAttributes(changeTheme ? R.style.kline_style : R.style.kline, R.styleable.KChartView);
                chartView.parseAttrs(typedArray, this);
                changeTheme = !changeTheme;
                break;
            case R.id.text_view_show_hide_vol:
                chartView.setVolShowState(!chartView.getVolShowState());
                break;
            case R.id.text_view_hide_master:
                chartView.hideSelectData();
                chartView.changeMainDrawType(Status.MAIN_NONE);

                break;
            case R.id.text_view_hide_sub:
                chartView.hideSelectData();
                chartView.setIndexDraw(Status.INDEX_NONE);
                break;
            case R.id.text_view_ma:
                chartView.hideSelectData();
                chartView.changeMainDrawType(Status.MAIN_MA);
                break;
            case R.id.text_view_boll:
                chartView.hideSelectData();
                chartView.changeMainDrawType(Status.MAIN_BOLL);
                break;
            case R.id.text_view_macd:
                chartView.hideSelectData();
                chartView.setIndexDraw(Status.INDEX_MACD);
                break;
            case R.id.text_view_kdj:
                chartView.hideSelectData();
                chartView.setIndexDraw(Status.INDEX_KDJ);
                break;
            case R.id.text_view_rsi:
                chartView.hideSelectData();
                chartView.setIndexDraw(Status.INDEX_RSI);
                break;
            case R.id.text_view_wr:
                chartView.hideSelectData();
                chartView.setIndexDraw(Status.INDEX_WR);
                break;
            case R.id.text_view_one_minute:
                chartView.hideSelectData();
                chartView.setKlineState(Status.K_LINE_SHOW_CANDLE_LINE);
                int i1 = new Random(System.currentTimeMillis()).nextInt(100);
                adapter.resetData(all.subList(5, 105));
                break;
            case R.id.text_view_five_minute:
                chartView.hideSelectData();
                chartView.setKlineState(Status.K_LINE_SHOW_CANDLE_LINE);
                i1 = new Random(System.currentTimeMillis()).nextInt(100);
                adapter.resetData(all.subList(4, 104));

                break;
            case R.id.text_view_half_hour:
                chartView.hideSelectData();
                chartView.setKlineState(Status.K_LINE_SHOW_CANDLE_LINE);
                i1 = new Random(System.currentTimeMillis()).nextInt(100);
                adapter.resetData(all.subList(3, 103));
                break;
            case R.id.text_view_one_week:
                chartView.hideSelectData();
                chartView.setKlineState(Status.K_LINE_SHOW_CANDLE_LINE);
                i1 = new Random(System.currentTimeMillis()).nextInt(100);
                adapter.resetData(all.subList(2, 102));
                break;
            case R.id.text_view_one_mounth:
                chartView.hideSelectData();
                chartView.setKlineState(Status.K_LINE_SHOW_CANDLE_LINE);
                i1 = new Random(System.currentTimeMillis()).nextInt(100);
                adapter.resetData(all.subList(1, 101));
                break;
            case R.id.text_view_change_label_state:
                if (i % 2 == 0) {
                    chartView.setYLabelState(Status.LABEL_NONE_GRID);
                } else {
                    chartView.setYLabelState(Status.LABEL_WITH_GRID);
                }
                i++;
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
                chartView.setMaxMinCalcModel(Status.CALC_CLOSE_WITH_LAST);
                chartView.hideSelectData();
                chartView.setKlineState(Status.KLINE_SHOW_TIME_LINE);
                adapter.resetData(all.subList(0, 100), false);
                break;
            case R.id.radio_button_fifteen:
                //计算最大最小值时包含指标值与最新数据
                chartView.setMaxMinCalcModel(Status.CALC_NORMAL_WITH_LAST);
                chartView.hideSelectData();
                chartView.setKlineState(Status.K_LINE_SHOW_CANDLE_LINE);
                adapter.resetData(all.subList(0, 100), false);

                break;
            case R.id.radio_button_one_hour:
                //计算最大最小值时包含指标值与最新数据
                chartView.setMaxMinCalcModel(Status.CALC_NORMAL_WITH_LAST);
                chartView.hideSelectData();
                chartView.setKlineState(Status.K_LINE_SHOW_CANDLE_LINE);
                adapter.resetData(all.subList(110, 400));
                break;
            case R.id.radio_button_four_hour:
                //计算最大最小值时包含指标值与最新数据
                chartView.setMaxMinCalcModel(Status.CALC_NORMAL_WITH_LAST);
                chartView.hideSelectData();
                chartView.setKlineState(Status.K_LINE_SHOW_CANDLE_LINE);
                adapter.resetData(all.subList(150, 450));

                break;
            case R.id.radio_button_one_day:
                //计算最大最小值时包含指标值与最新数据
                chartView.setMaxMinCalcModel(Status.CALC_NORMAL_WITH_LAST);
                chartView.hideSelectData();
                chartView.setKlineState(Status.K_LINE_SHOW_CANDLE_LINE);
                adapter.resetData(all.subList(10, 320));
                break;
            case R.id.radio_button_more:
                //计算最大最小值时包含指标值与最新数据
                chartView.setMaxMinCalcModel(Status.CALC_NORMAL_WITH_LAST);
                moreIndex.setVisibility(View.VISIBLE);
                klineOperater.setVisibility(View.VISIBLE);
                break;
            case R.id.radio_button_index_setting:
                //计算最大最小值时包含指标值与最新数据
                chartView.setMaxMinCalcModel(Status.CALC_NORMAL_WITH_LAST);
                masterOperater.setVisibility(View.VISIBLE);
                attachedOperater.setVisibility(View.VISIBLE);
                klineOperater.setVisibility(View.VISIBLE);
                break;

        }
    }

    int i;


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
