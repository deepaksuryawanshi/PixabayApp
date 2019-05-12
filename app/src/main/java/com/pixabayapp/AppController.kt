package com.pixabayapp

import android.app.Activity
import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import androidx.appcompat.app.AppCompatDelegate
import com.example.gconnect.di.activityModules
import com.example.gconnect.di.appModules
import com.facebook.drawee.backends.pipeline.Fresco
import org.koin.android.ext.android.startKoin

/**
 * Application class is base class for android app.
 */
class AppController : Application(), Application.ActivityLifecycleCallbacks {

    // Holds activity instance
    private var currentActivity: Activity = Activity()

    // Holds key and activity context
    companion object {
        lateinit var instance: Context
//        var KEY: String = "12423765-76440acdca76bfdddfb745568"
//        var KEY: String = "2373487-e5c49b5b62aac83ccd7a25e61"
        var KEY: String = "12465463-c560734664d1d996cba2c5c6f"

    }

    override fun onCreate() {

        if (BuildConfig.DEBUG) {
            StrictMode.enableDefaults()
        }

        super.onCreate()

//        MultiDex.install(this)

        instance = applicationContext
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)

        // Initialize Fresco library instance
        Fresco.initialize(this)

        // get all modules
        val moduleList = appModules + activityModules

        // set the module list
        startKoin(this, moduleList)

    }

    /**
     * Get uri to any resource type
     * @param context - context
     * @param resId - resource id
     * @throws Resources.NotFoundException if the given ID does not exist.
     * @return - Uri to resource by given id
     */
    @Throws(Resources.NotFoundException::class)
    fun getUriToResource(
        context: Context,
        resId: Int
    ): Uri {
        /** Return a Resources instance for your application's package.  */
        val res = context.resources
        /** return uri  */
        return Uri.parse(
            ContentResolver.SCHEME_ANDROID_RESOURCE +
                    "://" + res.getResourcePackageName(resId)
                    + '/'.toString() + res.getResourceTypeName(resId)
                    + '/'.toString() + res.getResourceEntryName(resId)
        )
    }


    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityResumed(activity: Activity) {
        currentActivity = activity
    }

    override fun onActivityStarted(activity: Activity) {
        currentActivity = activity
    }

    override fun onActivityDestroyed(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle?) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        currentActivity = activity
    }

    fun getCurrentActivity(): Activity {
        return currentActivity
    }

}