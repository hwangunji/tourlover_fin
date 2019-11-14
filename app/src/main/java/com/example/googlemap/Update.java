package com.example.googlemap;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.zip.Adler32;

public class Update extends AppCompatActivity implements View.OnClickListener {

    private FirebaseFirestore uStore = FirebaseFirestore.getInstance();
    DocumentReference UpdateRef = uStore.collection("board").document();
    CollectionReference deleteRef = uStore.collection("board");

        private EditText uWriteNameText;//비번 입력할 곳


        /*디비보드의 아이디 선언*/
         String id;
         String name = "";
         String content = "";

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_update);//만들어줘야해 레이아룻//
            TextView update_content = (TextView)findViewById(R.id.update_content_text);

            /*보드액티비티에서 한거처럼 위에 생성한 변수와 실제 레이아웃의 아이디를 연결*/
            uWriteNameText = findViewById(R.id.update_name_text);//만들어줘야해 ID변수//


            /*삭제버튼아이디와 연결하는코드*/
            findViewById(R.id.delete_button).setOnClickListener(this);//만들어줘야해 ID변수//

            Bundle extras = getIntent().getExtras();

            id = extras.getString("id");
            content=extras.getString("content");
            //String str_en = intent.getExtras().getString("str_en");
            ((TextView)update_content).setText(content);
            name = extras.getString("name");
            //이게 받아오는 변수 따옴표 없이 사용


        }//onCreateonCreate

    @Override
    public void onClick(View v) {//삭제버튼 클릭시

        String txt1= uWriteNameText.getText().toString();

        if(v.getId()== R.id.delete_button)
        {
            System.out.printf(name);
        if(txt1.equals(name))
            {
            uStore.collection("board").document(id)
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(Update.this, "삭제성공!", Toast.LENGTH_SHORT).show();
                            finish();//업로드 성공하고 바로 뒤로 갈수 있게

                        }
                    })//addOnSuccessListener
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Update.this, "삭제실패!", Toast.LENGTH_SHORT).show();
                        }//onFailure
                    });//addOnFailureListener//uStore
             }//if
        }//if2// }

    }//onClick

}

