package com.movieapp

import android.content.Context
import android.net.wifi.WifiManager
import androidx.test.espresso.IdlingResource
import com.pixabayapp.AppController

/**
 * Created by Administrator on 7/5/2018.
 */

class WifiTimeIdlingResource(flag: Boolean) : IdlingResource {
    private val TAG = WifiTimeIdlingResource::class.java.name
    private val startTime: Long
    private var flag = false
    private var resourceCallback: IdlingResource.ResourceCallback? = null

    private val isWifiEnabled: Boolean
        get() {
            val wifi = AppController.instance.getSystemService(Context.WIFI_SERVICE) as WifiManager
            return wifi.isWifiEnabled
        }

    init {
        this.startTime = System.currentTimeMillis()
        this.flag = flag
        setWiFiOnOff(flag)
    }

    override fun getName(): String {
        return WifiTimeIdlingResource::class.java.name + ":"
    }

    override fun isIdleNow(): Boolean {
        //        long elapsed = System.currentTimeMillis() - startTime;
        //        boolean idle = (elapsed >= waitingTime);
        //        if (idle) {
        //            resourceCallback.onTransitionToIdle();
        //        }
        //
        //        Log.v(TAG, "isIdleNow = "+idle);
        //        return idle;

        if (flag) {
            val idle = isWifiEnabled
            if (idle) {
                resourceCallback!!.onTransitionToIdle()
            }
            return idle
        } else {
            val idle = !isWifiEnabled
            if (idle) {
                resourceCallback!!.onTransitionToIdle()
            }
            return idle
        }
    }

    override fun registerIdleTransitionCallback(resourceCallback: IdlingResource.ResourceCallback) {
        this.resourceCallback = resourceCallback
    }

    private fun setWiFiOnOff(flag: Boolean) {
        (AppController.instance.getSystemService(Context.WIFI_SERVICE) as WifiManager).isWifiEnabled = flag
    }

}
