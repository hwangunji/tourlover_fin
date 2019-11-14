package com.example.googlemap;

//동영상의 메인엑티비티에 해당
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.support.v4.content.ContextCompat.startActivity;

public class Board_Main extends AppCompatActivity implements View.OnClickListener{

    /*파이어베이스 리드소스를 위한 바이어베이스 변수 선언*/
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();
    private RecyclerView mMainRecyclerView;

    private MainAdapter mAdapter;
    private List<Board_post> mBoardList;

    public static String id;
    public static String title;
    public static String contents;
    public static  String name;
    public static Date dateExample;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board__main);


       //RecyclerView의 id 연결
        mMainRecyclerView = findViewById(R.id.main_recycler_view);

        //이번엔 버튼 연결위에 온클릭
        findViewById(R.id.main_write_button).setOnClickListener(this);

        //#
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mMainRecyclerView.setLayoutManager(mLinearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mMainRecyclerView.getContext(),
                mLinearLayoutManager.getOrientation());
        mMainRecyclerView.addItemDecoration(dividerItemDecoration);



        //db안쓰고 text변수에 직접저장 null은 생성자에 서 생성항것 다 채워주기 위해 작성
        mBoardList = new ArrayList<>();


        //str_en 받을 변수//1008
        TextView tx1 = (TextView)findViewById(R.id.str_en);

        Intent intent = getIntent(); /*데이터 수신*/


        String str_en = intent.getExtras().getString("str_en");
        ((TextView)tx1).setText(str_en);//이게 받아오는 변수 따옴표 없이 사용


        /*아래에리드데이터즉파이어베이스에서 데이터 가져오는 코드를 복붙함(tool파이어베이스 코드에 있음whereEqualTo("title", "str_en")*/
        mStore.collection("board").whereEqualTo("title",str_en).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {

                                id = (String) document.getData().get("id");//고유번호 자동생성
                                title = (String) document.getData().get("title");//제목
                                contents = (String) document.getData().get("contents");//내용
                                name = (String) document.getData().get("name");//비번
                                dateExample =(Date) document.getData().get("dateExample");
                                Board_post data = new Board_post(id, title, contents, name, dateExample);

                                mBoardList.add(data);
                            }
                            mAdapter = new MainAdapter(mBoardList);
                            mMainRecyclerView.setAdapter(mAdapter);
                        }
                    }
                });



          class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

            private GestureDetector gestureDetector;
            private Board_Main.ClickListener clickListener;

            public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final Board_Main.ClickListener clickListener) {
                this.clickListener = clickListener;
                gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onSingleTapUp(MotionEvent e) {
                        return true;
                    }

                    @Override
                    public void onLongPress(MotionEvent e) {
                        View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                        if (child != null && clickListener != null) {
                            clickListener.onLongClick(child, recyclerView.getChildAdapterPosition(child));
                        }
                    }
                });
            }


            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                View child = rv.findChildViewUnder(e.getX(), e.getY());
                if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                    clickListener.onClick(child, rv.getChildAdapterPosition(child));
                }
                return false;
            }


            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
            }
        }

        mMainRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), mMainRecyclerView, new ClickListener() {

            public void onClick(View view, int position) {
                Board_post dict = mBoardList.get(position);
               // Toast.makeText(getApplicationContext(), dict.getId()+' '+dict.getName(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getBaseContext(), Update.class);

                intent.putExtra("id", dict.getId());
                intent.putExtra("content", dict.getContent());
                intent.putExtra( "name", dict.getName());

                startActivity(intent);
            }


            public void onLongClick(View view, int position) {
            }
        }));

    }




    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.main_write_button)
        {startActivity(new Intent(this, WriteActivity.class));}
    }

    //듀얼텍스트만들기
    private class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder> {
        private List<Board_post> mBoardList;
        //동영상의 Board=Board_post++

        public MainAdapter(List<Board_post> mBoardList){
            this.mBoardList = mBoardList;
        }
        @NonNull
        @Override
        public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//             return new MainViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main, parent, false));

            //아릴로 바꿈#
            View view=LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_main, parent, false);
            MainViewHolder viewHolder = new MainViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {

                Board_post data = mBoardList.get(position);
                //holder.mTitleTextview.setText(data.getTitle());
            holder.mContentTextView.setText(data.getContent());

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {//여기가 클릭이벤트

                }
            });

        }


        @Override
        public int getItemCount() {
            return mBoardList.size();
        }


        //아이템을 리사이클러에 보여주는 코드
        class MainViewHolder extends RecyclerView.ViewHolder {

            private TextView mContentTextView;//item_main의 황운지 연결

            public View mView;

            public MainViewHolder(View itemview) {
                super(itemview);
                //item_main.xml에 존재하는 택스트뷰들의 id와 연결할 변수들에 id 연결
                mView=itemView;

                mContentTextView = itemview.findViewById(R.id.item_content_text);
                mContentTextView.setSingleLine();
                mContentTextView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                mContentTextView.setSelected(true);

            }
        }
    }
}
