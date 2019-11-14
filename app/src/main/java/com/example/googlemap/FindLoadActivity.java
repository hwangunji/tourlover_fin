package com.example.googlemap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class FindLoadActivity extends AppCompatActivity {
    private List<String> list; // 데이터를 넣은 리스트변수
    private ListView listView1; // 출발지 검색을 보여줄 리스트변수
    private ListView listView2; //도착지 검색을 보여줄 리스트변수
    private EditText editDeparture; // 출발지를 입력할 Input 창
    private EditText editDestination; // 도착지를 입력할 Input 창
    private SearchAdapter adapter1; // 출발지 리스트뷰에 연결할 아답터
    private SearchAdapter adapter2; // 도착지 리스트뷰에 연결할 아답터
    private ArrayList<String> arraylist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_load);
        Intent intent = new Intent(this.getIntent());

        editDeparture = (EditText) findViewById(R.id.edit_departure);
        editDestination = (EditText) findViewById(R.id.edit_destination);
        listView1 = (ListView) findViewById(R.id.listView1);
        listView2 = (ListView) findViewById(R.id.listView2);

//slow load만 구현함!*************************************************************************
        Button slow_find = (Button)findViewById(R.id.slow_load);
        slow_find.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent2 = new Intent(getApplicationContext(), FindLoadDetailActivity.class);

                String start = editDeparture.getText().toString();
                String finish = editDestination.getText().toString();

                int check1=0;
                int check2=0;

//건물 정보 넣어줄 데이터 배열
                String data[] = {"E1-1", "E1-2", "E2", "E3", "E3-1", "E4-1", "E7-3", "E7-1", "E7-2", "E8-3", "E8-4", "E8-5", "E8-9",
                        "E8-11", "E8-8", "E8-7", "E8-6", "E10", "E8-10", "E9", "E8-2", "E8-1", "S21-5", "S21-4", "S21-3", "S20",
                        "S21-1", "S19", "S18", "S21-2", "S17-1", "S17-2", "S14", "S9", "S8", "S3", "S4-1", "S4-2", "S2", "S1-5",
                        "S1-4", "S1-3", "S1-7", "S1-2", "S1-1", "S10", "S11", "S1-6", "S7-3","N2","N4","N5", "N6","N7","N10", "N11","N12","N13", "N14","N15",
                        "N16-1", "N16-2","N16-3","N17-2","N17-3","N19","N20-1"};

//공백을 입력했을 경우
                if(start.equals("")||finish.equals(""))
                    Toast.makeText(getApplicationContext(),"출발지와 목적지의 값을 다시 입력해주세요.",Toast.LENGTH_LONG).show();

                for(int i=0;i<67;i++){
                    if(start.equals(data[i])){
                        check1=1;
                    }
                    if(finish.equals(data[i])){
                        check2=1;
                    }
                }

//올바른 경우
                if(check1==1&&check2==1){

//똑같은 값을 입력했을 경우
                    if(start.equals(finish)){
                        Toast.makeText(getApplicationContext(),"출발지와 도착지가 같습니다. 다시 입력해주세요.",Toast.LENGTH_SHORT).show();
                    }

//맞게 했을 경우 똑같은 값 아님.
                    else{
                        intent2.putExtra("editStart",start);
                        intent2.putExtra("editFinish",finish);

                        startActivity(intent2);
                    }
                }
                else if(check1==0&&check2==1){
                    Toast.makeText(getApplicationContext(),"출발지로 입력할 수 없는 건물입니다.",Toast.LENGTH_SHORT).show();
                }
                else if(check1==1&&check2==0){
                    Toast.makeText(getApplicationContext(),"도착지로 입력할 수 없는 건물입니다.",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(),"출발지와 목적지의 값을 다시 입력해주세요.",Toast.LENGTH_LONG).show();
                }
            }
        } );

        ImageButton close = (ImageButton) findViewById(R.id.backButton);
        close.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });

// 리스트를 생성한다.
        list = new ArrayList<String>();

// 검색에 사용할 데이터을 미리 저장한다.
        settingList();

