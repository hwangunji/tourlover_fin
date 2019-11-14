package com.example.googlemap;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class WriteActivity extends AppCompatActivity implements View.OnClickListener {

    /*이제 파이어베이스의 저장소와 연결할꺼야 저장소와 연결할 인스턴스
    변수를 선언한다 아래가 그코드*/
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();

    /*라이트액티비티xml파일과 연결하는 코드 --액티비티 보드와 비슷*/
    /*레이아웃과 연결할 변수 설정 이아래코드 다 제목과연결할꺼 하나 내용과연결할거 하나
    이름이랑 연결할거 하나 그럼 좋아요 같은거 할때도 다시 다 만들어 줘야겠지*/
    private EditText mWriteTitleText;
    private EditText mWriteContentText;
    private EditText mWriteNameText;

    /*디비보드의 아이디 선언*/
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);



        /*보드액티비티에서 한거처럼 위에 생성한 변수와 실제 레이아웃의 아이디를 연결*/
        mWriteTitleText = findViewById(R.id.write_title_text);
        mWriteContentText= findViewById(R.id.write_contents_text);
        mWriteNameText = findViewById(R.id.write_name_text);

        /*업로드버튼아이디와 연결하는코드*/
        findViewById(R.id.write_upload_button).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        /*위아아디변수를 여기서 받음*/
        id = mStore.collection("board").document().getId();
        /*파이어베이스와 연결해 저장한 위의 변수들을 저장소에 put한다*/
        /*post.put("id","");post라는 저장소변수에 put을 하는데 k에는 아이디를 쓰고
         v에는 형식을 지정해 두지 않지만 위에서 설정한 레이아웃과 연결한 변수를 put한다*/
        Map<String, Object> post = new HashMap<>();
        post.put("id",id);
        post.put("title",mWriteTitleText.getText().toString());
        post.put("contents",mWriteContentText.getText().toString());
        post.put("name",mWriteNameText.getText().toString());//비번
        post.put("dateExample", new Timestamp(new Date()));

        /* 이아래코드는파이어베이스저장소의board컬렉션에 업로드 하는 코드임*/
        mStore.collection("board").document(id).set(post)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(WriteActivity.this,"업로드성공!",Toast.LENGTH_SHORT).show();
                        finish();//업로드 성공하고 바로 뒤로 갈수 있게
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(WriteActivity.this,"업로드실패!",Toast.LENGTH_SHORT).show();
            }
        });
    }
}

