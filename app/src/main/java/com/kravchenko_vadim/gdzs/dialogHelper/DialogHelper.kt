package com.kravchenko_vadim.gdzs.dialogHelper

import android.app.AlertDialog
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
        if (index == DialogConst.Sign_Up_State){
            binding.tvSignTitle.text = act.resources.getString(R.string.ac_sign_up)
            binding.btSignUpIn.text = act.resources.getString(R.string.sign_up_action)

        } else {
            binding.tvSignTitle.text = act.resources.getString(R.string.ac_sign_in)
            binding.btSignUpIn.text = act.resources.getString(R.string.sign_in_action)
        }
        binding.btSignUpIn.setOnClickListener {
            if (index == DialogConst.Sign_Up_State){
                accHelper.signUpWithEmail(binding.edSignEmail.text.toString(), binding.edSignPassword.text.toString())
            }else{

            }
        }
        builder.setView(view)
        builder.show()
    }

}