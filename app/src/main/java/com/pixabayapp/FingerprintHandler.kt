package com.pixabayapp

import android.content.Intent
import android.hardware.fingerprint.FingerprintManager
import android.os.CancellationSignal
import android.widget.TextView

class FingerprintHandler (private val tv: TextView) : FingerprintManager.AuthenticationCallback() {
    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
        super.onAuthenticationError(errorCode, errString)
        tv.text = tv.context.getString(R.string.aut_fail)
    }

    override fun onAuthenticationHelp(helpCode: Int, helpString: CharSequence) {
        super.onAuthenticationHelp(helpCode, helpString)
    }

    override fun onAuthenticationSucceeded(result: FingerprintManager.AuthenticationResult) {
        super.onAuthenticationSucceeded(result)
        tv.text = tv.context.getString(R.string.auth_success)
        tv.setTextColor(tv.context.resources.getColor(android.R.color.holo_green_light))

        (tv.context as MainActivity).finish()
        tv.context.startActivity(Intent(tv.context, ActivitySearch::class.java))
    }

    override fun onAuthenticationFailed() {
        super.onAuthenticationFailed()
    }

    fun doAuth(manager: FingerprintManager,
               obj: FingerprintManager.CryptoObject) {
        val signal = CancellationSignal()
        try {
            manager.authenticate(obj, signal, 0, this, null)
        } catch (sce: SecurityException) {
        }

    }

}