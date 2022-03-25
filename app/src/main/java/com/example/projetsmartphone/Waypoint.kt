package com.example.projetsmartphone

/* RASSEL Valentin */
//Classe qui aura pour but d'être instancié pour enregistrement une coordonnée.

class Waypoint(lat : String, long : String, indiLat : String, indiLong : String, h : String)
{
    val latitude: String = lat
    val longitude: String = long

    val heure: String = h

    //NORTH / SOUTH
    val indicateurLatitude: String = indiLat
    //EAST / WEST
    val indicateurLongitude: String = indiLong

    /*
    pour test
    val wp = Waypoint("4618.070", "00109.482", "N", "W", "155816.881")

        println("---")
        println("${wp.heure}")
        println("---")
     */

}