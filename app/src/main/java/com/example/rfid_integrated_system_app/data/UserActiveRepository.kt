package com.example.rfid_integrated_system_app.data

import android.util.Log
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class UserActiveRepository {

    private var db = Firebase.firestore

    suspend fun loadActiveUsers() : ResourceRemote<QuerySnapshot?> {
        return try {
            val result = db.collection("id_reader").orderBy("date").get().await()
            ResourceRemote.Success(data = result)
        } catch (e: FirebaseFirestoreException){
            Log.e("FirebaseAuthException",e.localizedMessage)
            ResourceRemote.Error(message = e.localizedMessage)
        } catch (e: FirebaseNetworkException){
            Log.e("FirebaseNetworkException",e.localizedMessage)
            ResourceRemote.Error(message = e.localizedMessage)
        }
        catch (e: FirebaseException){
            Log.e("FirebaseException",e.localizedMessage)
            ResourceRemote.Error(message = e.localizedMessage)
        }
    }
}