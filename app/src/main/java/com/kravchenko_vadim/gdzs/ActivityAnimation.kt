package com.kravchenko_vadim.gdzs

import android.content.Context
import android.content.Intent
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat

class ActivityAnimation {
    companion object {
        fun startActivityWithAnimation(context: Context, intent: Intent) {
            val options = ActivityOptionsCompat.makeCustomAnimation(
                context,
                R.anim.fade_in, R.anim.fade_out
            )
            ActivityCompat.startActivity(context, intent, options.toBundle())
        }
    }
}