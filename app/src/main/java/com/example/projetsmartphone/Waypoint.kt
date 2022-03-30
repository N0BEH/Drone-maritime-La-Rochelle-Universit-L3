package com.example.projetsmartphone

/* RASSEL Valentin */
//Classe qui aura pour but d'être instancié pour enregistrement une coordonnée.

class Waypoint(lat : Double, long : Double, h : String)
{
    val latitude: Double = lat
    val longitude: Double = long

    val heure: String = h

    /*
    pour test
    val wp = Waypoint("4618.070", "00109.482", "N", "W", "155816.881")

        println("---")
        println("${wp.heure}")
        println("---")
     */

}