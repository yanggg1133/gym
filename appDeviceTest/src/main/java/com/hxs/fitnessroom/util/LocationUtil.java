package com.hxs.fitnessroom.util;

import android.content.Context;
import android.location.LocationListener;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.hxs.fitnessroom.BuildConfig;

/**
 * 获取定位信息工具类
 *
 * @see #Invalid_Location
 * Created by je on 9/4/17.
 */

public class LocationUtil
{

    //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
    //紧定位一次
    private static final int OP_ScanSpan = 2000;
    //坐标系类型 ，"bd09ll"为百度坐标系
    private static final String OP_CoorType = "bd09ll";

    private static LocationClient mLocationClient;

    private static String mlocationPoints = "";

    public static String getLastLocationPoints()
    {
        synchronized (mlocationPoints)
        {
            return mlocationPoints;
        }
    }

    private static void setLocationPoints(String locationPoints)
    {
        synchronized (mlocationPoints)
        {
            mlocationPoints = locationPoints;
        }
    }

    /**
     * 获取经度
     * @return
     */
    public static String getLongitude()
    {
        if(ValidateUtil.isNotEmpty(getLastLocationPoints()))
        {
            return getLastLocationPoints().split(",")[0];
        }
        return "0.0";
    }

    /**
     * 获取纬度
     * @return
     */
    public static String getLatitude()
    {
        if(ValidateUtil.isNotEmpty(getLastLocationPoints()))
        {
            return getLastLocationPoints().split(",")[1];
        }
        return "0.0";
    }


    /**
     * 无效的定位值
     *
     * @see LocationListener
     */
    private static final double Invalid_Location = 4.9E-324D;

    public static void appInitialization(Context context)
    {
        if (null == mLocationClient)
            mLocationClient = new LocationClient(context);

        mLocationClient.registerLocationListener(myListener);
        mLocationClient.setLocOption(getLocationClientOption());
    }

    public static void refreshLocation()
    {
        mLocationClient.start();
    }

    /**
     * 定位监听状态通知
     */
    private static BDAbstractLocationListener myListener = new BDAbstractLocationListener()
    {
        @Override
        public void onReceiveLocation(BDLocation location)
        {
            if (BuildConfig.DEBUG)
                LogUtil.d("Location_D", "Latitude=" + String.valueOf(location.getLatitude()) + ";Longitude=" + location.getLongitude());


            if (Invalid_Location == location.getLatitude() && "".equals(getLastLocationPoints()))
            {
                if (BuildConfig.DEBUG)
                    LogUtil.d("Location_D", "获取位置失败");
                return ;
            }

            setLocationPoints(String.valueOf(location.getLongitude()) + "," + location.getLatitude());
            mLocationClient.stop();
        }

        @Override
        public void onLocDiagnosticMessage(int locType, int diagnosticType, String diagnosticMessage)
        {
            if (BuildConfig.DEBUG)
                LogUtil.d("Location_D", "locType=" + locType + ";diagnosticType=" + diagnosticType + ";diagnosticMessage=" + diagnosticMessage);

            if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_BETTER_OPEN_GPS)
            {
                //建议打开GPS
            } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_BETTER_OPEN_WIFI)
            {
                //建议打开wifi，不必连接，这样有助于提高网络定位精度！
            } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_NEED_CHECK_LOC_PERMISSION)
            {
                //定位权限受限，建议提示用户授予APP定位权限！
            } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_NEED_CHECK_NET)
            {
                //网络异常造成定位失败，建议用户确认网络状态是否异常！
            } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_NEED_CLOSE_FLYMODE)
            {
                //手机飞行模式造成定位失败，建议用户关闭飞行模式后再重试定位！
            } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_NEED_INSERT_SIMCARD_OR_OPEN_WIFI)
            {
                //无法获取任何定位依据，建议用户打开wifi或者插入sim卡重试！
            } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_NEED_OPEN_PHONE_LOC_SWITCH)
            {
                //无法获取有效定位依据，建议用户打开手机设置里的定位开关后重试！
            } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_SERVER_FAIL)
            {
                //百度定位服务端定位失败
                //建议反馈location.getLocationID()和大体定位时间到loc-bugs@baidu.com
            } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_FAIL_UNKNOWN)
            {
                //无法获取有效定位依据，但无法确定具体原因
                //建议检查是否有安全软件屏蔽相关定位权限
                //或调用LocationClient.restart()重新启动后重试！
            }
        }


    };


    /**
     * 获取百度定位参数设置
     *
     * @return
     */
    private static LocationClientOption getLocationClientOption()
    {

        LocationClientOption option = new LocationClientOption();
        //使用节电模式
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);

        //使用百度经伟坐标
        option.setCoorType(OP_CoorType);

        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        //紧定位一次
        option.setScanSpan(OP_ScanSpan);

        //可选，设置是否需要地址信息，默认不需要
        option.setIsNeedAddress(true);

        //可选，默认false,设置是否使用gps
        option.setOpenGps(false);

        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setLocationNotify(false);

        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”

        //附近点信息，暂不需要
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        //option.setIsNeedLocationPoiList(true);

        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        //默认结束，省电
        option.setIgnoreKillProcess(true);

        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        option.setEnableSimulateGps(false);

        return option;
    }


}


