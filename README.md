# kline(尽量避免改动源码,方便修复bug重新依赖)
 ****************************************************
 * 加群反馈,优化功能会同步到群                  
 * QQ群: __163565953__     <a target="_blank" href="//shang.qq.com/wpa/qunwpa?idkey=578dc312547759f4a308318e159f92ab2354ca4bff7272cf197233e271f2e214"><img border="0" src="http://pub.idqqimg.com/wpa/images/group.png" alt="点击加群" title="点击加群"></a>                             
 * 觉得好,不求您打赏,请您留下star                   
 ****************************************************
## 效果图

<img src="https://github.com/icechao/KlineChart/blob/master/1565013719576.gif" width="320" hegiht="500" align=center />


## 使用步骤: 

1. 添加依赖  Add dependency
            
            项目的build.gradle中

                        buildscript {

                           repositories {
                              ...
                               jcenter()

                           }
                           ...
                        }

                        allprojects {
                           repositories {
                           ...
                               jcenter()
                           }
                        }
            
            
            工程的build.gradle中

                        implementation 'com.icechao.klinelib:klinelib:1.1.1'

            
2. <b>继承KlineEntry复写对应方法返回 高 开 低 收 量 时间 Inherit the corresponding method of klineentry replication to return the time, high ,opening and low, receipt</b>
  
            public class KChartBean extends KLineEntity {
            //必须复写此方法返回毫秒值
            @Override
            public Long getDate() {
              return date;
            }
            //必须复写此方法返回开盘价
            @Override
            public float getOpenPrice() {
              return open;
            }
            //必须复写此方法返回最高价
            @Override
            public float getHighPrice() {
              return high;
            }
            //必须复写此方法返回最高价
            @Override
            public float getLowPrice() {
              return low;
            }
            //必须复写此方法返回收盘价
            @Override
            public float getClosePrice() {
              return close;
            }
            //必须复写此方法返回成交量
            @Override
            public float getVolume() {
              return volume;
            }
            //其他方法不做限制
            ......
            }
          
3. <b>在布局中使用  Use in layout</b>
  
            <com.icechao.klinelib.view.KLineChartView
             android:id="@+id/kLineChartView"
             android:layout_width="matchParent"
             android:layout_height="580dp"
             android:background="@color/color_081734"
             //app:...
             />
             
