package com.kravchenko_vadim.gdzs

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager

//фунція для приховування клавіатури при натисканні на єкран
fun Activity.hideKeyboard() {
    val inputMethodManager =
        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    val currentFocusView = this.currentFocus
    if (currentFocusView != null) {
        inputMethodManager.hideSoftInputFromWindow(currentFocusView.windowToken, 0)
    }
}