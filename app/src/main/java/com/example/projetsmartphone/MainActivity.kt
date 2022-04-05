package com.example.projetsmartphone

import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView


open class MainActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        val mapFragment = MapFragment()
        val accelerometerFragment = AccelerometerFragment()
        val customMapFragment = CustomMapFragment()

        makeCurrentFragment(mapFragment)

        val buttomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        buttomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.map -> makeCurrentFragment(mapFragment)
                R.id.accelerometer -> makeCurrentFragment(accelerometerFragment)
                R.id.custom_map -> makeCurrentFragment(customMapFragment)
            }
            true
        }

    }

    private fun makeCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.container, fragment)
            commit()
        }
    }

}

