package com.hischool.hischool.utils

import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

object FirestoreHelper {
    fun loadKantinData(firestore: FirebaseFirestore, listener: OnSuccessListener<QuerySnapshot>) {
        firestore.collection("kantin").get().addOnSuccessListener(listener)
    }
}
