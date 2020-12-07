package com.example.serviceboundmusic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.IpSecManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.serviceboundmusic.MyService.MyBinder;

import java.text.SimpleDateFormat;
import java.time.Duration;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private MyService myService;
    private boolean isBound = false;
    private ServiceConnection connection;

    ImageButton imgplay,  imgfast , imgpause ;
    SeekBar seekBar;
    TextView tvtime, tvtimelive, start;
    CircleImageView circleImageView;
    ObjectAnimator animator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        animation();
        playmusic();

    }

    private void playmusic() {


//        final Button btOn = (Button) findViewById(R.id.btOn);
//        final Button btOff = (Button) findViewById(R.id.btOff);
//        final Button btFast = (Button) findViewById(R.id.btTua);

        // Khởi tạo ServiceConnection
        connection = new ServiceConnection() {

            // Phương thức này được hệ thống gọi khi kết nối tới service bị lỗi
            @Override
            public void onServiceDisconnected(ComponentName name) {

                isBound = false;
            }

            // Phương thức này được hệ thống gọi khi kết nối tới service thành công
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                MyBinder binder = (MyBinder) service;
                myService = binder.getService(); // lấy đối tượng MyService
                isBound = true;
            }
        };

        // Khởi tạo intent
        final Intent intent =
                new Intent(MainActivity.this,
                MyService.class);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isBound){
                    bindService(intent, connection,
                            Context.BIND_AUTO_CREATE);
                    animator.start();
                    //  imgplay.setImageResource(R.drawable.pause);
                    start.setText(R.string.off);
                }else {
                    unbindService(connection);
                    isBound = false;
                    animator.end();
                    start.setText(R.string.on);
                }

            }
        });

        imgplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Bắt đầu một service sủ dụng bind
                if (isBound){
                    myService.fastStart();
                    animator.start();

                }
                // Đối thứ ba báo rằng Service sẽ tự động khởi tạo
            }
        });

        imgpause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isBound){
                    myService.pause();
                    animator.cancel();
                }

            }
        });

        imgfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isBound){
                    myService.fastForward();
                    Toast.makeText(myService, "up 10s", Toast.LENGTH_SHORT).show();
                }

            }
        });


//        btOff.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // Nếu Service đang hoạt động
//                if(isBound){
//                    // Tắt Service
//                    unbindService(connection);
//                    isBound = false;
//                }
//            }
//        });
//
//        btFast.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // nếu service đang hoạt động
//                if(isBound){
//                    // tua bài hát
//                    myService.fastForward();
//                }else{
//                    Toast.makeText(MainActivity.this,
//                            "Service chưa hoạt động", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//        findViewById(R.id.btStart).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(isBound){
//                    // tua bài hát
//                    myService.fastStart();
//                }else{
//                    Toast.makeText(MainActivity.this,
//                            "Service chưa hoạt động", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

    }

    private void Time(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
    }

    private void animation() {
        animator = ObjectAnimator.ofFloat(circleImageView, "rotation", 360f,0f );
        animator.setDuration(10000);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.setInterpolator(new LinearInterpolator());
    }

    private void init() {
        tvtime = findViewById(R.id.tvtime);
        tvtimelive = findViewById(R.id.tvtimelive);
        seekBar = findViewById(R.id.sbMusic);
        imgplay = findViewById(R.id.imgbtnplay);
        imgpause = findViewById(R.id.imgbtnPause);
        start = findViewById(R.id.btnStart);
        imgfast = findViewById(R.id.imgbtnfastfwd);
        circleImageView = findViewById(R.id.imgviewdia);

    }
}