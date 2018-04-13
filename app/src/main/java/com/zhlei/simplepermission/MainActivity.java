package com.zhlei.simplepermission;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

/**
 * @author lezh04
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mBtnRequestSdcard;
    private Button mBtnRequestCamera;
    private Button mBtnRequestAll;

    private String[] mSdcardPermission = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private String[] mCameraPermission = new String[]{Manifest.permission.CAMERA};

    private String[] mAllPermission = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};

    private PermissionListener mListener =  new PermissionListener() {
        @Override
        public void onGranted() {
            Toast.makeText(MainActivity.this, "权限请求成功", Toast.LENGTH_SHORT)
                    . show();
        }

        @Override
        public void onDenied(List<String> permissions) {
            String deny = "";
            for (String permission: permissions) {
                deny +=  permission;
            }
            Toast.makeText(MainActivity.this, "权限请求被拒绝: ", Toast.LENGTH_LONG)
                    . show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_request_sdcard:
                PermissionRequestActivity.requestRuntimePermissions(this,
                        mSdcardPermission,
                        mListener);

                break;
            case R.id.btn_request_camera:
                PermissionRequestActivity.requestRuntimePermissions(this,
                        mCameraPermission,
                        mListener);

                break;
            case R.id.btn_request_all:
                PermissionRequestActivity.requestRuntimePermissions(this,
                        mAllPermission,
                        mListener);

                break;
            default:
                break;
        }
    }

    private void initView() {
        mBtnRequestSdcard = (Button) findViewById(R.id.btn_request_sdcard);
        mBtnRequestCamera = (Button) findViewById(R.id.btn_request_camera);
        mBtnRequestAll = (Button) findViewById(R.id.btn_request_all);

        mBtnRequestSdcard.setOnClickListener(this);
        mBtnRequestCamera.setOnClickListener(this);
        mBtnRequestAll.setOnClickListener(this);
    }
}
