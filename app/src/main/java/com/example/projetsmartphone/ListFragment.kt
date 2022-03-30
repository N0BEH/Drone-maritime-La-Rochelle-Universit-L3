package com.example.projetsmartphone

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast

class ListFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val listFragmentView = inflater.inflate(R.layout.fragment_list, container, false)
        val listView = listFragmentView.findViewById<ListView>(R.id.liste_itineraire)
        val nomItineraire = arrayOf("Mon iti 1", "Mon iti 2", "Mon iti 3")

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
}