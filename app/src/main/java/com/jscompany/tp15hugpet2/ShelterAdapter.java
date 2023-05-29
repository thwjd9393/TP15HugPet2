package com.jscompany.tp15hugpet2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ShelterAdapter extends RecyclerView.Adapter<ShelterAdapter.VH> {

    Context context;
    ArrayList<ShelterItem> items;

    public ShelterAdapter(Context context, ArrayList<ShelterItem> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(context).inflate(R.layout.item_shelter,parent,false);

        return new VH(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        ShelterItem item = items.get(position);

        holder.desertionNo.setText(item.desertionNo);
        holder.petKind.setText(item.petKind);

        //holder.popfile.setImageURI(Uri.parse(item.popfile));
        Glide.with(context).load(Uri.parse(item.popfile)).into(holder.popfile);
        // holder.popfile.setImageBitmap(item.popfile);

        int imgRnum = 0;
        if(item.sexCd.equals("M")) imgRnum = R.drawable.male;
        else if (item.sexCd.equals("F")) imgRnum = R.drawable.female;
        else if (item.sexCd.equals("Q")) imgRnum = R.drawable.unknown;
        holder.sexCd.setImageResource(imgRnum);

        holder.kindCd.setText(item.kindCd);
        holder.noticeNo.setText(item.noticeNo);

        int isStatus=0;
        if(item.processState.equals("보호중")) isStatus = R.drawable.status_ok;
        else isStatus = R.drawable.status_end;
        holder.processState.setImageResource(isStatus);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class VH extends RecyclerView.ViewHolder {

        RelativeLayout rcyviewToDetail;

        ImageView popfile, sexCd, processState;
        TextView desertionNo, kindCd, noticeNo, petKind;

        public VH(@NonNull View itemView) {
            super(itemView);

            desertionNo = itemView.findViewById(R.id.desertionNo);

            petKind = itemView.findViewById(R.id.petKind);

            popfile = itemView.findViewById(R.id.popfile);
            kindCd = itemView.findViewById(R.id.kindCd);
            sexCd = itemView.findViewById(R.id.sexCd);
            noticeNo = itemView.findViewById(R.id.noticeNo);
            processState = itemView.findViewById(R.id.processState);

            //디테일화면(Activity)으로 이동하는 부분
            rcyviewToDetail = itemView.findViewById(R.id.rcyview_to_detail);

            rcyviewToDetail.setClickable(true); //클릭이 가능 할 때
            rcyviewToDetail.setOnClickListener(view -> toDetail()); //클릭하면 화면 이동

        }

        private void toDetail() {

            int pos = getAdapterPosition(); //클릭한 아이템의 포지션
            if (pos != RecyclerView.NO_POSITION) { //리사이클뷰 아이템을 클릭한건지 체크
                Intent intent = new Intent(context, ShelterDetail.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                intent.putExtra("desertionNo", items.get(pos).desertionNo);
                intent.putExtra("petKind", items.get(pos).petKind);
                Log.i("TAG","어답터 desertionNo="+items.get(pos).desertionNo);

                context.startActivity(intent);

            }
        }
    }

}
