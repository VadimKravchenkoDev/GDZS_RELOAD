package com.kravchenko_vadim.gdzs


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.kravchenko_vadim.gdzs.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
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


}
