package com.example.googlemap;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import java.util.Vector;

public class FindLoadDetailActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {


    private TextToSpeech myTTS;

    ActionBarDrawerToggle drawerToggle;
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

    //Calendar today = new GregorianCalendar();



    /*파이어베이스 리드소스를 위한 바이어베이스 변수 선언*/
    private FirebaseFirestore rStore = FirebaseFirestore.getInstance();
    /*디비보드의 아이디 선언*/
    public String id;
    Date today = new Date();
    Date threedaysago = new Date();
    int compare_time2;


    LocationRequest locationRequest = new LocationRequest()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(UPDATE_INTERVAL_MS)
            .setFastestInterval(FASTEST_UPDATE_INTERVAL_MS);

    int n = 0;
    final static int m = 3000;
    public double[][] Repair_mark = new double[2][30];
    public static int ii=0;
    int data[][];
    boolean visit[];
    public static int dis[];
    int prev[];
    public static double Timet ;
    public static double Loadl ;
    public static int top= -1;
    public static int s, e;
    public static int stack[];

    public static String aaa;

    Vector<Integer> stackV;

    public FindLoadDetailActivity() {
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_load_detail);

        TextView tx1 = (TextView)findViewById(R.id.start22);
        TextView tx2 = (TextView)findViewById(R.id.finish22);
        TextView tx3 = (TextView)findViewById(R.id.time);   //도착 예상시간
        TextView tx4 = (TextView)findViewById(R.id.load);   //거리계산

        //today.add(Calendar.DATE,-1);
        //SimpleDateFormat format1= new SimpleDateFormat("HH:mm:ss",Locale.KOREA);


        Bundle intent = getIntent().getExtras();
        tx1.setText(intent.getString("editStart"));
        tx2.setText(intent.getString("editFinish"));


        String test1 = tx1.getText().toString();
        String test2 = tx2.getText().toString();

        int tstart=0;
        int tend=0;

        threedaysago.setTime(today.getTime()-((long)1000*60*60*24*3));

        if(test1.equals("N2")) tstart=5;
        else if(test1.equals("N4")) tstart=18;
        else if(test1.equals("N5")) tstart=15;
        else if(test1.equals("N6")) tstart=16;
        else if(test1.equals("N7")) tstart =17;
        else if(test1.equals("N10")) tstart =21;
        else if(test1.equals("N11")) tstart =24;
        else if(test1.equals("N12")) tstart =53;
        else if(test1.equals("N13")) tstart =42;
        else if(test1.equals("N14")) tstart =27;
        else if(test1.equals("N15")) tstart =44;
        else if(test1.equals("N16-1")) tstart =75;
        else if(test1.equals("N16-2")) tstart =28;
        else if(test1.equals("N16-3")) tstart =77;
        else if(test1.equals("N17-2")) tstart =33;
        else if(test1.equals("N17-3")) tstart =32;
        else if(test1.equals("N19")) tstart =70;
        else if(test1.equals("N20-1")) tstart =67;

        else if(test1.equals("E1-1")) tstart= 111;
        else if(test1.equals("E1-2")) tstart= 112;
        else if(test1.equals("E2")) tstart= 105;
        else if(test1.equals("E3")) tstart= 108;
        else if(test1.equals("E3-1")) tstart= 119;
        else if(test1.equals("E4-1")) tstart= 235;
        else if(test1.equals("E7-3")) tstart= 125;
        else if(test1.equals("E7-2")) tstart= 130;
        else if(test1.equals("E8-3")) tstart= 133;
        else if(test1.equals("E8-4")) tstart= 135;
        else if(test1.equals("E8-5")) tstart= 136;
        else if(test1.equals("E8-9")) tstart= 137;
        else if(test1.equals("E8-11")) tstart= 140;
        else if(test1.equals("E8-8")) tstart= 141;
        else if(test1.equals("E8-7")) tstart= 142;
        else if(test1.equals("E8-6")) tstart= 145;
        else if(test1.equals("E10")) tstart= 147;
        else if(test1.equals("E8-10")) tstart= 150;
        else if(test1.equals("E9")) tstart= 156;
        else if(test1.equals("E8-2")) tstart= 161;
        else if(test1.equals("E8-1")) tstart= 162;
        else if(test1.equals("S21-5")) tstart= 167;
        else if(test1.equals("S21-4")) tstart= 168;
        else if(test1.equals("S21-3")) tstart= 173;
        else if(test1.equals("S20")) tstart= 174;
        else if(test1.equals("S21-1")) tstart= 176;
        else if(test1.equals("S19")) tstart= 181;
        else if(test1.equals("S18")) tstart= 182;
        else if(test1.equals("S21-2")) tstart= 183;
        else if(test1.equals("S17-1")) tstart= 185;
        else if(test1.equals("S17-2")) tstart= 189;
        else if(test1.equals("S14")) tstart= 197;
        else if(test1.equals("S9")) tstart= 198;
        else if(test1.equals("S8")) tstart= 201;
        else if(test1.equals("S3")) tstart= 203;
        else if(test1.equals("S4-1")) tstart= 205;
        else if(test1.equals("S4-2")) tstart= 207;
        else if(test1.equals("S2")) tstart= 212;
        else if(test1.equals("S1-5")) tstart= 218;
        else if(test1.equals("S1-4")) tstart= 219;
        else if(test1.equals("S1-3")) tstart= 220;
        else if(test1.equals("S1-7")) tstart= 221;
        else if(test1.equals("S1-2")) tstart= 223;
        else if(test1.equals("S1-1")) tstart= 227;
        else if(test1.equals("S10")) tstart= 228;
        else if(test1.equals("S11")) tstart= 229;
        else if(test1.equals("S1-6")) tstart= 232;
        else if(test1.equals("S7-3")) tstart= 234;


