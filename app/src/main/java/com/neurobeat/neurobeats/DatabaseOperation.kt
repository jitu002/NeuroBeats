package com.neurobeat.neurobeats

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await


class DatabaseOperation {
    fun addDataToFirestore(user:User){
        val db= FirebaseFirestore.getInstance()
        val userMap = hashMapOf(
            "name" to user.usrName,
            "age" to user.usrAge,
            "email" to user.usrEmail
        )

        // Add user data to the specific document within the userDetail collection
        db.collection("userDetails").document("user")
            .set(userMap)
            .addOnSuccessListener {
                Log.d("Firebase","Successfully added document")
            }
            .addOnFailureListener { e ->
                // Handle failure
                Log.d("Firebase error","$e")
            }

    }
    suspend fun fetchDataFromFirebase(auth: FirebaseAuth, firestore: FirebaseFirestore):User{
        val userId= auth.currentUser?.uid

        var userData=User("",0,"")

        if(userId!=null){
            val dbData=firestore.collection("userDetails").document(userId)
            val dbDataSnap=dbData.get().await()

            if(dbDataSnap.exists()){
                val userDataSet=dbDataSnap.toObject(User::class.java)
                userDataSet?.let {
                    userData=it
                }

            }


            }
        return userData

    }

}