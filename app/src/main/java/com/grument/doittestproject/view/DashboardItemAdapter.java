package com.grument.doittestproject.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.grument.doittestproject.R;
import com.grument.doittestproject.dto.ImageDTO;
import com.grument.doittestproject.dto.ImageParamsDTO;
import com.grument.doittestproject.util.AppUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DashboardItemAdapter extends RecyclerView.Adapter<DashboardItemAdapter.ViewHolder> {


    public DashboardItemAdapter(Context context, List<ImageDTO> imageDTOList) {
        this.context = context;
        this.imageDTOList = imageDTOList;
    }

    private Context context;

    private List<ImageDTO> imageDTOList;


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_rv_dashboard, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        ImageDTO imageDTO = imageDTOList.get(position);
        ImageParamsDTO imageParamsDTO = imageDTO.getImageParamsDTO();

        if (AppUtil.isStringNotEmpty(imageParamsDTO.getAddress()))
            holder.addressTextView.setText(imageParamsDTO.getAddress());

        if (AppUtil.isStringNotEmpty(imageParamsDTO.getWeather()))
            holder.weatherTextView.setText(imageParamsDTO.getWeather());

        Glide.with(context)
                .load(imageDTO.getSmallImageUrlPath())
                .into(holder.dashboardImageView);

    }


    public void notifyAndShow() {
        notifyItemInserted(getItemCount());
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return imageDTOList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_dashboard_image)
        ImageView dashboardImageView;

        @BindView(R.id.tv_dashboard_address)
        TextView addressTextView;

        @BindView(R.id.tv_dashboard_weather)
        TextView weatherTextView;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}