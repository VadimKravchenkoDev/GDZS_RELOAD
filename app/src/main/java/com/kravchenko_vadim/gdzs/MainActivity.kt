package com.kravchenko_vadim.gdzs

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.GravityCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kravchenko_vadim.gdzs.constant.DialogConst
import com.kravchenko_vadim.gdzs.constant.GoogleAccConst
import com.kravchenko_vadim.gdzs.databinding.ActivityMainBinding
import com.kravchenko_vadim.gdzs.dialogHelper.DialogHelper

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var binding: ActivityMainBinding
    private val tvAccount: TextView by lazy {
        binding.navView.getHeaderView(0).findViewById(R.id.tvAccountEmail)
    }
    private lateinit var dialogs: DialogHelper
    val myAuth = FirebaseAuth.getInstance()
    lateinit var auth: FirebaseAuth
    private lateinit var googleSignInLauncher: ActivityResultLauncher<Intent>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        init()
        binding.buttonNext.setOnClickListener {
            val intent = Intent(this, CalculatorSettingsActivity::class.java)
            startActivity(intent)
            // запуск активити з анімацією
            val options = ActivityOptionsCompat.makeCustomAnimation(
                this,
                R.anim.fade_in, R.anim.fade_out
            )
            ActivityCompat.startActivity(this, intent, options.toBundle())
        }
        googleSignInLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                    try {
                        val account = task.getResult(ApiException::class.java)
                        if (account != null) {
                            dialogs.accHelper.signInFirebaseWithGoogle(account.idToken!!)
                        }
                    } catch (e: ApiException) {
                        Log.d("log", "Api exception")
                    }
                }
            }
        dialogs = DialogHelper(this, googleSignInLauncher)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == GoogleAccConst.GOOGLE_SIGN_IN_REQUEST_CODE){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account!= null){
                    dialogs.accHelper.signInFirebaseWithGoogle(account.idToken!!)
                }
            }catch (e:ApiException){
                Log.d("log", "Api exception")
            }

        }

        super.onActivityResult(requestCode, resultCode, data)
    }
    override fun onStart() {
        super.onStart()
        uiUpdate(myAuth.currentUser)
    }

    private fun init() {
        setSupportActionBar(binding.toolbar)
        val toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar,
            R.string.open,
            R.string.close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        binding.navView.setNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.ac_sign_in -> {
                dialogs.createSignDialog(DialogConst.Sign_In_State)
            }

            R.id.ac_sign_up -> {
                dialogs.createSignDialog(DialogConst.Sign_Up_State)
            }

            R.id.ac_sign_out -> {
                uiUpdate(null)
                myAuth.signOut()
            }

            R.id.guidebook_cat2 -> {
                Toast.makeText(this, "pressed", Toast.LENGTH_SHORT).show()
            }

            R.id.settings_cat3 -> {
                val intent = Intent(this, MainActivitySettings::class.java)
                startActivity(intent)
                val options = ActivityOptionsCompat.makeCustomAnimation(
                    this,
                    R.anim.fade_in, R.anim.fade_out
                )
                ActivityCompat.startActivity(this, intent, options.toBundle())
            }

            R.id.about_cat4 -> {
                Toast.makeText(this, "pressed", Toast.LENGTH_SHORT).show()
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    fun uiUpdate(user: FirebaseUser?) {
        tvAccount.text = if (user == null) {
            resources.getString(R.string.not_reg)
        } else {
            user.email
        }
    }
}
