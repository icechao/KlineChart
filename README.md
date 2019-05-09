# kline
改自开源项目的K线
完整的K线功能,可拿来直接使用

使用步骤: 

  1. <b>继承KlineEntry写一个model<b>
  2. 复写方法
          /**
           * 开盘价
           */
          float getOpenPrice();

          /**
           * 最高价
           */
          float getHighPrice();

          /**
           * 最低价
           */
          float getLowPrice();

          /**
           * 收盘价
           */
          float getClosePrice();

          /**
           * 交易量
           *
           * @return
           */
          float getVolume();
     如果日期不是Long类型,需要复写getId方法返回Long类型时间
          getDate()
  3. 在布局中直接使用

            <com.icechao.klinelib.view.KLineChartView
              android:id="@+id/kLineChartView"
              android:layout_width="match_parent"
              android:layout_height="580dp"
              android:background="@color/color_081734" />
      支持自定义属性
              自定义属性
              attrs.xml
  4. 初始化k线,更多方法见 KLineChartView
  
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
                     chartView.getVolDraw().setValueFormatter(new ValueFormatter() {
                         @Override
                         public String format(float value) {
                             return String.format("%.2f", value);
                         }
                     });
                     chartView.setOverScrollRange(getWindowManager().getDefaultDisplay().getWidth() / 5);
                     chartView.showLoading();
                 }
  5.设置数据 
  
           重新填充数据
           resetData(List<KlineEntry>);
           尾部追加数据 
           addLast(KlineEntry);
           修改某个数据 
           changeItem(KlineEntry);
       如果有需要在前面追加数据可以自定义方法参考addLast


    
   justShowLoading :  一个只显示loading不会显示后面的K线loading慢时可能会是一个底色页面
   showLoading : 显示loading的同时,只有当重置数据k线才会变化,不然k线依旧会展示在loading的下层



      - 修改十字线绘制
      - 修改分时线绘制
      - 重写CandleLine绘制
      - 重写滑动计算方式
      - 重写放大计算方式
      - 添加深度图
      - 分时线尾添加呼吸灯效果
      - K线结尾数据发生变化时添加动画效果
      - 添加当前价格的时间线
      - 重写选中的绘制
      - 优化重置数据计算
      - 优化当前页面K线数目的计算
      - 重写Y轴label的绘制
      - 重写网格的绘制
      - 最新价格和屏幕右侧添加指示线
      - 重写十字线纵线的绘制
      - 修改选中框的计算方式
      - 添加追加尾部数据的方法
      - 优化成交量柱状图的绘制
      - 删除加载更多逻辑
      - 添加时间和值的动态设置格式化
      - 优化macd  rsi 指标线的算法
      - 重写网格绘制算法
      - 添加设置LoadingView的方法,loadingView可以在调用show/Hide  Loading时执行一些动画

  
  
 QQ群 :  163565953
 有问题可以加这个群反馈
  
#### 效果图
后面复用历史数据会导致K线看起来有点乱,数据问题



<img src="https://github.com/icechao/KlineChart/blob/master/7i7by-qncwl.gif" width="300" hegiht="500" align=center />


修改自:https://github.com/tifezh/KChartView

