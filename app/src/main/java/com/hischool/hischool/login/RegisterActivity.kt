package com.hischool.hischool.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.hischool.hischool.R
import com.hischool.hischool.data.entity.School
import com.hischool.hischool.data.entity.User
import com.hischool.hischool.home.HomeActivity
import com.hischool.hischool.utils.KeyboardHelper
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    val firestore = Firebase.firestore
    val auth = FirebaseAuth.getInstance()

    private val listSchool = ArrayList<String>()
    private val listSchoolId = ArrayList<Int>()

    private var selectedSchool: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        firestore.collection("schools").get().addOnSuccessListener {
            for (doc in it.documents) {
                val school: School = doc.toObject()!!

                listSchool.add(school.schoolName!!)
                listSchoolId.add(school.schoolId!!)
            }

            selectedSchool = listSchoolId[0]

            setupSchoolSpinner()

            tv_to_login.setOnClickListener {
                startActivity(Intent(this, LoginActivity::class.java))
            }

            btnRegister.setOnClickListener {
                val fullName = edt_register_input_full_name.text.toString()
                val nickname = edt_register_input_nickname.text.toString()
                val school = selectedSchool
                val classRoom = edt_register_input_class_room.text.toString()
                val email = edt_register_input_email.text.toString()
                val password = edt_login_input_password.text.toString()


                // validating
                var notValid = false

                if (fullName.isEmpty()) {
                    edt_register_input_full_name.error = "Nama lengkap tidak boleh kosong"
                    notValid = true
                }

                if (nickname.isEmpty()) {
                    edt_register_input_full_name.error = "Nama panggilan tidak boleh kosong"
                    notValid = true
                }

                if (classRoom.isEmpty()) {
                    edt_register_input_full_name.error = "Ruang kelas tidak boleh kosong"
                    notValid = true
                }

                if (email.isEmpty()) {
                    edt_register_input_email.error = "Email tidak boleh kosong"
                    notValid = true
                }

                if (password.isEmpty()) {
                    edt_login_input_password.error = "Password tidak boleh kosong"
                    notValid = true
                }

                if (email.isNotEmpty() && !KeyboardHelper.isValidEmail(email)) {
                    edt_register_input_email.error = "Harap isi email anda dengan benar"
                    notValid = true
                }

                if (notValid) {
                    return@setOnClickListener
                }

                iv_loading_background.visibility = View.VISIBLE
                pb_loading_indicator.visibility = View.VISIBLE

                auth.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener { userResult ->
                        firestore.collection("users").document(userResult.user!!.uid).set(
                            User(school, fullName, nickname, classRoom, email, "")
                        ).addOnSuccessListener {
                            auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
                                val settings = FirebaseFirestoreSettings.Builder()
                                    .setPersistenceEnabled(true)
                                    .build()

                                firestore.firestoreSettings = settings

                                firestore
                                    .collection("users")
                                    .document(FirebaseAuth.getInstance().currentUser?.uid!!)
                                    .get()
                                    .addOnSuccessListener {
                                        moveToHome()
                                    }
                            }
                        }
                    }
            }
        }
    }

    private fun moveToHome() {
        val intent = Intent(this, HomeActivity::class.java)

        intent.flags = (Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)

        startActivity(intent)

        finish()
    }

    private fun setupSchoolSpinner() {
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listSchool)

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spn_register_input_school.adapter = arrayAdapter

        spn_register_input_school.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedSchool = listSchoolId[position]
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }
    }
}
