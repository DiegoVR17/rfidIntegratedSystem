package com.example.rfid_integrated_system_app.data

import android.util.Log
import com.example.rfid_integrated_system_app.data.model.User
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class UserAddRepository {

    private var db = Firebase.firestore
    suspend fun createUser(user: User, id: String): ResourceRemote<String?>{
        return try {
            db.collection("users_system").document(id).set(user).await()
            //auth.sendPasswordResetEmail(email)
            //result.user?.sendEmailVerification()
            //result.user?.isEmailVerified
            ResourceRemote.Success(data = user.id)
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

     suspend fun loadRegisteredUsers() : ResourceRemote<QuerySnapshot?> {
        return try {
            val result = db.collection("users_system").get().await()
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

    suspend fun deleteUser(user: User?): ResourceRemote<String?>{
        return try {
            val result = user?.id?.let { db.collection("users_system").document(it).delete().await() }
            ResourceRemote.Success(data = user?.id)
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