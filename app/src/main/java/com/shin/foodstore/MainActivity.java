package com.shin.foodstore;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.shin.foodstore.Fragment.FragmentStatistical;
import com.shin.foodstore.Fragment.Fragmentcategory;
import com.shin.foodstore.Fragment.Fragmentmanager;
import com.shin.foodstore.Fragment.Fragmentproflie;

public class MainActivity extends AppCompatActivity {
    public static Toolbar toolbar;
    public static BottomNavigationView bnv;
    TextView titletoolbar;
    public static String idstore="";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bnv = findViewById(R.id.bnv);
        toolbar = findViewById(R.id.toolbar);
        titletoolbar = findViewById(R.id.toolbar_title);
        getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity.this, R.color.xam));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        titletoolbar.setText("Trang Chủ");
        titletoolbar.setTextSize(30);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Intent intent = getIntent();
        idstore=intent.getStringExtra("email");

        if(savedInstanceState == null){
            bnv.setSelectedItemId(R.id.thongke);
            loadFragment(new FragmentStatistical());
        }
        toolbar.setNavigationIcon(R.drawable.ic_baseline_sort_);
        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()){
                    case R.id.thongke:
                        titletoolbar.setText("Thống Kê");
                        fragment = new FragmentStatistical();
                        loadFragment(fragment);
                        return true;
                    case R.id.category:
                        titletoolbar.setText("Đồ Ăn");
                        fragment = new Fragmentcategory();
                        loadFragment(fragment);
                        return true;
                    case R.id.manager:
                        titletoolbar.setText("Nhắn Tin");
                        fragment = new Fragmentmanager();
                        loadFragment(fragment);
                        return true;
                    case R.id.profile:
                        titletoolbar.setText("Thông Tin Cá Nhân");
                        fragment = new Fragmentproflie();
                        loadFragment(fragment);
                        return true;
                }


                return false;
            }
        });


    }
    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fr_l, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}