package com.example.projetsmartphone

interface MessageListener
{
    //Methode à implémenter pour récuuperer les trames NMEA du websocket.
    fun onMessage(text: String?)

    //Exemple d'utilisation :
    /*
    override fun onMessage(text: String?) {

        var wp = NMEAConverter.trameToWaypoint(text)

        println(wp.latitude)
        println(wp.longitude)
        println(wp.heure)
        println(wp.vitesseNoeud)
        println(wp.vitesseKmh)
        println("\n\n")

    }*/

    //Méthode à implémenter pour lance client.
    //A call dans le main.
    fun launchClient()

    //Exemple
    /*
    override fun launchClient()
    {
        WebSocketManager.init("http://192.168.1.181:9000", this)
        WebSocketManager.connect()
    }*/

}