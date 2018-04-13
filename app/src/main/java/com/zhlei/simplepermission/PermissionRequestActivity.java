package com.zhlei.simplepermission;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 权限请求
 * @author lezh04
 */
public class PermissionRequestActivity extends AppCompatActivity {

    private static final String TAG = "PermissionRequestActivity";

    public static final String PERMISSIONS = "permissions";
    public static final int REQUEST_CODE = 1;
    private static PermissionListener sPermissionListener;

    public static void requestRuntimePermissions(Context context,
                                          String[] permissions,
                                          PermissionListener permissionListener){
        sPermissionListener = permissionListener;
        Intent intent = new Intent(context, PermissionRequestActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(PERMISSIONS, permissions);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        invasionStatusBar(this);
        requestRuntimePermissions();
    }

    /**
     * Set the content layout full the StatusBar, but do not hide StatusBar.
     */
    private static void invasionStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            View decorView = window.getDecorView();
            decorView.setSystemUiVisibility(decorView.getSystemUiVisibility()
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    private void requestRuntimePermissions() {
        Intent intent = getIntent();
        String[] permissions = intent.getStringArrayExtra(PERMISSIONS);
        if (permissions == null || permissions.length == 0) {
            finish();
            return;
        }

        List<String> requestPermissionList = new ArrayList<>();
        for (String permission : permissions) {
            if (PermissionChecker.checkSelfPermission(this,permission)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissionList.add(permission);
            }
        }

        if (requestPermissionList.isEmpty()) {
            sPermissionListener.onGranted();
        } else {
            ActivityCompat.requestPermissions(this, requestPermissionList.toArray(new
                    String[requestPermissionList.size()]), REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            ArrayList<String> deniedPermissionList = new ArrayList<>();
            if (grantResults.length > 0) {
                for (int i = 0; i < grantResults.length; i++) {
                    int result = grantResults[i];
                    if (result != PackageManager.PERMISSION_GRANTED
                            && (PermissionChecker.checkSelfPermission(this, permissions[i])
                            != PackageManager.PERMISSION_GRANTED)) {
                        deniedPermissionList.add(permissions[i]);
                    }
                }

                if (deniedPermissionList.isEmpty()) {
                    // 都被授权  回调 Listener onGranted 方法 已授权
                    if (sPermissionListener != null) {
                        sPermissionListener.onGranted();
                    }
                } else {
                    // 有权限被拒绝 回调 Listener onDenied 方法
                    if (sPermissionListener != null) {
                        sPermissionListener.onDenied(deniedPermissionList);
                    }
                }
            }
        }

        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sPermissionListener = null;
    }
}
