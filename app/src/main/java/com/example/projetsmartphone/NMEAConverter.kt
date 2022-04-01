package com.example.projetsmartphone

import java.io.BufferedReader

/* RASSEL Valentin */
//Class qui aura pour but de convertir des tram NMEA en données utilisable et inversement.

class NMEAConverter {

    fun trameToWaypoint(trame : String?): Waypoint
    {
        var wp = Waypoint()

        var res : List<String>? = trame?.split("$", "!")


        res?.forEach { it ->

            var splittedLine : List<String> = it.split(",")

            if(splittedLine[0].equals("GPGGA"))
            {
                wp.latitude = splittedLine[2].toDouble()
                wp.longitude = splittedLine[4].toDouble()
                wp.heure = splittedLine[1]
            }

            if(splittedLine[0].equals("GPVTG"))
            {
                wp.vitesseNoeud = splittedLine[5].toDouble()
                wp.vitesseKmh = splittedLine[7].toDouble()
            }

        }

       return wp
    }

}