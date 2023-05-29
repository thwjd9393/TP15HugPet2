package com.jscompany.tp15hugpet2;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jscompany.tp15hugpet2.Model.BoardService;
import com.jscompany.tp15hugpet2.Model.RetrofitBaseUrl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class YouMeAdapter extends RecyclerView.Adapter<YouMeAdapter.VH> {

    Context context;
    ArrayList<YouMeItem> items;

    public YouMeAdapter(Context context, ArrayList items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_youme,parent,false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        YouMeItem item = items.get(position);

        holder.tv_boardnum.setText(item.bNo + "");
        holder.tv_title.setText(item.title);
        holder.tv_content.setText(item.content);

        holder.tv_date.setText(item.date);
        holder.tv_nic.setText(item.nicName);
        holder.tv_view_cnt.setText(item.viewCnt);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class VH extends RecyclerView.ViewHolder {

        RelativeLayout board_item_warp;

        TextView tv_boardnum,tv_title, tv_nic, tv_content, tv_view_cnt, tv_date;

        public VH(@NonNull View itemView) {
            super(itemView);

            board_item_warp = itemView.findViewById(R.id.board_item_warp);

            tv_boardnum = itemView.findViewById(R.id.tv_boardnum);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_nic = itemView.findViewById(R.id.tv_nic);
            tv_content = itemView.findViewById(R.id.tv_content);
            tv_view_cnt = itemView.findViewById(R.id.tv_view_cnt);
            tv_date = itemView.findViewById(R.id.tv_date);

            board_item_warp.setOnClickListener(view -> clickItem());

        }
        //item 클릭 했을 때

        private void clickItem() {
            int boardN = Integer.parseInt(tv_boardnum.getText().toString());

            viewCntUp(boardN);
        }

        //조회수 +1
        private void viewCntUp(int boardN){
//            int viewCnt = Integer.parseInt(tv_view_cnt.getText().toString());
//
//            Common.makeToast(context,tv_view_cnt.getText().toString());
//            Common.makeToast(context,viewCnt +"");
//
//            viewCnt+=1;
//
//            String vCntResult = viewCnt + "";
//
//            SQLiteDatabase board_db = context.openOrCreateDatabase("hugpet_db",context.MODE_PRIVATE,null);
//            board_db.execSQL("UPDATE board SET view_cnt = '"+ vCntResult + "'where boardNum=?", new String[] {boardN+""});

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
