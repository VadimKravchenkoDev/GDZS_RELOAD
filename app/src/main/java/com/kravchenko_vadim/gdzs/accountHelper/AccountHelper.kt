package com.kravchenko_vadim.gdzs.accountHelper

import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.kravchenko_vadim.gdzs.MainActivity
import com.kravchenko_vadim.gdzs.R
import com.kravchenko_vadim.gdzs.constant.GoogleAccConst

class AccountHelper(act: MainActivity) {
    private val act = act
    private lateinit var signInClient: GoogleSignInClient
    fun signUpWithEmail(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            act.myFirebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->

                    if (task.isSuccessful) {
                        sendEmailVerification(task.result?.user!!)
                        act.uiUpdate(task.result?.user)
                    } else {
                        Toast.makeText(
                            act,
                            act.resources.getString(R.string.sign_up_error),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }
    }
    fun signInWithEmail(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            act.myFirebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->

                    if (task.isSuccessful) {
                        act.uiUpdate(task.result?.user)
                    } else {
                        Toast.makeText(
                            act,
                            act.resources.getString(R.string.sign_in_error),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }
    }
    private fun getSignInClient():GoogleSignInClient{
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
        requestIdToken(act.getString(R.string.default_web_client_id)).build()
        return GoogleSignIn.getClient(act,gso)
    }
    fun signInWithGoogle(){
        signInClient=getSignInClient()
        val intent = signInClient.signInIntent
        act.startActivityForResult(intent, GoogleAccConst.GOOGLE_SIGN_IN_REQUEST_CODE)
    }

    fun signInFirebaseWithGoogle(token:String) {
        Log.d("MyLog", "Signing in with Google")
        val credential = GoogleAuthProvider.getCredential(token, null)

        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(act.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(act, googleSignInOptions)

// Отзыв доступа при необходимости
        googleSignInClient.revokeAccess().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Обработка успешного отзыва доступа
                Log.d("MyLog", "Access revoked successfully")
            } else {
                // Обработка ошибки отзыва доступа
                Log.e("MyLog", "Access revoke failed: ${task.exception?.message}")
            }
        }

        act.myFirebaseAuth.signInWithCredential(credential).addOnCompleteListener {
            task-> if(task.isSuccessful){
            Log.d("MyLog", "Sign in with Google successful")
                Toast.makeText(act, "Реєстрація пройшла успішно", Toast.LENGTH_LONG).show()
        }else {
            Log.e("MyLog", "Sign in with Google failed: ${task.exception?.message}")
            Toast.makeText(
                act,
                act.resources.getString(R.string.sign_in_error),
                Toast.LENGTH_LONG
            ).show()
        }}
    }



    private fun sendEmailVerification(user: FirebaseUser) {
        user.sendEmailVerification().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(
                    act,
                    act.resources.getString(R.string.send_verification_done),
                    Toast.LENGTH_LONG
                ).show()

            } else {
                Toast.makeText(
                    act,
                    act.resources.getString(R.string.send_verification_email_error),
                    Toast.LENGTH_LONG
                ).show()

            }
        }
    }
}