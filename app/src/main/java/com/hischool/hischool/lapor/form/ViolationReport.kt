package com.hischool.hischool.lapor.form

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.hischool.hischool.R
import com.hischool.hischool.data.entity.Report
import com.hischool.hischool.data.entity.User
import com.hischool.hischool.utils.AuthHelper
import com.hischool.hischool.utils.ButtonHelper
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_violent_report.*
import java.io.IOException
import java.util.*

class ViolationReport : AppCompatActivity() {
    private val PICK_IMAGE_REQUEST = 71

    private var filePath: Uri? = null

    private var currentUser: FirebaseUser? = null
    private var currentUserData: User? = null

    private val firestore = Firebase.firestore
    private val storage = Firebase.storage.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_violent_report)

        currentUser = AuthHelper.loginCheck(this)

        AuthHelper.getUserData(currentUser!!, object : AuthHelper.UserDataListener {
            override fun onUserData(userData: User) {
                currentUserData = userData
            }
        })

        ButtonHelper.setupBackButton(this, btnBackViolation)

        btnUpload.setOnClickListener {
            chooseImage()
        }

        btnSubmitReport.setOnClickListener {
            submitReport()
        }
    }

    private fun submitReport() {
        if (filePath != null) {
            frame_loading.visibility = View.VISIBLE
            edtReportDescription.visibility = View.GONE

            val reportId = UUID.randomUUID().toString()
            val reportDesc = edtReportDescription.text!!.toString()

            firestore.collection("reports").add(
                Report(
                    currentUserData?.schoolId,
                    currentUser?.uid,
                    reportId,
                    reportDesc,
                    "violation"
                )
            )

            val ref = storage.child("images/${reportId}.jpg")

            ref.putFile(filePath!!).addOnSuccessListener {
                Toasty.success(this, "Laporan berhasil terkirim...", Toast.LENGTH_SHORT).show()
                frame_loading.visibility = View.GONE
                btnUpload.visibility = View.VISIBLE
                imagePreview.setImageBitmap(null)
                edtReportDescription.setText("")
                edtReportDescription.visibility = View.VISIBLE
            }.addOnFailureListener {
                Toasty.error(this, "Gagal, " + it.message, Toast.LENGTH_LONG).show()
                frame_loading.visibility = View.GONE
                edtReportDescription.visibility = View.VISIBLE
            }
        }
    }

    private fun chooseImage() {
        val intent = Intent(Intent.ACTION_PICK)

        intent.type = "image/*"

        val mimeTypes = arrayOf("image/jpeg", "image/png")

        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)

        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            filePath = data.data!!

            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                imagePreview.setImageBitmap(bitmap)
                btnUpload.visibility = View.GONE
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}