// 리스트의 모든 데이터를 arraylist에 복사한다.// list 복사본을 만든다.
        arraylist = new ArrayList<String>();
        arraylist.addAll(list);

// 리스트에 연동될 아답터를 생성한다.
        adapter1 = new SearchAdapter(list, this);
        adapter2 = new SearchAdapter(list, this);

// 리스트뷰에 아답터를 연결한다.
        listView1.setAdapter(adapter1);
        listView2.setAdapter(adapter2);

// 출발지창에 검색어를 입력시 "addTextChangedListener" 이벤트 리스너를 정의한다.
        editDeparture.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
// input창에 문자를 입력할때마다 호출된다.
// search 메소드를 호출한다.
                String text = editDeparture.getText().toString();
                search1(text);
            }
        });

// 도착지창에 검색어를 입력시 "addTextChangedListener" 이벤트 리스너를 정의한다.
        editDestination.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
// input창에 문자를 입력할때마다 호출된다.
// search 메소드를 호출한다.
                String text = editDestination.getText().toString();
                search2(text);
            }
        });
    }
    // 검색을 수행하는 메소드
    public void search1(String charText) {

// 문자 입력시마다 리스트를 지우고 새로 뿌려준다.
        list.clear();

// 문자 입력이 없을때는 모든 데이터를 보여준다.
        if (charText.length() == 0) {
            list.addAll(arraylist);
        }
// 문자 입력을 할때..
        else
        {
// 리스트의 모든 데이터를 검색한다.
            for(int i = 0;i < arraylist.size(); i++)
            {
// arraylist의 모든 데이터에 입력받은 단어(charText)가 포함되어 있으면 true를 반환한다.
                if (arraylist.get(i).toLowerCase().contains(charText))
                {
// 검색된 데이터를 리스트에 추가한다.
                    list.add(arraylist.get(i));
                }
            }
        }
// 리스트 데이터가 변경되었으므로 아답터를 갱신하여 검색된 데이터를 화면에 보여준다.
        adapter1.notifyDataSetChanged();
    }
    // 검색을 수행하는 메소드
    public void search2(String charText) {

// 문자 입력시마다 리스트를 지우고 새로 뿌려준다.
        list.clear();

// 문자 입력이 없을때는 모든 데이터를 보여준다.
        if (charText.length() == 0) {
            list.addAll(arraylist);
        }
// 문자 입력을 할때..
        else
        {
// 리스트의 모든 데이터를 검색한다.
            for(int i = 0;i < arraylist.size(); i++)
            {
// arraylist의 모든 데이터에 입력받은 단어(charText)가 포함되어 있으면 true를 반환한다.
                if (arraylist.get(i).toLowerCase().contains(charText))
                {
// 검색된 데이터를 리스트에 추가한다.
                    list.add(arraylist.get(i));
                }
            }
        }
// 리스트 데이터가 변경되었으므로 아답터를 갱신하여 검색된 데이터를 화면에 보여준다.
        adapter2.notifyDataSetChanged();
    }
    // 검색에 사용될 데이터를 리스트에 추가한다.
    private void settingList(){
        list.add("N1 정문수위실");list.add("N2 법학전문대학원");list.add("N3 테니스장관리사");list.add("N4 산학협력관");
        list.add("N5 국제교류본부 2호관");list.add("N6 고시원");list.add("N7 형설관");
        list.add("N8 보육교사교육원");list.add("N9 국제교류본부 3호관");list.add("N10 대학본부, 국제교류본부");
        list.add("N10-1 대학본부 차고");list.add("N11 공동실험실습관");list.add("N12 중앙도서관");
        list.add("N13 경영학관");list.add("N14 인문사회관(강의동)");list.add("N15 사회과학대학");
        list.add("N16-1 인문대학");list.add("N16-2 미술관");list.add("N16-3 미술과");
        list.add("N17-1 개성재(수위실)");list.add("N17-2 개성재수위실");list.add("N17-3 개성재(진리관)");
        list.add("N17-4 개성재(정의관)");list.add("N17-5 개성재(개척관)");list.add("N17-6 계영원");
        list.add("N18 법학관");list.add("N19 제 2본관");list.add("N20-1 생활과학대학");
        list.add("N20-2 어린이집");list.add("N21 은하수식당");
        list.add("E1-1 사범대학실험동");list.add("E1-2 사범대학강의동");list.add("E2 개신문화관");list.add("E3 제1학생회관");
        list.add("E3-1 NH관");list.add("E4-1 실내체육관");list.add("E4-2 운동장본부석");
        list.add("E4-3 보조체육관");list.add("E5 123학군단");list.add("E6 특고압 변전실");
        list.add("E7-1 의과대학1호관");list.add("E7-2 임상연구동");list.add("E7-3 의과대학2호관");
        list.add("E8-1 공과대학본관");list.add("E8-2 합동강의실");list.add("E8-3 제2공학관");
        list.add("E8-4 제1공장동");list.add("E8-5 제2공장동");list.add("E8-6 제3공학관");
        list.add("E8-7 전자정보1관");list.add("E8-8 공학지원센터");list.add("E8-9 신소재재료실험실");
        list.add("E8-10 제5공학관");list.add("E8-11 양진재(BTL)");list.add("E9 학연산공동기술연구원");
        list.add("E10 전자정보2관");list.add("E11-1 목장창고");list.add("E11-2 목장관리사");
        list.add("E11-3 우사");list.add("E11-4 건조창고");list.add("E11-5 동물자원연구지원센터");
        list.add("E11-6 돈사창고");list.add("E12-1 수의과대학 및 동물병원");
        list.add("E12-2 수의과대학2호관");list.add("E12-3 실험동물연구지원센터");
        list.add("S1-1 자연대1호관");list.add("S1-2 자연대2호관");list.add("S1-3 자연대3호관");list.add("S1-4 자연대4호관");
        list.add("S1-5 자연대5호관");list.add("S1-6 자연대6호관");list.add("S1-7 과학기술도서관");
        list.add("S2 전산정보원");list.add("S3 본부관리동");list.add("S4-1 전자정보3관");
        list.add("S4-2");list.add("S4-3");list.add("S5-1 농장관리실");
        list.add("S5-2 농기계창고");list.add("S6-1 자연대 온실1");list.add("S6-2 자연대 온실2");
        list.add("S7-1 자연대실험동");list.add("S7-2 교육대학원, 동아리방");list.add("S8 야외공연장");
        list.add("S9 박물관");list.add("S10 차고");list.add("S11 유류저장창고");
        list.add("S12 쓰레기처리장");list.add("S13 목공실");list.add("S14 제2학생회관");
        list.add("S17-1 양성재(지선관)");list.add("S17-2 양성재(명덕관)");list.add("S17-3 양성재(신민관)");
        list.add("S17-4 양성재(수위실)");list.add("S17-5 양성재(청운관)");list.add("S17-6 양성재(등용관)");list.add("S17-7 양성재(관리동)");
        list.add("S18 승리관(운동부합숙소)");list.add("S19 종양연구소");list.add("S20 첨단바이오 연구센터");
        list.add("S21-1 농업전문 창업보육센터");list.add("S21-2 임산가공 공장");list.add("S21-3 농업과학기술교육센터");list.add("S21-4 농업생명환경대학");
        list.add("S21-5 농생대연구동");list.add("S21-6 농대건조실");list.add("S21-7 온실(특용식물학과)");list.add("S21-8 온실(식물자원학과)");
        list.add("S21-9 온실(식물의학과)");list.add("S21-10 온실(산림학과)");list.add("S21-11 온실(원예과학과");list.add("S21-12 온실(원예과학과)");
        list.add("S21-13 온실창고");list.add("S21-14 온실(1)");list.add("S21-15 온실(2)");list.add("S21-16 온실(3)");
        list.add("S21-17 온실(4)");list.add("S21-18 온실(5)");list.add("S21-19 넷트하우스");list.add("S21-20 온실관리동");
        list.add("S21-21 농대동위원소실");list.add("S21-22 농기계공작실");list.add("S21-23 농기계실습실");list.add("S21-24 농대부속공장");
    }
}