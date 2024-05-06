package com.example.healthcarez;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class BodyMassActivity extends AppCompatActivity {

    EditText edittimeTxt, editweightTxt;
    Button saveBtn, deleteBtn;
    TextView theodoilichsuTxt;

    String lichsu = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body_mass);

        edittimeTxt = findViewById(R.id.edittimeTxt);
        editweightTxt = findViewById(R.id.editweightTxt);
        theodoilichsuTxt = findViewById(R.id.theodoilichsuTxt);
        deleteBtn = findViewById(R.id.deleteBtn);
        saveBtn = findViewById(R.id.saveBtn);

        SharedPreferences mypref = getSharedPreferences("myweightsave",MODE_PRIVATE);
        lichsu = mypref.getString("ls","");
        theodoilichsuTxt.setText(lichsu);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String time = edittimeTxt.getText().toString();
                // Lấy cân nặng từ EditText khác
                String weight = editweightTxt.getText().toString();
                lichsu = "Thời gian: " + time + " Cân nặng: " + weight + " kg \n";
               theodoilichsuTxt.append(lichsu);
               lichsu += "\n";

            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lichsu = "";
                theodoilichsuTxt.setText(lichsu);
            }
        });








        ImageView imageBtnback = findViewById(R.id.imageBtnback);
        imageBtnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(BodyMassActivity.this,MainActivity.class));

            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences mypref = getSharedPreferences("myweightsave",MODE_PRIVATE);
        SharedPreferences.Editor myedit = mypref.edit();
        myedit.putString("ls",lichsu);
        myedit.commit();
    }
}