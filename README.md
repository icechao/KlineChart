# kline

借鉴开源项目,重写K线,优化性能



### QQ群 :  163565953
### 有问题可以加这个群反馈
### bug持续优化中,争取为大家提供一个最好用的KLine

## 使用步骤: 

  1. <b>继承KlineEntry实现Bean类,复写对应方法返回 高 开 低 收 量 时间</b>
  
            public class KChartBean extends KLineEntity {
            
                  @Override
                  public Long getDate() {
                      try {
                          return new SimpleDateFormat("yyy/MM/dd").parse(date).getTime();
                      } catch (ParseException e) {
                          e.printStackTrace();
                          return 0L;
                      }
                  }

                  @Override
                  public float getOpenPrice() {
                      return open;
                  }

                  @Override
                  public float getHighPrice() {
                      return high;
                  }

                  @Override
                  public float getLowPrice() {
                      return low;
                  }

                  @Override
                  public float getClosePrice() {
                      return close;
                  }

                  @Override
                  public float getVolume() {
                      return volume;
                  }
                  ......
            }
          
  2. <b>在布局中直接使用</b>
  
            <com.icechao.klinelib.view.KLineChartView
              android:id="@+id/kLineChartView"
              android:layout_width="match_parent"
              android:layout_height="580dp"
              android:background="@color/color_081734" />
             
          支持自定义属性
              自定义属性查看:
             
        [属性列表](https://github.com/icechao/KlineChart/blob/master/klinelib/src/main/res/values/attrs.xml)
              
  3. <b>初始化k线</b>
  
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
                     //设置K线左侧滑动的超出距离
                     chartView.setOverScrollRange(getWindowManager().getDefaultDisplay().getWidth() / 5);
                     //显示loading
                     chartView.showLoading();
                 }
                 
         
  4.<b>使用KLineChartAdapter设置数据</b>
  
           如果没有将数据适配器保存可以通过ChartView的getAdapter方法获取
           chartView.getAdapter()
  
           填充或重新填充数据
           resetData(List<KlineEntry>);
           
           尾部追加数据 
           addLast(KlineEntry);
           
           修改某个数据 
           changeItem(KlineEntry);
           
           如果有需要在前面追加多个数据可以继承KLineChartAdapter自定义方法参考addLast方法

# 查看全部API点击[这里KLineChartView](https://github.com/icechao/KlineChart/blob/master/klinelib/src/main/java/com/icechao/klinelib/view/KLineChartView.java)或加QQ群咨询

# 部分API 
     
 ### Loadding展示

       justShowLoading :  一个只显示loading不会显示后面的K线loading慢时可能会是一个底色页面
       showLoading : 显示loading的同时,只有当重置数据k线才会变化,不然k线依旧会展示在loading的下层

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

 ### 设置是适应X左右边轴坐标的位置

        chartView.setBetterX(boolean betterX);
           默认值true  X轴最左边和最右边label会向中间缩进显示保证label全部显示在屏幕内
           传入false显示为X轴最左边和最右边label会以与对方的坐标点中间对齐的方式显示 
           
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
      - 添加手指出动时十字线的绘制方式(Y值跟随手指,Y轴指向最新价)
      - 优化左右两边X坐标显示

  
# 效果图
后面复用历史数据会导致K线看起来有点乱,数据问题



<img src="https://github.com/icechao/KlineChart/blob/master/7i7by-qncwl.gif" width="300" hegiht="500" align=center />




