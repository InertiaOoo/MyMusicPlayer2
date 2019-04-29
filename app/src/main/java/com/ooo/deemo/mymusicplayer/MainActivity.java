package com.ooo.deemo.mymusicplayer;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.jaeger.library.StatusBarUtil;

public class MainActivity extends MyActivity implements Runnable{
    final Handler mHandler = new Handler();

    private String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private AlertDialog dialog;

    int flag = 0;//设置一个标志，供点击“开始/暂停”按钮使用



private Button bt_go;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mHandler.postDelayed(this, 2000);




        setStatusBar();

        hideBottomUIMenu();





        bt_go = findViewById(R.id.bt_go);


        bt_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              jumpGo();
            }
        });

    }

private void jumpGo(){
    Intent intent = new Intent();
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
    intent.setClass(MainActivity.this,FirstActivity.class);
    startActivity(intent);
}

    protected void setStatusBar() {

        StatusBarUtil.setTransparent(this);
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








    /*
    获取权限
     */
    private void getPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 检查该权限是否已经获取
            for (int i = 0; i < permissions.length; i++) {
                int j = ContextCompat.checkSelfPermission(this, permissions[i]);
                // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
                if (j != PackageManager.PERMISSION_GRANTED) {
                    // 如果没有授予该权限，就去提示用户请求
                    showDialogTipUserRequestPermission(i);
                }
            }
        }
    }

    /*
          权限申请
           */
    private void showDialogTipUserRequestPermission(int i) {
        String str_1 = "";
        String str_2 = "";
        switch (i) {
            case 0:
                str_1 = "位置权限不可用";
                str_2 = "由于需要获取位置权限信息；\n否则，您将无法正常使用";
                break;
            case 1:
                str_1 = "位置权限不可用";
                str_2 = "由于需要位置权限信息；\n否则，您将无法正常使用";
                break;
            case 2:
                str_1 = "在SDCard中创建与删除文件权限不可用";
                str_2 = "在SDCard中创建与删除文件权限；\n否则，您将无法正常使用";
                break;
            case 3:
                str_1 = "往SDCard写入数据权限不可用";
                str_2 = "往SDCard写入数据权限；\n否则，您将无法正常使用";
                break;
            case 4:
                str_1 = "往SDCard读出数据权限不可用";
                str_2 = "往SDCard读出数据权限；\n否则，您将无法正常使用";
                break;

            default:
                break;
        }


        new AlertDialog.Builder(this)
                .setTitle(str_1)
                .setMessage(str_2)
                .setPositiveButton("立即开启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startRequestPermission();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).setCancelable(false).show();
    }


    // 开始提交请求权限
    private void startRequestPermission() {
        ActivityCompat.requestPermissions(this, permissions, 321);
    }

    // 用户权限 申请 的回调方法
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 321) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    // 判断用户是否 点击了不再提醒。(检测该权限是否还可以申请)
                    boolean b = shouldShowRequestPermissionRationale(permissions[0]);
                    if (!b) {
                        // 用户还是想用我的 APP 的
                        // 提示用户去应用设置界面手动开启权限
                        showDialogTipUserGoToAppSetting();
                    } else
                        finish();
                } else {
                    Log.e("","权限获取成功");
                    flag = 1;
                    jumpGo();
                }
            }
        }
    }

    // 提示用户去应用设置界面手动开启权限

    private void showDialogTipUserGoToAppSetting() {

        dialog = new AlertDialog.Builder(this)
                .setTitle("存储权限不可用")
                .setMessage("请在-应用设置-权限-中，允许支付宝使用存储权限来保存用户数据")
                .setPositiveButton("立即开启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 跳转到应用设置界面
                        goToAppSetting();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).setCancelable(false).show();
    }

    // 跳转到当前应用的设置界面
    private void goToAppSetting() {
        Intent intent = new Intent();

        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);

        startActivityForResult(intent, 123);
    }

    //
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123) {

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // 检查该权限是否已经获取
                int i = ContextCompat.checkSelfPermission(this, permissions[0]);
                // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
                if (i != PackageManager.PERMISSION_GRANTED) {
                    // 提示用户应该去应用设置界面手动开启权限
                    showDialogTipUserGoToAppSetting();
                } else {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }

                    Log.e("","权限获取成功");
                    flag = 1;
                    jumpGo();
                }
            }
        }
    }


    @Override
    public void run() {
        startRequestPermission();
    }
}
