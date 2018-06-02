package com.braincorp.petrolwatcher.feature.auth.controller

import android.content.Intent
import android.util.Log
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient

class MainActivityController :
        GoogleApiClient.OnConnectionFailedListener, FacebookCallback<LoginResult> {

    private companion object {
        const val TAG = "PETROL_WATCHER"
    }

    override fun onConnectionFailed(result: ConnectionResult) {
        Log.w(TAG, "Connection failed! Result -> $result")
    }

    override fun onSuccess(result: LoginResult?) {
        Log.d(TAG, "Facebook login successful")
    }

    override fun onCancel() {
        Log.w(TAG, "Facebook login cancelled")
    }

    override fun onError(error: FacebookException?) {
        Log.e(TAG, error?.message, error)
    }

    fun handleGoogleSignInResult(data: Intent?) {
        val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
        Log.d(TAG, "result -> ${result.isSuccess}")
        if (result.isSuccess) {
            Log.d(TAG, "Google sign in successful")
        }
    }

}