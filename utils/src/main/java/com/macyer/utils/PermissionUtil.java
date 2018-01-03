package com.macyer.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.TextUtils;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 请求权限工具类-使用说明见类下方
 * Created by liuxiu on 2016/11/2.
 */

public abstract class PermissionUtil {

    /**
     * shouldShowRequestPermissionRationale的返回值全部为true时flag才为true
     */
    private static boolean flag = true;

    /**
     * 权限状态监听接口
     */
    public interface PermissionCallBack extends ActivityCompat.OnRequestPermissionsResultCallback {
        /**
         * 权限被授予
         */
        void permissionGranted(int requestCode, List<String> perms);

        /**
         * 权限被拒绝
         */
        void permissionDenied(int requestCode, List<String> perms);
    }

    private PermissionUtil() {
    }

    /**
     * 请求方法-Activity中
     * <p>
     * 在onRequestPermissionsResult方法中实现PermissionUtil.requestPermisson方法
     *
     * @param permission         需要请求的权限
     * @param permissionDes      需要请求的权限中文描述
     * @param PERMS_REQUEST_CODE onRequestPermissionsResult中的请求码
     * @param context            环境变量
     */
    @TargetApi(Build.VERSION_CODES.M)
    public static void requestPermisson(@NonNull Context context, int PERMS_REQUEST_CODE,
                                        @NonNull String permissionDes, @NonNull String... permission) {
        requestPermisson(context, null, PERMS_REQUEST_CODE, permissionDes, permission);
    }

    /**
     * 请求方法-Fragment中
     * <p>
     * 在onRequestPermissionsResult方法中实现PermissionUtil.requestPermisson方法
     *
     * @param permission         需要请求的权限
     * @param permissionDes      需要请求的权限中文描述
     * @param PERMS_REQUEST_CODE onRequestPermissionsResult中的请求码
     * @param context            环境变量
     * @param fragment           传递fragment.this
     */
    @TargetApi(Build.VERSION_CODES.M)
    public static void requestPermisson(@NonNull Context context, @NonNull Fragment fragment, int PERMS_REQUEST_CODE,
                                        @NonNull String permissionDes, @NonNull String... permission) {
        final Activity mActivity = (Activity) context;
        Object obj = null;
        if (null != fragment) obj = fragment;
        else obj = mActivity;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            String[] perTempPermissionDenied = checkPermissionGranted(mActivity, fragment, permission);
            if (perTempPermissionDenied.length > 0) {
                String[] perTempRequireToInfo = processPermissionNative(perTempPermissionDenied, 1);
                if (flag) {
                    if (perTempPermissionDenied.length == perTempRequireToInfo.length) {
                        //权限拒绝并且都没有提示
                        if (null != fragment) {
                            fragment.requestPermissions(perTempRequireToInfo, PERMS_REQUEST_CODE);
                        } else {
                            mActivity.requestPermissions(perTempRequireToInfo, PERMS_REQUEST_CODE);
                        }
                    } else {//拒绝过部分没有提示
                        if (obj instanceof PermissionCallBack)
                            ((PermissionCallBack) obj).permissionDenied(PERMS_REQUEST_CODE, Arrays.asList(permission));
                        ToastUtil.show("您已经拒绝提示申请(" + permissionDes + ")权限,暂时无法使用此功能，请手动打开");
                    }
                } else {
                    flag = true;
                    if (obj instanceof PermissionCallBack)
                        ((PermissionCallBack) obj).permissionDenied(PERMS_REQUEST_CODE, Arrays.asList(permission));
                    ToastUtil.show("您已经拒绝提示申请(" + permissionDes + ")权限,暂时无法使用此功能，请手动打开");
                }
            } else {
                processPermissionNative(permission, 2);
                if (obj instanceof PermissionCallBack)
                    ((PermissionCallBack) obj).permissionGranted(PERMS_REQUEST_CODE, Arrays.asList(permission));
            }
        } else {
            processPermissionNative(permission, 2);
            if (obj instanceof PermissionCallBack)
                ((PermissionCallBack) obj).permissionGranted(PERMS_REQUEST_CODE, Arrays.asList(permission));
        }
    }

    /**
     * 配置本地已经配置过的权限过滤
     *
     * @param perTemp
     * @param processType 1、保存；2、清除
     * @return
     */
    private static String[] processPermissionNative(String[] perTemp, int processType) {
//        String nativeProcess = PublicFunctionU.getPrefString("permission_native_process", "");
        String nativeProcess = "";
        ArrayList<String> list = new ArrayList<>();
        for (String permission : perTemp) {
            if (processType == 1) {
                if (!nativeProcess.contains(permission)) {
                    list.add(permission);
                    nativeProcess = nativeProcess + "#" + permission;
                }
            } else if (processType == 2) {
                nativeProcess = nativeProcess.replace("#" + permission, "").trim();
            }
        }
//        PublicFunctionU.setPrefString("permission_native_process", nativeProcess);
        return list.toArray(new String[list.size()]);
    }

    /**
     * 提取所有没有被授予的权限,如果有权限被设置为不再提示则设置flag为false
     *
     * @param mActivity
     * @param permissions
     * @return 没有被授予的权限
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private static String[] checkPermissionGranted(Activity mActivity, Fragment fragment, @NonNull String[] permissions) {
        ArrayList<String> list = new ArrayList<>();
        if (permissions.length > 0) {
            for (String permission : permissions) {
                if (!TextUtils.isEmpty(permission.trim())
                        && (mActivity.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED)) {
                    list.add(permission);
                    if (null != fragment) {
                        if (!fragment.shouldShowRequestPermissionRationale(permission))
                            flag = false;
                    } else {
                        if (!mActivity.shouldShowRequestPermissionRationale(permission))
                            flag = false;
                    }
                }
            }
        }
        return (String[]) list.toArray(new String[list.size()]);
    }

    /**
     * 对权限选择之后的onRequestPermissionsResult进行处理
     *
     * @param permsRequestCode
     * @param permissions
     * @param grantResults
     * @param context
     * @param activity         传递this
     */
    @TargetApi(Build.VERSION_CODES.M)
    public static void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults
            , @NonNull final Context context, @NonNull Activity activity) {
        int grantResultsCount = 0;
        for (int grantResult : grantResults) {
            if (PackageManager.PERMISSION_GRANTED == grantResult) {
                grantResultsCount++;
            } else break;
        }
        if (grantResultsCount == grantResults.length) {
            ((PermissionCallBack) activity).permissionGranted(permsRequestCode, Arrays.asList(permissions));
        } else {
            ((PermissionCallBack) activity).permissionDenied(permsRequestCode, Arrays.asList(permissions));
            if (grantResults.length == 1) ToastUtil.show("权限被拒绝,不能使用此功能");
            else ToastUtil.show("全部或部分权限被拒绝，不能使用此功能");
        }
    }

    /**
     * 对权限选择之后的onRequestPermissionsResult进行处理
     *
     * @param permsRequestCode
     * @param permissions
     * @param grantResults
     * @param context
     * @param fragment         传递Fragment.this
     */
    @TargetApi(Build.VERSION_CODES.M)
    public static void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults
            , @NonNull final Context context, @NonNull Fragment fragment) {
        int grantResultsCount = 0;
        for (int grantResult : grantResults) {
            if (PackageManager.PERMISSION_GRANTED == grantResult) {
                grantResultsCount++;
            } else break;
        }
        if (grantResultsCount == grantResults.length) {
            ((PermissionCallBack) fragment).permissionGranted(permsRequestCode, Arrays.asList(permissions));
        } else {
            ((PermissionCallBack) fragment).permissionDenied(permsRequestCode, Arrays.asList(permissions));
            if (grantResults.length == 1) ToastUtil.show("权限被拒绝,不能使用此功能");
            else ToastUtil.show("全部或部分权限被拒绝，不能使用此功能");
        }
    }
}

