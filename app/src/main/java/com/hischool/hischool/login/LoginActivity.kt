package com.hischool.hischool.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.hischool.hischool.R
import com.hischool.hischool.home.HomeActivity
import com.hischool.hischool.utils.KeyboardHelper
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        if (FirebaseAuth.getInstance().currentUser != null) {
            moveToHome()
        }

        btnLogin.setOnClickListener {
            val email = edt_login_input_email.text.toString()
            val password = edt_login_input_password.text.toString()

            var notValid = false

            if (email.isEmpty()) {
                edt_login_input_email.error = "Email tidak boleh kosong"
                notValid = true
            }

            if (password.isEmpty()) {
                edt_login_input_password.error = "Password tidak boleh kosong"
                notValid = true
            }

            if (email.isNotEmpty() && !KeyboardHelper.isValidEmail(email)) {
                edt_login_input_email.error = "Harap isi email anda dengan benar"
                notValid = true
            }

            if (notValid) {
                return@setOnClickListener
            }

            startLoading()

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
                    stopLoading()

                    var errorMessage = it.message

                    try {
                        when ((it as FirebaseAuthException).errorCode) {
                            "ERROR_USER_NOT_FOUND" -> errorMessage = "Email tidak terdaftar..."
                            "ERROR_WRONG_PASSWORD" -> errorMessage = "Password salah..."
                        }
                    } catch (e: ClassCastException) {
                        val exception = it as FirebaseException

                        errorMessage = exception.message
                    }

                    Toasty.error(this, errorMessage.toString(), Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun startLoading() {
        pb_loading_indicator.visibility = View.VISIBLE
        iv_loading_background.visibility = View.VISIBLE
        btnLogin.visibility = View.GONE

        pb_loading_indicator.indeterminateDrawable.setColorFilter(
            ContextCompat.getColor(this, R.color.colorPrimary),
            android.graphics.PorterDuff.Mode.MULTIPLY
        )
    }


    private fun stopLoading() {
        pb_loading_indicator.visibility = View.GONE
        iv_loading_background.visibility = View.GONE
        btnLogin.visibility = View.VISIBLE
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
