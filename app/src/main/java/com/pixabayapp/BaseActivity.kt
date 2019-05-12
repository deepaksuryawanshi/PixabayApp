package com.pixabayapp

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_search.*

/**
 * Base activity for all other activities.
 */
abstract class BaseActivity : AppCompatActivity() {

    /**
     *  Progress dialog instance to show downloading progress.
     */
    var progressDialog: ProgressDialog? = null


    // Menu item id popular.
    var SEARCH: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResource())
        configureToolbar()
    }

    /**
     * Configure toolbar for the activity.
     */
    private fun configureToolbar() {
//        supportActionBar!!.title = getToolbarTitle()
        toolbar!!.title = getToolbarTitle()
    }

    abstract fun getLayoutResource(): Int

    abstract fun getToolbarTitle(): String?

    /**
     * Display progress dialog.
     */
    fun showProgressDialog() {
        if (progressDialog == null)
            progressDialog = ProgressDialog(this)
        if (!progressDialog!!.isShowing) {
            progressDialog!!.setMessage("Please wait....")
            progressDialog!!.setCancelable(false)
            progressDialog!!.show()
        }
    }

    /**
     * Dismiss progress dialog.
     */
    fun dismissProgressDialog() {
        if (progressDialog != null) {
            progressDialog!!.dismiss()
            progressDialog = null;
        }
    }

    /**
     * Show toast message utility.
     */
    fun showToast(message: String) {
        Toast.makeText(applicationContext, " " + message, Toast.LENGTH_SHORT).show()
    }

    /**
     * Check network availability.
     */
    fun isNetworkAvailable(): Boolean {
        val cm = AppController.instance.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }

    /**
     * Hide keyboard programmatically.
     */
    fun hidekeyboard() {
        val inputManager: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(currentFocus.windowToken, InputMethodManager.SHOW_FORCED)
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {

        menu.clear();
        menu.add(0, SEARCH, Menu.NONE, "NOW PLAYING").setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW)
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            SEARCH -> {
                startActivity(Intent(this, ActivitySearch::class.java))
            }
        }
        return false
    }
}