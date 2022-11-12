package com.example.duan_oder_doan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duan_oder_doan.adapter.PagerAdapter_ThongBao;
import com.example.duan_oder_doan.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import me.relex.circleindicator.CircleIndicator3;


public class TrangChuUser extends AppCompatActivity {

    private ViewPager2 viewPager2;
    int[] images = {R.drawable.mhc1,R.drawable.mhc2,R.drawable.mhc4,R.drawable.mhc5,R.drawable.mhc6};
    private PagerAdapter_ThongBao pagerAdapterThongBao;
    private CircleIndicator3 circleIndicator;
    private Timer timer;
    private EditText edt_searchfood;

    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;

    private CircleImageView img_avatar;
    private TextView tv_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trang_chu_user);

        tv_name = findViewById(R.id.tv_name);
        img_avatar = findViewById(R.id.img_avatar);

        // Lay thong tin tu firebase hien thi len
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if (userProfile != null) {
                    String fullName = userProfile.getFullName();
                    tv_name.setText("Hi "+ fullName+"!");
                    int image = userProfile.getImage();
                    img_avatar.setImageResource(image);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(TrangChuUser.this,"Something wrong happened!", Toast.LENGTH_LONG).show();
            }
        });



        edt_searchfood = findViewById(R.id.edt_searchfood);
        edt_searchfood.setFocusable(false);
        edt_searchfood.setOnClickListener(v->{
            Intent intent = new Intent(this, TimKiemUser.class);
            Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
            startActivity(intent, bundle);
        });
        findViewById(R.id.img_avatar).setOnClickListener(v ->{
            Intent intent = new Intent(this, GioiThieuUser.class);
            Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
            startActivity(intent, bundle);
        });

        viewPager2 = findViewById(R.id.viewpager2_thongbao);
        pagerAdapterThongBao = new PagerAdapter_ThongBao(images );
        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(View.OVER_SCROLL_NEVER);
        viewPager2.setAdapter(pagerAdapterThongBao);

        CompositePageTransformer transformer = new CompositePageTransformer();
        transformer.addTransformer(new MarginPageTransformer(8));
        transformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float v = 1-Math.abs(position);

                page.setScaleY(0.8f+v*0.2f);
            }
        });

        circleIndicator = findViewById(R.id.indicator);
        circleIndicator.setViewPager(viewPager2);
        pagerAdapterThongBao.registerAdapterDataObserver(circleIndicator.getAdapterDataObserver());
        circleIndicator.createIndicators(5,0);
        circleIndicator.animatePageSelected(2);

        viewPager2.setPageTransformer(transformer);
        autoSile();


        findViewById(R.id.img_trangchu).setOnClickListener(v ->{
            Intent intent = new Intent(this, TrangChuUser.class);
            Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
            startActivity(intent, bundle);
        });
        findViewById(R.id.img_tintuc).setOnClickListener(v ->{
            Intent intent = new Intent(this, YeuThichUser.class);
            Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
            startActivity(intent, bundle);
        });
        findViewById(R.id.img_giohang).setOnClickListener(v ->{
            Intent intent = new Intent(this, GioHangUser.class);
            Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
            startActivity(intent, bundle);
        });
        findViewById(R.id.img_thongtin).setOnClickListener(v ->{
            Intent intent = new Intent(this, ThongTinUser.class);
            Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
            startActivity(intent, bundle);
        });

    }

    private void autoSile() {
        if (timer == null) {
            timer = new Timer();
        }

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        int currentItem = viewPager2.getCurrentItem();
                        int totalItem = 5;
                        if (currentItem <totalItem) {
                            currentItem++;
                            viewPager2.setCurrentItem(currentItem);

                        }
                        if (currentItem >= 5) {
                            viewPager2.setCurrentItem(0);
                        }
                    }
                });
            }
        },6000, 6000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
}