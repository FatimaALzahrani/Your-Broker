package com.market.your_broker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;

public class Menu extends AppCompatActivity {
    MeowBottomNavigation bottomNavigation;
    int num=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        bottomNavigation=findViewById(R.id.navi);
//        Intent intent=getIntent();
//        if(intent.getStringExtra("num")!=null && intent.getStringExtra("num")!="null")
//        num=Integer.parseInt(intent.getStringExtra("num"));
        bottomNavigation.add(new MeowBottomNavigation.Model(5, R.drawable.home2));
        bottomNavigation.add(new MeowBottomNavigation.Model(4, R.drawable.myplant2));
        bottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.buy));
        bottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.chat2));
        bottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.profile2));
        bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
//                Toast.makeText(getApplicationContext(),"Clicked " +item.getId(),Toast.LENGTH_LONG).show();
            }
        });
        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                Fragment fragment;
//                if(num>0) {
//                    load(num);
//                    item.setId(num);
//                }
//                else {
                if (item.getId() == 4) {
                    fragment = new Saves();
                } else if (item.getId() == 3) {
                    fragment = new Buy();
                } else if (item.getId() == 2) {
                    fragment = new Chat();
                } else if (item.getId() == 1) {
                    fragment = new Profile();
                } else
                    fragment = new Home();
                loadFragment(fragment);
            }
//            }
        });
        bottomNavigation.show(5,true);
        bottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {
                Toast.makeText(getApplicationContext(), "تمت إعادة التحديد", Toast.LENGTH_LONG).show();
            }
        });

//        bottomNavigation.setCount(4,"5");

    }

    private void load(int num) {
        Fragment fragment;
        if (num == 4) {
            fragment = new Saves();
        } else if (num == 3) {
            fragment = new Buy();
        } else if (num == 2) {
            fragment = new Chat();
        } else if (num == 1) {
            fragment = new Profile();
        } else
            fragment = new Home();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frag,fragment, null)
                .commit();
        num=0;
        Intent intent=new Intent();
        intent.putExtra("num","null");
    }

    //define a load method to feed the screen
    private void loadFragment(Fragment fragment) {
        //replace the fragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frag,fragment, null)
                .commit();
    }
}