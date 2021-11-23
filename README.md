# Star一下次直接找到不用再搜索
# kline(尽量避免改动源码,方便修复bug重新依赖)
 ****************************************************
 * 加群反馈,优化功能会同步到群                  
 * QQ群: __163565953__     <a target="_blank" href="//shang.qq.com/wpa/qunwpa?idkey=578dc312547759f4a308318e159f92ab2354ca4bff7272cf197233e271f2e214"><img border="0" src="http://pub.idqqimg.com/wpa/images/group.png" alt="点击加群" title="点击加群"></a>                             
 * 点击Star 防止丢失                   
 ****************************************************

## 效果图

<img src="https://github.com/icechao/KlineChart/blob/master/1565013719576.gif" width="320" hegiht="480" align=center />

## 微信
<img src="https://github.com/icechao/KlineChart/blob/master/wxGroup.jpg" width="320" hegiht="480" align=center />

## 五步接入: 

1. 添加依赖  Add dependency
            
            项目的build.gradle中  In the project's build.gradle

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
            
            
            工程的build.gradle中  In the mudule's build.gradle

                        implementation 'com.icechao.klinelib:klinelib:2.0.5'

            
2. <b>继承KlineEntry定义自己的K线的数据Bean,复写抽象方法返回 :高 开 低 收 量 时间 The order information inherits the data bean of klineentry to define its own K-line, and the replication abstract method returns: high opening and low receipt time</b>
  
            public class KChartBean extends KLineEntity {
            //必须覆写此方法返回毫秒值 Override this method to return the time in milliseconds
            @Override
            public Long getDate() {
              return date;
            }
            //必须覆写此方法返回开盘价 Override this method to return the opening price
            @Override
            public float getOpenPrice() {
              return open;
            }
            //必须覆写此方法返回最高价  Override this method to return the maximum price
            @Override
            public float getHighPrice() {
              return high;
            }
            //必须覆写此方法返回最低价  Override this method to return the lowest price
            @Override
            public float getLowPrice() {
              return low;
            }
            //必须覆写此方法返回收盘价  Override this method to return the closing price
            @Override
            public float getClosePrice() {
              return close;
            }
            //必须覆写此方法返回成交量  Override this method to return volume
            @Override
            public float getVolume() {
              return volume;
            }
            //其他方法不做限制
            ......
            }
          
3. <b>在布局中使用  Use in layout</b>
  
            <com.icechao.klinelib.view.KChartView
             android:id="@+id/kLineChartView"
             android:layout_width="matchParent"
             android:layout_height="580dp"
             android:background="@color/color_081734"
             //app:...
             />
             
4. <b>使用自己定义的数据Bean为K线绑定数据  Bind data for K-line with self-defined data bean</b>
  
            kLineChartView.getAdapter().resetData(List<KChartBean>)
             
