package com.example.googlemap;
import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import android.widget.ArrayAdapter;
import android.widget.Spinner;
import java.io.IOException;
import java.io.InputStream;
import java.security.PublicKey;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import noman.googleplaces.NRPlaces;
import noman.googleplaces.Place;
import noman.googleplaces.PlaceType;
import noman.googleplaces.PlacesException;
import noman.googleplaces.PlacesListener;

import static com.example.googlemap.R.id.dl_main_drawer_root;

public class MainActivity extends AppCompatActivity
        implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, NavigationView.OnNavigationItemSelectedListener  {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;
    Toolbar toolbar;

    //Calendar today = new GregorianCalendar();



    private FileSplit finString;
    final static int m = 3000;
    public static int data[][];

    public double[][] Repair_mark = new double[2][30];
    public double[][] New_mark = new double[2][30];
    public double[][] Basic_mark = new double[2][30];

    public static int ii=0;
    public static int abcabc = 0;
    private static int ii2=0;
    /*파이어베이스 리드소스를 위한 바이어베이스 변수 선언*/
    private FirebaseFirestore rStore = FirebaseFirestore.getInstance();
    /*디비보드의 아이디 선언*/
    public String id;

    private PolylineOptions polylineOptions;
    private PolygonOptions polygonOptions;
    private GoogleApiClient mGoogleApiClient = null;
    private GoogleMap mGoogleMap = null;
    private Marker currentMarker = null;

    private static final String TAG = "googlemap_example";
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 2002;
    private static final int UPDATE_INTERVAL_MS = 1000;
    private static final int FASTEST_UPDATE_INTERVAL_MS = 500;

    private AppCompatActivity mActivity;
    boolean askPermissionOnceAgain = false;
    boolean mRequestingLocationUpdates = false;
    Location mCurrentLocation;
    boolean mMoveMapByUser = true;
    boolean mMoveMapByAPI = true;
    LatLng currentPosition;


    //board read변수
    public static String title;
    public static Date dateExample;
    Date today = new Date();
    Date yesterday = new Date();
    Date threedaysago = new Date();
    int compare_time;
    int compare_time2;


    LocationRequest locationRequest = new LocationRequest()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(UPDATE_INTERVAL_MS)
            .setFastestInterval(FASTEST_UPDATE_INTERVAL_MS);

//    List<Marker> previous_marker =null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

//
//        Date yesterday= new Date(Date.parse(now)-1*1000*60*60*24);//yesterday
//        long time = yesterday.getTime();
//        Timestamp ts = new Timestamp(Timestamp.valueOf(time));


        try {
            InputStream fi = getResources().openRawResource(R.raw.n_info);
            if (fi != null) {
                byte[] data = new byte[fi.available()];
                fi.read(data);
                fi.close();
                String s = new String(data, "UTF-8");
                finString = new FileSplit(s);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        //DB에 저장된 위도경도 불러오기
        rStore.collection("repair").get()//id = rStore.collection("repair").document().getId();
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            ii = 0;
                            for (DocumentSnapshot document : task.getResult()) {

                                //String id = (String) document.getData().get("id");
                                String wedo = (String) document.getData().get("wedo");
                                String gengdo = (String) document.getData().get("gengdo");
                                //dateExample =(Date) document.getData().get("dateExample");//건물게시글날짜
                                Date date= (Date) document.getData().get("date");
                                //게시들날짜(24)가 3일전날짜(27)보다 작을때 뜨지 않아
                                //3일전날짜가 게시글날짜보다 커야햄
                                compare_time2=threedaysago.compareTo(date);

                                if(compare_time2<=0)
                                {

                                    Repair_mark[0][ii] = Double.parseDouble(wedo);
                                    Repair_mark[1][ii] = Double.parseDouble(gengdo);
                                    ii++;

                                }
                            }//for1013
                        }//if1013
                    }//public1013

                });//add1013


        yesterday.setTime(today.getTime()-((long)1000*60*60*24));
        threedaysago.setTime(today.getTime()-((long)1000*60*60*24*3));

        //DB에 저장된 건물 이름 불러오기//new시작
        rStore.collection("board").get()//id = rStore.collection("repair").document().getId();
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                title = (String) document.getData().get("title");//제목

                                dateExample =(Date) document.getData().get("dateExample");//건물게시글날짜
                                //게시들날짜(24)가 어제날짜(26)보다 작을때 뜨지 않아
                                //어제날짜가 게시글날짜보다 커야햄
                                //compare_time=yesterday.compareTo(dateExample);

                                if(compare_time<=0)//어제날짜가 게시글날짜보다 클때,즉 게시글날짜가 어제 날짜를 지났을때
                                {
                                    //여기에 new마커 띄우기
                                    for(int abc=1; abc<31; abc++) {

                                        //title이랑 텍스트 파일에 있는 건물 이름이 같을 때
                                        if (title.equals(FileSplit.structInfo[abc][1])){

                                            //해당 건물의 위도 경도 출력

                                            New_mark[0][abcabc] = Double.parseDouble(FileSplit.structInfo[abc][9]);
                                            New_mark[1][abcabc] = Double.parseDouble(FileSplit.structInfo[abc][10]);

                                            abcabc++;
                                        }
                                    }
                                }
                            }//for1013
                        }//if1013
                    }//public1013
                });//add1013


        mActivity=this;

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

    MapFragment mapFragment = (MapFragment)getFragmentManager()
            .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mapFragment.getMapAsync(MainActivity.this);

        initLayout();

        Button button_wr = (Button)findViewById(R.id.btn_repair_wr);
        button_wr.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent4 = new Intent(getApplicationContext(), test.class);
                startActivity(intent4);
            }
        });

        Intent intent = getIntent();

}

    @Override
    public void onResume() {

        super.onResume();

        if (mGoogleApiClient.isConnected()) {

            Log.d(TAG, "onResume : call startLocationUpdates");
            if (!mRequestingLocationUpdates) startLocationUpdates();
        }
        if (askPermissionOnceAgain) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                askPermissionOnceAgain = false;

                checkPermissions();
            }
        }
        }

    private void startLocationUpdates() {

        if (!checkLocationServicesStatus()) {

            Log.d(TAG, "startLocationUpdates : call showDialogForLocationServiceSetting");
            showDialogForLocationServiceSetting();
        }else {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                Log.d(TAG, "startLocationUpdates : 퍼미션 안가지고 있음");
                return;
            }

            Log.d(TAG, "startLocationUpdates : call FusedLocationApi.requestLocationUpdates");
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);
            mRequestingLocationUpdates = true;

            mGoogleMap.setMyLocationEnabled(true);

        }

    }

    private void stopLocationUpdates() {

        Log.d(TAG,"stopLocationUpdates : LocationServices.FusedLocationApi.removeLocationUpdates");
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        mRequestingLocationUpdates = false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapReady :");
        mGoogleMap = googleMap;
        setDefaultLocation();

        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(17));
        mGoogleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {

            @Override
            public boolean onMyLocationButtonClick() {

                Log.d(TAG, "onMyLocationButtonClick : 위치에 따른 카메라 이동 활성화");
                mMoveMapByAPI = true;
                return true;
            }
        });

        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Log.d(TAG, "onMapClick");
            }
        });
        mGoogleMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {

            @Override
            public void onCameraMoveStarted(int i) {

                if (mMoveMapByUser == true && mRequestingLocationUpdates) {

                    Log.d(TAG, "onCameraMove : 위치에 따른 카메라 이동 비활성화");
                    mMoveMapByAPI = false;
                }

                mMoveMapByUser = true;

            }
        });

        mGoogleMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {

            @Override
            public void onCameraMove() {

            }
        });

        LatLng[] point_new = new LatLng[91];

        point_new[0] = new LatLng(36.632491, 127.452976);
        point_new[1] = new LatLng(36.632444, 127.453181);
        point_new[2] = new LatLng(36.630657, 127.455447);
        point_new[3] = new LatLng(36.631865, 127.452996);
        point_new[4] = new LatLng(36.632145, 127.453725);
        point_new[5] = new LatLng(36.630887, 127.454566);
        point_new[6] = new LatLng( 36.631489, 127.454605);
        point_new[7] = new LatLng( 36.631591, 127.454615);
        point_new[8] = new LatLng(36.631380, 127.454790);
        point_new[9] = new LatLng(36.630668, 127.454820);
        point_new[10] = new LatLng(36.632202, 127.455297);
        point_new[11] = new LatLng( 36.632225, 127.455532);
        point_new[12] = new LatLng(36.632139, 127.455629);
        point_new[13] = new LatLng(36.632366, 127.455941);
        point_new[14] = new LatLng(36.632186, 127.455873);
        point_new[15] = new LatLng(36.632476, 127.455512);
        point_new[16] = new LatLng(36.632812, 127.455951);
        point_new[17] = new LatLng(36.632499, 127.455190);
        point_new[18] = new LatLng( 36.629752, 127.453922);
        point_new[19] = new LatLng( 36.630386, 127.454810);
        point_new[20] = new LatLng( 36.630128, 127.454732);
        point_new[21] = new LatLng( 36.629008, 127.454927);
        point_new[22] = new LatLng( 36.629149, 127.455073);
        point_new[23] = new LatLng( 36.629298, 127.455210);
        point_new[24] = new LatLng( 36.630026, 127.456136);
        point_new[25] = new LatLng( 36.630746, 127.456751);
        point_new[26] = new LatLng( 36.630934, 127.456429);
        point_new[27] = new LatLng( 36.630550, 127.457073);
        point_new[28] = new LatLng( 36.631127, 127.457401);
        point_new[29] = new LatLng( 36.631247, 127.457282);
        point_new[30] = new LatLng( 36.631199, 127.457501);
        point_new[31] = new LatLng( 36.631016, 127.457729);
        point_new[32] = new LatLng( 36.631382, 127.457719);
        point_new[33] = new LatLng( 36.630606, 127.457474);
        point_new[34] = new LatLng( 36.630686, 127.458189);
        point_new[35] = new LatLng( 36.630247, 127.457424);
        point_new[36] = new LatLng( 36.629952, 127.457505);
        point_new[37] = new LatLng( 36.629420, 127.457104);
        point_new[38] = new LatLng( 36.629342, 127.457241);
        point_new[39] = new LatLng( 36.629491, 127.456929);
        point_new[40] = new LatLng( 36.631118, 127.457116 );
        point_new[41] = new LatLng( 36.629838, 127.457293 );
        point_new[43] = new LatLng( 36.629265, 127.457344 );
        point_new[44] = new LatLng( 36.628780, 127.457558 );
        point_new[45] = new LatLng( 36.629034, 127.457161 );
        point_new[46] = new LatLng( 36.628901, 127.457362 );
        point_new[47] = new LatLng( 36.628476, 127.456682 );
        point_new[48] = new LatLng( 36.628121, 127.456343 );
        point_new[49] = new LatLng( 36.628470, 127.455587 );
        point_new[50] = new LatLng( 36.627891, 127.457027 );
        point_new[51] = new LatLng( 36.627882, 127.457661 );
        point_new[52] = new LatLng( 36.628158, 127.457237 );
        point_new[53] = new LatLng( 36.628182, 127.457902 );
        point_new[54] = new LatLng( 36.627736, 127.457963 );
        point_new[55] = new LatLng( 36.627979, 127.458195 );
        point_new[56] = new LatLng( 36.628143, 127.458435 );
        point_new[57] = new LatLng( 36.628522, 127.458607 );
        point_new[58] = new LatLng( 36.629069, 127.458944 );
        point_new[59] = new LatLng( 36.629284, 127.459199 );
        point_new[60] = new LatLng( 36.629448, 127.459607 );
        point_new[61] = new LatLng( 36.629426, 127.459832 );
        point_new[62] = new LatLng( 36.629474, 127.460030);
        point_new[63] = new LatLng( 36.629512, 127.460288);
        point_new[64] = new LatLng( 36.629676, 127.459757 );
        point_new[65] = new LatLng( 36.629749, 127.460776 );
        point_new[66] = new LatLng( 36.630167, 127.460889 );
        point_new[67] = new LatLng( 36.630111, 127.461210 );
        point_new[68] = new LatLng( 36.630330, 127.461286 );
        point_new[69] = new LatLng( 36.630464, 127.460026 );
        point_new[70] = new LatLng( 36.630348, 127.460020 );
        point_new[71] = new LatLng( 36.630373, 127.459853 );
        point_new[72] = new LatLng( 36.630447, 127.459685 );
        point_new[73] = new LatLng( 36.630533, 127.459203 );
        point_new[74] = new LatLng( 36.630391, 127.458282 );
        point_new[75] = new LatLng( 36.630481, 127.458387 );
        point_new[76] = new LatLng( 36.630628, 127.458462 );
        point_new[77] = new LatLng( 36.630675, 127.458119 );
        point_new[78] = new LatLng( 36.629000, 127.457802 );
        point_new[79] = new LatLng( 36.629265, 127.457344 );
        point_new[80] = new LatLng (36.633064, 127.456535 );
        point_new[81] = new LatLng (36.630551, 127.455749 );
        point_new[82] = new LatLng (36.632325, 127.452952 );
        point_new[83] = new LatLng (36.633273, 127.457051 );
        point_new[84] = new LatLng (36.631345, 127.457509 );
        point_new[85] = new LatLng (36.631177, 127.458144 );
        point_new[86] = new LatLng (36.631584, 127.458511 );
        point_new[87] = new LatLng (36.631838, 127.458596 );
        point_new[88] = new LatLng (36.630964, 127.459390 );
        point_new[89] = new LatLng (36.630719, 127.460318 );
        point_new[90] = new LatLng (36.629883, 127.460200 );

        int []building={5,18,15,16,17,21,24,53,42,27,44,75,28,77,33,32,70,67,79,80,81,82,83,84,85,86,87,88,89};

        for (int a=0 ;a< 29; a++) {
              MarkerOptions mOptions= new MarkerOptions();
             BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.location_marker);
            Bitmap b = bitmapdraw.getBitmap();
            Bitmap startmarker = Bitmap.createScaledBitmap(b, 100, 100, false);
            mOptions.position(point_new[building[a]-1 ]);
            mOptions.icon(BitmapDescriptorFactory.fromBitmap(startmarker));
            mOptions.title(Integer.toString(building[a]));
            mGoogleMap.addMarker(mOptions);

           }



        mGoogleMap.setOnMarkerClickListener(new OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                for(int kk = 1; kk<31; kk++){

                    if(marker.getTitle().equals(FileSplit.structInfo[kk][8])){

                        Intent intent_detail = new Intent(getApplicationContext(), Detail.class);

                        intent_detail.putExtra("id",String.valueOf(FileSplit.structInfo[kk][0]));
                        intent_detail.putExtra("str_en",String.valueOf(FileSplit.structInfo[kk][1]));
                        intent_detail.putExtra("str_ko",String.valueOf(FileSplit.structInfo[kk][2]));
                        intent_detail.putExtra("toilet_detail",String.valueOf(FileSplit.structInfo[kk][3]));
                        intent_detail.putExtra("elevator",String.valueOf(FileSplit.structInfo[kk][4]));
                        intent_detail.putExtra("access",String.valueOf(FileSplit.structInfo[kk][5]));
                        intent_detail.putExtra("toilet",String.valueOf(FileSplit.structInfo[kk][6]));
                        intent_detail.putExtra("elevator_detail",String.valueOf(FileSplit.structInfo[kk][7]));
                        intent_detail.putExtra("slow_detail",String.valueOf(FileSplit.structInfo[kk][11]));
                        startActivity(intent_detail);

                    }
                }

                return false;
            }
        });

        }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.item1:
                //Toast.makeText(this, "item1 clicked..", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent1);
                break;
            case R.id.item2:
                //Toast.makeText(this, "item2 clicked..", Toast.LENGTH_SHORT).show();
                Intent intent2 = new Intent(getApplicationContext(), FindLoadActivity.class);
                startActivity(intent2);
                break;
            case R.id.item3:
                //Toast.makeText(this, "개발자 문의 클릭", Toast.LENGTH_SHORT).show();
                Intent intent3 = new Intent(getApplicationContext(), Inquiry.class);
                startActivity(intent3);
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceStare has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    private void initLayout() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_folder_open_white);

        drawerLayout = (DrawerLayout) findViewById(dl_main_drawer_root);
        navigationView = (NavigationView) findViewById(R.id.nv_main_navigation_root);
        drawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.drawer_open,
                R.string.drawer_close
        );
        drawerLayout.addDrawerListener(drawerToggle);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        currentPosition
                = new LatLng( location.getLatitude(), location.getLongitude());


        Log.d(TAG, "onLocationChanged : ");

        String markerTitle = getCurrentAddress(currentPosition);
        String markerSnippet = "위도:" + String.valueOf(location.getLatitude())
                + " 경도:" + String.valueOf(location.getLongitude());

