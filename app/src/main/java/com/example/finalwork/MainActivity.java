package com.example.finalwork;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import dto.TranslatePostDTO;
import dto.TranslateResultDTO;
import provider.TranslateProvider;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    TranslatePostDTO translatePostDTO = new TranslatePostDTO();
                    String inputString = new String("你是谁");
                    translatePostDTO.setQ(inputString);
                    translatePostDTO.setFrom("auto");
                    translatePostDTO.setTo("en");
                    translatePostDTO.setAppid("20190606000305552");
                    double d = Math.random()*10000;
                    int tempSalt = (int) d;
                    translatePostDTO.setSalt(Integer.toString(tempSalt));
                    String tempSign = new String("20190606000305552"+inputString+Integer.toString(tempSalt)+
                            "K2ACZZtjVUHaHIac4oDP");
                    // md5加密签名
                    MessageDigest message = null;
                    try {
                        message = MessageDigest.getInstance("MD5");
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                    try {
                        message.update(tempSign.getBytes("UTF8" ));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    byte s[ ]=message.digest( );
                    String sign = "";
                    for (int i=0; i<s.length;i++){
                        sign+=Integer.toHexString((0x000000ff & s[i]) | 0xffffff00).substring(6);
                    }
                    Handler mainHandler = new Handler();
                    translatePostDTO.setSign(sign);
                    TranslateProvider translateProvider = new TranslateProvider();
                    TranslateResultDTO translateResult = new TranslateResultDTO();
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try  {
                                TranslateProvider translateProvider = new TranslateProvider();
                                TranslateResultDTO translateResult = translateProvider.getTranslateResuly(translatePostDTO);
                                Handler mainHandler = new Handler(Looper.getMainLooper());
                                mainHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        String[] ans = translateResult.getTrans_result();
                                        String inputData = translateResult.getTrans_result()[0].split(",")[1].split(":")[1];
                                        inputData = inputData.substring(0, inputData.length()-2);
                                        String outputData = translateResult.getTrans_result()[0].split(",")[0].split(":")[1];
                                        mTextMessage.setText(inputData+"  翻译后是  "+outputData);
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    thread.start();
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
}
