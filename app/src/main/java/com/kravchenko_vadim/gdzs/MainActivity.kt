package com.kravchenko_vadim.gdzs

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.kravchenko_vadim.gdzs.databinding.ActivityMainBinding
import com.kravchenko_vadim.gdzs.dialogHelper.DialogHelper

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var binding: ActivityMainBinding
    private var dialogs = DialogHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(findViewById<Toolbar>(R.id.toolbar))
        val toggle = ActionBarDrawerToggle(this, binding.drawerLayout, toolbar, R.string.open, R.string.close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        binding.navView.setNavigationItemSelectedListener(this)
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

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.ac_sign_in -> {
                Toast.makeText(this, "pressed", Toast.LENGTH_SHORT).show()
            }
            R.id.ac_sign_up -> {
                Toast.makeText(this, "pressed", Toast.LENGTH_SHORT).show()
            }
            R.id.ac_sign_out -> {
                Toast.makeText(this, "pressed", Toast.LENGTH_SHORT).show()
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
}
