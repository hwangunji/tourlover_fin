package com.example.googlemap;

import android.app.Activity;

public class FileSplit extends Activity {

    public static String structInfo[][] = new String[31][12];

    public FileSplit(String str){
        String tmp[] = str.split("\n");
        String s;
        char ch;
        for(int i=0; i<tmp.length; i++){
            s=tmp[i];
            String tmp2[] = s.split(":");
            for(int j=0;j<12;j++){
                tmp2[j]=tmp2[j].trim(); //trim메소드를 사용하여 빈 공간을 없앤다.
                structInfo[i][j]=tmp2[j];

                //제대로 분할되었는지 확인 함.
                //Log.d(this.getClass().getName(), structInfo[i][j]);
            }
        }
    }
}

