package com.example.projetsmartphone

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.produce
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.InetSocketAddress
import java.net.Socket

/*
class ClientTcpNMEA : Thread() {
    override fun run() {
        val client = Socket()

        //FAIRE ATTENTION A L'IP SA DEPEND DE LA MACHINE !!!
        val socketAddress : InetSocketAddress = InetSocketAddress("192.168.37.74", 9000)
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
    }*/

class ClientTcpNMEA {

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