        if(test2.equals("N2")) tend=5;
        else if(test2.equals("N4")) tend=18;
        else if(test2.equals("N5")) tend =15;
        else if(test2.equals("N6")) tend =16;
        else if(test2.equals("N7")) tend =17;
        else if(test2.equals("N10")) tend =21;
        else if(test2.equals("N11")) tend =24;
        else if(test2.equals("N12")) tend =53;
        else if(test2.equals("N13")) tend =42;
        else if(test2.equals("N14")) tend =27;
        else if(test2.equals("N15")) tend =44;
        else if(test2.equals("N16-1")) tend =75;
        else if(test2.equals("N16-2")) tend =28;
        else if(test2.equals("N16-3")) tend =77;
        else if(test2.equals("N17-2")) tend =33;
        else if(test2.equals("N17-3")) tend =32;
        else if(test2.equals("N19")) tend =70;
        else if(test2.equals("N20-1")) tend =67;
        else if(test2.equals("E1-1")) tend= 111;
        else if(test2.equals("E1-2")) tend= 112;
        else if(test2.equals("E2")) tend= 105;
        else if(test2.equals("E3")) tend= 108;
        else if(test2.equals("E3-1")) tend= 119;
        else if(test2.equals("E4-1")) tend= 235;
        else if(test2.equals("E7-3")) tend= 125;
        else if(test2.equals("E7-2")) tend= 130;
        else if(test2.equals("E8-3")) tend= 133;
        else if(test2.equals("E8-4")) tend= 135;
        else if(test2.equals("E8-5")) tend= 136;
        else if(test2.equals("E8-9")) tend= 137;
        else if(test2.equals("E8-11")) tend= 140;
        else if(test2.equals("E8-8")) tend= 141;
        else if(test2.equals("E8-7")) tend= 142;
        else if(test2.equals("E8-6")) tend= 145;
        else if(test2.equals("E10")) tend= 147;
        else if(test2.equals("E8-10")) tend= 150;
        else if(test2.equals("E9")) tend= 156;
        else if(test2.equals("E8-2")) tend= 161;
        else if(test2.equals("E8-1")) tend= 162;
        else if(test2.equals("S21-5")) tend= 167;
        else if(test2.equals("S21-4")) tend= 168;
        else if(test2.equals("S21-3")) tend= 173;
        else if(test2.equals("S20")) tend= 174;
        else if(test2.equals("S21-1")) tend= 176;
        else if(test2.equals("S19")) tend= 181;
        else if(test2.equals("S18")) tend= 182;
        else if(test2.equals("S21-2")) tend= 183;
        else if(test2.equals("S17-1")) tend= 185;
        else if(test2.equals("S17-2")) tend= 189;
        else if(test2.equals("S14")) tend= 197;
        else if(test2.equals("S9")) tend= 198;
        else if(test2.equals("S8")) tend= 201;
        else if(test2.equals("S3")) tend= 203;
        else if(test2.equals("S4-1")) tend= 205;
        else if(test2.equals("S4-2")) tend= 207;
        else if(test2.equals("S2")) tend= 212;
        else if(test2.equals("S1-5")) tend= 218;
        else if(test2.equals("S1-4")) tend= 219;
        else if(test2.equals("S1-3")) tend= 220;
        else if(test2.equals("S1-7")) tend= 221;
        else if(test2.equals("S1-2")) tend= 223;
        else if(test2.equals("S1-1")) tend= 227;
        else if(test2.equals("S10")) tend= 228;
        else if(test2.equals("S11")) tend= 229;
        else if(test2.equals("S1-6")) tend= 232;
        else if(test2.equals("S7-3")) tend= 234;


        int m = 3000;
        int [][] data;
        data = new int[237][237];
        for(int a=0; a<237;a++)
        {
            for(int b=0; b<237;b++) {
                data[a][b] = m;
            }
        }

        for (int a=0;a<237;a++)
        {
            data[a][a] =0;
        }

        data[1][0]=23;
        data[3][0]=72;
        data[2][1]=31;
        data[4][2]=43;
        data[4][3]=78; data[3][4]=78;
        data[5][3]=180;
        data[7][4]=112;
        data[7][6]=26;
        data[8][6]=22;
        data[8][5]=30;
        data[11][10]=25;
        data[12][11]=18;

        data[0][1]= 23;
        data[0][3]= 72;
        data[1][2]= 31;
        data[2][4]=43;
        data[3][5]=180;
        data[4][7]=112;
        data[6][7]=26;
        data[6][8]=22;
        data[5][8]=30;
        data[10][11]=25;
        data[11][12]=18;

        data[12][13]=16;
        data[11][17]=36;
        data[13][14]=15;
        data[13][15]=39;
        data[15][16]=42;
        data[5][18]=133;
        data[5][9]=26;
        data[9][19]=35;
        data[19][20]=19;
        data[18][21]=128;
        data[21][22]=25;

        data[13][12]=16;
        data[17][11]=36;
        data[14][13]=15;
        data[15][13]=39;
        data[16][15]=42;
        data[18][5]=133;
        data[9][5]=26;
        data[19][9]=35;
        data[20][19]=19;
        data[21][18]=128;
        data[22][21]=25;

        data[22][23]=17;
        data[21][49]=102;
        data[49][48]=51;
        data[48][50]=68;
        data[48][47]=53;
        data[47][45]=75;
        data[45][46]=30;
        data[50][52]=46;
        data[52][51]=64;
        data[51][53]=36;
        data[53][55]=25;
        data[54][55]=37;
        data[47][52]=67;
        data[46][44]=29;
        data[45][79]=31;
        data[46][79]=46;
        data[78][79]=53;
        data[78][43]=31;
        data[44][78]=34;
        data[78][57]=93;
        data[57][58]=64;
        data[59][60]=46;
        data[60][61]= 8;
        data[61][62]= 26;
        data[62][63]= 25;
        data[63][65]= 58;
        data[60][64]= 46;
        data[58][59]=44;
        data[64][71]= 56;
        data[70][71]= 22;
        data[70][69]= 24;
        data[71][72]= 13;
        data[70][66]= 78;
        data[66][67]= 35;
        data[67][68]= 23;
        data[72][73]= 35;
        data[73][76]=85;
        data[75][74]=14;
        data[33][28]=66;
        data[28][30]=17;
        data[28][29]=12;
        data[30][32]=25;
        data[30][31]=28;
        data[29][40]=18;
        data[25][27]=28;
        data[25][26]=19;
        data[24][25]=110;
        data[37][36]=69;
        data[39][37]=16;
        data[37][38]=16;
        data[38][79]=16;
        data[39][41]=44;
        data[39][24]=96;
        data[9][24]=144;
        data[65][66]=39;
        data[33][34]=37;
        data[34][77]=28;
        data[76][77]=34;
        data[75][76]=22;
        data[10][8]=127;

        data[23][22]=17;
        data[49][21]=102;
        data[48][49]=51;
        data[50][48]=68;
        data[47][48]=53;
        data[45][47]=75;
        data[46][45]=30;
        data[52][50]=46;
        data[51][52]=64;
        data[53][51]=36;
        data[55][53]=25;
        data[55][54]=37;
        data[52][47]=67;
        data[44][46]=29;
        data[79][45]=31;
        data[79][46]=46;
        data[79][78]=53;
        data[43][78]=31;
        data[78][44]=34;
        data[57][78]=93;
        data[58][57]=64;
        data[59][58] =44;
        data[60][59]=46;
        data[61][60]= 8;
        data[62][61]= 26;
        data[63][62]= 25;
        data[65][63]= 58;
        data[64][60]= 46;
        data[71][64]= 56;
        data[71][70]= 22;
        data[69][70]= 24;
        data[72][71]= 13;
        data[66][70]= 78;
        data[67][66]= 35;
        data[68][67]=23;
        data[73][72]=35;
        data[76][73]=85;
        data[74][75]=14;
        data[28][33]=66;
        data[30][28]=17;
        data[29][28]=12;
        data[32][30]=25;
        data[31][30]=28;
        data[40][29]=18;
        data[27][25]=28;
        data[26][25]=19;
        data[25][24]=110;
        data[36][37]=69;
        data[37][39]=16;
        data[38][37]=16;
        data[79][38]=16;
        data[41][39]=44;
        data[24][39]=96;
        data[24][9]=144;
        data[66][65]=39;
        data[34][33]=37;
        data[77][34]=28;
        data[77][76]=34;
        data[76][75]=22;
        data[8][10]=127;

