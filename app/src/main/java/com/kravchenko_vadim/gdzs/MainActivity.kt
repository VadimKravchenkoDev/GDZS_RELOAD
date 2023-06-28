package com.kravchenko_vadim.gdzs

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kravchenko_vadim.gdzs.accountHelper.AccountHelper
import com.kravchenko_vadim.gdzs.constant.DialogConst
import com.kravchenko_vadim.gdzs.constant.GoogleAccConst
import com.kravchenko_vadim.gdzs.databinding.ActivityMainBinding
import com.kravchenko_vadim.gdzs.dialogHelper.DialogHelper

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var tvAccount : TextView
    lateinit var binding: ActivityMainBinding
    private var dialogs = DialogHelper(this)
    val myAuth = FirebaseAuth.getInstance()
    lateinit var auth: FirebaseAuth
    lateinit var launcher: ActivityResultLauncher<Intent>
    val accountHelper = AccountHelper(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try{
                val account = task.getResult(ApiException::class.java)
                if (account != null){
                    dialogs.accHelper.signInWithGoogle(account.idToken)
                }
            }catch (e:ApiException){
                Log.d("log", "Api exception")
            }
        }
        init()
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        binding.buttonNext.setOnClickListener {
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }
    }
    override fun onStart() {
        super.onStart()
        uiUpdate(myAuth.currentUser)
    }
    private fun init(){
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(findViewById<Toolbar>(R.id.toolbar))
        val toggle = ActionBarDrawerToggle(this, binding.drawerLayout, toolbar, R.string.open, R.string.close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        binding.navView.setNavigationItemSelectedListener(this)
        tvAccount = binding.navView.getHeaderView(0).findViewById(R.id.tvAccountEmail)
    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
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
                Toast.makeText(this, "pressed", Toast.LENGTH_SHORT).show()
            }
            R.id.about_cat4 -> {
                Toast.makeText(this, "pressed", Toast.LENGTH_SHORT).show()
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
    fun uiUpdate(user:FirebaseUser?){
        tvAccount.text = if(user == null){
            resources.getString(R.string.not_reg)
        } else {
           user.email
        }
    }
}
