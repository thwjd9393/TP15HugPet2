package com.jscompany.tp15hugpet2;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NoticeMainAdapter extends RecyclerView.Adapter<NoticeMainAdapter.VH> {

    Context context;
    ArrayList<NoticeItem> items;

    public NoticeMainAdapter(Context context, ArrayList<NoticeItem> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_main_notice, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        NoticeItem item = items.get(position);
        holder.noticeTitle.setText(item.title);
        holder.noticeDate.setText(item.date);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class VH extends RecyclerView.ViewHolder {
        RelativeLayout noticeWarp;
        TextView noticeTitle, noticeDate;

        public VH(@NonNull View itemView) {
            super(itemView);

            noticeWarp = itemView.findViewById(R.id.notice_warp);
            noticeTitle = itemView.findViewById(R.id.notice_title);
            noticeDate = itemView.findViewById(R.id.notice_date);

            noticeWarp.setOnClickListener(v -> clickWarp());

        }

        private void clickWarp() {
            Intent intent = new Intent(context, NoticeActivity.class);
            context.startActivity(intent);
            intent = null;
        }
    }
}
