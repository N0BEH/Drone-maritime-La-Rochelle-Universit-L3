package com.example.projetsmartphone

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.InetSocketAddress
import java.net.Socket


open class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*
        val client : ClientTcpNMEA = ClientTcpNMEA()
        client.start()
        */

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

        val producer = producewp()

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



    fun CoroutineScope.producewp() = produce<Waypoint> {

        val client = Socket()

        //FAIRE ATTENTION A L'IP SA DEPEND DE LA MACHINE !!!
        val socketAddress : InetSocketAddress = InetSocketAddress("192.168.37.74", 9000)
        try{
            client.connect(socketAddress)

            val buffer = BufferedReader(InputStreamReader(client.inputStream))

            val nmea = NMEAConverter()
            val wp = nmea.trameToWaypoint(buffer)
            while(true) send(wp)

            //Gerer l'affichage du point sur la map pour visualiser le déplacement du bateau.


        }catch(e : Exception){
            println("aaaaaa")
            client.close()
            println("Exception caught:$e")
        }
    }

}

