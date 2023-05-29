package com.jscompany.tp15hugpet2;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jscompany.tp15hugpet2.Model.BoardService;
import com.jscompany.tp15hugpet2.Model.RetrofitBaseUrl;

import java.util.ArrayList;
import java.util.zip.Inflater;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainBestYoyMeAdapter extends RecyclerView.Adapter<MainBestYoyMeAdapter.VH> {

    Context context;
    ArrayList<YouMeItem> items;

    public MainBestYoyMeAdapter(Context context, ArrayList<YouMeItem> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_main,parent,false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        YouMeItem item = items.get(position);

        holder.desertionNo.setText(item.bNo +"");
        holder.kindCd.setText(item.title);
        holder.tv_view_cnt.setText(item.viewCnt);

        //이미지 저장 배우면 바꾸기
        holder.popfile.setImageResource(R.drawable.no_img);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class VH extends RecyclerView.ViewHolder {

        RelativeLayout rcyview_to_detail;
        ImageView popfile;
        TextView kindCd; //제목
        TextView desertionNo; //글 번호
        TextView tv_view_cnt; //조회수

        public VH(@NonNull View itemView) {
            super(itemView);

            rcyview_to_detail = itemView.findViewById(R.id.rcyview_to_detail);
            popfile = itemView.findViewById(R.id.popfile);
            kindCd = itemView.findViewById(R.id.kindCd); //제목
            desertionNo = itemView.findViewById(R.id.desertionNo); //글 번호
            tv_view_cnt = itemView.findViewById(R.id.tv_view_cnt);

            rcyview_to_detail.setOnClickListener(v -> clickItem());
        }

        private void clickItem() {
            //디테일 테이지로 이동
            int boardN = Integer.parseInt(desertionNo.getText().toString());

            viewCntUp(boardN);

        }

        private void viewCntUp(int boardN){
            RetrofitBaseUrl.getRetrofitInstance(Common.dotHomeUrl).create(BoardService.class).boardViewCnt(boardN+"").enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    String result = response.body();

                    if(result.equals("true")) {
                        Intent intent = new Intent(context, YouMeDetail.class);
                        intent.putExtra("boardN",boardN);
                        context.startActivity(intent);
                    } else {
                        Toast.makeText(context, "조회수 카운트 실패", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(context, "네트워크 문제", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
}