5. <b>使用KlineLib中的ScrollView已解决滑动冲突,也可参考解决滑动布局 Use the Scrollview in klinelib to solve the sliding conflict, or refer to solve the sliding layout</b>
             


      使用属性配置   属性  
        
      | Attribute name | Value type | Attribute implication | Default value |
      | ------ | ------ | ------ | ------ |  
      | selectedXLabelBackgroundColor | color|  设置选中X轴坐标背景色 |   |
      | priceLabelInLineTextColor | color|  设置价格线上的文字颜色 |   |
      | priceLabelInLineTextSize | color|  设置价格线上的文字大小 |   |
      | selectedLabelTextColor | color|  设置选中X坐标文字颜色 |   |
      | selectedLabelTextSize | dimension |  设置选中X坐标文字大小 |   |
      | priceLabelInLineBoxMarginRight | dimension |  设置价格线上价格框离右距离 |   |
      | priceLabelInLineShapeWidth | dimension | 价格线上价格图形宽  |   |
      | priceLabelInLineShapeHeight | dimension | 价格线上价格图形高  |   |
      | priceLabelInLineBoxHeight | dimension | 设置价格线上价格框高度  |   |
      | priceLabelInLineBoxPadding | dimension | 设置价格线上价格框内边距  |   |
      | priceLabelInLineBoxShapeTextMargin | dimension | 价格线上价格文字与图形的间隔  |   |
      | priceLabelInLineClickable | boolean |  设置价格线价格框可点击 |   |
      | priceLabelInLineBoxBackgroundColor | color |  设置右侧价格框背景色 |   |
      | priceLabelRightBackgroundColor | color |  设置价格线右侧框背景 |   |
      | priceLabelInLineBoxBorderColor | color |  设置价格线右侧框边框颜色 |   |
      | priceLabelInLineBoxBorderWidth | dimension |  设置价格线框边框宽度 |   |
      | priceLabelInLineBoxRadius | dimension |  设置价格线上价格框圆角半径 |   |
      | priceLineRightLabelBackGroundAlpha | integer | 设置价格线右侧标签的背景透明度  |   |
      | priceLabelRightTextColor | color | 设置价格线右侧价格文字的颜色  |   |
      | priceLineRightColor | color |  设置价格线右侧的颜色 |   |
      | ppriceLineWidth | color |  设置价格线的宽度  |   |
      | showPriceLine| boolean|  设置显示价格线|   |
      | showPriceLabelInLine| boolean |  设置显示价格线上的价格 |   |
      | priceLineColor | color |  设置价格线颜色 |   |
      | priceLineDotSolidWidth | dimension |  价格线虚线实心宽度 |   |
      | priceLineDotStrokeWidth | dimension | 价格线实心间隔  |   |
      | showRightDotPriceLine| boolean| 显示右侧虚线和价格  |   |
      | selectedXLineWidth | dimension |  设置选择器横线宽 |   |
      | selectedYLineWidth | dimension | 设置十字线竖线宽度  |   |
      | selectedXLineColor | color |  设置十字线横线颜色  |   |
      | selectedYLineColor | color |  设置十字线竖线画笔颜色  |   |
      | selectedYColor | color | 选中的线的Y轴颜色  |   |
      | selectedCrossBigColor | color | 设置都十字线选中点外圆颜色|   |
      | selectedCrossPointRadius | dimension |  设置十字线相交小圆半径 |   |
      | selectedCrossPointColor | color |  设置十字线交点小圆颜色 |   |
      | selectedShowCrossPoint | boolean | 设置选中时是否显示十字线的交点圆  |   |
      | selectedPriceBoxBackgroundColor | color | 设置选中Y值背景色  |   |
      | selectedInfoTextSize | dimension |  设置选择器文字大小 |   |
      | selectedPriceBoxHorizontalPadding | dimension | 选中时价格label的横向padding  |   |
      | selectedPriceBoxVerticalPadding | dimension | 选中时价格label的纵向padding  |   |
      | selectedInfoBoxPadding | dimension |  选中信息框内边距,上下为此值*2 |   |
      | selectedInfoBoxMargin | dimension | 选中行弹出框与边缘的距离  |   |
      | selectedInfoBoxTextColor | color |  设置选择器弹出框文字颜色 |   |
      | selectedInfoBoxBorderColor | color | 设置选择器弹出框边框颜色  |   |
      | selectedInfoBoxBackgroundColor | color |设置选择器背景颜色  |   |
      | selectedLabelBorderWidth | dimension |  选中时X坐标边框线宽|   |
      | selectedLabelBorderColor | color | 选中时X坐标边框线颜色  |   |
      | selectedInfoLabel | reference | 设置选中框的文本  |   |
      | closeFollowTouch | boolean | 设置十字线跟随手势移动/显示收盘价  |   |
      | yLabelMarginBorder | dimension | 设置y轴上Label与视图右边距  |   |
      | backgroundFillTopColor | color |  设置背景色顶部颜色  |   |
      | backgroundFillBottomColor | color |  设置背景色底部颜色  |   |
      | timeLineColor | color | 设置分时线颜色  |   |
      | timeLineFillTopColor | color | 设置分时线填充渐变的顶部颜色  |   |
      | timeLineFillBottomColor | color | 设置分时线填充渐变的底部颜色  |   |
      | timeLineEndPointColor | color | 分时线呼吸灯的颜色  |   |
      | timeLineEndRadius | integer | 分时线呼吸灯的颜色半径  |   |
      | timeLineEndMultiply |float | timeLineEndMultiply  |   |
      | selectedDateBoxVerticalPadding | dimension | 选中十字线X轴坐标连框纵向内边距  |   |
      | selectedDateBoxHorizontalPadding | dimension | 选中十字线X轴坐标连框横向内边距  |   |
      | mainLegendMarginTop | dimension | 设置主实图图例距离视图上边缘的距离  |   |
      | legendMarginLeft | dimension | 设置图例距离视图左边缘的距离  |   |
      | increaseColor | color |  设置涨的颜色 |   |
      | decreaseColor | color | 设置跌的颜色  |   |
      | betterXLabel | boolean | 设置是否自适应X左右边轴坐标的位置,默认true |   |
      | labelTextSize | dimension | 设置坐标文字大小 |   |
      | labelTextColor | color | 设置坐标轴坐标文字颜色  |   |
      | yLabelAlign | boolean |  设置Y轴显示在左侧/右侧 |   |
      | betterSelectedXLabel | boolean | b设置是否自适应X左右边轴坐标的位置,默认true|   |
      | commonTextSize | dimension | 统一设置设置文字大小  |   |
      | mainMarginTop | dimension | 设置上方padding  |   |
      | paddingBottom | dimension | 设置下方padding  |   |
      | childPaddingTop | dimension | 子视图的顶部padding  |   |
      | commonTextColor | color |  设置通用文字颜色 |   |
      | lineWidth | dimension | 全局通用线宽  |   |
      | itemWidth | dimension | 设置每根K线总宽度(包含外间隙)  |   |
      | candleWidth | dimension | 设置每根蜡烛图宽度  |   |
      | candleLineWidth | dimension |   设置蜡烛线画笔宽(空心时的线宽) |   |
      | chartLogo | reference | 设置K线显示的logo |   |
      | limitTextColor | color | 设置主视图最大/最小值文字颜色 |   |
      | limitTextSize | dimension | 设置主图片最大/最小值文字大小 |   |
      | candleHollow | integer | 蜡烛是否空心  |  `` 原属性为candleSolid `` |
      | gridLineWidth | dimension |  设置背景网格线宽 |   |
      | gridLineColor | color |  设置背景网格线颜色 |   |
      | gridLineRows | integer | 设置背景网格行数  |    |
      | gridLineColumns | integer | 设置背景网格列数  |   |
      | macdStrokeWidth | color | macd空心时线宽  |   |
      | macdIncreaseColor | color | 设置macd 上涨颜色|   |
      | macdDecreaseColor | color | 设置macd 下跌颜色 |   |
      | macdHolow| color | 设置macd 下跌颜色 |   |
      | macdWidth | dimension |  macd柱状图宽 |   |
      | difColor | color | 设置DIF颜色  |   |
      | deaColor | color | 设置DEA颜色  |   |
      | macdColor | color | 设置MACD颜色  |   |
      | wr1Color | color | 设置WR1颜色  |   |
      | wr2Color | color | 设置WR2颜色    |   |
      | wr3Color | color | 设置WR3颜色    |   |
      | kColor | color |  设置K颜色 |   |
      | dColor | color |  设置D颜色 |   |
      | jColor | color | 设置J颜色  |   |
      | rsi1Color | color | 设置RSI1颜色  |   |
      | rsi2Color | color | 设置RSI2颜色 |   |
      | ris3Color | color | 设置RSI3颜色   |   |
      | upColor | color | 设置UP颜色 |   |
      | mbColor | color | 设置MB颜色 |   |
      | dnColor | color | 设置DN颜色 |   |
      | ma1Color | color | 设置MA1颜色 |   |
      | ma2Color | color | 设置MA2颜色  |   |
      | ma3Color | color | 设置MA3颜色  |   |
      | volMa1Color | color | 设置交易量MA1颜色  |   |
      | volMa2Color | color | 设置交易量MA2颜色  |   |
      | volLegendColor | color |  交易量图例颜色 |   |
      | volLineChartColor | color |  设置当成交量视图显示为线时的颜色 |   |
      | volLegendMarginTop | dimension | 交易量图例距离量视图上边缘的距离  |   |

              