        data[18][163]=56; data[163][18]=56;
        data[163][164]=52; data[164][163]=52;
        data[164][165]=30; data[165][164]=30;
        data[164][166]=16; data[166][164]=16;
        data[163][168]=99; data[168][163]=99;
        data[168][169]=104; data[169][168]=104;
        data[169][170]=18; data[170][169]=18;
        data[170][173]=52; data[173][170]=52;
        data[170][171]=69; data[171][170]=69;
        data[171][172]=39; data[172][171]=39;
        data[171][167]=18; data[167][171]=18;
        data[169][174]=39; data[174][169]=39;
        data[174][175]=21; data[175][174]=21;
        data[168][176]=90; data[176][168]=90;
        data[176][177]=120; data[177][176]=120;
        data[177][178]=60; data[178][177]=60;
        data[178][179]=16; data[179][178]=16;
        data[179][173]=30; data[173][179]=30;
        data[180][178]=12; data[178][180]=12;
        data[177][183]=60; data[183][177]=60;
        data[183][184]=23; data[184][183]=23;
        data[183][185]=65; data[185][183]=65;
        data[185][179]=48; data[179][185]=48;
        data[189][233]=18; data[233][189]=18;
        data[189][188]=45; data[188][189]=45;
        data[176][186]=40; data[186][176]=40;
        data[186][187]=37; data[187][186]=37;
        data[187][184]=46; data[184][187]=46;
        data[187][188]=44; data[188][187]=44;
        data[188][190]=22; data[190][188]=22;
        data[190][186]=48; data[186][190]=48;
        data[190][191]=31; data[191][190]=31;
        data[191][195]=100; data[195][191]=100;
        data[195][194]=58; data[194][195]=58;
        data[194][192]=24; data[192][194]=24;
        data[191][192]=86; data[192][191]=86;
        data[196][194]=21; data[194][196]=21;
        data[194][193]=23; data[193][194]=23;
        data[192][198]=91; data[198][192]=91;
        data[198][197]=26; data[197][198]=26;
        data[192][199]=97; data[199][192]=97;
        data[199][200]=26; data[200][199]=26;
        data[199][192]=99; data[192][199]=99;
        data[199][201]=66; data[201][199]=66;
        data[201][204]=56; data[204][201]=56;
        data[201][202]=29; data[202][201]=29;
        data[201][203]=77; data[203][201]=77;
        data[203][205]=107; data[205][203]=107;
        data[205][207]=40; data[207][205]=40;
        data[207][208]=51; data[208][207]=51;
        data[208][209]=36; data[209][208]=36;
        data[209][210]=36; data[210][209]=36;
        data[210][206]=14; data[206][210]=14;
        data[203][212]=40; data[212][203]=40;
        data[212][214]=66; data[214][212]=66;
        data[214][217]=37; data[217][214]=37;
        data[217][216]=41; data[217][216]=41;
        data[216][215]=49; data[215][216]=49;
        data[215][213]=22; data[213][215]=22;
        data[212][213]=93; data[213][212]=93;
        data[211][213]=164; data[213][211]=164;
        data[215][218]=35; data[218][215]=35;
        data[215][162]=101; data[162][215]=101;
        data[162][153]=68; data[153][162]=68;
        data[153][152]=27; data[152][153]=27;
        data[152][229]=56; data[229][152]=56;
        data[229][231]=24; data[231][229]=24;
        data[229][230]=28; data[230][229]=28;
        data[230][217]=32; data[217][230]=32;
        data[152][151]=55; data[151][152]=55;
        data[151][232]=61; data[232][151]=61;
        data[232][231]=43; data[231][232]=43;
        data[151][150]=125; data[150][151]=125;
        data[150][147]=10; data[147][150]=10;
        data[153][154]=45; data[154][153]=45;
        data[154][156]=32; data[156][154]=32;
        data[146][156]=36; data[156][146]=36;
        data[154][155]=19; data[155][154]=19;
        data[156][142]=85; data[142][156]=85;
        data[146][145]=25; data[145][146]=25;
        data[145][147]=24; data[147][145]=24;
        data[147][148]=44; data[148][147]=44;
        data[148][149]=18; data[149][158]=18;
        data[148][144]=49; data[144][148]=49;
        data[159][141]=163; data[141][159]=163;
        data[159][162]=43; data[162][159]=43;
        data[159][158]=54; data[158][159]=54;
        data[158][219]=61; data[219][158]=61;
        data[158][157]=118; data[157][158]=118;
        data[157][221]=52; data[221][157]=52;
        data[222][221]=14; data[221][222]=14;
        data[221][220]=20; data[220][221]=20;
        data[221][223]=66; data[223][221]=66;
        data[223][224]=64; data[224][223]=64;
        data[224][225]=21; data[225][224]=21;
        data[225][226]=41; data[226][225]=41;
        data[225][49]=92; data[49][225]=92;
        data[50][226]=30; data[226][50]=30;
        data[102][157]=77; data[157][102]=77;
        data[102][109]=133; data[109][102]=133;
        data[109][108]=26; data[108][109]=26;
        data[118][108]=25; data[108][118]=25;
        data[55][107]=43; data[107][55]=43;
        data[56][105]=70; data[105][56]=70;
        data[104][105]=44; data[105][104]=44;
        data[105][106]=42; data[106][105]=42;
        data[106][115]=19; data[115][106]=19;
        data[115][116]=21; data[116][115]=21;
        data[116][114]=17; data[114][116]=17;
        data[115][108]=62; data[108][115]=62;
        data[109][130]=43; data[130][109]=43 ;
        data[130][119]=7; data[119][130]=7;
        data[119][114]=85; data[114][119]=85;
        data[114][113]=42; data[113][114]=42;
        data[113][112]=48; data[112][113]=48;
        data[112][111]=66; data[111][112]=66;
        data[103][112]=108; data[112][103]=108;
        data[103][58]=111; data[58][103]=111;
        data[58][111]=146; data[111][58]=146;
        data[60][111]=96; data[111][60]=96;
        data[111][110]=82; data[110][111]=82;
        data[113][234]=75; data[234][113]=75;
        data[119][120]=49; data[120][119]=49;
        data[120][121]=62; data[121][120]=62;
        data[120][131]=36; data[131][120]=36;
        data[131][132]=35; data[132][131]=35;
        data[131][133]=140; data[133][131]=140;
        data[133][142]=140; data[142][133]=140;
        data[142][145]=65; data[145][142]=65;
        data[133][134]=39; data[134][133]=39;
        data[133][135]=62; data[135][133]=62;
        data[142][143]=79; data[143][142]=79;
        data[143][140]=40; data[140][143]=40;
        data[121][137]=122; data[137][121]=122;
        data[137][138]=264; data[138][137]=264;
        data[138][139]=45; data[139][138]=45;
        data[121][122]=75; data[122][121]=75;
        data[122][123]=22; data[123][122]=22;
        data[123][125]=29; data[125][123]=29;
        data[125][126]=41; data[126][125]=41;
        data[126][235]=39; data[235][126]=39;
        data[235][127]=14; data[127][235]=14;
        data[128][129]=88; data[129][128]=88;

        com.example.googlemap.FindLoadDetailActivity k = new com.example.googlemap.FindLoadDetailActivity();
        k.init(data);
        k.start(tstart, tend);



        Loadl = dis[e-1]/1000.0;
        Timet = dis[e-1]/(2700.0/60.0);

        double aa = Double.parseDouble(String.format("%.2f",Loadl));
        double b = Double.parseDouble(String.format("%.2f",Timet));

        String test123 = Double.toString(b);
        String test456 = Double.toString(aa);

        tx3.setText(test123);
        tx4.setText(test456);



        final int testestet = (int)Math.round((b*10)/10);

        myTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {


//음성 톤
                myTTS.setPitch(0.7f);

//읽는 속도
                myTTS.setSpeechRate(1.0f);

                String myText1 ="경로 안내를 시작합니다. 목적지까지의 도착 시간은";
                myTTS.speak(myText1,TextToSpeech.QUEUE_FLUSH,null);

                String myText2 = "경로 안내를 시작합니다. 목적지까지의 도착 예상 시간은 ";

                String bbb =Integer.toString(testestet);

                myText2 = myText2+bbb + "분 입니다.";
                myTTS.speak(myText2,TextToSpeech.QUEUE_FLUSH,null);

            }
        });


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        /*아래에리드데이터즉파이어베이스에서 데이터 가져오는 코드를 복붙함(tool파이어베이스 코드에 있음*/
        rStore.collection("repair").get()//id = rStore.collection("repair").document().getId();
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                //String id = (String) document.getData().get("id");
                                String wedo= (String) document.getData().get("wedo");
                                String gengdo= (String) document.getData().get("gengdo");
                                Date date= (Date) document.getData().get("date");
                                //게시들날짜(24)가 3일전날짜(27)보다 작을때 뜨지 않아
                                //3일전날짜가 게시글날짜보다 커야햄
                                compare_time2=threedaysago.compareTo(date);
                                if(compare_time2<=0)
                                {
                                    Repair_mark[0][ii]=Double.parseDouble(wedo);
                                    Repair_mark[1][ii]=Double.parseDouble(gengdo);
                                    ii++;

                                }
                            }//for1013


                        }//if1013

                    }//public1013

                });//add1013




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
        } else {

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

        Log.d(TAG, "stopLocationUpdates : LocationServices.FusedLocationApi.removeLocationUpdates");
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


 MarkerOptions marker1 = new MarkerOptions();
 MarkerOptions marker2 = new MarkerOptions();
 MarkerOptions marker3 = new MarkerOptions();
 MarkerOptions marker4 = new MarkerOptions();
 MarkerOptions marker5 = new MarkerOptions();
 MarkerOptions marker6 = new MarkerOptions();
 MarkerOptions marker7 = new MarkerOptions();
 MarkerOptions marker8 = new MarkerOptions();
 MarkerOptions marker9 = new MarkerOptions();
 MarkerOptions marker10 = new MarkerOptions();
 MarkerOptions marker11 = new MarkerOptions();
 MarkerOptions marker12 = new MarkerOptions();
 MarkerOptions marker13 = new MarkerOptions();
 MarkerOptions marker14 = new MarkerOptions();
 MarkerOptions marker15 = new MarkerOptions();
 MarkerOptions marker16 = new MarkerOptions();
 MarkerOptions marker17 = new MarkerOptions();
 MarkerOptions marker18 = new MarkerOptions();
 MarkerOptions marker19 = new MarkerOptions();
 MarkerOptions marker20 = new MarkerOptions();
 MarkerOptions marker21 = new MarkerOptions();
 MarkerOptions marker22 = new MarkerOptions();
 MarkerOptions marker23 = new MarkerOptions();
 MarkerOptions marker24 = new MarkerOptions();
 MarkerOptions marker25 = new MarkerOptions();
 MarkerOptions marker26 = new MarkerOptions();
 MarkerOptions marker27 = new MarkerOptions();
 MarkerOptions marker28 = new MarkerOptions();
 MarkerOptions marker29 = new MarkerOptions();
 MarkerOptions marker30 = new MarkerOptions();
 MarkerOptions marker31 = new MarkerOptions();
 MarkerOptions marker32 = new MarkerOptions();
 MarkerOptions marker33 = new MarkerOptions();
 MarkerOptions marker34 = new MarkerOptions();
 MarkerOptions marker35 = new MarkerOptions();
 MarkerOptions marker36 = new MarkerOptions();
 MarkerOptions marker37 = new MarkerOptions();
 MarkerOptions marker38 = new MarkerOptions();
 MarkerOptions marker39 = new MarkerOptions();
 MarkerOptions marker40 = new MarkerOptions();
 MarkerOptions marker41 = new MarkerOptions();
 MarkerOptions marker42 = new MarkerOptions();
 MarkerOptions marker43 = new MarkerOptions();
 MarkerOptions marker44 = new MarkerOptions();
 MarkerOptions marker45 = new MarkerOptions();
 MarkerOptions marker46 = new MarkerOptions();
 MarkerOptions marker47 = new MarkerOptions();
 MarkerOptions marker48 = new MarkerOptions();
 MarkerOptions marker49 = new MarkerOptions();
 MarkerOptions marker50 = new MarkerOptions();
 MarkerOptions marker51 = new MarkerOptions();
 MarkerOptions marker52 = new MarkerOptions();
 MarkerOptions marker53 = new MarkerOptions();
 MarkerOptions marker54 = new MarkerOptions();
 MarkerOptions marker55 = new MarkerOptions();
 MarkerOptions marker56 = new MarkerOptions();
 MarkerOptions marker57 = new MarkerOptions();
 MarkerOptions marker58 = new MarkerOptions();
 MarkerOptions marker59 = new MarkerOptions();
 MarkerOptions marker60 = new MarkerOptions();
 MarkerOptions marker61 = new MarkerOptions();
 MarkerOptions marker62 = new MarkerOptions();
 MarkerOptions marker63 = new MarkerOptions();
 MarkerOptions marker64 = new MarkerOptions();
 MarkerOptions marker65 = new MarkerOptions();
 MarkerOptions marker66 = new MarkerOptions();
 MarkerOptions marker67 = new MarkerOptions();
 MarkerOptions marker68 = new MarkerOptions();
 MarkerOptions marker69 = new MarkerOptions();
 MarkerOptions marker70 = new MarkerOptions();
 MarkerOptions marker71 = new MarkerOptions();
 MarkerOptions marker72 = new MarkerOptions();
 MarkerOptions marker73 = new MarkerOptions();
 MarkerOptions marker74 = new MarkerOptions();
 MarkerOptions marker75 = new MarkerOptions();
 MarkerOptions marker76 = new MarkerOptions();
 MarkerOptions marker77 = new MarkerOptions();
 MarkerOptions marker78 = new MarkerOptions();
 MarkerOptions marker79 = new MarkerOptions();
 MarkerOptions marker80 = new MarkerOptions();
 MarkerOptions marker103 = new MarkerOptions();
 MarkerOptions marker104 = new MarkerOptions();
 MarkerOptions marker105 = new MarkerOptions();
 MarkerOptions marker106 = new MarkerOptions();
 MarkerOptions marker107 = new MarkerOptions();
 MarkerOptions marker108 = new MarkerOptions();
 MarkerOptions marker109 = new MarkerOptions();
 MarkerOptions marker110 = new MarkerOptions();
 MarkerOptions marker111 = new MarkerOptions();
 MarkerOptions marker112 = new MarkerOptions();
 MarkerOptions marker113 = new MarkerOptions();
 MarkerOptions marker114 = new MarkerOptions();
 MarkerOptions marker115 = new MarkerOptions();
 MarkerOptions marker116 = new MarkerOptions();
 MarkerOptions marker117 = new MarkerOptions();
 MarkerOptions marker118 = new MarkerOptions();
 MarkerOptions marker119 = new MarkerOptions();
 MarkerOptions marker120 = new MarkerOptions();
 MarkerOptions marker121 = new MarkerOptions();
 MarkerOptions marker122 = new MarkerOptions();
 MarkerOptions marker123 = new MarkerOptions();
 MarkerOptions marker124 = new MarkerOptions();
 MarkerOptions marker125 = new MarkerOptions();
 MarkerOptions marker126 = new MarkerOptions();
 MarkerOptions marker127 = new MarkerOptions();
 MarkerOptions marker128 = new MarkerOptions();
 MarkerOptions marker129 = new MarkerOptions();
 MarkerOptions marker130 = new MarkerOptions();
 MarkerOptions marker131 = new MarkerOptions();
 MarkerOptions marker132 = new MarkerOptions();
 MarkerOptions marker133 = new MarkerOptions();
 MarkerOptions marker134 = new MarkerOptions();
 MarkerOptions marker135 = new MarkerOptions();
 MarkerOptions marker136 = new MarkerOptions();
 MarkerOptions marker137 = new MarkerOptions();
 MarkerOptions marker138 = new MarkerOptions();
 MarkerOptions marker139 = new MarkerOptions();
 MarkerOptions marker140 = new MarkerOptions();
 MarkerOptions marker141 = new MarkerOptions();
 MarkerOptions marker142 = new MarkerOptions();
 MarkerOptions marker143 = new MarkerOptions();
 MarkerOptions marker144 = new MarkerOptions();
 MarkerOptions marker145 = new MarkerOptions();
 MarkerOptions marker146 = new MarkerOptions();
 MarkerOptions marker147 = new MarkerOptions();
 MarkerOptions marker148 = new MarkerOptions();
 MarkerOptions marker149 = new MarkerOptions();
 MarkerOptions marker150 = new MarkerOptions();
 MarkerOptions marker151 = new MarkerOptions();
 MarkerOptions marker152 = new MarkerOptions();
 MarkerOptions marker153 = new MarkerOptions();
 MarkerOptions marker154 = new MarkerOptions();
 MarkerOptions marker155 = new MarkerOptions();
 MarkerOptions marker156 = new MarkerOptions();
 MarkerOptions marker157 = new MarkerOptions();
 MarkerOptions marker158 = new MarkerOptions();
 MarkerOptions marker159 = new MarkerOptions();
 MarkerOptions marker160 = new MarkerOptions();
 MarkerOptions marker161 = new MarkerOptions();
 MarkerOptions marker162 = new MarkerOptions();
 MarkerOptions marker163 = new MarkerOptions();
 MarkerOptions marker164 = new MarkerOptions();
 MarkerOptions marker165 = new MarkerOptions();
 MarkerOptions marker166 = new MarkerOptions();
 MarkerOptions marker167 = new MarkerOptions();
 MarkerOptions marker168 = new MarkerOptions();
 MarkerOptions marker169 = new MarkerOptions();
 MarkerOptions marker170 = new MarkerOptions();
 MarkerOptions marker171 = new MarkerOptions();
 MarkerOptions marker172 = new MarkerOptions();
 MarkerOptions marker173 = new MarkerOptions();
 MarkerOptions marker174 = new MarkerOptions();
 MarkerOptions marker175 = new MarkerOptions();
 MarkerOptions marker176 = new MarkerOptions();
 MarkerOptions marker177 = new MarkerOptions();
 MarkerOptions marker178 = new MarkerOptions();
 MarkerOptions marker179 = new MarkerOptions();
 MarkerOptions marker180 = new MarkerOptions();
 MarkerOptions marker181 = new MarkerOptions();
 MarkerOptions marker182 = new MarkerOptions();
 MarkerOptions marker183 = new MarkerOptions();
 MarkerOptions marker184 = new MarkerOptions();
 MarkerOptions marker185 = new MarkerOptions();
 MarkerOptions marker186 = new MarkerOptions();
 MarkerOptions marker187 = new MarkerOptions();
 MarkerOptions marker188 = new MarkerOptions();
 MarkerOptions marker189 = new MarkerOptions();
 MarkerOptions marker190 = new MarkerOptions();
 MarkerOptions marker191 = new MarkerOptions();
 MarkerOptions marker192 = new MarkerOptions();
 MarkerOptions marker193 = new MarkerOptions();
 MarkerOptions marker194 = new MarkerOptions();
 MarkerOptions marker195 = new MarkerOptions();
 MarkerOptions marker196 = new MarkerOptions();
 MarkerOptions marker197 = new MarkerOptions();
 MarkerOptions marker198 = new MarkerOptions();
 MarkerOptions marker199 = new MarkerOptions();
 MarkerOptions marker200 = new MarkerOptions();
 MarkerOptions marker201 = new MarkerOptions();
 MarkerOptions marker202 = new MarkerOptions();
 MarkerOptions marker203 = new MarkerOptions();
 MarkerOptions marker204 = new MarkerOptions();
 MarkerOptions marker205 = new MarkerOptions();
 MarkerOptions marker206 = new MarkerOptions();
 MarkerOptions marker207 = new MarkerOptions();
 MarkerOptions marker208 = new MarkerOptions();
 MarkerOptions marker209 = new MarkerOptions();
 MarkerOptions marker210 = new MarkerOptions();
 MarkerOptions marker211 = new MarkerOptions();
 MarkerOptions marker212 = new MarkerOptions();
 MarkerOptions marker213 = new MarkerOptions();
 MarkerOptions marker214 = new MarkerOptions();
 MarkerOptions marker215 = new MarkerOptions();
 MarkerOptions marker216 = new MarkerOptions();
 MarkerOptions marker217 = new MarkerOptions();
 MarkerOptions marker218 = new MarkerOptions();
 MarkerOptions marker219 = new MarkerOptions();
 MarkerOptions marker220 = new MarkerOptions();
 MarkerOptions marker221 = new MarkerOptions();
 MarkerOptions marker222 = new MarkerOptions();
 MarkerOptions marker223 = new MarkerOptions();
 MarkerOptions marker224 = new MarkerOptions();
 MarkerOptions marker225 = new MarkerOptions();
 MarkerOptions marker226 = new MarkerOptions();
 MarkerOptions marker227 = new MarkerOptions();
 MarkerOptions marker228 = new MarkerOptions();
 MarkerOptions marker229 = new MarkerOptions();
 MarkerOptions marker230 = new MarkerOptions();
 MarkerOptions marker231 = new MarkerOptions();
 MarkerOptions marker232 = new MarkerOptions();
 MarkerOptions marker233 = new MarkerOptions();
 MarkerOptions marker234 = new MarkerOptions();
 MarkerOptions marker235 = new MarkerOptions();
 MarkerOptions marker236 = new MarkerOptions();


 marker1
 .position(new LatLng(36.632491, 127.452976));

 marker2
 .position(new LatLng(36.632444, 127.453181));

 marker3
 .position(new LatLng(36.630657, 127.455447));

 marker4
 .position(new LatLng(36.631865, 127.452996));

 marker5
 .position(new LatLng(36.632145, 127.453725))
 .title("법학전문대학원"); // 타이틀.
 marker6
 .position(new LatLng(36.630887, 127.454566));
 marker7
 .position(new LatLng( 36.631489, 127.454605));
 marker8
 .position(new LatLng( 36.631591, 127.454615));
 marker9
 .position(new LatLng(36.631380, 127.454790));
 marker10
 .position(new LatLng(36.630668, 127.454820));
 marker11
 .position(new LatLng(36.632202, 127.455297));

 marker12
 .position(new LatLng( 36.632225, 127.455532));
 marker13
 .position(new LatLng(36.632139, 127.455629));
 marker14
 .position(new LatLng(36.632366, 127.455941));

 marker15
 .position(new LatLng(36.632186, 127.455873));

 marker16
 .position(new LatLng(36.632476, 127.455512));

 marker17
 .position(new LatLng(36.632812, 127.455951))
 .title("형설관");

 marker18
 .position(new LatLng(36.632499, 127.455190))
 .title("산학협력관");

 marker19
 .position(new LatLng( 36.629752, 127.453922));

 marker20
 .position(new LatLng( 36.630386, 127.454810));


 marker21
 .position(new LatLng( 36.630128, 127.454732))
 .title("대학본부");
 marker22
 .position(new LatLng( 36.629008, 127.454927));

 marker23
 .position(new LatLng( 36.629149, 127.455073));

 marker24
 .position(new LatLng( 36.629298, 127.455210))
 .title("대학본부");

 marker25
 .position(new LatLng( 36.630026, 127.456136));

 marker26
 .position(new LatLng( 36.630746, 127.456751));

 marker27
 .position(new LatLng( 36.630934, 127.456429))
 .title("N14");

 marker28
 .position(new LatLng( 36.630550, 127.457073))
 .title("N16-2");

 marker29
 .position(new LatLng( 36.631127, 127.457401));

 marker30
 .position(new LatLng( 36.631247, 127.457282));

 marker31
 .position(new LatLng( 36.631199, 127.457501));

 marker32
 .position(new LatLng( 36.631016, 127.457729));

 marker33
 .position(new LatLng( 36.631382, 127.457719));

 marker34
 .position(new LatLng( 36.630606, 127.457474));

 marker35
 .position(new LatLng( 36.630686, 127.458189));

 marker36
 .position(new LatLng( 36.630247, 127.457424));

 marker37
 .position(new LatLng( 36.629952, 127.457505));

 marker38
 .position(new LatLng( 36.629420, 127.457104));

 marker39
 .position(new LatLng( 36.629342, 127.457241));

 marker40
 .position(new LatLng( 36.629491, 127.456929));

 marker41
 .position(new LatLng( 36.631118, 127.457116 ));

 marker42
 .position(new LatLng( 36.629838, 127.457293 ))
 .title("N13");

 marker44
 .position(new LatLng( 36.629265, 127.457344 ));

 marker45
 .position(new LatLng( 36.628780, 127.457558 ));

 marker46
 .position(new LatLng( 36.629034, 127.457161 ));

 marker47
 .position(new LatLng( 36.628901, 127.457362 ));

 marker48
 .position(new LatLng( 36.628476, 127.456682 ));

 marker49
 .position(new LatLng( 36.628121, 127.456343 ));

 marker50
 .position(new LatLng( 36.628470, 127.455587 ));

 marker51
 .position(new LatLng( 36.627891, 127.457027 ));

 marker52
 .position(new LatLng( 36.627882, 127.457661 ));

 marker53
 .position(new LatLng( 36.628158, 127.457237 ))
 .title("N13");

 marker54
 .position(new LatLng( 36.628182, 127.457902 ));

 marker55
 .position(new LatLng( 36.627736, 127.457963 ));

 marker56
 .position(new LatLng( 36.627979, 127.458195 ));

 marker57
 .position(new LatLng( 36.628143, 127.458435 ));

 marker58
 .position(new LatLng( 36.628522, 127.458607 ));

 marker59
 .position(new LatLng( 36.629069, 127.458944 ));

 marker60
 .position(new LatLng( 36.629284, 127.459199 ));

 marker61
 .position(new LatLng( 36.629448, 127.459607 ));

 marker62
 .position(new LatLng( 36.629426, 127.459832 ));

 marker63
 .position(new LatLng( 36.629474, 127.460030));

 marker64
 .position(new LatLng( 36.629512, 127.460288));

 marker65
 .position(new LatLng( 36.629676, 127.459757 ));

 marker66
 .position(new LatLng( 36.629749, 127.460776 ));

 marker67
 .position(new LatLng( 36.630167, 127.460889 ));

 marker68
 .position(new LatLng( 36.630111, 127.461210 ));

 marker69
 .position(new LatLng( 36.630330, 127.461286 ));

 marker70
 .position(new LatLng( 36.630464, 127.460026 ))
 .title("N19");

 marker71
 .position(new LatLng( 36.630348, 127.460020 ));

 marker72
 .position(new LatLng( 36.630373, 127.459853 ));

 marker73
 .position(new LatLng( 36.630447, 127.459685 ));

 marker74
 .position(new LatLng( 36.630533, 127.459203 ));

 marker75
 .position(new LatLng( 36.630391, 127.458282 ))
 .title("N16-1");

 marker76
 .position(new LatLng( 36.630481, 127.458387 ));

 marker77
 .position(new LatLng( 36.630628, 127.458462 ))
 .title("N16-3");

 marker78
 .position(new LatLng( 36.630675, 127.458119 ));

 marker79
 .position(new LatLng( 36.629000, 127.457802 ));

 marker80
 .position(new LatLng( 36.629265, 127.457344 ));

 /* 여기부터 E구역역*/

// marker100
// .position(new LatLng( 36.628499, 127.458642 ));
// marker101
// .position(new LatLng( 36.628151, 127.458471 ));
// marker102
// .position(new LatLng( 36.627893, 127.458229 ));
 marker103
 .position(new LatLng( 36.627467, 127.457848 ));
 marker104
 .position(new LatLng( 36.628657, 127.459632 ));
 marker105
 .position(new LatLng( 36.628208, 127.459459 ))
 .title("E2");
 marker106
 .position(new LatLng( 36.627877, 127.459051 ));
 marker107
 .position(new LatLng( 36.629265, 127.457344 ));
 marker108
 .position(new LatLng( 36.627563, 127.458817 ))
 .title("E3");
 marker109
 .position(new LatLng( 36.627188, 127.458956 ));
 marker110
 .position(new LatLng( 36.626957, 127.458793 ));
 marker111
 .position(new LatLng( 36.629001, 127.460816 ))
 .title("E1-1");
 marker112
 .position(new LatLng( 36.628535, 127.460304 ))
 .title("E1-2");
 marker113
 .position(new LatLng( 36.627970, 127.460219 ));
 marker114
 .position(new LatLng( 36.627695, 127.459953 ));
 marker115
 .position(new LatLng( 36.627398, 127.459699 ));
 marker116
 .position(new LatLng( 36.627603, 127.459334 ));
 marker117
 .position(new LatLng( 36.627487, 127.459534 ));
 marker118
 .position(new LatLng( 36.627827, 127.459804 ));
 marker119
 .position(new LatLng( 36.627150, 127.459269 ))
 .title("E3-1");
 marker120
 .position(new LatLng( 36.626740, 127.459101 ));
 marker121
 .position(new LatLng( 36.626473, 127.459550 ));
 marker122
 .position(new LatLng( 36.626112, 127.460075 ));
 marker123
 .position(new LatLng( 36.625778, 127.460678 ));
 marker124
 .position(new LatLng( 36.625628, 127.460507 ));
 marker125
 .position(new LatLng( 36.625577, 127.460222 ))
 .title("E7-3");
 marker126
 .position(new LatLng( 36.625500, 127.460771 ));
 marker127
 .position(new LatLng( 36.625349, 127.460649 ));
 marker128
 .position(new LatLng( 36.625038, 127.460999 ))
 .title("E7-1");
 marker129
 .position(new LatLng( 36.625172, 127.460510 ));
 marker130
 .position(new LatLng( 36.624645, 127.460701 ))
 .title("E7-2");
 marker131
 .position(new LatLng( 36.626796, 127.459023 ));
 marker132
 .position(new LatLng( 36.626233, 127.459327 ));
 marker133
 .position(new LatLng( 36.626283, 127.459025 ))
 .title("E8-3");
 marker134
 .position(new LatLng( 36.625419, 127.458613 ));
 marker135
 .position(new LatLng( 36.625390, 127.458974 ))
 .title("E8-5");
 marker136
 .position(new LatLng( 36.625035, 127.458838 ))
 .title("E8-6");
 marker137
 .position(new LatLng( 36.625166, 127.459256 ))
 .title("E8-9");
 marker138
 .position(new LatLng( 36.625716, 127.459702 ));
 marker139
 .position(new LatLng( 36.624591, 127.459919 ));
 marker140
 .position(new LatLng( 36.623821, 127.459151 ))
 .title("E8-11");
 marker141
 .position(new LatLng( 36.624553, 127.459260 ))
 .title("E8-8");
 marker142
 .position(new LatLng( 36.625399, 127.458288 ))
 .title("E8-7");
 marker143
 .position(new LatLng( 36.624967, 127.458211 ));
 marker144
 .position(new LatLng( 36.624585, 127.458907 ));
 marker145
 .position(new LatLng( 36.624509, 127.458514 ));
 marker146
 .position(new LatLng( 36.624641, 127.457908 ));
 marker147
 .position(new LatLng( 36.624761, 127.457720 ))
 .title("E10");
 marker148
 .position(new LatLng( 36.624494, 127.457774 ));
 marker149
 .position(new LatLng( 36.624290, 127.458124 ));
 marker150
 .position(new LatLng( 36.624094, 127.458066 ))
 .title("E8-10");
 marker151
 .position(new LatLng( 36.624414, 127.457687 ));
 marker152
 .position(new LatLng( 36.625064, 127.456573 ));
 marker153
 .position(new LatLng( 36.625240, 127.456707 ));
 marker154
 .position(new LatLng( 36.625456, 127.456901 ));
 marker155
 .position(new LatLng( 36.625273, 127.457239 ));
 marker156
 .position(new LatLng( 36.625090, 127.457195 ))
 .title("E9");
 marker157
 .position(new LatLng( 36.625096, 127.457555 ));
 marker158
 .position(new LatLng( 36.627000, 127.457360 ));
 marker159
 .position(new LatLng( 36.626795, 127.457334 ));
 marker160
 .position(new LatLng( 36.626253, 127.457280 ));
 marker161
 .position(new LatLng( 36.626341, 127.457688 ))
 .title("E8-2");
 marker162
 .position(new LatLng( 36.626659, 127.458358 ))
 .title("E8-1");
 marker163
 .position(new LatLng( 36.625940, 127.457158 ));

 /* 여기부터 S구역 */
 marker164
 .position(new LatLng( 36.629438, 127.453743 ));
 marker165
 .position(new LatLng( 36.629636, 127.453438 ));
 marker166
 .position(new LatLng( 36.629756, 127.453202 ))
 .title("E9");
 marker167
 .position(new LatLng( 36.629950, 127.453341 ))
 .title("S21-5");
 marker168
 .position(new LatLng( 36.629446, 127.452542 ))
 .title("S21-4");
 marker169
 .position(new LatLng( 36.628852, 127.453395 ));
 marker170
 .position(new LatLng( 36.629098, 127.452925 ));
 marker171
 .position(new LatLng( 36.629202, 127.452619 ));
 marker172
 .position(new LatLng( 36.629552, 127.452074 ));
 marker173
 .position(new LatLng( 36.629521, 127.451509 ))
 .title("S21-3");
 marker174
 .position(new LatLng( 36.629026, 127.452502 ))
 .title("S20");
 marker175
 .position(new LatLng( 36.628895, 127.452812 ));
 marker176
 .position(new LatLng( 36.628882, 127.452978 ))
 .title("S21-1");
 marker177
 .position(new LatLng( 36.628233, 127.453004 ));
 marker178
 .position(new LatLng( 36.628326, 127.452386 ));
 marker179
 .position(new LatLng( 36.628560, 127.451859 ));
 marker180
 .position(new LatLng( 36.628678, 127.451947 ));
 marker181
 .position(new LatLng( 36.628717, 127.451783 ))
 .title("S19");
 marker182
 .position(new LatLng( 36.628502, 127.451396 ))
 .title("S18");
 marker183
 .position(new LatLng( 36.628989, 127.451466 ))
 .title("S21-2");
 marker184
 .position(new LatLng( 36.628048, 127.452134 ));
 marker185
 .position(new LatLng( 36.627984, 127.452407 ))
 .title("S17-1 지선관");
 marker186
 .position(new LatLng( 36.627638, 127.451810 ));
 marker187
 .position(new LatLng( 36.627975, 127.452935 ));
 marker188
 .position(new LatLng( 36.627810, 127.452705 ));
 marker189
 .position(new LatLng( 36.627478, 127.452897 ))
 .title("S17-2 명덕관");
 marker190
 .position(new LatLng( 36.627398, 127.452290 ));
 marker191
 .position(new LatLng( 36.627642, 127.452998 ));
 marker192
 .position(new LatLng( 36.627411, 127.453717 ));
 marker193
 .position(new LatLng( 36.627512, 127.454552 ));
 marker194
 .position(new LatLng( 36.627734, 127.454859 ));
 marker195
 .position(new LatLng( 36.627754, 127.454334 ));
 marker196
 .position(new LatLng( 36.627691, 127.453934 ));
 marker197
 .position(new LatLng( 36.627973, 127.454291 ))
 .title("S14");
 marker198
 .position(new LatLng( 36.627659, 127.455302 ))
 .title("S9");
 marker199
 .position(new LatLng( 36.627466, 127.455132 ));
 marker200
 .position(new LatLng( 36.626573, 127.454113 ));
 marker201
 .position(new LatLng( 36.626850, 127.453906 ))
 .title("S8");
 marker202
 .position(new LatLng( 36.626145, 127.454281 ));
 marker203
 .position(new LatLng( 36.626260, 127.454507 ))
 .title("S3");
 marker204
 .position(new LatLng( 36.626039, 127.454984 ));
 marker205
 .position(new LatLng( 36.625617, 127.454406 ))
 .title("S4-1");
 marker206
 .position(new LatLng( 36.625752, 127.454946 ));
 marker207
 .position(new LatLng( 36.625203, 127.454844 ))
 .title("S4-2");
 marker208
 .position(new LatLng( 36.625408, 127.454938 ));
 marker209
 .position(new LatLng( 36.625378, 127.454413 ));
 marker210
 .position(new LatLng( 36.625008, 127.454436 ));
 marker211
 .position(new LatLng( 36.625023, 127.454862 ));
 marker212
 .position(new LatLng( 36.626364, 127.455449 ))
 .title("S2");
 marker213
 .position(new LatLng( 36.626037, 127.455426 ));
 marker214
 .position(new LatLng( 36.626064, 127.456111 ));
 marker215
 .position(new LatLng( 36.625770, 127.455449 ));
 marker216
 .position(new LatLng( 36.626034, 127.456309 ));
 marker217
 .position(new LatLng( 36.625729, 127.456275 ));
 marker218
 .position(new LatLng( 36.625579, 127.455895 ))
 .title("S1-5");
 marker219
 .position(new LatLng( 36.626217, 127.456556 ))
 .title("S1-4");
 marker220
 .position(new LatLng( 36.626663, 127.456784 ))
 .title("S1-3");
 marker221
 .position(new LatLng( 36.626909, 127.457084 ))
 .title("S1-7");
 marker222
 .position(new LatLng( 36.626964, 127.456872 ));
 marker223
 .position(new LatLng( 36.627104, 127.456859 ))
 .title("S1-2");
 marker224
 .position(new LatLng( 36.626924, 127.456269 ));
 marker225
 .position(new LatLng( 36.627358, 127.456043 ));
 marker226
 .position(new LatLng( 36.627661, 127.456018 ));
 marker227
 .position(new LatLng( 36.627751, 127.456643 ))
 .title("S1-1");
 marker228
 .position(new LatLng( 36.627962, 127.455474 ))
 .title("S10");
 marker229
 .position(new LatLng( 36.627824, 127.455527 ))
 .title("S11");
 marker230
 .position(new LatLng( 36.625413, 127.456224 ));
 marker231
 .position(new LatLng( 36.625411, 127.455737 ));
 marker232
 .position(new LatLng( 36.625070, 127.455946 ))
 .title("S1-6");

 marker233
 .position(new LatLng( 36.624726, 127.456292 ));

 marker234
 .position(new LatLng( 36.627225, 127.452173 ))
 .title("S7-3 신민관");
 marker235
 .position(new LatLng( 36.627288, 127.460569 ))
 .title("E4-1 실내체육관");
 marker236
 .position(new LatLng( 36.625251, 127.460865 ));





        LatLng[] point_new = new LatLng[80];

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

        for (int i = top; i > -1; i--) {
            int num = stack[i];

            MarkerOptions mOptions = new MarkerOptions();
            if(num==0)
                break;
            if(i==top) {
                BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.start_flag);
                Bitmap b=bitmapdraw.getBitmap();
                Bitmap startmarker = Bitmap.createScaledBitmap(b, 200, 200, false);
                mOptions.position(point_new[num-1]);
                mOptions.icon(BitmapDescriptorFactory.fromBitmap(startmarker));
                mGoogleMap.addMarker(mOptions);
            }
            else if(i==0) {
                BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.finish_flag);
                Bitmap b=bitmapdraw.getBitmap();
                Bitmap finishmarker = Bitmap.createScaledBitmap(b, 200, 200, false);
                mOptions.position(point_new[num-1]);
                mOptions.icon(BitmapDescriptorFactory.fromBitmap(finishmarker));
                mGoogleMap.addMarker(mOptions);
            }
            else {
                BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.marker_circle);
                Bitmap b=bitmapdraw.getBitmap();
                Bitmap marker = Bitmap.createScaledBitmap(b, 20, 20, false);
                mOptions.position(point_new[num-1]);
                mOptions.icon(BitmapDescriptorFactory.fromBitmap(marker));
                mGoogleMap.addMarker(mOptions);
            }

        }


        for (int i = top; i > -1; i--)  {
            int k= stack[i];
            if(i-1<0 || stack[i]==0)
                break;
            int t= stack[i-1];
            if((t==0 && k==1)||(t==1 && k==0)||(t==6 && k==8)||(t==8 && k==6)||(t==10 && k==11)||(t==11 && k==10)||(t==58 && k==59)||(t==59 && k==58)||(t==59 && k==60)||(t==60 && k==59)||(t==60 && k==61)||(t==61 && k==60)||(t==61 && k==62)||(t==62 && k==61)||(t==62 && k==63)||(t==63 && k==62)||(t==63 && k==65)||(t==65 && k==63)||(t==60 && k==64)||(t==64 && k==60)||(t==64 && k==71)||(t==71 && k==64) )
                mGoogleMap.addPolyline(new PolylineOptions().add(point_new[k-1], point_new[t-1]).width(10).color(Color.YELLOW));
            else if ((t==5 && k==8)||(t==8 && k==5))
                mGoogleMap.addPolyline(new PolylineOptions().add(point_new[k-1], point_new[t-1]).width(10).color(Color.RED));
            else {

                if(t==0)
                    continue;
                mGoogleMap.addPolyline(new PolylineOptions().add(point_new[k - 1], point_new[t - 1]).width(10).color(Color.BLUE));
            }
        }

    }