//현재 위치에 마커 생성하고 이동
        setCurrentLocation(location, markerTitle, markerSnippet);

        mCurrentLocation = location;

        for (int j = 0; j < ii+1; j++) {

            MarkerOptions makerOptions = new MarkerOptions();

            BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.maps_fix);
            Bitmap b=bitmapdraw.getBitmap();
            Bitmap smallMarker = Bitmap.createScaledBitmap(b, 200, 200, false);
            makerOptions
                    .position(new LatLng(Repair_mark[0][j], Repair_mark[1][j]))
                    .icon(BitmapDescriptorFactory.fromBitmap(smallMarker));

            mGoogleMap.addMarker(makerOptions);

        }
        for (int j = 0; j < abcabc+1; j++) {

            MarkerOptions makerOptions = new MarkerOptions();

            BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.new_marker);
            Bitmap b=bitmapdraw.getBitmap();
            Bitmap smallMarker = Bitmap.createScaledBitmap(b, 100, 100, false);
            makerOptions
                    .position(new LatLng(New_mark[0][j], New_mark[1][j]))
                    .icon(BitmapDescriptorFactory.fromBitmap(smallMarker));

            mGoogleMap.addMarker(makerOptions);

        }



    }

    @Override
    protected void onStart() {

        if(mGoogleApiClient != null && mGoogleApiClient.isConnected() == false){

            Log.d(TAG, "onStart: mGoogleApiClient connect");
            mGoogleApiClient.connect();
        }

        super.onStart();
    }

    @Override
    protected void onStop() {

        if (mRequestingLocationUpdates) {

            Log.d(TAG, "onStop : call stopLocationUpdates");
            stopLocationUpdates();
        }

        if ( mGoogleApiClient.isConnected()) {

            Log.d(TAG, "onStop : mGoogleApiClient disconnect");
            mGoogleApiClient.disconnect();
        }

        super.onStop();
    }

    @Override
    public void onConnected(Bundle connectionHint) {


        if ( mRequestingLocationUpdates == false ) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);

                if (hasFineLocationPermission == PackageManager.PERMISSION_DENIED) {

                    ActivityCompat.requestPermissions(mActivity,
                            new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

                } else {

                    Log.d(TAG, "onConnected : 퍼미션 가지고 있음");
                    Log.d(TAG, "onConnected : call startLocationUpdates");
                    startLocationUpdates();
                    mGoogleMap.setMyLocationEnabled(true);
                }

            }else{

                Log.d(TAG, "onConnected : call startLocationUpdates");
                startLocationUpdates();
                mGoogleMap.setMyLocationEnabled(true);
            }
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        Log.d(TAG, "onConnectionFailed");
        setDefaultLocation();
    }


    @Override
    public void onConnectionSuspended(int cause) {

        Log.d(TAG, "onConnectionSuspended");
        if (cause == CAUSE_NETWORK_LOST)
            Log.e(TAG, "onConnectionSuspended(): Google Play services " +
                    "connection lost. Cause: network lost.");
        else if (cause == CAUSE_SERVICE_DISCONNECTED)
            Log.e(TAG, "onConnectionSuspended(): Google Play services " +
                    "connection lost. Cause: service disconnected");
    }

    public String getCurrentAddress(LatLng latlng) {

//지오코더... GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        List<Address> addresses;

        try {

            addresses = geocoder.getFromLocation(
                    latlng.latitude,
                    latlng.longitude,
                    1);
        } catch (IOException ioException) {
//네트워크 문제
            Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";

        }


        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(this, "주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견";

        } else {
            Address address = addresses.get(0);
            return address.getAddressLine(0);
        }

    }


    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }


    public void setCurrentLocation(Location location, String markerTitle, String markerSnippet) {

        mMoveMapByUser = false;


        if (currentMarker != null) currentMarker.remove();


        LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(currentLatLng);
        markerOptions.title(markerTitle);
        markerOptions.snippet(markerSnippet);
        markerOptions.draggable(true);


        currentMarker = mGoogleMap.addMarker(markerOptions);


        if ( mMoveMapByAPI ) {

            Log.d( TAG, "setCurrentLocation : mGoogleMap moveCamera "
                    + location.getLatitude() + " " + location.getLongitude() ) ;
// CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(currentLatLng, 15);
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(currentLatLng);
            mGoogleMap.moveCamera(cameraUpdate);
        }
    }


    public void setDefaultLocation() {

        mMoveMapByUser = false;


//디폴트 위치, Seoul
        LatLng DEFAULT_LOCATION = new LatLng(36.629549, 127.457840);
        String markerTitle = "위치정보 가져올 수 없음";
        String markerSnippet = "위치 퍼미션과 GPS 활성 요부 확인하세요";


        if (currentMarker != null) currentMarker.remove();

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(DEFAULT_LOCATION);
        markerOptions.title(markerTitle);
        markerOptions.snippet(markerSnippet);
        markerOptions.draggable(true);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        currentMarker = mGoogleMap.addMarker(markerOptions);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION, 15);
        mGoogleMap.moveCamera(cameraUpdate);

    }


    //여기부터는 런타임 퍼미션 처리을 위한 메소드들
    @TargetApi(Build.VERSION_CODES.M)
    private void checkPermissions() {
        boolean fineLocationRationale = ActivityCompat
                .shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);

        if (hasFineLocationPermission == PackageManager
                .PERMISSION_DENIED && fineLocationRationale)
            showDialogForPermission("앱을 실행하려면 퍼미션을 허가하셔야합니다.");

        else if (hasFineLocationPermission
                == PackageManager.PERMISSION_DENIED && !fineLocationRationale) {
            showDialogForPermissionSetting("퍼미션 거부 + Don't ask again(다시 묻지 않음) " +
                    "체크 박스를 설정한 경우로 설정에서 퍼미션 허가해야합니다.");
        } else if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED) {


            Log.d(TAG, "checkPermissions : 퍼미션 가지고 있음");

            if ( mGoogleApiClient.isConnected() == false) {

                Log.d(TAG, "checkPermissions : 퍼미션 가지고 있음");
                mGoogleApiClient.connect();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (permsRequestCode
                == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION && grantResults.length > 0) {

            boolean permissionAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

            if (permissionAccepted) {


                if ( mGoogleApiClient.isConnected() == false) {

                    Log.d(TAG, "onRequestPermissionsResult : mGoogleApiClient connect");
                }


                mGoogleApiClient.connect();

            } else {

                checkPermissions();
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void showDialogForPermission(String msg) {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("알림");
        builder.setMessage(msg);
        builder.setCancelable(false);
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                ActivityCompat.requestPermissions(mActivity,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }
        });

        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });
        builder.create().show();
    }


    private void showDialogForPermissionSetting(String msg) {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("알림");
        builder.setMessage(msg);
        builder.setCancelable(true);
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                askPermissionOnceAgain = true;

                Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:" + mActivity.getPackageName()));
                myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
                myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mActivity.startActivity(myAppSettings);
            }
        });
        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });
        builder.create().show();
    }

    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하실래요?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case GPS_ENABLE_REQUEST_CODE:

//사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {

                        Log.d(TAG, "onActivityResult : 퍼미션 가지고 있음");


                        if ( mGoogleApiClient.isConnected() == false ) {

                            Log.d( TAG, "onActivityResult : mGoogleApiClient connect ");
                            mGoogleApiClient.connect();
                        }
                        return;
                    }
                }

                break;
        }
    }









}