package com.zhlei.simplepermission;

import java.util.List;

/**
 * 权限请求接口回调
 * @author lezh04
 * @date 2018/4/13
 */

public interface PermissionListener {
    void onGranted();
    void onDenied(List<String> permissions);
}
