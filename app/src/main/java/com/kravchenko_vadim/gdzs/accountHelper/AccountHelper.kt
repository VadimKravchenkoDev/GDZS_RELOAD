package com.kravchenko_vadim.gdzs.accountHelper

import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.*
import com.kravchenko_vadim.gdzs.MainActivity
import com.kravchenko_vadim.gdzs.R
import com.kravchenko_vadim.gdzs.constant.FirebaseConstant
import com.kravchenko_vadim.gdzs.constant.GoogleAccConst

class AccountHelper(act:MainActivity) {
    private val act = act
    private lateinit var signInClient: GoogleSignInClient


    fun signUpWithEmail(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            act.myAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        sendEmailVerification(task.result?.user!!)
                        act.uiUpdate(task.result?.user)
                    } else {
                        //Log.d("mylog", "Exception: " + task.exception)
                        //Log.d("mylog", "Exception: ${exception.errorCode}")
                        if (task.exception is FirebaseAuthUserCollisionException) {
                            val exception = task.exception as FirebaseAuthUserCollisionException
                            if (exception.errorCode == FirebaseConstant.ERROR_EMAIL_ALREADY_IN_USE) {
                                //Toast.makeText(act, FirebaseConstant.ERROR_EMAIL_ALREADY_IN_USE, Toast.LENGTH_LONG).show()
                                linkEmailTOg(email, password)
                            }
                        } else if (task.exception is FirebaseAuthInvalidCredentialsException) {
                            val exception = task.exception as FirebaseAuthInvalidCredentialsException
                            if (exception.errorCode == FirebaseConstant.ERROR_INVALID_EMAIL) {
                                Toast.makeText(act, FirebaseConstant.ERROR_INVALID_EMAIL, Toast.LENGTH_LONG).show()
                            }
                        }
                        if (task.exception is FirebaseAuthWeakPasswordException) {
                            val exception = task.exception as FirebaseAuthWeakPasswordException
                            Log.d("mylog", "Exception: " + exception.errorCode)
                            if (exception.errorCode == FirebaseConstant.ERROR_WEAK_PASSWORD) {
                                Toast.makeText(act, FirebaseConstant.ERROR_WEAK_PASSWORD, Toast.LENGTH_LONG).show()
                            }
                        }
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
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        val exception = task.exception as FirebaseAuthInvalidCredentialsException
                        Log.d("mylog", "Exception: + ${exception.errorCode}")
                        if (exception.errorCode == FirebaseConstant.ERROR_INVALID_EMAIL) {
                            Toast.makeText(act, FirebaseConstant.ERROR_INVALID_EMAIL, Toast.LENGTH_LONG).show()
                        } else if (exception.errorCode == FirebaseConstant.ERROR_WRONG_PASSWORD) {
                            Toast.makeText(act, FirebaseConstant.ERROR_WRONG_PASSWORD, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }
    private  fun linkEmailTOg(email: String, password: String){
        val credential = EmailAuthProvider.getCredential(email,password)
        if (act.myAuth.currentUser != null) {
            act.myAuth.currentUser?.linkWithCredential(credential)?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(act, act.resources.getString(R.string.link_done), Toast.LENGTH_LONG).show()
                }
            }
        } else {
            Toast.makeText(act, act.resources.getString(R.string.enter_to_g), Toast.LENGTH_LONG).show()
        }
    }
    private fun getSignInClient():GoogleSignInClient{
        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("350006194847-gc3g770mfaff512r4gfr2hn2uq0had70.apps.googleusercontent.com")
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(act, gso)
    }

    fun signOutGoogle() {
        getSignInClient().signOut()

    }
    fun firebaseAuthWithGoogle(idToken: String){
        val credencial = GoogleAuthProvider.getCredential(idToken, null)
        act.auth.signInWithCredential(credencial).addOnCompleteListener { task ->
            if (task.isSuccessful){
                Toast.makeText(act, "Реєстрація пройшла успішно", Toast.LENGTH_SHORT).show()
                act.uiUpdate(task.result?.user)
            } else {
                Toast.makeText(act, "Помилка при реєстрації", Toast.LENGTH_SHORT).show()
                Log.d("mylog", "Exception: + ${task.exception}")
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

    fun signInWithGoogle() {
        signInClient = getSignInClient()
        val intent = signInClient.signInIntent
        act.startActivityForResult(intent, GoogleAccConst.GOOGLE_SIGN_IN_REQUEST_CODE)
    }
}