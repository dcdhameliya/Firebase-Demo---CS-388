package com.dc.firebasedemo

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

//        need to initilize firebase app
        FirebaseApp.initializeApp(applicationContext)


        val auth = FirebaseAuth.getInstance()

        auth.addAuthStateListener {
            if (auth.currentUser != null) {
                val i = Intent(this@SplashActivity, MainActivity::class.java)
                startActivity(i)
            } else {
                val i = Intent(this@SplashActivity, LoginActivity::class.java)
                startActivity(i)
            }
        }
    }
}