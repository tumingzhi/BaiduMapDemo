package helloworld.tumingzhi.com.mapdemo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.poi.BaiduMapPoiSearch;
import com.baidu.mapapi.utils.poi.PoiParaOption;
import com.baidu.mapapi.utils.route.BaiduMapRoutePlan;
import com.baidu.mapapi.utils.route.RouteParaOption;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    //展示地图视图
    private MapView mMapView;
    //控制器
    private BaiduMap mBaiduMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //获取地图控件引用
        mMapView = this.findViewById(R.id.mapView);
        mBaiduMap=mMapView.getMap();
        //设置地图的类型
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
        //文本覆盖物
        //addText();
        //多边形覆盖物
        //addPoly();
        //addMarker();
        //addInfo();
        //poiSearch();
        //rountPlan();
        location();
    }
    public void addText(){
        TextOptions opt=new TextOptions();
        opt.text("88142725屠明智");
        opt.fontColor(Color.RED);
        opt.fontSize(96);
        LatLng lat=new LatLng(39.963175,116.400244);
        opt.position(lat);
        mBaiduMap.addOverlay(opt);
    }
    public void addPoly(){
        PolygonOptions poly=new PolygonOptions();
        List<LatLng> lats=new ArrayList<>();
        LatLng point1=new LatLng(39.963175,116.400244);
        lats.add(point1);
        LatLng point2=new LatLng(39.063184,116.000244);
        lats.add(point2);
        LatLng point3=new LatLng(40.963175,117.400244);
        lats.add(point3);
        poly.points(lats);
        mBaiduMap.addOverlay(poly);
    }
    public void addMarker(){
        MarkerOptions opt=new MarkerOptions();
        BitmapDescriptor icon= BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher);
        opt.icon(icon);
        LatLng lat=new LatLng(39.963175,116.400244);
        opt.position(lat);
        mBaiduMap.addOverlay(opt);
    }
    public void addInfo(){
        BitmapDescriptor icon= BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher);
        LatLng lat=new LatLng(39.963175,116.400244);
        InfoWindow info =new InfoWindow(icon,lat,0,new InfoWindowOnClickListener());

        //添加到百度地图
        mBaiduMap.showInfoWindow(info);
    }
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_icon:
                mBaiduMap.snapshot(new BaiduMap.SnapshotReadyCallback() {
                    @Override
                    public void onSnapshotReady(Bitmap icon) {
                        Toast.makeText(MainActivity.this, "地址:"+icon, Toast.LENGTH_LONG).show();
                    }
                });
                break;
        }
    }
    class InfoWindowOnClickListener implements InfoWindow.OnInfoWindowClickListener{

        @Override
        public void onInfoWindowClick() {
            Toast.makeText(MainActivity.this,"点击了",Toast.LENGTH_SHORT).show();
        }
    }
    //poi检索
    public void poiSearch(){
        BaiduMapPoiSearch poi=new BaiduMapPoiSearch();
        PoiParaOption opt=new PoiParaOption();
        opt.key("学校");
        opt.radius(5000);
        LatLng point1=new LatLng(39.963175,116.400244);
        opt.center(point1);
        poi.openBaiduMapPoiNearbySearch(opt,this);
    }
    //线路规划
    public void rountPlan(){
        BaiduMapRoutePlan plan=new BaiduMapRoutePlan();
        RouteParaOption opt=new RouteParaOption();
        opt.cityName("北京");
        LatLng start=new LatLng(39.93923,116.357428);
        LatLng end=new LatLng(39.91923,116.327428);
        opt.startPoint(start);
        opt.endPoint(end);
        opt.startName("天安门");
        opt.endName("龙泽");
        plan.openBaiduMapTransitRoute(opt,this);
    }

    MyLocationListener myLocationListener;
    LocationClient client;
    //定位
    public void location(){
        //允许进行定位
        mBaiduMap.setMyLocationEnabled(true);
        LocationClientOption opt=new LocationClientOption();
        opt.setCoorType("bd0911");//坐标系
        opt.setOpenGps(true);//打开GPS
        opt.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//定位模式
        client = new LocationClient(this,opt);

        //设置监听
        myLocationListener=new MyLocationListener();
        client.registerLocationListener(myLocationListener);
        //启动定位
        client.start();
        mBaiduMap.setMyLocationEnabled(false);

    }
    class MyLocationListener implements BDLocationListener{

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {

            MyLocationData data=new MyLocationData.Builder()
                    .latitude(bdLocation.getLatitude())
                    .longitude(bdLocation.getLongitude())
                    .build();
            mBaiduMap.setMyLocationData(data);
            //地图的中心移动到定位的位置
            LatLng lat=new LatLng(bdLocation.getLatitude(),bdLocation.getLongitude());
            MapStatusUpdate update= MapStatusUpdateFactory.newLatLng(lat);
            mBaiduMap.animateMapStatus(update);
            //窗口信息
            BitmapDescriptor icon=BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher);
            InfoWindow info=new InfoWindow(icon,lat,0,null);

            mBaiduMap.showInfoWindow(info);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        client.unRegisterLocationListener(myLocationListener);
        client.stop();
    }
}