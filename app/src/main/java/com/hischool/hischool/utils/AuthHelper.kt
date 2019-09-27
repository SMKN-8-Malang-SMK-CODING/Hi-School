package com.hischool.hischool.utils

import android.app.Activity
import android.content.Intent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.hischool.hischool.login.LoginActivity

object AuthHelper {
    fun loginCheck(context: Activity): FirebaseUser? {
        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser == null) {
            val intent = Intent(context, LoginActivity::class.java)

            intent.flags = (Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)

            context.startActivity(intent)

            return null
        }

        return currentUser
    }

    fun signOut(context: Activity) {
        FirebaseAuth.getInstance().signOut()

        val intent = Intent(context, LoginActivity::class.java)

        intent.flags = (Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)

        context.startActivity(intent)
        context.finish()
    }
}
