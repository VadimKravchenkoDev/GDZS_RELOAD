package com.kravchenko_vadim.gdzs.dialogHelper

import android.app.AlertDialog
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import com.kravchenko_vadim.gdzs.MainActivity
import com.kravchenko_vadim.gdzs.R
import com.kravchenko_vadim.gdzs.accountHelper.AccountHelper
import com.kravchenko_vadim.gdzs.constant.DialogConst
import com.kravchenko_vadim.gdzs.databinding.SignDialogBinding

class DialogHelper(private val activity: MainActivity, private val googleSignInLauncher: ActivityResultLauncher<Intent>) {
    private val act = activity
    val accHelper = AccountHelper(act)
    fun createSignDialog(index:Int){
        val builder = AlertDialog.Builder(act)
        val binding = SignDialogBinding.inflate(act.layoutInflater)
        val view = binding.root
        builder.setView(view)
        setDialogState(index, binding)
        val dialog = builder.create()
        binding.btSignUpIn.setOnClickListener {
            setOnClickSignUpIn(index, binding, dialog)
        }
        binding.btForgetPass.setOnClickListener {
            setOnClickResetPassword(binding, dialog)
        }
        binding.btGoogleSignIn.setOnClickListener {
            accHelper.signInWithGoogle()
        }
        dialog.show()
    }
    private fun setOnClickResetPassword(binding: SignDialogBinding, dialog: AlertDialog?) {
        if (binding.edSignEmail.text.isNotEmpty()){
            act.myAuth.sendPasswordResetEmail(binding.edSignEmail.text.toString()).addOnCompleteListener { task ->
                if (task.isSuccessful){
                    Toast.makeText(act,  R.string.reset_email_was_sent, Toast.LENGTH_LONG).show()
                }
            }
            dialog?.dismiss()
        } else{
            binding.tvDialogMessage.visibility = View.VISIBLE
        }
    }
    private fun setOnClickSignUpIn(index: Int, binding: SignDialogBinding, dialog: AlertDialog?) {
        dialog?.dismiss()
        when (index) {
            DialogConst.Sign_Up_State -> {
                accHelper.signUpWithEmail(binding.edSignEmail.text.toString(), binding.edSignPassword.text.toString())
            }
            DialogConst.Sign_In_State -> {
                val signInIntent = accHelper.getSignInIntent()
                googleSignInLauncher.launch(signInIntent)
            }
            else -> {
                accHelper.signInWithEmail(binding.edSignEmail.text.toString(), binding.edSignPassword.text.toString())
            }
        }
    }
    private fun setDialogState(index: Int, binding: SignDialogBinding) {
        if (index == DialogConst.Sign_Up_State){
            binding.tvSignTitle.text = act.resources.getString(R.string.ac_sign_up)
            binding.btSignUpIn.text = act.resources.getString(R.string.sign_up_action)
        } else {
            binding.tvSignTitle.text = act.resources.getString(R.string.ac_sign_in)
            binding.btSignUpIn.text = act.resources.getString(R.string.sign_in_action)
            binding.btForgetPass.visibility = View.VISIBLE
        }
    }
}