4. <b>使用KlineLib中的ScrollView已解决滑动冲突,也可参考解决滑动布局</b>
             


      使用属性配置   属性  
        
      | Attribute name | Value type | Attribute implication | Default value | 
      | ------ | ------ | ------ | ------ |  
      | betterXLabel  | boolean | 两边X轴坐标向内缩进 | true,可能会引起X轴label重叠可设置为false |
      | betterSelectedXLabel  | boolean | 选中X坐标在屏幕边缘时向内缩进 |  |
      | marketInfosLabel  | resource | 行情文字(字符串数组) | 行情文字 默认中文 |
      | maiLegendMarginTop  | dimension | 主视图Legend上边距 | 10 |
      | dateLabelHorizentalPadding  | dimension | X轴label横向pading,影响选中时 | 10 |
      | dateLabelVerticalPadding  | dimension | X轴label纵向pading | 10 |
      | maiLegendMarginTop  | dimension | 主视图Legend上边距 | 10 |
      | paddingBottom  | dimension | chart上部内容边距 | 15,底部显示X轴label空间 |
      | paddingTop  | dimension | 主视图Lengend上边距 | 30,第一个网格的位置 |
      | childPaddingTop  | dimension | 所有子视图上边距 | 15 |
      | chartLogo  | resource | 主视图logo | |
      | candleRightPadding  | dimension | 主视图与右侧内边距 | |
      | increaseColor  | color | 涨颜色| |
      | decreaseColor  | color | 跌颜色| |
      | chartTextSize  | dimension | 通用文字大小| |
      | textColor  | color | 文字颜色| |
      | limitTextColor  | color | 最大最小值文字颜色| |
      | limitTextSize  | dimension | 最大最小值文字大小| |
      | lineWidth  | dimension | 指标线线宽 | |
      | klineItemWidth  | dimension | 蜡烛图加外围空隙宽| |
      | candleWidth  | dimension | 蜡烛图柱宽 | |
      | candleLineWidth  | dimension | K线空心时宽度 | |
      | priceLineWidth  | dimension | 价格线宽 | 1dp |
      | priceLineColor  | color | 价格线颜色 | |
      | priceLineRightColor  | color | 价格线右侧虚线时颜色 | |
      | priceLineRightTextColor  | color | 价格线右侧虚线价格文字颜色 | |
      | priceLineBackgroundColor  | color | 价格线横框背景色 | #CFD3A9 |
      | priceLineBoxMarginRight  | dimension | 价格框右边距 | 120 |
      | priceLineBoxShapeWidth  | dimension | 价格线框内三角形占宽| 10 |
      | priceLineBoxShapeHeight  | dimension | 价格线框内三角形占高| 20 |
      | priceLineBoxHeight  | dimension | 价格线上的框高度| 40 |
      | priceLineBoxPadding  | dimension | 价格线上的框的内边距| 20 |
      | priceLineBoxShapeTextMargin  | dimension | 价格框文字与图形距离| 10 |
      | priceLineBoxBorderWidth  | dimension | 价格线框边框宽度| 1 |
      | priceLineBoxBorderColor  | color | 价格线框边框颜色| #CFD3A9 |
      | backgroundFillTopColor  | color |背景渐变上部颜色 | #1C1E27 |
      | backgroundFillBottomColor  | color |背景渐变下部颜色 | 控制背景色 |
      | backgroundAlpha  | integer | 背景色透明度| 控制背景色 default 18 |
      | backgroundColor   | color | 背景色  | 控制背景色 |
      | dotRightPriceBoxBackGroundAlpha  | integer | 虚线右侧最新价格填充透明度 default 150 | |
      | yLabelMarginRight  | dimension | y轴上label与右侧边距 |10 |
      | timeLineColor   | color| 分时线颜色| |
      | timeLineFillTopColor  | color | 分时线填充渐变上部颜色 | |
      | timeLineFillBottomColor  | color |背景色渐变下部颜色 | |
      | timeLine_endPointColor  | color | 分时线尾颜色 | |
      | timeLine_endMultiply  | int | 分时线尾圆变化最大倍数 | |
      | timeLine_endRadiu  | dimension | 分时线尾圆半径 | |
      | candleSolid  | boolean | K线是否空心| false |
      | gridLineWidth  | dimension | 网格线宽| |
      | gridLineColor  | color | 网格颜色 | |
      | gridLineColumns  | integer | 网格列数 | |
      | gridLineRows  | integer | 网格行数 | |
      | selectXLineWidth  | dimension | 选中十字线X轴线宽| |
      | selectLabelBoderWidth  | dimension | 选中十字线label边框线宽| 2 |
      | selectLabelBoderColor  | color | 选中坐标边框颜色| |
      | selectYLineWidth  | dimension | 十字线Y轴线宽| |
      | selectXLineColor  | color |选中十字线X线颜色 | |
      | selectYLineColor  | color |  选中十字线Y线画笔颜色| |
      | selectYColor  | color | 选中十字线Y轴渐变色,-1时不绘制渐变| |
      | selectCrossBigColor  | color | 选中十字线相交点圆颜色| |
      | selectCrossPointColor  | color | 选中十字线相交点圆颜色 | |
      | selectShowCrossPoint  | color | 是否显示选中十字线相交点圆 | |
      | selectCrossPointRadiu  | color | 选中十字线相交点小圆半径 | |
      | selectPriceBoxBackgroundColor  | color | 选中价格框背景色 | |
      | selectPriceBoxHorizentalPadding  | dimension | 选中价格框的横向padding,三角形的高为横+纵padding | 4dp |
      | selectPriceBox_verticalPadding  | dimension | 选中价格框的纵向向padding,三角形的高为横+纵padding | 2dp |
      | selectInfoBoxMargin  | dimension | 选中行弹出框的margin | |
      | selectInfoBoxPadding  | dimension | 选中行弹出框行间距,上下为此值*2 | |
      | selectInfoBoxTextColor  | dimension | 选中行弹出框文字颜色 | Color.WHITE |
      | selectInfoBoxBorderColor  | dimension | 选中行弹出框边框颜色 | Color.WHITE |
      | selectInfoBoxBackgroundColor  | dimension | 选中行弹出框背景颜色 | Color.DKGRAY |
      | selectTextSize  | dimension | 选择框文字大小 | |
      | macdIncreaseColor  | color | macd标准线上柱颜色| |
      | macdDecreaseColor  | color | macd标准线下柱颜色 | |
      | macdWidth  | dimension | macd柱状图宽度 | |
      | difColor  | color | dif线颜色 | |
      | deaColor  | color | dea线颜色 | |
      | macdColor  | color | macd线颜色 | |
      | kColor  | color | k线的颜色 | |
      | dColor  | color | d线的颜色 | |
      | jColor  | color | j线的颜色 | |
      | rsi1Color  | color | 第1根rsi线的颜色 | |
      | rsi2Color  | color |第2根rsi线的颜色 | 暂无 |
      | ris3Color  | color | 第3根rsi线的颜色 | 暂无 |
      | upColor  | color | up线颜色 | |
      | mbColor  | color | mb线颜色 | |
      | dnColor  | color | dn线颜色 | |
      | wr1Color  | color | 第1根wr线的颜色| |
      | wr2Color  | color | 第2根wr线的颜色| 暂无 |
      | wr3Color  | color | 第3根wr线的颜色| 暂无 |
      | ma1Color  | color | 第1根ma线的颜色 | |
      | ma2Color  | color | 第2根ma线的颜色 | |
      | ma3Color  | color | 第3根ma线的颜色 | |
      | volMa1Color  | color | 第1根量ma线的颜色 | |
      | volMa2Color  | color | 第2根量ma线的颜色 | |
      | volLengendColor   | color| 成交量图例颜色 | |
      | volLineChartColor  | color | 成交量显示线状图时的颜色 | #4B85D6 |
      | volLengendMarginTop  | dimension | 成交量图例距离成交量顶部距离 | 10 |

              
