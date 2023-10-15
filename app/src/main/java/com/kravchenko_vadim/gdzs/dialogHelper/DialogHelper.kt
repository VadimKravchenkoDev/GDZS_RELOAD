package com.kravchenko_vadim.gdzs.dialogHelper

import android.app.AlertDialog
import android.view.View
import com.kravchenko_vadim.gdzs.MainActivity
import com.kravchenko_vadim.gdzs.R
import com.kravchenko_vadim.gdzs.accountHelper.AccountHelper
import com.kravchenko_vadim.gdzs.constant.DialogConst
import com.kravchenko_vadim.gdzs.databinding.SignDialogBinding

class DialogHelper(act: MainActivity) {
    private val act = act
    private val accHelper = AccountHelper(act)
    fun createSignDialog(index: Int) {
        val builder = AlertDialog.Builder(act)
        val binding = SignDialogBinding.inflate(act.layoutInflater)
        val view = binding.root
        builder.setView(view)

        setDialogState(index, binding)
        val dialog = builder.create()
        binding.btSignUpIn.setOnClickListener {
            dialog.dismiss()
            if (index == DialogConst.Sign_Up_State){
                accHelper.signUpWithEmail(binding.edSignEmail.text.toString(), binding.edSignPassword.text.toString())
            }else{
                accHelper.signInWithEmail(binding.edSignEmail.text.toString(), binding.edSignPassword.text.toString())

            }
        }

        dialog.show()
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