/*

使用权限工具类

public static final int PERMS_REQUEST_CODE_LOCATION = 0x112;

## 1、PermissionCallBack实现接口
## 2、重写系统权限结果方法 onRequestPermissionsResult 统一处理
    //a.  Activity中
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //activity中一定重写此方法，否则fragment接收不到结果
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtil.onRequestPermissionsResult(requestCode, permissions, grantResults, MainActivity.this, this);
    }
    //b.  Fragment中
    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionUtil.onRequestPermissionsResult(requestCode, permissions, grantResults, mContext, HomeFrag.this);
    }
## 3、调用权限判断
    //a.  Activity中
    PermissionUtil.requestPermisson(MainActivity.this, PERMS_REQUEST_CODE_LOCATION,
                getString(R.string.permission_request_location), Manifest.permission.ACCESS_COARSE_LOCATION);
    //b.  Fragment中
    PermissionUtil.requestPermisson(mContext, CommunityProjectFragment.this, PERMS_REQUEST_CODE_LOCATION,
                getString(R.string.permission_request_location), Manifest.permission.ACCESS_COARSE_LOCATION);

## 4、返回结果
    @Override
    public void permissionGranted(int requestCode, List<String> perms) {
        //TODO
    }

    @Override
    public void permissionDenied(int requestCode, List<String> perms) {
        //TODO
    }




####    例子a  ####
public class MainActivity extends FragmentActivity implements PermissionCallBack {

    public static final int PERMS_REQUEST_CODE_LOCATION = 0x112;

    @Override
    protected void onStart() {
        super.onStart();
        PermissionUtil.requestPermisson(MainActivity.this, PERMS_REQUEST_CODE_LOCATION,
                getString(R.string.permission_request_location), Manifest.permission.ACCESS_COARSE_LOCATION);
    }


    @Override
    public void permissionGranted(int requestCode, List<String> perms) {
        if (requestCode == PERMS_REQUEST_CODE_LOCATION) {
            registerNetStateReceiver();
        }
    }

    @Override
    public void permissionDenied(int requestCode, List<String> perms) {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtil.onRequestPermissionsResult(requestCode, permissions, grantResults, MainActivity.this, this);
    }
}


####    例子b  ####
public abstract class HomeFrag extends BaseFrag implements PermissionUtil.PermissionCallBack {

    @Override
    public void permissionGranted(int requestCode, List<String> perms) {
        switch (requestCode) {
            case QD_SCANNER_GOODS:
                toQrScan();
                break;
        }
    }

    @Override
    public void permissionDenied(int requestCode, List<String> perms) {
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionUtil.onRequestPermissionsResult(requestCode, permissions, grantResults, mContext, HomeFrag.this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_left_iv:
                PermissionUtil.requestPermisson(mContext,HomeFrag.this, QD_SCANNER_GOODS,
                        getString(R.string.permission_request_camera), Manifest.permission.CAMERA);
                break;
        }
    }
}

*/
