package com.ooo.deemo.mymusicplayer;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.jaeger.library.StatusBarUtil;
import com.mxn.soul.flowingdrawer_core.ElasticDrawer;
import com.mxn.soul.flowingdrawer_core.FlowingDrawer;
import com.ooo.deemo.mymusicplayer.Utils.MUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class FirstActivity extends MyActivity implements View.OnClickListener {

    private String TAG = "FirstActivity";

    private String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private AlertDialog dialog;

    private static boolean mBackKeyPressed = false;

    private RecyclerView rv_log ;

   private MusicListAdapter rAdapter;

private static List<Song> tl_List;

private List<Song> searchList = new ArrayList<>();

private Button pre_bt ;

public static Button play_bt;

private Button next_bt;

private EditText search_edit;

private ImageButton search_bt;

private ImageButton flow_bt;

    private FlowingDrawer mDrawer;


    //

    private static String currentSong;

    private static String currentSinger;

    private static int currentTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_first);

        setStatusBar();
        hideBottomUIMenu();

initView();


search_edit.addTextChangedListener(new TextWatcher() {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
if(s.toString().length()!=0) {


    searchMusic(s.toString());
    Log.e(TAG, "afterTextChanged:" + s.toString());

    rAdapter = new MusicListAdapter(searchList);
    rv_log.setAdapter(rAdapter);
    rAdapter.notifyDataSetChanged();
}else {
    search_edit.setCursorVisible(false);


    rAdapter = new MusicListAdapter(tl_List);
    rv_log.setAdapter(rAdapter);
    rAdapter.notifyDataSetChanged();

}

    }
});

//侧边栏flower

        mDrawer.setOnDrawerStateChangeListener(new ElasticDrawer.OnDrawerStateChangeListener() {
            @Override
            public void onDrawerStateChange(int oldState, int newState) {
                if (newState == ElasticDrawer.STATE_CLOSED) {
                    Log.e("FirstActivity", "Drawer STATE_CLOSED");
                }
            }
            @Override
            public void onDrawerSlide(float openRatio, int offsetPixels) {

            }
        });

    }













    //隐藏底部栏
    protected void hideBottomUIMenu() {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    //状态栏透明
    protected void setStatusBar() {
        StatusBarUtil.setTransparent(this);
    }

//双击返回键退出程序
    @Override
    public void onBackPressed() {


        Log.e(TAG,"onBackPressed");
        search_edit.setCursorVisible(false);

        if(!mBackKeyPressed){

            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();

            mBackKeyPressed = true;

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    mBackKeyPressed = false;
                }
            },2000);
        }else {

            this.finish();
            System.exit(0);
        }
    }

    //注册控件
private void initView(){

    tl_List = new ArrayList<>();
    tl_List = MUtils.getmusic(this);
    rv_log =findViewById(R.id.rv_log);
    LinearLayoutManager layoutManager = new LinearLayoutManager(this);
    rv_log.setLayoutManager(layoutManager);
    rv_log.setFocusableInTouchMode(true);
    rAdapter = new MusicListAdapter(tl_List);
    rv_log.setAdapter(rAdapter);

    mDrawer = findViewById(R.id.drawerlayout);
    mDrawer.setTouchMode(ElasticDrawer.TOUCH_MODE_BEZEL);


    search_edit = findViewById(R.id.search_edit);

    search_bt = findViewById(R.id.search_bt);

play_bt = findViewById(R.id.play_bt);

pre_bt = findViewById(R.id.pre_bt);

next_bt = findViewById(R.id.next_bt);

flow_bt = findViewById(R.id.flow_bt);

play_bt.setOnClickListener(this);

pre_bt.setOnClickListener(this);

next_bt.setOnClickListener(this);

search_bt.setOnClickListener(this);

search_edit.setOnClickListener(this);

flow_bt.setOnClickListener(this);


currentSong = "当前无音乐";
currentSinger = "--";
currentTime = 0;
}



//实现点击事件
    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.play_bt:
if(MusicListAdapter.mPlayer.isPlaying()) {
    Log.e(TAG,"pause");

    play_bt.setText("PLAY");

    MusicListAdapter.mPlayer.pause();
}else {

    Log.e(TAG,"start");
    play_bt.setText("PAUSE");

    MusicListAdapter.mPlayer.start();
}
                break;



            case R.id.pre_bt:

                Log.e(TAG,"pre");
                MusicListAdapter.musicPre();

                play_bt.setText("PAUSE");
                break;


            case R.id.next_bt:
                Log.e(TAG,"next");
                MusicListAdapter.musicNext();
                play_bt.setText("PAUSE");


                break;

            case R.id.search_bt:

                if(search_edit.getText()==null){
                    search_edit.setCursorVisible(false);

                }

                search_edit.setText("");

                break;

            case R.id.search_edit:

          search_edit.setCursorVisible(true);




                break;


            case R.id.flow_bt:

                mDrawer.openMenu(true,500);


                break;
                default:


                    break;

        }
    }


/*
获取当前播放
 */

public static void getCurrentMusic(int position){

    currentSong = tl_List.get(position).song;

    currentSinger = tl_List.get(position).singer;
    currentTime = tl_List.get(position).duration;


}



    /*
    歌曲搜索功能
     */

    private void searchMusic(String s){
        int i = 0;
         i = s.length();
        searchList.clear();
        String sLength = "";
        Log.e(TAG,"String:"+s+"---s.length:"+i);
Log.e(TAG, tl_List.get(0).song);
        for (Song songitem : tl_List){
            if(songitem.song.length()>i) {
                sLength = songitem.song.substring(1, i + 1);

                if (s.toUpperCase().equals(sLength.toUpperCase())) {

                    searchList.add(songitem);

                }

            }
            if(songitem.singer.length()>i){
                sLength = songitem.singer.substring(0,i);
                if (s.toUpperCase().equals(sLength.toUpperCase())){
                    searchList.add(songitem);
                }
            }
        }

    }








}
