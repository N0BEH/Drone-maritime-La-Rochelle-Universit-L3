package com.example.projetsmartphone

/* RASSEL Valentin */
//Classe qui aura pour but d'être instancié pour enregistrement une coordonnée.

class Waypoint(lat: Double = 0.0, long: Double = 0.0, h: String = "", vn: Double = 0.0, vk: Double = 0.0)
{
    var latitude: Double = lat
    var longitude: Double = long

    var vitesseNoeud: Double = vn
    var vitesseKmh: Double = vk

    var heure: String = h

}