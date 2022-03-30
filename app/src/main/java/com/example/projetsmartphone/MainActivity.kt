package com.example.projetsmartphone


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.InetSocketAddress
import java.net.Socket

open class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Lancement du thread qui va permettre la lecture sur le serveur du generateur NMEA.
        //Les deux lignes si dessous sont à mettre dans l'activity qui va afficher les points.
        //NE PAS SUPPRIMER CE CODE DU COUP SVP.
        //C'est ClientTCP qui génère les Waypoint, a voir pour essayer de les récuperer ici.
        val client : ClientTcpNMEA = ClientTcpNMEA()
        client.start()
    }
}