//    public static void registerListener(LocationListener locationListener)
//    {
//        if(mLocationListeners.contains(locationListener))
//            throw new IllegalArgumentException("注册重复的定位监听器");
//
//        if(mLocationClient.isStarted())
//            mLocationClient.stop();
//        mLocationListeners.add(locationListener);
//
//        mLocationClient.start();
//
//
//    }
//
//    public static void unRegisterListener(LocationListener locationListener)
//    {
//        if(!mLocationListeners.contains(locationListener))
//            throw new IllegalArgumentException("定位监听器未注册");
//
//        if(mLocationClient.isStarted())
//            mLocationClient.stop();
//        mLocationListeners.remove(locationListener);
//
//        if(mLocationListeners.size() != 0)
//            mLocationClient.start();
//    }

//    /**
//     * 监听接口
//     */
//    public interface LocationListener
//    {
//        /**
//         * 定位信息返回
//         *
//         * 如果定位失败，经纬度会返回 {@link #Invalid_Location}
//         *
//         * @param latitude 经度
//         * @param longitude 纬度
//         *
//         * @see #Invalid_Location
//         */
//        void onLocationMessage(double latitude, double longitude);
//    }
//获取定位结果
//            location.getTime();    //获取定位时间
//            location.getLocationID();    //获取定位唯一ID，v7.2版本新增，用于排查定位问题
//            location.getLocType();    //获取定位类型
//            location.getLatitude();    //获取纬度信息
//            location.getLongitude();    //获取经度信息
//            location.getRadius();    //获取定位精准度
//            location.getAddrStr();    //获取地址信息
//            location.getCountry();    //获取国家信息
//            location.getCountryCode();    //获取国家码
//            location.getCity();    //获取城市信息
//            location.getCityCode();    //获取城市码
//            location.getDistrict();    //获取区县信息
//            location.getStreet();    //获取街道信息
//            location.getStreetNumber();    //获取街道码
//            location.getLocationDescribe();    //获取当前位置描述信息
//            location.getPoiList();    //获取当前位置周边POI信息
//
//            location.getBuildingID();    //室内精准定位下，获取楼宇ID
//            location.getBuildingName();    //室内精准定位下，获取楼宇名称
//            location.getFloor();    //室内精准定位下，获取当前位置所处的楼层信息
//
//            if (location.getLocType() == BDLocation.TypeGpsLocation)
//            {
//
//                //当前为GPS定位结果，可获取以下信息
//                location.getSpeed();    //获取当前速度，单位：公里每小时
//                location.getSatelliteNumber();    //获取当前卫星数
//                location.getAltitude();    //获取海拔高度信息，单位米
//                location.getDirection();    //获取方向信息，单位度
//
//            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation)
//            {
//
//                //当前为网络定位结果，可获取以下信息
//                location.getOperators();    //获取运营商信息
//
//            } else if (location.getLocType() == BDLocation.TypeOffLineLocation)
//            {
//
//                //当前为网络定位结果
//
//            } else if (location.getLocType() == BDLocation.TypeServerError)
//            {
//
//                //当前网络定位失败
//                //可将定位唯一ID、IMEI、定位失败时间反馈至loc-bugs@baidu.com
//
//            } else if (location.getLocType() == BDLocation.TypeNetWorkException)
//            {
//
//                //当前网络不通
//
//            } else if (location.getLocType() == BDLocation.TypeCriteriaException)
//            {
//
//                //当前缺少定位依据，可能是用户没有授权，建议弹出提示框让用户开启权限
//                //可进一步参考onLocDiagnosticMessage中的错误返回码
//
//            }