// @Override
// protected void onPostCreate(Bundle savedInstanceState) {
// super.onPostCreate(savedInstanceState);
// // Sync the toggle state after onRestoreInstanceState has occurred.
// drawerToggle.syncState();
// }

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



        mCurrentLocation = location;


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

    private void showDialogForPermissionSetting(String msg) {

        AlertDialog.Builder builder = new AlertDialog.Builder(FindLoadDetailActivity.this);
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
    public void setDefaultLocation() {

        mMoveMapByUser = false;


//디폴트 위치, Seoul
        LatLng DEFAULT_LOCATION = new LatLng(36.629038,127.456347);
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

        AlertDialog.Builder builder = new AlertDialog.Builder(FindLoadDetailActivity.this);
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
    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(FindLoadDetailActivity.this);
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

    public void init(int datal[][]) {
        data = datal;
        n = data.length;

        dis = new int[n];
        visit = new boolean[n];
        prev = new int[n];
        stack = new int[n];
        stackV = new Vector<Integer>();
    }

    public int theLeastDistance() {
        return dis[e - 1];

    }

    public void start(int start, int end) {
        System.out.println("=====================");
        System.out.println("다익스트라 시작");
        System.out.println("startpoint:" + start);
        System.out.println("endpoint:" + end);
        System.out.println("=====================");
        s = start;
        e = end;

        int k = 0;
        int min = 0;

        for (int i = 0; i < n; i++) {
            dis[i] = m;
            prev[i] = 0;
            visit[i] = false;
        }
        dis[s - 1] = 0; /* 시작점의 거리는 0 */

        for (int i = 0; i < n; i++) {
            min = m;
            for (int j = 0; j < n; j++) { /* 정점의 수만큼 반복 */
                if (visit[j] == false && dis[j] < min) { /* 확인하지 않고 거리가 짧은 정점을 찾음 */
                    k = j;
                    min = dis[j];
                }
            }
            visit[k] = true; /* 해당 정점 확인 체크 */

            if (min == m) break; /* 연결된 곳이 없으면 종료 */

/****
 * * I -> J 보다 I -> K -> J의
 * * 거리가 더 작으면
 * * 갱신
 ****/
            for (int j = 0; j < n; j++) {
                if (dis[k] + data[k][j] < dis[j]) {
                    dis[j] = dis[k] + data[k][j]; /* 최단거리 저장*/
                    prev[j] = k; /* J로 가기 위해서는 K를 거쳐야 함 */
                }
            }
        }
        nowLeastDistance(); //콘솔에서 최단거리 출력
        inverseFind(); // 콘솔에서 최단 경로 출력
    }

    public void nowLeastDistance() {
        System.out.printf("최단거리: %10d ", dis[e-1] );






    }

    public void inverseFind() {
        int tmp = 0;

        tmp = e -1;
        while (true) {
            stack[++top] = tmp + 1;
            if (tmp == s - 1) break;
            tmp = prev[tmp];
        }



        stackV.removeAllElements();
        for (int i = top; i > -1; i--) {
            System.out.printf("%d", stack[i]);
            stackV.add(stack[i]);
            if (i != 0) System.out.printf(" -> ");
        }

    }

    public Vector<Integer> getStack() {
        return stackV;
    }

}