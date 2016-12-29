package com.wzx.buptschedule.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.avoscloud.leanchatlib.activity.AVBaseActivity;
import com.avoscloud.leanchatlib.utils.LogUtils;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfigeration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.wzx.buptschedule.R;

/**
 * Select the Loaction
 */
public class LocationActivity extends AVBaseActivity implements
        OnGetGeoCoderResultListener {
  public static final int SEND = 0;
  public static final String TYPE = "type";
  public static final String TYPE_SELECT = "select";
  public static final String LATITUDE = "latitude";
  public static final String LONGITUDE = "longitude";
  public static final String TYPE_SCAN = "scan";
  public static final String ADDRESS = "address";
  private static BDLocation lastLocation = null;
  private LocationClient locClient;
  private MyLocationListener myListener = new MyLocationListener();
  private MapView mapView;
  private BaiduMap baiduMap;
  private BaiduReceiver receiver;
  private GeoCoder geoCoder = null;
  private BitmapDescriptor descriptor = BitmapDescriptorFactory.fromResource(R.drawable.chat_location_activity_icon_geo);
  private String intentType;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.chat_location_activity);
    initBaiduMap();
  }

  private void initBaiduMap() {
    mapView = (MapView) findViewById(R.id.bmapView);
    baiduMap = mapView.getMap();
    baiduMap.setMaxAndMinZoomLevel(18, 13);
    IntentFilter iFilter = new IntentFilter();
    iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
    iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
    receiver = new BaiduReceiver();
    registerReceiver(receiver, iFilter);

    geoCoder = GeoCoder.newInstance();
    geoCoder.setOnGetGeoCodeResultListener(this);

    Intent intent = getIntent();
    intentType = intent.getStringExtra(TYPE);
    setTitle(R.string.chat_position);
    if (intentType.equals(TYPE_SELECT)) {
      baiduMap.setMyLocationEnabled(true);
      baiduMap.setMyLocationConfigeration(new MyLocationConfigeration(
          MyLocationConfigeration.LocationMode.NORMAL, true, null));
      locClient = new LocationClient(this);
      locClient.registerLocationListener(myListener);
      LocationClientOption option = new LocationClientOption();
      option.setProdName("avosim");
      option.setOpenGps(true);
      option.setCoorType("bd09ll");
      option.setScanSpan(1000);
      option.setOpenGps(true);
      option.setIsNeedAddress(true);
      option.setIgnoreKillProcess(true);
      locClient.setLocOption(option);
      locClient.start();
      if (locClient != null && locClient.isStarted()) {
        locClient.requestLocation();
      }
      if (lastLocation != null) {
        LatLng ll = new LatLng(lastLocation.getLatitude(),
            lastLocation.getLongitude());
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
        baiduMap.animateMapStatus(u);
      }
    } else {
      Bundle b = intent.getExtras();
      LatLng latlng = new LatLng(b.getDouble(LATITUDE), b.getDouble(LONGITUDE));
      baiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(latlng));
      OverlayOptions ooA = new MarkerOptions().position(latlng).icon(descriptor).zIndex(9);
      baiduMap.addOverlay(ooA);
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    if (intentType != null && intentType.equals(TYPE_SELECT)) {
      MenuItem add = menu.add(0, SEND, 0, R.string.chat_location_activity_send);
    }
    alwaysShowMenuItem(menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    if (id == SEND) {
      gotoChatPage();
    }
    return super.onOptionsItemSelected(item);
  }

  private void gotoChatPage() {
    if (lastLocation != null) {
      Intent intent = new Intent();
      intent.putExtra(LATITUDE, lastLocation.getLatitude());// 经度
      intent.putExtra(LONGITUDE, lastLocation.getLongitude());// 维度
      intent.putExtra(ADDRESS, lastLocation.getAddrStr());
      setResult(RESULT_OK, intent);
      this.finish();
    } else {
      showToast(R.string.chat_getGeoInfoFailed);
    }
  }

  @Override
  public void onGetGeoCodeResult(GeoCodeResult arg0) {
    // TODO Auto-generated method stub
  }

  @Override
  public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
    // TODO Auto-generated method stub
    if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
      toast(getString(R.string.chat_cannotFindResult));
      return;
    }
    LogUtils.d(getString(R.string.chat_reverseGeoCodeResultIs) + result.getAddress());
    lastLocation.setAddrStr(result.getAddress());
  }

  @Override
  protected void onPause() {
    mapView.onPause();
    super.onPause();
    lastLocation = null;
  }

  @Override
  protected void onResume() {
    mapView.onResume();
    super.onResume();
  }

  @Override
  protected void onDestroy() {
    if (locClient != null && locClient.isStarted()) {
      // 退出时销毁定位
      locClient.stop();
    }
    // 关闭定位图层
    baiduMap.setMyLocationEnabled(false);
    mapView.onDestroy();
    mapView = null;
    // 取消监听 SDK 广播
    unregisterReceiver(receiver);
    super.onDestroy();
    // 回收 bitmap 资源
    descriptor.recycle();
  }

  public static void startToSeeLocationDetail(Context ctx, double latitude, double longitude) {
    Intent intent = new Intent(ctx, LocationActivity.class);
    intent.putExtra(LocationActivity.TYPE, LocationActivity.TYPE_SCAN);
    intent.putExtra(LocationActivity.LATITUDE, latitude);
    intent.putExtra(LocationActivity.LONGITUDE, longitude);
    ctx.startActivity(intent);
  }

  public static void startToSelectLocationForResult(Activity from, int requestCode) {
    Intent intent = new Intent(from, LocationActivity.class);
    intent.putExtra(LocationActivity.TYPE, LocationActivity.TYPE_SELECT);
    from.startActivityForResult(intent, requestCode);
  }

  /**
   * Location SDK Listener
   */
  public class MyLocationListener implements BDLocationListener {

    @Override
    public void onReceiveLocation(BDLocation location) {
      // map view 销毁后不在处理新接收的位置
      if (location == null || mapView == null)
        return;

      if (lastLocation != null) {
        if (lastLocation.getLatitude() == location.getLatitude()
            && lastLocation.getLongitude() == location
            .getLongitude()) {
          LogUtils.d(getString(R.string.chat_geoIsSame));
          locClient.stop();
          return;
        }
      }
      lastLocation = location;

      LogUtils.d("lontitude = " + location.getLongitude() + ",latitude = "
              + location.getLatitude() + "," + getString(R.string.chat_position) + " = "
              + lastLocation.getAddrStr());

      MyLocationData locData = new MyLocationData.Builder()
          .accuracy(location.getRadius())
          .direction(100).latitude(location.getLatitude())
          .longitude(location.getLongitude()).build();
      baiduMap.setMyLocationData(locData);
      LatLng ll = new LatLng(location.getLatitude(),
          location.getLongitude());
      String address = location.getAddrStr();
      if (address != null && !address.equals("")) {
        lastLocation.setAddrStr(address);
      } else {
        geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(ll));
      }
      MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
      baiduMap.animateMapStatus(u);
    }
  }


  public class BaiduReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
      String s = intent.getAction();
      if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)) {
        toast(getString(R.string.chat_location_mapKeyErrorTips));
      } else if (s
          .equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {
        toast(getString(R.string.chat_pleaseCheckNetwork));
      }
    }
  }

}
