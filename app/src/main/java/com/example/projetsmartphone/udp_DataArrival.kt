package com.example.projetsmartphone

import com.e.udpchat1.MainActivity

class udp_DataArrival: Runnable, MainActivity() {
    public override fun run() {
        println("${Thread.currentThread()} Runnable Thread Started.")
        while (true){
            receiveUDP()
        }
    }
}