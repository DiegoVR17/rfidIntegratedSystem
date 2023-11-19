package com.example.rfid_integrated_system_app.data

import android.util.Log
import com.example.rfid_integrated_system_app.data.model.UserRegistered
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class UserRepository {

    private var db = Firebase.firestore

    private var auth: FirebaseAuth = Firebase.auth
    suspend fun registerUser(email: String, password: String) : ResourceRemote<String?>{
        return try {
            val result = auth.createUserWithEmailAndPassword(email,password).await()
            //auth.sendPasswordResetEmail(email)
            //result.user?.sendEmailVerification()
            //result.user?.isEmailVerified
            ResourceRemote.Success(data = result.user?.uid)
        } catch (e: FirebaseAuthException){
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

    suspend fun loginUser(email: String, password: String): ResourceRemote<String?>{
        return try {
            val result = auth.signInWithEmailAndPassword(email,password).await()
            //auth.sendPasswordResetEmail(email)
            //result.user?.sendEmailVerification()
            //result.user?.isEmailVerified
            ResourceRemote.Success(data = result.user?.uid)
        } catch (e: FirebaseAuthException){
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

    suspend fun createUser(userRegistered: UserRegistered): ResourceRemote<String?> {
        return try {
            userRegistered.uid?.let { db.collection("users").document(it).set(userRegistered).await() }
            //auth.sendPasswordResetEmail(email)
            //result.user?.sendEmailVerification()
            //result.user?.isEmailVerified
            ResourceRemote.Success(data = userRegistered.uid)
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