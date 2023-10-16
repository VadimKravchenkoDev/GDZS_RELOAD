package com.kravchenko_vadim.gdzs


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.GravityCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.kravchenko_vadim.gdzs.constant.DialogConst
import com.kravchenko_vadim.gdzs.constant.GoogleAccConst
import com.kravchenko_vadim.gdzs.databinding.ActivityMainBinding
import com.kravchenko_vadim.gdzs.dialogHelper.DialogHelper


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var textNameAccount: TextView
    lateinit var binding: ActivityMainBinding
    private val dialogHelper = DialogHelper(this)
    val myFirebaseAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init() //navigation view
        binding.buttonNext.setOnClickListener {
            // запуск активити з анімацією
            val intent = Intent(this, CalculatorSettingsActivity::class.java)
            ActivityAnimation.startActivityWithAnimation(this, intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == GoogleAccConst.GOOGLE_SIGN_IN_REQUEST_CODE){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try{
                val account = task.getResult(ApiException::class.java)
                if (account != null){
                    dialogHelper.accHelper.signInFirebaseWithGoogle(account.idToken!!)
                }
            }catch (e:ApiException){
                Log.d("mylog", "Api error: ${e.message}")
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onStart() {
        super.onStart()
        uiUpdate(myFirebaseAuth.currentUser)
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
        textNameAccount = binding.navView.getHeaderView(0).findViewById(R.id.textNameAccountEmail)// ім'я акаунту
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.ac_sign_up -> {
                dialogHelper.createSignDialog(DialogConst.Sign_Up_State)
            }

            R.id.ac_sign_in -> {
                dialogHelper.createSignDialog(DialogConst.Sign_In_State)
            }

            R.id.ac_sign_out -> {
                uiUpdate(null)
                myFirebaseAuth.signOut()
            }

            R.id.guidebook_cat2 -> {
                Toast.makeText(this, "pressed", Toast.LENGTH_SHORT).show()
            }

            R.id.settings_cat3 -> {
                val intent = Intent(this, MainActivitySettings::class.java)
                ActivityAnimation.startActivityWithAnimation(this, intent)
            }

            R.id.about_cat4 -> {
                Toast.makeText(this, "pressed", Toast.LENGTH_SHORT).show()
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
    //ім'я акаунту в Navigation view
    fun uiUpdate(user: FirebaseUser?){
        textNameAccount.text = if (user == null){
            resources.getString(R.string.not_reg)
        }else{
            user.email
        }
    }

}
