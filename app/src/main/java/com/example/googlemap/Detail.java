package com.example.googlemap;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import android.app.AlertDialog;

public class Detail extends Activity {

    public static String test;
    public String ev_detail;
    public String wc_detail;
    public String sl_detail;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        TextView tx1 = (TextView)findViewById(R.id.str_en);
        TextView tx2 = (TextView)findViewById(R.id.str_ko);
        //TextView tx3 = (TextView)findViewById(R.id.struct);
        TextView tx4 = (TextView)findViewById(R.id.elevator);
        TextView tx5 = (TextView)findViewById(R.id.access);
        TextView tx6 = (TextView)findViewById(R.id.toilet);

        Intent intent = getIntent();

        String str_en = intent.getExtras().getString("str_en");
        ((TextView)tx1).setText(str_en);

        test=str_en;
        String str_ko = intent.getExtras().getString("str_ko");
        ((TextView)tx2).setText(str_ko);
        //String struct = intent.getExtras().getString("struct");
        //((TextView)tx3).setText(struct);

        String elevator = intent.getExtras().getString("elevator");
        ((TextView)tx4).setText(elevator);
        String access = intent.getExtras().getString("access");
        ((TextView)tx5).setText(access);
        String toilet = intent.getExtras().getString("toilet");
        ((TextView)tx6).setText(toilet);

        ev_detail = intent.getExtras().getString("elevator_detail");
        wc_detail = intent.getExtras().getString("toilet_detail");
        sl_detail = intent.getExtras().getString("slow_detail");

        ImageButton close = (ImageButton)findViewById(R.id.backButton2);
        close.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });

        ImageButton elevator_bt=(ImageButton)findViewById(R.id.elevator_detail);
        elevator_bt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                show();
            }
        });

        ImageButton toilet_bt=(ImageButton)findViewById(R.id.toilet_detail);
        toilet_bt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                show1();
            }
        });

        ImageButton slow_bt=(ImageButton)findViewById(R.id.slow_detail);
        slow_bt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                show2();
            }
        });

        Button button_e = (Button)findViewById(R.id.button);
        button_e.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent1 = new Intent(getApplicationContext(), Board_Main.class);
                intent1.putExtra("str_en",test);
                startActivity(intent1);
                     }
        });
    }

    void show()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("엘리베이터 위치");
        builder.setMessage(ev_detail);

        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
//Toast.makeText(getApplicationContext(),"예를 선택했습니다.",Toast.LENGTH_LONG).show();
                    }
                });
        builder.show();
    }

    void show1()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("장애인 화장실 위치");
        builder.setMessage(wc_detail);

        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
//Toast.makeText(getApplicationContext(),"예를 선택했습니다.",Toast.LENGTH_LONG).show();
                    }
                });
        builder.show();
    }
    void show2()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("휠체어 진입로 위치");
        builder.setMessage(sl_detail);

        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
//Toast.makeText(getApplicationContext(),"예를 선택했습니다.",Toast.LENGTH_LONG).show();
                    }
                });
        builder.show();
    }
}



