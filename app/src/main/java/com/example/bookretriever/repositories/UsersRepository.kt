package com.example.bookretriever.repositories

import android.util.Log
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

sealed class UserState {
    object NotVerified : UserState()
    object Verified : UserState()
    object Unknown : UserState()
    data class Error(val message: String) : UserState()
}

class UsersRepository {
    companion object {
        private const val TAG = "USERS_REPOSITORY"
    }

    private val _userState = MutableStateFlow<UserState>(UserState.Unknown)
    val userState = _userState.asStateFlow()

    private val _auth = Firebase.auth
    private val databaseReference = FirebaseDatabase.getInstance().reference

    //change state flow according to the result of sign in action
    suspend fun logInUser(email: String, password: String) =
        suspendCoroutine { continuation ->
            _auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                if (!it.isSuccessful) {
                    _userState.value = UserState.Error(it.exception?.message.toString())
                    return@addOnCompleteListener
                }
                val userVerified = it.result.user?.isEmailVerified == true

                _userState.value = if (userVerified) UserState.Verified else UserState.NotVerified
                continuation.resume(userVerified)
                Log.d(
                    TAG,
                    "Login state: $userVerified"
                )
            }
        }

    suspend fun registerUser(name: String, email: String, password: String) =
        suspendCoroutine {
            _auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val firebaseUser: FirebaseUser? = _auth.currentUser
                    if (firebaseUser == null) {
                        Log.d(TAG, "registerUser: user is null")
                        it.resume(false)
                        return@addOnCompleteListener
                    }

                    //send email verification link
                    emailVerification(firebaseUser)

                    signOut()
                    it.resume(true)

//                val user = User(name, email, password)
//            `    databaseReference.child(email)
//                    .child(firebaseUser.uid)
//                    .child("user")
//                    .setValue(user)
//                    .addOnCompleteListener {
//                        if (it.isSuccessful) {
//                            _isRegistered.value = true
//                            Log.d(TAG, "registerUser: success")
//                        } else {
//                            _isRegistered.value = false
//                            Log.d(
//                                TAG,
//                                "registerUser: fail\nMessage: ${it.exception}"
//                            )
//                        }
//                    }

                } else {
                    Log.d(TAG, "Register state: failed\nException: ${task.exception}")
                    it.resume(false)
                }
            }
        }

    private fun emailVerification(firebaseUser: FirebaseUser) {
        firebaseUser.sendEmailVerification().addOnSuccessListener {
            Log.d(TAG, "Verification state: successful")
        }.addOnFailureListener {
            Log.d(TAG, "Verification state: failed\nMessage: ${it.message}")
        }
    }

    fun resetPassword(resetMail: String): Boolean {
        var wasReset = false
//        _auth.sendPasswordResetEmail(resetMail).addOnSuccessListener {
//            Log.d(TAG, "resetPassword state: success")
//            wasReset = true
//        }.addOnFailureListener {
//            Log.d(TAG, "resetPassword state: fail\nMessage: ${it.message}")
//        }
        return wasReset
    }

    private fun signOut() {
        _auth.signOut()
        _userState.value = UserState.Unknown
    }

}