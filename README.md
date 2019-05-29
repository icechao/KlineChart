# kline
写过两次K线, 借鉴开源项目,改良一个能拿来直接用的K线,基于原项目基础[ScallAnddScrallView](https://github.com/tifezh/KChartView),重写绘制计算,优化渲染速度,优化绘制算法


使用步骤: 

1. <b>继承KlineEntry写一个model</b>
2. <b>复写方法</b>

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
         */
        float getVolume();
     如果日期不是Long类型,需要复写getId方法返回Long类型时间
        getDate()
        
        
3. <b>在布局中直接使用</b>

          <com.icechao.klinelib.view.KLineChartView
            android:id="@+id/kLineChartView"
            android:layout_width="match_parent"
            android:layout_height="580dp"
            android:background="@color/color_081734" />

        支持自定义属性
            自定义属性查看:

      [属性列表](https://github.com/icechao/KlineChart/blob/master/klinelib/src/main/res/values/attrs.xml)

4. <b>初始化k线,   更多方法见 KLineChartView</b>

            private void initKline() {
            
                   //设置K线的数据适配器
                   chartView.setAdapter(new KLineChartAdapter());
                   
                   //设置X轴时间格式化对象,根据不同波段可以重新设置
                   chartView.setDateTimeFormatter(new DateFormatter());
                   
                   //背景网络列
                   chartView.setGridColumns(5);
                   
                   //背景网络行
                   chartView.setGridRows(5);
                   
                   //设置Y轴值格式化对象,默认保留两位小数
                   chartView.setValueFormatter(new ValueFormatter() {
                       @Override
                       public String format(float value) {
                           return String.format("%.2f", value);
                       }
                   });
                   
                   //设置交易量格式化对象,默认保留两位小数
                   chartView.getVolDraw().setValueFormatter(new ValueFormatter() {
                       @Override
                       public String format(float value) {
                           return String.format("%.2f", value);
                       }
                   });
                   
                   //设置K线最右侧缩进距离
                   chartView.setOverScrollRange(getWindowManager().getDefaultDisplay().getWidth() / 5);
                   //显示loading
                   chartView.showLoading();
                   
               }
5.<b>设置数据</b>

         //重新填充数据
         resetData(List<KlineEntry>);
         
         //尾部追加数据 
         addLast(KlineEntry);
         
         //修改某个数据 
         changeItem(KlineEntry);
         
     如果有需要在前面追加数据可以自定义方法参考addLast


### Loadding展示

       justShowLoading() //  一个只显示loading不会显示后面的K线loading慢时可能会是一个底色页面
       showLoading() // 显示loading的同时,只有当重置数据k线才会变化,不然k线依旧会展示在loading的下层

### 主图MA/BOLL切换
        
        chartView为KLineChartView对象
        
        chartView.changeMainDrawType(MainStatus.MA);
            
              MainStatus.MA, //显示ma
              MainStatus.BOLL, //显示boll
              MainStatus.NONE //只显示CandleLine

### 子图指标图切换

        chartView为KLineChartView对象
        
        chartView.setChildDraw(ChildStatus.MACD);
        
           NONE,//隐藏子图
           MACD,
           KDJ, 
           RSI, 
           WR;
           
### K线与分时线切换

        chartView为KLineChartView对象
        
        chartView.setMainDrawLine(false);
        
           传入true显示折线的
           传入false显示为CandleLine 
           
### 十字线跟随模式

        chartView为KLineChartView对象
        
        chartView.setCrossFollowTouch(boolean crossFollowTouch) ;
        
           传入true跟随手指
           传入false显示为收盘价


#### 更多设置API查看

   [KLineChartView](klinelib/src/main/java/com/icechao/klinelib/view/KLineChartView.java)

  
  
 QQ群 :  163565953
 有问题可以加这个群反馈
  
#### 效果图
后面复用历史数据会导致K线看起来有点乱,数据问题



<img src="https://github.com/icechao/KlineChart/blob/master/7i7by-qncwl.gif" width="300" hegiht="500" align=center />




