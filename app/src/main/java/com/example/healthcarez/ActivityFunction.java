package com.example.healthcarez;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventCallback;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ShareActionProvider;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;



public class ActivityFunction extends AppCompatActivity {


    private SensorManager sensorManager;
    private SensorEventCallback sensorListenner;


    private TextView stepTextView;
    private Sensor accelerometerSensor;

    private boolean isWalking = false;
    private int stepCount = 0;
    private static final float STEP_THRESHOLD = 12.0f;

    private Button resetButton;

    private int stopCount = 0;
    private ProgressBar progressBar;
    private static final float TARGET = 100;

    Button  stopBtn,clearBtn;
    TextView lichsuTxt;
    String lichsu= "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.function_tracer);


        //Anh xa
        stopBtn = findViewById(R.id.stopBtn);
        clearBtn = findViewById(R.id.clearBtn);
        lichsuTxt = findViewById(R.id.lichsu_txt);
        stepTextView = findViewById(R.id.stepTextView);

        //doc lai du lieu
        SharedPreferences mypref = getSharedPreferences("mysave", MODE_PRIVATE);
        lichsu = mypref.getString("ls","");
        lichsuTxt.setText(lichsu);
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tăng số lần mỗi khi bấm nút Stop
                int currentCount = ++stopCount;
                // Tạo chuỗi mới để lưu lịch sử
                lichsu = "Lần " + currentCount + ": " + stepCount + " bước " + "\n";
                // Cập nhật TextView hiển thị lịch sử
                lichsuTxt.append(lichsu);
                // Lưu lịch sử vào SharedPreferences

            }
        });



        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lichsu = "";
                lichsuTxt.setText(lichsu);
                SharedPreferences mypref = getSharedPreferences("mysave", MODE_PRIVATE);
                SharedPreferences.Editor myedit = mypref.edit();
                myedit.remove("ls");
                myedit.apply();
            }
        });






        ImageView imageBtnback = findViewById(R.id.imageBtnback);
        imageBtnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityFunction.this,MainActivity.class));

            }
        });









        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);


        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometerSensor != null) {
            stepTextView = findViewById(R.id.stepTextView);
            ((ViewGroup)stepTextView.getParent()).setVisibility(View.VISIBLE);
        }

        sensorListenner = new SensorEventCallback() {
            @Override
            public void onSensorChanged(SensorEvent event) {

                if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                    float x = event.values[0];
                    float y = event.values[1];
                    float z = event.values[2];
                    // Tính toán gia tốc
                    float acceleration = (float) Math.sqrt(x * x + y * y + z * z);

                    // Kiểm tra nếu đang đi bộ và có sự thay đổi trong gia tốc độ dọc
                    if (isWalking && acceleration < STEP_THRESHOLD) {
                        isWalking = false;
                    } else if (!isWalking && acceleration > STEP_THRESHOLD) {
                        isWalking = true;
                        stepCount++;
                        stepTextView.setText("" + stepCount);
                        progressBar.setProgress((int) ((100 * stepCount) / TARGET));
                    }
                }
            }
        };
        resetButton = findViewById(R.id.resetButton);
        progressBar = findViewById(R.id.progressBar);
        resetButton.setOnClickListener(v -> {
            stepCount = 0;
            stepTextView.setText("0");
            progressBar.setProgress(0);
        });



    }




    @Override
    protected void onStart() {
        super.onStart();
        sensorManager.registerListener(sensorListenner, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onStop() {
        super.onStop();
        sensorManager.unregisterListener(sensorListenner);
        SharedPreferences mypref = getSharedPreferences("mysave",MODE_PRIVATE);
        SharedPreferences.Editor myedit = mypref.edit();
        myedit.putString("ls",lichsu);
        myedit.commit();
    }
}