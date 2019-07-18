# kline

借鉴开源项目,重写K线,优化性能



### QQ群 :  163565953
### 有问题可以加这个群反馈
### bug持续优化中,争取为大家提供一个最好用的Kline

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

# 部分API  ________  kLineChartView为KLineChartView对象
     
  ### Loadding展示

       kLineChartView.justShowLoading() ;
       
            只显示loading不会显示后面的K线,K线部队只显示背景
            
       kLineChartView.showLoading();
       
            显示loading的同时显示K线

  ### 主图MA/BOLL切换
        
        kLineChartView.changeMainDrawType(MainStatus.MA);
            参数    
            MainStatus.MA, : 显示ma  默认值
            MainStatus.BOLL: 显示boll
            MainStatus.NONE: 只显示CandleLine

  ### 子图指标图切换

        kLineChartView.setChildDraw(ChildStatus.MACD);
            参数
            NONE : //不显示子图  默认值
            MACD : macd
            KDJ  : kdj
            RSI  : rsi
            WR   : wr

  ### 设置是适应X左右边轴坐标的位置

        kLineChartView.setBetterX(boolean betterX);
            参数
            true : X轴最左边和最右边label会向中间缩进显示保证label全部显示在屏幕内  默认值  
            false: 显示为X轴最左边和最右边label会以与对方的坐标点中间对齐的方式显示 
           
  ### K线与分时线切换

        kLineChartView.setMainDrawLine(false);
            参数
            true : 显示折线的
            false: 显示为CandleLine  默认值
           
  ### 十字线跟随手指

        kLineChartView.setCrossFollowTouch(boolean crossFollowTouch) ;
            参数
            true : 跟随手指
            false: 显示为收盘价  默认值
           
  ### 动画加载加载数据时(默认是左到右的加载动画 可以加载数据前设置不使用动画)
        
        kLineChartView.setAnimLoadData(boolean withAnim) ;
            参数
            true : 执行加载数据动画  默认值
            false: 不执行加载数据动画
       
### 功能及优化

  - 重写分时线绘制
  - 重写十字线绘制
  - 重写选中的绘制
  - 重写网格的绘制
  - 重写背景网格绘制
  - 重写Y轴label的绘制
  - 重写十字线纵线的绘制
  - 重写CandleLine绘制
  - 重写成交量柱状图的绘制
  - 重写数据计算
  - 重写滑动计算
  - 重写放大计算
  - 重写选中框的计算
  - 重写指标线的算法
  - 重写当前页面K线数目的计算
  - 添加深度图
  - 添加当前价格的时间线
  - 添加分时线尾呼吸灯效果
  - 添加追加尾部数据的方法
  - 添加主视图logo相关设置
  - 添加时间和值的动态格式化
  - 添加K线数据变化时动画效果
  - 添加加载数据的动画执行开关
  - 添加最新价格和屏幕右侧指示线
  - 添加十字线模式开关(选中Y值跟随手指或指向最新价)
  - 添加LoadingView设置(可以自定义View并自定义动画)
  - 添加X轴左右坐标显示模式开关(相对坐标点居中,屏幕内  默认屏幕内显示)
  - 删除加载更多逻辑(重置设置数据不需要加载更多)

  
# 效果图
后面复用历史数据会导致K线看起来有点乱,数据问题



<img src="https://github.com/icechao/KlineChart/blob/master/7i7by-qncwl.gif" width="300" hegiht="500" align=center />




