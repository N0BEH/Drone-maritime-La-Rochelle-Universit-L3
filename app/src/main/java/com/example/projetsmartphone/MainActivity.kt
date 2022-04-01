package com.example.projetsmartphone

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.*
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.InetSocketAddress
import java.net.Socket
import java.util.concurrent.TimeUnit


open class MainActivity : AppCompatActivity(), MessageListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        val mapFragment = MapFragment()
        val listFragment = ListFragment()
        val customMapFragment = CustomMapFragment()

        makeCurrentFragment(mapFragment)

        val buttomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        buttomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.map -> makeCurrentFragment(mapFragment)
                R.id.list -> makeCurrentFragment(listFragment)
                R.id.custom_map -> makeCurrentFragment(customMapFragment)
            }
            true
        }
        launchClient()
    }

    override fun onMessage(text: String?) {

        var nmea = NMEAConverter()

        var wp = nmea.trameToWaypoint(text)

        println(wp.latitude)
        println(wp.longitude)
        println(wp.heure)
        println(wp.vitesseNoeud)
        println(wp.vitesseKmh)
        println("\n\n")

    }

    //On lance la connexion au websocket.
    fun launchClient()
    {
        WebSocketManager.init("http://192.168.1.181:9000", this)
        WebSocketManager.connect()
    }


    private fun makeCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.container, fragment)
            commit()
        }
    }

    internal fun onOpenMap(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, CustomMapFragment())
            .commitNow()
    }


}

