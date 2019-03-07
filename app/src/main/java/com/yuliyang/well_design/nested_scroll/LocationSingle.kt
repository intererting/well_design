package com.yuliyang.well_design.nested_scroll

import androidx.lifecycle.MutableLiveData
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.yuliyang.well_design.MyApplication

object LocationSingle {

    val locationLivedata = MutableLiveData<AMapLocation>()
    private val locationClient by lazy {
        AMapLocationClient(MyApplication.provideInstance()).apply {
            //设置定位参数
            setLocationOption(getDefaultOption())
            // 设置定位监听
            setLocationListener { aMapLocation ->
                if (null != aMapLocation && aMapLocation.errorCode == 0) {
                    locationLivedata.postValue(aMapLocation)
                }
            }
        }
    }

    private fun getDefaultOption(): AMapLocationClientOption {
        val mOption = AMapLocationClientOption()
        mOption.locationMode =
                AMapLocationClientOption.AMapLocationMode.Hight_Accuracy//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.isGpsFirst = true//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.interval = 5000//可选，设置定位间隔。默认为2秒
        mOption.isSensorEnable = true//可选，设置是否使用传感器。默认是false
        mOption.isLocationCacheEnable = true //可选，设置是否使用缓存定位，默认为true
        return mOption
    }

    fun startLocation() {
        // 启动定位
        locationClient.startLocation()
    }

    fun stopLocation() {
        // 停止定位
        locationClient.onDestroy()
    }
}