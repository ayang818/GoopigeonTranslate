package com.example.finalwork;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import MyFragment.HistoryFragment;
import MyFragment.IndexFragment;
import MyFragment.IntroduceFragment;
import dto.TranslatePostDTO;
import dto.TranslateResultDTO;
import provider.TranslateProvider;

public class MainActivity extends FragmentActivity {

    private ViewPager mViewPager;
    private TextView mTextMessage;
    private FragmentPagerAdapter mAdapter;
    private List<Fragment> mFragments;
    private BottomNavigationView mbuttomNavView;
    private static int REQUEST_PERMISSION_CODE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    public String CREATE_PATH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
        CREATE_PATH = getApplicationContext().getFilesDir().getAbsolutePath() + "/data.txt";
        //初始化控件
        initViews();
        // 初始化导航
        initBottonNavView();
        //初始化PageAdapter
        initAdapter();

    }

    // 在Fragment中调用这个方法
    public String getPath(){
        return CREATE_PATH;
    }
    //初始化控件
    private void initViews() {
        mViewPager = (ViewPager) findViewById(R.id.main_container);
        mbuttomNavView = findViewById(R.id.navigation);
    }

    //初始化PageAdapter
    private void initAdapter() {
        //将四个Fragment加入集合中
        mFragments = new ArrayList<>();
        mFragments.add(new IndexFragment());
        mFragments.add(new HistoryFragment());
        mFragments.add(new IntroduceFragment());

        //初始化适配器
        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return mFragments.get(i);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }
        };
        //设置ViewPager的适配器 切换监听
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                //设置position对应的集合中的Fragment
                mbuttomNavView.getMenu().getItem(i).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    //初始化BottomNavigation
    private void initBottonNavView() {
        mbuttomNavView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_home:
                        mViewPager.setCurrentItem(0);
                        return true;
                    case R.id.navigation_dashboard:
                        mViewPager.setCurrentItem(1);
                        return true;
                    case R.id.navigation_notifications:
                        mViewPager.setCurrentItem(2);
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    // 翻译界面点击按钮
    public void onclick(View v) {

        mTextMessage = (TextView) findViewById(R.id.message);
        TranslatePostDTO translatePostDTO = new TranslatePostDTO();
        String inputString = "";
        EditText editText1 = (EditText) findViewById(R.id.inputWord);
        inputString = editText1.getText().toString();
        translatePostDTO.setQ(inputString);
        Spinner spinner;
        spinner = (Spinner) findViewById(R.id.spinner1);
        String carnumber = spinner.getSelectedItem().toString();
        // 下面开始填充tranlatePostDTO的属性
        translatePostDTO.setFrom("auto");
        if ("中文".equals(carnumber)) {
            translatePostDTO.setTo("zh");
        }
        if ("英语".equals(carnumber)) {
            translatePostDTO.setTo("en");
        }
        if ("文言文".equals(carnumber)) {
            translatePostDTO.setTo("wyw");
        }
        if ("日语".equals(carnumber)) {
            translatePostDTO.setTo("jp");
        }
        if ("法语".equals(carnumber)) {
            translatePostDTO.setTo("fra");
        }
        if ("德语".equals(carnumber)) {
            translatePostDTO.setTo("de");
        }
        translatePostDTO.setAppid("20190606000305552");
        double d = Math.random() * 10000;
        int tempSalt = (int) d;
        translatePostDTO.setSalt(Integer.toString(tempSalt));
        String tempSign = new String("20190606000305552" + inputString + Integer.toString(tempSalt) +
                "K2ACZZtjVUHaHIac4oDP");
        // md5加密签名
        MessageDigest message = null;
        try {
            message = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            message.update(tempSign.getBytes("UTF8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte s[] = message.digest();
        String sign = "";
        for (int i = 0; i < s.length; i++) {
            sign += Integer.toHexString((0x000000ff & s[i]) | 0xffffff00).substring(6);
        }
        Handler mainHandler = new Handler();
        translatePostDTO.setSign(sign);
        TranslateProvider translateProvider = new TranslateProvider();
        TranslateResultDTO translateResult = new TranslateResultDTO();
        // 创建子线程进行http请求
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    TranslateProvider translateProvider = new TranslateProvider();
                    // 将url参数传入
                    TranslateResultDTO translateResult = translateProvider.getTranslateResuly(translatePostDTO);
                    // 在子线程中更新主线程的视图
                    Handler mainHandler = new Handler(Looper.getMainLooper());
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String[] ans = translateResult.getTrans_result();
                                System.out.println(translateResult.getTrans_result());
                                String inputData = translateResult.getTrans_result()[0].split(",")[1].split(":")[1];
                                inputData = inputData.substring(0, inputData.length() - 2);
                                String outputData = translateResult.getTrans_result()[0].split(",")[0].split(":")[1];
                                String text = inputData + "\"" + "  :  " + outputData;
                                System.out.println(CREATE_PATH);
                                mTextMessage.setText(text);
                                bufferSave(CREATE_PATH, text);
                            } catch (Exception e) {
                                System.out.println("读文件报错了");
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public static void bufferSave(String filename, String msg) {
        try {
            BufferedWriter bfw = new BufferedWriter(new FileWriter(filename, true));
            bfw.write(msg);
            bfw.newLine();
            bfw.flush();
            bfw.close();
            System.out.println("写入成功");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 申请文件读取权限
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                Log.i("MainActivity", "申请的权限为：" + permissions[i] + ",申请结果：" + grantResults[i]);
            }
        }
    }
}
