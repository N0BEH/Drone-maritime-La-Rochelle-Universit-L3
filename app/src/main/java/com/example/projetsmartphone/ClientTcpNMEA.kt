package com.example.projetsmartphone

import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.InetSocketAddress
import java.net.Socket

class ClientTcpNMEA : Thread() {
    override fun run() {
        val client = Socket()
        val socketAddress : InetSocketAddress = InetSocketAddress("10.13.8.197", 9000)
        try{
            client.connect(socketAddress)

            val buffer = BufferedReader(InputStreamReader(client.inputStream))
            var txt = buffer.readLine()
            while(txt != null) {
                println("Le client recoit $txt")
                txt = buffer.readLine()
            }
        }catch(e : Exception){
            println("aaaaaa")
            client.close()
            println("Exception caught:$e")
        }
    }
}