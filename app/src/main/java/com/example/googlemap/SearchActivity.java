package com.example.googlemap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    public List<String> list; // 데이터를 넣은 리스트변수
    public ListView listView; // 검색을 보여줄 리스트변수
    public SearchAdapter adapter; // 리스트뷰에 연결할 아답터
    public ArrayList<String> arraylist;

    private FileSplit finString;
    //public static String info[][] = new String[31][9];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        try {
            InputStream fi = getResources().openRawResource(R.raw.n_info);
            if(fi != null){
                byte[] data = new byte[fi.available()];
                fi.read(data);
                fi.close();
                String s = new String(data, "UTF-8");
                finString = new FileSplit(s);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(this.getIntent());

        listView = (ListView) findViewById(R.id.listView);

        ImageButton close = (ImageButton) findViewById(R.id.backButton);
        close.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });

// 리스트를 생성한다.
        list = new ArrayList<String>();

// 연결 어답터 생성
        adapter = new SearchAdapter(list, this);


        //각 각 버튼 클릭 시
        Button button_n = (Button)findViewById(R.id.button_N);
        button_n.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                list.clear();

// 검색에 사용할 데이터을 미리 저장한다.
                settingList_N();

// 리스트의 모든 데이터를 arraylist에 복사한다.// list 복사본을 만든다.
                arraylist = new ArrayList<String>();
                arraylist.addAll(list);

// 리스트뷰에 아답터를 연결한다.
                listView.setAdapter(adapter);

//리스트뷰 클릭시!!
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                    public void onItemClick(AdapterView<?> parent, View v, int position, long id){
                        Intent intent1 = new Intent(getApplicationContext(), Detail.class);
//번호:건물기호:건물이름:공사유무:엘베유무:장애인진입로유무:장애인화장실 있는 층 위치

                        int i = position +1;

                        intent1.putExtra("id",String.valueOf(FileSplit.structInfo[i][0]));
                        intent1.putExtra("str_en",String.valueOf(FileSplit.structInfo[i][1]));
                        intent1.putExtra("str_ko",String.valueOf(FileSplit.structInfo[i][2]));
                        intent1.putExtra("toilet_detail",String.valueOf(FileSplit.structInfo[i][3]));
                        intent1.putExtra("elevator",String.valueOf(FileSplit.structInfo[i][4]));
                        intent1.putExtra("access",String.valueOf(FileSplit.structInfo[i][5]));
                        intent1.putExtra("toilet",String.valueOf(FileSplit.structInfo[i][6]));
                        intent1.putExtra("elevator_detail",String.valueOf(FileSplit.structInfo[i][7]));
                        intent1.putExtra("slow_detail",String.valueOf(FileSplit.structInfo[i][11]));
                        startActivity(intent1);
                    }
                });

            }
        });

        Button button_s = (Button)findViewById(R.id.button_S);
        button_s.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                list.clear();

                settingList_S();

                arraylist = new ArrayList<String>();
                arraylist.addAll(list);
                listView.setAdapter(adapter);

//리스트뷰 클릭시!!
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                    public void onItemClick(AdapterView<?> parent, View v, int position, long id){
                        Intent intent1 = new Intent(getApplicationContext(), Detail.class);
//번호:건물기호:건물이름:공사유무:엘베유무:장애인진입로유무:장애인화장실 있는 층 위치

                        int i = position +66;

                        intent1.putExtra("id",String.valueOf(FileSplit.structInfo[i][0]));
                        intent1.putExtra("str_en",String.valueOf(FileSplit.structInfo[i][1]));
                        intent1.putExtra("str_ko",String.valueOf(FileSplit.structInfo[i][2]));
                        intent1.putExtra("toilet_detail",String.valueOf(FileSplit.structInfo[i][3]));
                        intent1.putExtra("elevator",String.valueOf(FileSplit.structInfo[i][4]));
                        intent1.putExtra("access",String.valueOf(FileSplit.structInfo[i][5]));
                        intent1.putExtra("toilet",String.valueOf(FileSplit.structInfo[i][6]));
                        intent1.putExtra("elevator_detail",String.valueOf(FileSplit.structInfo[i][7]));
                        intent1.putExtra("slow_detail",String.valueOf(FileSplit.structInfo[i][11]));
                        startActivity(intent1);
                    }
                });

            }
        });

        Button button_e = (Button)findViewById(R.id.button_E);
        button_e.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                list.clear();

                settingList_E();

                arraylist = new ArrayList<String>();
                arraylist.addAll(list);
                listView.setAdapter(adapter);

//리스트뷰 클릭시!!
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                    public void onItemClick(AdapterView<?> parent, View v, int position, long id){
                        Intent intent1 = new Intent(getApplicationContext(), Detail.class);
//번호:건물기호:건물이름:공사유무:엘베유무:장애인진입로유무:장애인화장실 있는 층 위치

                        int i = position +31;

                        intent1.putExtra("id",String.valueOf(FileSplit.structInfo[i][0]));
                        intent1.putExtra("str_en",String.valueOf(FileSplit.structInfo[i][1]));
                        intent1.putExtra("str_ko",String.valueOf(FileSplit.structInfo[i][2]));
                        intent1.putExtra("toilet_detail",String.valueOf(FileSplit.structInfo[i][3]));
                        intent1.putExtra("elevator",String.valueOf(FileSplit.structInfo[i][4]));
                        intent1.putExtra("access",String.valueOf(FileSplit.structInfo[i][5]));
                        intent1.putExtra("toilet",String.valueOf(FileSplit.structInfo[i][6]));
                        intent1.putExtra("elevator_detail",String.valueOf(FileSplit.structInfo[i][7]));
                        intent1.putExtra("slow_detail",String.valueOf(FileSplit.structInfo[i][11]));
                        startActivity(intent1);
                    }
                });

            }
        });

//리스트뷰 클릭시!!
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            public void onItemClick(AdapterView<?> parent, View v, int position, long id){
                Intent intent1 = new Intent(getApplicationContext(), Detail.class);

//번호:건물기호:건물이름:공사유무:엘베유무:장애인진입로유무:장애인화장실 있는 층 위치

                int i = position +1;

                intent1.putExtra("id",String.valueOf(FileSplit.structInfo[i][0]));
                intent1.putExtra("str_en",String.valueOf(FileSplit.structInfo[i][1]));
                intent1.putExtra("str_ko",String.valueOf(FileSplit.structInfo[i][2]));
                intent1.putExtra("toilet_detail",String.valueOf(FileSplit.structInfo[i][3]));
                intent1.putExtra("elevator",String.valueOf(FileSplit.structInfo[i][4]));
                intent1.putExtra("access",String.valueOf(FileSplit.structInfo[i][5]));
                intent1.putExtra("toilet",String.valueOf(FileSplit.structInfo[i][6]));
                intent1.putExtra("elevator_detail",String.valueOf(FileSplit.structInfo[i][7]));
                intent1.putExtra("slow_detail",String.valueOf(FileSplit.structInfo[i][11]));
                startActivity(intent1);
            }
        });
    }

    // 검색에 사용될 데이터를 리스트에 추가. N 구역만
    public void settingList_N(){
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
    }

    // 검색에 사용될 데이터를 리스트에 추가. E 구역
    public void settingList_E(){
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
    }

    // 검색에 사용될 데이터를 리스트에 추가. S 구역
    public void settingList_S(){
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