4. <b>初始化k线</b>
  
            //设置数据适配器
            chartView.setAdapter(adapter)
                    //设置开场动画
                    .setAnimLoadData(false)
                    //添加日期格式化,可动态修改
                    .setDateTimeFormatter(new DateFormatter())
                    //网格列
                    .setGridColumns(5)
                    //网格行
                    .setGridRows(5)
                    //logo
                    .setLogoBigmap(bitmap)
                    //logo透明度
                    .setLogoAlpha(100)
                    //左滑超出宽度
                    .setOverScrollRange(getWindowManager().getDefaultDisplay().getWidth() / 5)
                    //滑动边界监听(可能重复调用)
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
                    //Y值精度格式化(可重复设置)
                    .setValueFormatter(new ValueFormatter() {
                        @Override
                        public String format(float value) {
                            return String.format("%.03f", value);
                        }
                    })
                    //成交量格式化
                    .setVolFormatter(new ValueFormatter() {
                        @Override
                        public String format(float value) {
                            return String.format("%.03f", value);
                        }
                    })
                    //设置loadingView
                    .setLoadingView(textView)
                    //显示loading
                    .showLoading();

         
5. <b>使用KLineChartAdapter设置数据</b>
  
            如果没有将数据适配器保存可以通过ChartView的getAdapter方法获取
            chartView.getAdapter()

            填充或重新填充数据,bool表示是否重置展示位
            resetData(List<KlineEntry>,boolean);

            尾部追加数据 
            addLast(KlineEntry);

            修改某个数据 
            changeItem(KlineEntry);

            如果有需要在前面追加多个数据可以继承KLineChartAdapter自定义方法参考addLast方法
            
            添加完数据后需要手动隐藏loading
            chartView.hideLoading();
    
6. <b>(可选)解决滑动冲突</b>
  
            滑动布局使用 com.icechao.klinelib.view.ScrollView
    

## 更多API查看[KLineChartView](https://github.com/icechao/KlineChart/blob/master/klinelib/src/main/java/com/icechao/klinelib/view/KLineChartView.java)或加QQ群咨询

