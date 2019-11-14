package com.example.googlemap;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

//슬라이드 메뉴에서 개발자 문의 누르면 넘어오는 곳

public class Inquiry extends AppCompatActivity {

    //EditText etAddr, etTitle, etMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_inquiry);
        Intent intent = new Intent(this.getIntent());

        Intent it = new Intent(Intent.ACTION_SEND);
        String[] mailaddr = {"cola68@naver.com"};

        it.setType("plain/text");
        it.putExtra(Intent.EXTRA_EMAIL, mailaddr);
        it.putExtra(Intent.EXTRA_SUBJECT,"[충대워커 문의메일입니다]");
        it.putExtra(Intent.EXTRA_TEXT,"\n\n");  //첨부내용

        startActivity(it);


    }
}

