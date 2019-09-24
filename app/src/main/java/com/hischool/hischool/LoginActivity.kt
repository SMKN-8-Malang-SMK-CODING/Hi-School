package com.hischool.hischool

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.hischool.hischool.home.HomeActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        if (FirebaseAuth.getInstance().currentUser != null) {
            moveToHome()
        }

        btnLogin.setOnClickListener {
            val email = edt_login_input_email.text.toString()
            val password = edt_login_input_password.text.toString()

            if (email.isEmpty()) {
                edt_login_input_email.error = "Harap isi email anda..."
            }

            if (password.isEmpty()) {
                edt_login_input_password.error = "Harap isi password anda"
            }

            if (email.isEmpty() || password.isEmpty()) {
                return@setOnClickListener
            }

            pb_loading_indicator.visibility = View.VISIBLE

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    val settings = FirebaseFirestoreSettings.Builder()
                        .setPersistenceEnabled(true)
                        .build()

                    firestore.firestoreSettings = settings

                    firestore
                        .collection("user")
                        .document(FirebaseAuth.getInstance().currentUser?.uid!!)
                        .get()
                        .addOnSuccessListener {
                            moveToHome()
                        }
                }.addOnFailureListener {
                    pb_loading_indicator.visibility = View.GONE
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun moveToHome() {
        val intent = Intent(this, HomeActivity::class.java)

        intent.flags = (Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)

        startActivity(intent)

        finish()
    }

    override fun onBackPressed() {
        finish()
    }
}
