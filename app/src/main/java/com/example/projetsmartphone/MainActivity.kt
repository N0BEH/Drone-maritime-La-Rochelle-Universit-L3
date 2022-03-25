package com.example.projetsmartphone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val wp = Waypoint("4618.070", "00109.482", "N", "W", "155816.881")

        println("---")
        println("${wp.heure}")
        println("---")
    }
}