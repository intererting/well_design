package com.yuliyang.well_design.map

import android.Manifest
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory.newCameraPosition
import com.amap.api.maps.LocationSource
import com.amap.api.maps.model.CameraPosition
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.MyLocationStyle
import com.yuliyang.well_design.R
import kotlinx.android.synthetic.main.activity_app_map.*

class AMapTestActivity : AppCompatActivity(), LocationSource, AMapLocationListener {

    companion object {
        private val STROKE_COLOR = Color.argb(45, 3, 145, 255)
        private val FILL_COLOR = Color.argb(10, 0, 0, 180)
        private val LOCATIONS = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        private const val MAP_DEFAULT_SCALE_SIZE = 18F
    }

    private lateinit var aMap: AMap
    private lateinit var mLocationChangeListener: LocationSource.OnLocationChangedListener

    private val mLocationClient by lazy {
        AMapLocationClient(this@AMapTestActivity.application).apply {
            val mLocationOption = AMapLocationClientOption()
            //设置定位监听
            setLocationListener(this@AMapTestActivity)
            mLocationOption.isGpsFirst = true
            mLocationOption.isSensorEnable = true
            mLocationOption.interval = 5000
            //设置定位参数
            setLocationOption(mLocationOption)
        }
    }

    override fun onLocationChanged(aMapLocation: AMapLocation?) {
        if (null != aMapLocation && aMapLocation.errorCode == 0) {
            mLocationChangeListener.onLocationChanged(aMapLocation)// 显示系统定位小蓝点
            animateToPoi(aMapLocation)
        }
    }

    override fun deactivate() {
        mLocationClient.stopLocation()
        mLocationClient.onDestroy()
    }

    override fun activate(listener: LocationSource.OnLocationChangedListener) {
        mLocationChangeListener = listener
        //上次定位
        val location = mLocationClient.lastKnownLocation
        location?.apply {
            animateToPoi(location)
        }
        mLocationClient.startLocation()
    }

    /**
     * 将中心移动到指定点
     */
    private fun animateToPoi(aMapLocation: AMapLocation) {
        val latLng = LatLng(aMapLocation.latitude, aMapLocation.longitude)
        val update = newCameraPosition(
            CameraPosition(latLng, MAP_DEFAULT_SCALE_SIZE, 0f, 30f)
        )
        aMap.animateCamera(update, 500, null)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_map)
        mapView.onCreate(savedInstanceState)
        requestPermissions(LOCATIONS, 100)
        aMap = mapView.map
        setUpMap()
    }

    private fun setUpMap() {
        val mUiSettings = aMap.uiSettings
        mUiSettings.isZoomControlsEnabled = false
        mUiSettings.isScaleControlsEnabled = true
        mUiSettings.isMyLocationButtonEnabled = true// 设置默认定位按钮是否显示
        aMap.setLocationSource(this)// 设置定位监听
        aMap.isMyLocationEnabled = true// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        setupLocationStyle()
    }

    private fun setupLocationStyle() {
        // 自定义系统定位蓝点
        aMap.myLocationStyle = MyLocationStyle()
            .interval(5000)
            .strokeColor(STROKE_COLOR)
            .strokeWidth(5f)
            .radiusFillColor(FILL_COLOR)
    }

    public override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    public override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    public override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    public override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
        deactivate()
    }
}