package com.kravchenko_vadim.gdzs.accountHelper

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.kravchenko_vadim.gdzs.MainActivity
import com.kravchenko_vadim.gdzs.R
import com.kravchenko_vadim.gdzs.constant.GoogleAccConst

class AccountHelper(act:MainActivity) {
    private val act = act
    private lateinit var signInClient: GoogleSignInClient


    fun signUpWithEmail(email:String, password: String){
        if (email.isNotEmpty() && password.isNotEmpty()){
            act.myAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful){
                    sendEmailVerification(task.result?.user!!)
                    act.uiUpdate(task.result?.user)
                }else {
                    Toast.makeText(act, act.resources.getString(R.string.sign_up_error), Toast.LENGTH_LONG).show()
                }
            }
        }
    }
    fun signInWithEmail(email:String, password: String){
        if (email.isNotEmpty() && password.isNotEmpty()){
            act.myAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful){
                    act.uiUpdate(task.result?.user)
                }else {
                    Toast.makeText(act, act.resources.getString(R.string.sign_in_error), Toast.LENGTH_LONG).show()
                }
            }
        }
    }
    private fun getSignInClient():GoogleSignInClient{
        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("350006194847-gc3g770mfaff512r4gfr2hn2uq0had70.apps.googleusercontent.com")
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(act.applicationContext, gso)
    }
    fun signInWithGoogle() {
        signInClient = getSignInClient()
        act.launcher.launch(signInClient.signInIntent)
    }
    fun firebaseAuthWithGoogle(idToken: String){
        val credencial = GoogleAuthProvider.getCredential(idToken, null)
        act.auth.signInWithCredential(credencial).addOnCompleteListener { task ->
            if (task.isSuccessful){
                Toast.makeText(act, "Реєстрація прошла успішно", Toast.LENGTH_SHORT).show()
                act.uiUpdate(task.result?.user)
            } else {
                Toast.makeText(act, "Помилка при реєстрації", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun sendEmailVerification(user:FirebaseUser){
        user.sendEmailVerification().addOnCompleteListener { task->
            if (task.isSuccessful) {
                Toast.makeText(act, act.resources.getString(R.string.send_verification_done), Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(act, act.resources.getString(R.string.send_verification_email_error), Toast.LENGTH_LONG).show()
            }
        }
    }
}