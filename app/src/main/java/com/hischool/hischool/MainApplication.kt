package com.hischool.hischool

import android.app.Application
import android.content.Intent
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.hischool.hischool.home.HomeActivity

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        Toast.makeText(this, "MainApplication: onCreate", Toast.LENGTH_SHORT).show()

        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser != null) {
            val intent = Intent(this, HomeActivity::class.java)

            intent.flags = (Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)

            startActivity(intent)
        }
    }
}
