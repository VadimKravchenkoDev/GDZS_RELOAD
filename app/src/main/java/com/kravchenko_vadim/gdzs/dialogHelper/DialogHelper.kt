package com.kravchenko_vadim.gdzs.dialogHelper

import android.app.AlertDialog
import com.kravchenko_vadim.gdzs.MainActivity
import com.kravchenko_vadim.gdzs.databinding.SignDialogBinding

class DialogHelper(act:MainActivity) {
    private val act = act
    fun createSignDialog(){
        val builder = AlertDialog.Builder(act)
        val rootDialogElement = SignDialogBinding.inflate(act.layoutInflater)
        val view = rootDialogElement.root
        builder.setView(view)
        builder.show()
    }
}