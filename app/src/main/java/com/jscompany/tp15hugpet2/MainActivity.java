package com.jscompany.tp15hugpet2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {

    ViewPager2 viewPager;
    MainFragAdapter mainFragAdapter;

    TabLayout tabLayout;

    DrawerLayout drawerLayout;
    NavigationView navigationView; //숨어있는 애
    ActionBarDrawerToggle drawerToggle;

    LinearLayout container_warp;

    TextView headerViewUserName;

    //탭 이름
    String[] tabTitle = {"홈", "보호소", "너와나"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        data();
    }

    void init(){
        container_warp = findViewById(R.id.container_warp);

        viewPager = findViewById(R.id.view_pager);
        mainFragAdapter = new MainFragAdapter(this);
        viewPager.setAdapter(mainFragAdapter);

        //툴바
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //토글 버튼
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav);

        drawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.yes,R.string.no);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        drawerToggle.syncState();
        drawerLayout.addDrawerListener(drawerToggle);

        nav();

        //탭이랑 페이저 연동 : TabLayoutMediator
        tabLayout = findViewById(R.id.tab_layout);
        TabLayoutMediator mediator = new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(tabTitle[position]); //탭 이름 연동
            }
        });
        mediator.attach();
    }//init

    //플래그 먼트 이동!!!
    public void onFragmentChange(int idex){
        tabLayout.selectTab(tabLayout.getTabAt(idex));
    }

    void nav(){

        //헤더 뷰 관리
        View headerView = navigationView.getHeaderView(0);
        headerViewUserName = headerView.findViewById(R.id.nav_nic);
        headerViewUserName.setText(Common.getUserNic(this));

        //클릭하면 화면 전환
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                if(item.getItemId() == R.id.menu_mypet){
                    intent = new Intent(MainActivity.this, LikePetActivity.class);
                    startActivity(intent);
                    intent = null;
                } else if (item.getItemId() == R.id.menu_pwchg) {
                    intent = new Intent(MainActivity.this, PasswordChgActivity.class);
                    startActivity(intent);
                    intent = null;
                } else if (item.getItemId() == R.id.menu_notice) {
                    intent = new Intent(MainActivity.this, NoticeActivity.class);
                    startActivity(intent);
                    intent = null;
                } else if (item.getItemId() == R.id.menu_logout) {
                    intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    //저장된 값 지우기
                    SharedPreferences pref = getSharedPreferences("UserInfo", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.clear();
                    editor.commit();
                    
                    Common.makeToast(MainActivity.this,"로그아웃 되었습니다");

                    intent = null;
                    finish();
                }

                return false;
            }
        });
    }

    //테이블
    void data(){

        SQLiteDatabase sqLiteDatabase = openOrCreateDatabase("hugpet_db",MODE_PRIVATE,null);
        //board_db.execSQL("DELETE FROM board");
        //sqLiteDatabase.execSQL("DROP TABLE  my_like");

        //좋아요 테이블
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS my_like(like_num INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "desertionNo VARCHAR(20) NOT NULL,petKind VARCHAR(20) NOT NULL,user_num Integer,"
                + "date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,"
                + "FOREIGN KEY(user_num) REFERENCES member(num))");

        //공지사항 테이블
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS notice(niti_num INTEGER PRIMARY KEY AUTOINCREMENT,title VARCHAR(50) NOT NULL," +
                "content TEXT NOT NULL, user_num Integer,date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL," +
                "FOREIGN KEY(user_num) REFERENCES member(num))");

//        //베스트 입양후기
//        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS board(boardNum INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                "title VARCHAR(150) NOT NULL,content TEXT NOT NULL, view_cnt TEXT DEFAULT '0', userNum VARCHER(8)," +
//                "img TEXT," +
//                "date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL," +
//                "FOREIGN KEY(userNum) REFERENCES member(num))");

    }
}