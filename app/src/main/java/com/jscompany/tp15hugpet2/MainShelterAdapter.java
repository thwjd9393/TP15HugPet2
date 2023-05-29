package com.jscompany.tp15hugpet2;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MainShelterAdapter extends RecyclerView.Adapter <MainShelterAdapter.VH> {

    Context context;
    ArrayList<ShelterItem> items;

    public MainShelterAdapter(Context context, ArrayList<ShelterItem> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(context).inflate(R.layout.item_main,parent,false);

        return new VH(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        ShelterItem item = items.get(position);

        holder.desertionNo.setText(item.desertionNo);
        //holder.desertionNo.setText(item.popfile);
        //holder.popfile.setImageBitmap(item.popfile);
        Glide.with(context).load(Uri.parse(item.popfile)).into(holder.popfile);
        holder.kindCd.setText(item.kindCd);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class VH extends RecyclerView.ViewHolder {

        //디테일 화면으로 이동하기 위한 것
        RelativeLayout rcyviewToDetail;

        TextView kindCd, desertionNo;
        ImageView popfile;

        public VH(@NonNull View itemView) {
            super(itemView);
            kindCd = itemView.findViewById(R.id.kindCd);
            desertionNo = itemView.findViewById(R.id.desertionNo);
            popfile = itemView.findViewById(R.id.popfile);

            //디테일화면(Activity)으로 이동하는 부분
            rcyviewToDetail = itemView.findViewById(R.id.rcyview_to_detail);

            rcyviewToDetail.setClickable(true); //클릭이 가능 할 때
            rcyviewToDetail.setOnClickListener(view -> toDetail()); //클릭하면 화면 이동

        }

        private void toDetail() {

            int pos = getAdapterPosition(); //클릭한 아이템의 포지션
            if(pos != RecyclerView.NO_POSITION){ //리사이클뷰 아이템을 클릭한건지 체크
                Intent intent = new Intent(context, ShelterDetail.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                intent.putExtra("desertionNo", items.get(pos).desertionNo);
                intent.putExtra("petKind", Common.UPKIND_DOG);

                context.startActivity(intent);

            }

        }

    }

}
