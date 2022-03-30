package com.example.projetsmartphone

import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.InetSocketAddress
import java.net.Socket

class ClientTcpNMEA : Thread() {
    override fun run() {
        val client = Socket()
        //FAIRE ATTENTION A L'IP SA DEPEND DE LA MACHINE !!!
        val socketAddress : InetSocketAddress = InetSocketAddress("192.168.84.74", 9000)
        try{
            client.connect(socketAddress)

            val buffer = BufferedReader(InputStreamReader(client.inputStream))

            val nmea = NMEAConverter()
            //Creation d'un nouveau waypoint.
            val wp = nmea.trameToWaypoint(buffer)

            //Gerer l'affichage du point sur la map pour visualiser le déplacement du bateau.

        }catch(e : Exception){
            println("aaaaaa")
            client.close()
            println("Exception caught:$e")
        }
    }
}