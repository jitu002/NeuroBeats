package com.neurobeat.neurobeats

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject


class DatabaseOperation {
    fun addDataToFirestore(user:User,userId:String){

        println(userId)
        val db= FirebaseFirestore.getInstance()
        val userMap = hashMapOf(
            "usrName" to user.usrName,
            "usrAge" to user.usrAge,
            "usrEmail" to user.usrEmail
        )

        // Add user data to the specific document within the userDetail collection
        db.collection("userDetails").document(userId)
            .set(userMap)
            .addOnSuccessListener {
                Log.d("Firebase","Successfully added document")
            }
            .addOnFailureListener { e ->
                // Handle failure
                Log.d("Firebase error", "$e")
            }


    }
    fun fetchDataFromFirebase(auth: FirebaseAuth, firestore: FirebaseFirestore, callback: (User?) -> Unit) {
        val userId = auth.currentUser?.uid
        println("UserId: $userId")

        if (userId != null) {
            try {
                firestore.collection("userDetails").document(userId).get()
                    .addOnSuccessListener { doc ->
                        if (doc.exists()) {
                            val userData = doc.toObject<User>()
                            callback(userData) // Pass the userData to the callback
                        } else {
                            Log.d("User", "No such document")
                            callback(null) // Handle the case where document does not exist
                        }
                    }
                    .addOnFailureListener { e ->
                        Log.e("fetchDataFromFirebase", "Error fetching document", e)
                        callback(null) // Handle the failure case
                    }
            } catch (e: Exception) {
                println("$e")
                callback(null) // Handle any exception
            }
        } else {
            callback(null)
        }
    }
}