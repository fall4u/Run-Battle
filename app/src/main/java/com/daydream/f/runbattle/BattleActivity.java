package com.daydream.f.runbattle;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.LatLng;

public class BattleActivity extends AppCompatActivity implements AMapLocationListener {

    MapView mMapView = null;
    private TextView tvResult;

    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;

    public final static int MSG_LOCATION_START = 0;
    /**
     * 定位完成
     */
    public final static int MSG_LOCATION_FINISH = 1;
    /**
     * 停止定位
     */
    public final static int MSG_LOCATION_STOP= 2;


    double preLongtitude = 0;
    double curLongtitude = 0;
    double preLatitude = 0;
    double curLatitude = 0;
    float distance = 0;

    int drop_count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle);

        // mapview
        mMapView = (MapView) findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);

        tvResult = (TextView) findViewById(R.id.result);


        // location

        locationClient = new AMapLocationClient(this.getApplicationContext());
        locationOption = new AMapLocationClientOption();

        locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        locationClient.setLocationListener(this);

        // start location
        initOption();
        locationClient.startLocation();
        mHandler.sendEmptyMessage(MSG_LOCATION_START);

  //      LatLng  prelng = new LatLng(31.557793, 120.351321);
  //      LatLng curlng = new LatLng(31.557793, 120.351321);
  //      Log.i("RunBattle", "distance : " + AMapUtils.calculateLineDistance(prelng, curlng));

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();

        if(locationClient != null){
            locationClient.onDestroy();
            locationClient = null;
            locationOption = null;
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，实现地图生命周期管理
        mMapView.onSaveInstanceState(outState);
    }

    // 根据控件的选择，重新设置定位参数
    private void initOption() {
        // 设置是否需要显示地址信息
        locationOption.setNeedAddress(true);
        // 设置是否开启缓存
        locationOption.setLocationCacheEnable(true);
        locationOption.setInterval(1000);
    }
    Handler mHandler = new Handler(){
        public void dispatchMessage(android.os.Message msg) {
            switch (msg.what) {
                case MSG_LOCATION_START:
                    tvResult.setText("正在定位...");
                    break;
                //定位完成
                case MSG_LOCATION_FINISH:

                    if(drop_count < 3){
                        drop_count ++;
                    }else {
                        AMapLocation loc = (AMapLocation) msg.obj;
                        StringBuffer displayString = new StringBuffer();

                        String result = getLocationStr(loc);
                        displayString.append(result);
                        String distance = calculateDistance(loc);
                        displayString.append(distance);
                        tvResult.setText(displayString.toString());
                    }
                    break;
                case MSG_LOCATION_STOP:
                    tvResult.setText("定位停止");
                    break;
                default:
                    break;
            }
        };
    };


    // 定位监听
    @Override
    public void onLocationChanged(AMapLocation loc) {
        if (null != loc) {
            Message msg = mHandler.obtainMessage();
            msg.obj = loc;
            msg.what = MSG_LOCATION_FINISH;
            mHandler.sendMessage(msg);
        }
    }

    /**
     * 根据定位结果返回定位信息的字符串
     * @param loc
     * @return
     */
    public synchronized static String getLocationStr(AMapLocation location){
        if(null == location){
            return null;
        }
        StringBuffer sb = new StringBuffer();
        //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
        if(location.getErrorCode() == 0){
            sb.append("定位成功" + "\n");
            sb.append("定位类型: " + location.getLocationType() + "\n");
            sb.append("经    度    : " + location.getLongitude() + "\n");
            sb.append("纬    度    : " + location.getLatitude() + "\n");
            sb.append("精    度    : " + location.getAccuracy() + "米" + "\n");
            sb.append("提供者    : " + location.getProvider() + "\n");

            if (location.getProvider().equalsIgnoreCase(
                    android.location.LocationManager.GPS_PROVIDER)) {
                // 以下信息只有提供者是GPS时才会有
                sb.append("速    度    : " + location.getSpeed() + "米/秒" + "\n");
                sb.append("角    度    : " + location.getBearing() + "\n");
                // 获取当前提供定位服务的卫星个数
                sb.append("星    数    : "
                        + location.getSatellites() + "\n");
            } else {
                // 提供者是GPS时是没有以下信息的
//                sb.append("国    家    : " + location.getCountry() + "\n");
//                sb.append("省            : " + location.getProvince() + "\n");
//                sb.append("市            : " + location.getCity() + "\n");
//                sb.append("城市编码 : " + location.getCityCode() + "\n");
//                sb.append("区            : " + location.getDistrict() + "\n");
//                sb.append("区域 码   : " + location.getAdCode() + "\n");
//                sb.append("地    址    : " + location.getAddress() + "\n");
//                sb.append("兴趣点    : " + location.getPoiName() + "\n");
            }
        } else {
            //定位失败
            sb.append("定位失败" + "\n");
            sb.append("错误码:" + location.getErrorCode() + "\n");
            sb.append("错误信息:" + location.getErrorInfo() + "\n");
            sb.append("错误描述:" + location.getLocationDetail() + "\n");
        }
        return sb.toString();
    }

    public String calculateDistance(AMapLocation loc){
        if(loc.getErrorCode() == 0) {
            curLatitude = loc.getLatitude();
            curLongtitude = loc.getLongitude();

            if(preLatitude == 0 && preLongtitude == 0){
                preLatitude = curLatitude;
                preLongtitude = curLongtitude;

                Log.i("RunBattle","first preLatitude: " + preLatitude + "first preLongtitude: " + preLongtitude);

            }else{


                Log.i("RunBattle","preLatitude: " + preLatitude + " preLongtitude: " + preLongtitude);
                Log.i("RunBattle","curLatitude: " + curLatitude + " curLongtitude: " + curLongtitude);
                LatLng  prelng = new LatLng(preLatitude, preLongtitude);
                LatLng curlng = new LatLng(curLatitude, curLongtitude);

                float deltaDistance = AMapUtils.calculateLineDistance(prelng, curlng);
                if(deltaDistance > loc.getAccuracy() * 0.68) {
                    distance += deltaDistance;
                    Log.i("RunBattle", "distance : " + deltaDistance );
                    Log.i("RunBattle", "accruacy : " + loc.getAccuracy());

                    preLatitude = curLatitude;
                    preLongtitude = curLongtitude;
                }else{
                    Log.i("RunBattle","deltaDistance: " + deltaDistance);
                    Log.i("RunBattle","accracy: " + loc.getAccuracy());
                }

    //            locationClient.stopLocation();



            }
        }
        return "距离" + Float.toString(distance);
    }
}
