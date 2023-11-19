package com.example.rfid_integrated_system_app.data

import android.content.ContentValues
import android.util.Log
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class DoorControlRepository {

    private var db = Firebase.firestore
    suspend fun open_close_door(estadoSwitch: HashMap<String, Boolean>): ResourceRemote<String?>{
        return try {
            db.collection("state_door")
                .document("open_door")
                .set(estadoSwitch).await()
            ResourceRemote.Success(data = "ok")
        } catch (e: FirebaseFirestoreException){
            Log.e("FirebaseFirestoreException",e.localizedMessage)
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