4. <b>初始化k线  Initialize K-line</b>
  
            //设置数据适配器  Set up data adapter
            chartView.setAdapter(adapter)
                    //显示加载动画  Show load animation
                    .setAnimLoadData(false)
                    //日期格式器  Date formatter
                    .setDateTimeFormatter(new DateFormatter())
                    //网格列  Grid column
                    .setGridColumns(5)
                    //网格行 Grid row
                    .setGridRows(5)
                    //logo
                    .setLogoBigmap(bitmap)
                    //logo透明度  Logo transparency
                    .setLogoAlpha(100)
                    //Slide left K line inside indent width
                    .setOverScrollRange(getWindowManager().getDefaultDisplay().getWidth() / 5)
                    //滑动边界监听  Sliding boundary monitoring
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
                    //Y值精度格式化   Y value precision format
                    .setValueFormatter(new ValueFormatter() {
                        @Override
                        public String format(float value) {
                            return String.format("%.03f", value);
                        }
                    })
                    //成交量格式化 Volume format
                    .setVolFormatter(new ValueFormatter() {
                        @Override
                        public String format(float value) {
                            return String.format("%.03f", value);
                        }
                    })
                    //设置loadingView   set loading view
                    .setLoadingView(textView)
                    //显示loading  show loading view
                    .showLoading();

         
