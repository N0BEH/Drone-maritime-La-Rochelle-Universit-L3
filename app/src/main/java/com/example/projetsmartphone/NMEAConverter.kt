package com.example.projetsmartphone

import java.io.BufferedReader

/* RASSEL Valentin */
//Class qui aura pour but de convertir des tram NMEA en données utilisable et inversement.

class NMEAConverter {

    //Classe Static ?

    //NMEA --> Waypoint
    //Waypoint --> NMEA

    fun trameToWaypoint(trame : BufferedReader): Waypoint
    {
        print("call par clienttcp")

        var wp = Waypoint()

        var txt = trame.readLine()
        while(txt != null) {

            var splittedLine : List<String> = txt.split(",")


            if(splittedLine[0].equals("\$GPGGA"))
            {
                wp.latitude = splittedLine[2].toDouble()
                wp.longitude = splittedLine[4].toDouble()
                wp.heure = splittedLine[1]
            }

            if(splittedLine[0].equals("\$GPVTG"))
            {
                wp.vitesseNoeud = splittedLine[5].toDouble()
                wp.vitesseKmh = splittedLine[7].toDouble()
            }

            println("reception")
            print(wp.latitude)
            print(" - ")
            println(wp.longitude)

            //println("Le client recoit $txt")
            txt = trame.readLine()
        }


       return wp
    }

}