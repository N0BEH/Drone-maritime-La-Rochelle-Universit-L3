package com.example.projetsmartphone

import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.Socket

class ClientTcpNMEA {
    private var adresse : String = "localhost"
    private var port : Int = 3000




    public fun reqTcp(){
        val client = Socket(adresse, port)
        val input = BufferedReader(InputStreamReader(client.inputStream))
        println("Client receiving [${input.readLine()}]")
        client.close()
    }
}