5. <b>使用KLineChartAdapter设置数据  Using klinechartadapter to set up data</b>
  
            如果没有将数据适配器保存可以通过ChartView的getAdapter方法获取  If the data adapter is not saved, it can be obtained through the getadapter method of chartview
            chartView.getAdapter()

            填充或重新填充数据,bool表示是否重置展示位   Fill or repopulate the data. Bool indicates whether to reset the display bit
            resetData(List<KlineEntry>,boolean);

            尾部追加数据  Append data at the end
            addLast(KlineEntry);

            修改某个数据   Modify a data
            changeItem(int position, T data)

            如果有需要在前面追加多个数据可以继承KLineChartAdapter自定义方法参考addLast方法   If you need to append more than one data, you can inherit the klinechartadapter custom method reference addlast method
            添加完数据后需要手动隐藏loading   After adding data, you need to hide loading manually
            
            chartView.hideLoading();
    
6. <b>(可选)解决滑动冲突  (optional) resolve sliding conflict</b>
  
            滑动布局 com.icechao.klinelib.view.ScrollView
    

## 所有属性相关均有与之对应的Set方法


## 部分API  ________  kLineChartView为KLineChartView对象


  ### 自定义数据计算方法  Custom data calculation method
       
       klineChartView.getAdapter.setDataTools(DataTools tools)
            
            不实现此方法会使用默认的计算方法  Do not implement this method will use the default calculation method

     
  ### Loadding展示  show loading view

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

  ### 设置是否适应X左右边轴坐标的位置 Set whether to adapt to the position of left and right axis coordinates of X

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
            

  ### K线左滑 右侧超出范围 Left sliding right side of K-line is out of range
        
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
        
         kLineChartView.setMacdStrokeModel(Status.HollowModel model);
            参数
            NONE_HOLLOW     :   所有的值实心
            All_HOLLOW      :   所有的值空心
            INCREASE_HOLLOW :   上扬的值空心
            DECREASE_HOLLOW :   下扬的值空心

  ### K线空心设置 Setting of Kline hollow histogram
          
         kLineChartView.setCandleHolow(Status.HollowModel model);
            参数
            NONE_HOLLOW     :   所有的值实心
            All_HOLLOW      :   所有的值空心
            INCREASE_HOLLOW :   增长的值空心
            DECREASE_HOLLOW :   下跌的值空心
                  
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
          
  ### 设置Y轴显示模式独立显示/与网格同时显示 Set the y-axis display mode to display independently / simultaneously with the grid

          kLineChartView.setYLabelState(Status.YLabelShowModel status) 
          参数 
              YLabelShowModel.LABEL_WITH_GRID :   Y轴显示在网格上
              YLabelShowModel.LABEL_NONE_GRID :   Y轴独立显示与网格不重叠
  
  ### 最大最小值计算模式,影响Y轴的最大最小值 Max min calculation mode, affecting the max min value of Y axis

          kLineChartView.setMaxMinCalcModel(Status.MaxMinCalcModel model)
          参数 
              MaxMinCalcModel.CALC_NORMAL_WITH_LAST :   计算显示的线和最新线的全部值 默认值
              MaxMinCalcModel.CALC_CLOSE_WITH_LAST  :   计算显示的线和最新线的close值
              MaxMinCalcModel.CALC_NORMAL_WITH_SHOW :   计算显示的线全部值 
              MaxMinCalcModel.CALC_CLOSE_WITH_SHOW  :   计算显示的线的close值
            
  ### 指标线所有指标可配置 具体配置方法查看Constants类  All indicators of the indicator line can be configured. See the Constants class for the specific configuration method

          修改后重置数据,会根据新指标计算          
 
 ## 2.0.1更新

     - 支持右侧坐标独立显示与内容不重叠
     - 支持坐标轴显示在左侧
     - 支持右侧独立坐标轴配置单独背景色
     - 统一属性值名称

 ## 2.0.2更新

    - 属性selecedLabel修改为selectedXlabel
    - 修改HollowModel枚举内容名称
    - 修复resetDate展示位重置问题
    - 修复值为成交量为0时计算出现负数问题

 ## 2.0.3更新

    - 添加setAutoFixScrollEnable方法控制可滚动为手动控制还是自动控制
    - 网格线宽重置位置
    - 渲染控制resetData时不渲染直到数据设置完成
        
 ## 2.0.4更新

    - 修复独立显示Y轴时最大最小值可能显示一半的问题
    - 修复滑动时Y轴不变化问题
    - 添加最大最小缩放系数,显示最大最小值通过系数设置   
    
 ## 2.0.5更新

    - 修复bug
    - 最大最小值绘制优化
    
    
## 更新预告 
   - K线画线
   - 性能优化
   - 修复bug
