package com.icechao.demo;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.icechao.kline.R;
import com.icechao.klinelib.model.MarketTradeItem;

import java.util.ArrayList;
import java.util.List;

/*************************************************************************
 * Description   :
 *
 * @PackageName  : com.icechao.klinelib.adapter
 * @FileName     : DepthRecycleViewAdapter.java
 * @Author       : chao
 * @Date         : 2019/4/11
 * @Email        : icechliu@gmail.com
 * @version      : V1
 *************************************************************************/
public class DepthRecycleViewAdapter extends RecyclerView.Adapter<DepthRecycleViewAdapter.DepthViewHolder> {

    private Handler handler = new Handler(Looper.getMainLooper());
    private List<MarketTradeItem> leftDatas = new ArrayList<>();
    private List<MarketTradeItem> rightDatas = new ArrayList<>();

    private Context context;

    public DepthRecycleViewAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<MarketTradeItem> leftDatas, List<MarketTradeItem> rightDatas) {

        if (0 != this.leftDatas.size() || 0 != this.rightDatas.size()) {
            this.leftDatas.clear();
            this.rightDatas.clear();
        }
        this.leftDatas = leftDatas;
        this.rightDatas = rightDatas;
        handler.post(() -> notifyDataSetChanged());

    }

    @NonNull
    @Override
    public DepthViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_horizental_depth, viewGroup, false);
        return new DepthViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DepthViewHolder viewHolder, int i) {
        MarketTradeItem left = leftDatas.get(i);
        MarketTradeItem right = rightDatas.get(i);
        String index = String.valueOf(i + 1);

        viewHolder.textViewLeftIndex.setText(index);
        viewHolder.textViewRightIndex.setText(index);

        viewHolder.textViewLeftPrice.setText(left.getPrice() + "");
        viewHolder.textViewRightPrice.setText(right.getPrice() + "");

        viewHolder.textViewLeftAmount.setText(left.getAmount() + "");
        viewHolder.textViewRightAmount.setText(right.getAmount() + "");

        viewHolder.progressLeft.setProgress(100 - left.getProgress());
        viewHolder.progressRight.setProgress(right.getProgress());

    }


    @Override
    public int getItemCount() {
        int size = rightDatas.size();
        return size;
    }

    public class DepthViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewLeftIndex;
        private TextView textViewLeftAmount;
        private TextView textViewLeftPrice;
        private TextView textViewRightIndex;
        private TextView textViewRightAmount;
        private TextView textViewRightPrice;
        private ProgressBar progressLeft;
        private ProgressBar progressRight;

        public DepthViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewLeftIndex = itemView.findViewById(R.id.text_view_left_index);
            textViewLeftAmount = itemView.findViewById(R.id.text_view_left_amount);
            textViewLeftPrice = itemView.findViewById(R.id.text_view_left_price);
            textViewRightIndex = itemView.findViewById(R.id.text_view_right_index);
            textViewRightAmount = itemView.findViewById(R.id.text_view_right_amount);
            textViewRightPrice = itemView.findViewById(R.id.text_view_right_price);
            progressLeft = itemView.findViewById(R.id.progress_left);
            progressRight = itemView.findViewById(R.id.progress_right);
            progressLeft.setProgressDrawable(context.getResources().getDrawable(R.drawable.depth_progress_bar_left));
        }

    }

}