## 部分API  ________  kLineChartView为KLineChartView对象


  ### 自定义数据计算方法
       
       klineChartView.getAdapter.setDataTools(DataTools tools)
            
            不实现此方法会使用默认的计算方法

     
  ### Loadding展示

       kLineChartView.justShowLoading() ;
       
            只显示loading不会显示后面的K线,K线部队只显示背景
            Showing only loading does not show the K-line behind. K-line troops only show the background.
            
       kLineChartView.showLoading();
       
            显示loading的同时显示K线
            Display loading while displaying K-line
            
  ### 国际化 Internationalization
        
        kLineChartView.setMarketInfoText(String[] marketInfoText);
              参数    
            marketInfoText : 时间 开 高 低 收 涨跌额 涨跌幅 成交量 组成的文字数组
                             Text Array of Time Opening, High and Low Closing, Rising and Declining Volume, Rising and Declining Volume

  ### 主图MA/BOLL切换 MA/BOLL Switching of Main Graph
        
        kLineChartView.changeMainDrawType(Status.MainStatus.MA);
            参数    
            Status.MainStatus.MA, : 显示ma  默认值
            Status.MainStatus.BOLL: 显示boll
            Status.MainStatus.NONE: 只显示CandleLine

  ### 子图指标图切换 Subgraph Index Graph Switching

        kLineChartView.setIndexdDraw(Status.ChildStatus.MACD);
            参数
            Status.ChildStatus.NONE : //不显示子图  默认值
            Status.ChildStatus.MACD : macd
            Status.ChildStatus. KDJ : kdj
            Status.ChildStatus.RSI  : rsi
            Status.ChildStatus.WR   : wr

  ### 设置是适应X左右边轴坐标的位置 Setting is the position that suits the coordinates of the left and right axes of X.

        kLineChartView.setBetterX(boolean betterX);
            参数
            true : X轴最左边和最右边label会向中间缩进显示保证label全部显示在屏幕内  默认值  
            false: 显示为X轴最左边和最右边label会以与对方的坐标点中间对齐的方式显示 
           
  ### K线与分时线切换 K-line and time-sharing line switching

        kLineChartView.setKlineState(Status.KlineStatus klineStatus);
            参数
            Status.KlineStatus.K_LINE    : 显示K线
            Status.KlineStatus.TIME_LINE : 显示为分时线
           
  ### 十字线显示模式 Crossline display mode

        kLineChartView.setCrossFollowTouch(Status.TouchCrossModel model) ;
            参数
            TouchCrossModel.FOLLOW_FIGNERS : 跟随手指
            TouchCrossModel.SHOW_CLOSE     : 显示为收盘价  默认值
           
  ### 动画加载加载数据(默认是左到右的加载动画) When animation loads data
        
        kLineChartView.setAnimLoadData(boolean withAnim);
            参数
            true : 执行加载数据动画  默认值
            false: 不执行加载数据动画
   
  ### K线右侧内边距 K-line right inner margin
  
        kLineChartView.setKlineRightPadding(float klineRightPadding);
            参数
            float : 内边距
  
  ### 设置十字线触发模式 Setting Crossline Trigger Mode
  
        kLineChartView.setSelectedTouchModle(Status.ShowCrossModle showCrossModle);
            参数 
            Status.ShowCrossModle.SELECT_TOUCHE : 点击显示
            Status.ShowCrossModle.SELECTPRESS   : 长按显示
            Status.ShowCrossModle.SELECT_BOTH   : 点击长按混合
            
  ### 添加logo  Add logo
  
        kLineChartView.setLogoBigmap(Bitmap bitmap);
            参数
            Bitmap : 图片Bitmap 可以设置好大后传入
        
        kLineChartView.setLogoResouce(int bitmapRes);
            参数
            bitmapRes : 资源id 原大小显示
            
  ### logo透明度设置 Logo Transparency Settings
        
        kLineChartView.setLogoAlpha(int alpha);
            参数
            alpha : 0-255 logo透明度
            
  ### logo位置    --修改方法   Logo location
        
        kLineChartView.setLogoPadding(float left, float top);
            参数
            left : logo的左侧padding,相对KlineChartView主视图区域
            top  : logo的顶部padding,相对KlineChartView主视图区域
            
  ### K线滑动监听  K-line Sliding Monitor
        
        kLineChartView.setSlidListener(SlidListener slidListener);
            参数
            slidListener : SlidListener对象 监听滑动到最左和最右
            

  ### K线左滑的超出范围 K-Line Left Slip Out of Range
        
         kLineChartView.setOverScrollRange(float overScrollRange);
            参数
            overScrollRange : 滑动距离
                 
  ### 是否显示十字线的交点圆 Whether to show the intersection circle of the cross
        
         kLineChartView.setSelectedShowCrossPoint(boolean showCrossPoint);
            参数
            true  : 显示
            false : 隐藏
                 
  ### 选中坐标边框线宽 Select the coordinate border width
        
         kLineChartView.setSelectedLabelBorderWidth(float width);
            参数
            width : label边框线宽 
                 
  ### 选中坐标边框线颜色 Select coordinate border color
        
         kLineChartView.setSelectedLabelBorderColor(int color);
            参数
            color : 颜色值
                 
  ### 价格线框边框宽度 Price wire frame border width
        
         kLineChartView.setPricelineBoxBorderWidth(float width);
            参数
            width : 价格线浮框的线宽
                 
  ### 价格线框边框颜色 Price wireframe border color
        
         kLineChartView.setPricelineBoxBorderColor(int color);
            参数
            color : 价格线浮框的边框颜色

  ### macd颜色 macd color
        
         kLineChartView.setMacdChartColor(int inColor, int deColor);
            参数
            inColor : >0 波段的颜色
            deColor : <0 波段的颜色
            
  ### macd空心设置 Setting of MACD hollow histogram
        
         kLineChartView.setMacdStrokeModel(Status.MacdStrokeModel model);
            参数
            NONE_STROKE     :   增长的值实心
            All_STROKE      :   所有的值空心
            INCREASE_STROKE :   增长的值空心
            DECREASE_STROKE :   下跌的值空心
            
  ### macd空心时线宽设置 Line width of MACD hollow histogram
        
         kLineChartView.setMacdStrockWidth(int lineWidth);
            参数
            lineWidth : macd柱子空心时线宽 默认0.8dp

  ### 隐藏选择器内容 Hidden selector content
        
         kLineChartView.hideSelectData();
         
         隐藏选中的信息
         
  ### 主视图所占高度比例 Proportion of the height of the main view
        
         kLineChartView.setMainPercent(float mainPresent);
            参数 
            mainPresent : : 占比,用0-1之间的数表示
         
  ### 量视图所占高度比例  Proportion of Height in Quantitative View
        
         kLineChartView.setVolPercent(float volPresent);
            参数 
            volPresent : : 占比,用0-1之间的数表示
         
  ### 子视图所占高度比例 Subview Height Ratio
        
         kLineChartView.setChildPercent(float childPresent);
            参数
            childPresent : 占比,用0-1之间的数表示

  ### 切换显示/隐藏交易量 Switching Display/Hide Trading Volume
        
         kLineChartView.setVolShowState(boolean state);
            参数
            true : 显示 
            false: 隐藏
            
  ### 获取当前显示/隐藏交易量 Get the current display/hide transaction volume
        
         kLineChartView.getVolShowState();
             
  ### 设置交易量绘制 BarChart/LineChart  Setting Trading Volume to Draw BarChart/LineChart
        
         kLineChartView.setVolChartStatues(Status.VolChartStatus volChartStatus);
            参数 
            null : defaul , time line show line chart , K line show barChart
            Status.VolChartStatus.BAR_CHART  : 柱状图
            Status.VolChartStatus.LINE_CHART : 线状图
            
  ### 强制隐藏信息框 Forced Hiding Information Box
        kLineChartView.hideMarketInfoBox(boolean forceHide)  
        参数 
            true  : 隐藏
            false : 显示       
  
  ### 替换MainDraw,继承后自由实现功能 Replace MainDraw and implement functionality freely after inheritance
          kLineChartView.resetMainDraw(T t)
           
  ### 替换volDraw,继承后自由实现功能 Replace volDraw and implement functions freely after inheritance
          kLineChartView.resetVoDraw(T t)
                    
  ### 替换IndexDraw,继承后自由实现功能 Replace IndexDraw and implement functionality freely after inheritance
          kLineChartView.resetIndexDraw(Status.IndexStatus status, T t) 
  
  ### 最大最小值计算模式,影响Y轴的最大最小值 Max min calculation mode, affecting the max min value of Y axis
          kLineChartView.setMaxMinCalcModel(Status.MaxMinCalcModel model)
          参数 
              MaxMinCalcModel.CALC_NORMAL_WITH_LAST :   计算显示的线和最新线的全部值 默认值
              MaxMinCalcModel.CALC_CLOSE_WITH_LAST  :   计算显示的线和最新线的close值
              MaxMinCalcModel.CALC_NORMAL_WITH_SHOW :   计算显示的线全部值 
              MaxMinCalcModel.CALC_CLOSE_WITH_SHOW  :   计算显示的线的close值
            
            
 
