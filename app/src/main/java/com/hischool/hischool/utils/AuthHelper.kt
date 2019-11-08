package com.hischool.hischool.utils

import android.app.Activity
import android.content.Intent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.hischool.hischool.data.entity.User
import com.hischool.hischool.login.LoginActivity

object AuthHelper {
    private var userData: User? = null

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

        userData = null

        val intent = Intent(context, LoginActivity::class.java)

        intent.flags = (Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)

        context.startActivity(intent)
        context.finish()
    }

    fun getUserData(currentUser: FirebaseUser, userDataListener: UserDataListener) {
        if (userData != null) return userDataListener.onUserData(userData!!)

        FirebaseFirestore.getInstance().collection("users").document(currentUser.uid).get()
            .addOnSuccessListener {
                val data = it.toObject<User>()

                userData = data!!

                userDataListener.onUserData(data)
            }
    }

    interface UserDataListener {
        fun onUserData(userData: User)
    }
}
