package com.marcingantkowski.tolkienquizapp

import android.app.Application
import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TolkienQuizApp : Application() {

    override fun onCreate() {
        super.onCreate()
        signInAnonymously()
    }

    private fun signInAnonymously() {
        val auth = Firebase.auth
        if (auth.currentUser == null) {
            auth.signInAnonymously()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("TolkienQuizApp", "signInAnonymously:success")
                    } else {
                        Log.w("TolkienQuizApp", "signInAnonymously:failure", task.exception)
                    }
                }
        }
    }
}