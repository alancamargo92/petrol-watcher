package com.braincorp.petrolwatcher.feature.auth.controller

import android.util.Log
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import java.lang.Exception

class EmailSignInActivityController : OnSuccessListener<AuthResult>, OnFailureListener {

    private companion object {
        const val TAG = "PETROL_WATCHER"
    }

    override fun onSuccess(result: AuthResult?) {
        Log.d(TAG, "Login successful")
        // TODO: start map activity
    }

    override fun onFailure(e: Exception) {
        Log.e(TAG, "Authentication failed", e)
        // TODO
    }

    fun signIn(email: String, password: String) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(this)
                .addOnFailureListener(this)
    }

}