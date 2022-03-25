package com.example.projetsmartphone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.Socket

class MainActivity : AppCompatActivity() {
    private var client : ClientTcpNMEA = ClientTcpNMEA()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val adresse : String = "0.0.0.0"
        val port : Int = 9000
        val client = Socket(adresse, port)
        val input = BufferedReader(InputStreamReader(client.inputStream))
        println("Client receiving [${input.readLine()}]")
    }
}