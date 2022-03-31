package com.example.projetsmartphone

import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class ListFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val listFragmentView = inflater.inflate(R.layout.fragment_list, container, false)
        val listView = listFragmentView.findViewById<ListView>(R.id.liste_itineraire)
        val nomItineraire = arrayOf(String())


        val state = Environment.getExternalStorageState()

        if (state == Environment.MEDIA_MOUNTED) {
            Log.d("TAG", "couocu")
            if (isExternalStorageReadable()) {
                Log.d("TAG", "is external")
                try {

                    var i = 0

                    Log.d("TAG", "${requireActivity().openFileInput("$i.txt").available()}")
                    while (File("$i.txt").exists()){

                        Log.d("TAG", "dans el while $i")

                        var fileName = "$i.txt"

                        var fileInputStream: FileInputStream = requireActivity().openFileInput(fileName)

                        var text = ""

                        fileInputStream.use {
                            text =  it.bufferedReader().use {
                                it.readText()
                            }

                            Log.d("TAG", "LOADED: $text")
                        }
                        fileInputStream.close()

                        nomItineraire[i] = "Mon itinéraire $i"

                        i += 1

                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }





        val arrayAdapter: ArrayAdapter<String> = ArrayAdapter(
            listFragmentView.context, android.R.layout.simple_list_item_1, nomItineraire
        )

        listView.adapter = arrayAdapter

        listView.setOnItemClickListener { adapterView, view, i, l ->
            Toast.makeText(listFragmentView.context, nomItineraire[i], Toast.LENGTH_LONG)
                .show()
        }

        return listFragmentView
    }

    fun isExternalStorageReadable(): Boolean {
        return Environment.getExternalStorageState() in
                setOf(Environment.MEDIA_MOUNTED, Environment.MEDIA_MOUNTED_READ_ONLY)
    }

}