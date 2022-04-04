package com.example.projetsmartphone

/* RASSEL Valentin */
//Class qui aura pour but de convertir des tram NMEA en données utilisable et inversement.

class NMEAConverter {

    //Methode static à utiliser directement, pas besoin d'instancier de NMEAConverter
    companion object {
        fun trameToWaypoint(trame : String?): Waypoint
        {
            var wp = Waypoint()

            var res : List<String>? = trame?.split("$", "!")


            res?.forEach { it ->

                var splittedLine : List<String> = it.split(",")

                if(splittedLine[0].equals("GPGGA"))
                {
                    var latDizaine: Int = (splittedLine[2].toDouble()/100).toInt()
                    var latUnites : Double = splittedLine[2].toDouble() - latDizaine*100

                    var longDizaine: Int = (splittedLine[4].toDouble()/100).toInt()
                    var longUnites : Double = splittedLine[4].toDouble() - longDizaine*100


                    wp.latitude = ((latUnites/60)+latDizaine)
                    wp.longitude = -((longUnites/60)+longDizaine)
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



}