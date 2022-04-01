package com.example.projetsmartphone

import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class CustomMapFragment : Fragment(){

    private lateinit var mMap: GoogleMap
    private val mMarkers = arrayListOf<Waypoint>()
    private var mapReady = false
    private var nbrFichier = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_custom_map, container, false)
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment


        val bouttonSave = view.findViewById<Button>(R.id.buttonSave) as Button

        bouttonSave.setOnClickListener {
            if ( mMarkers.size > 0) {
                clicBtnSave()
                mMap.clear()
                mMarkers.clear()
            }
        }

        mapFragment.getMapAsync {
            googleMap -> mMap = googleMap
            mMap.clear()
            mMarkers.clear()

            mapReady = true

            val minime = LatLng(46.14547556383779, -1.1685415729880333)
            mMap.moveCamera(CameraUpdateFactory.newLatLng(minime))
            mMap.animateCamera(CameraUpdateFactory.zoomTo( 12f ))

            clicMap()
            clicMarker()
        }
        return view
    }

    private fun clicBtnSave(){

        val state = Environment.getExternalStorageState()

        if (state == Environment.MEDIA_MOUNTED) {

            val fileName = "$nbrFichier.txt"
            var text = ""
            nbrFichier += 1

            if (isExternalStorageWritable() && isExternalStorageReadable()) {

                mMarkers.forEach{
                    text += "${it.latitude},${it.longitude},${it.heure}\n"
                }

                var fileOutputStream: FileOutputStream

                try {
                    fileOutputStream = requireActivity().openFileOutput(fileName, Context.MODE_PRIVATE)
                    fileOutputStream.write("$text".toByteArray())

                    fileOutputStream.close()


                    var fileInputStream: FileInputStream = requireActivity().openFileInput(fileName)

                    var myExternalFile = File(fileName).path


                    fileInputStream.use {
                        text =  it.bufferedReader().use {
                            it.readText()
                        }

                        Log.d("TAG", "LOADED: $text")
                        Log.d("TAG", "dir: $myExternalFile")
                    }
                    fileInputStream.close()


                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

    }


    private fun clicMap(){

        mMap.setOnMapClickListener { it ->

            val marker = LatLng(it.latitude, it.longitude)
            mMap.addMarker(MarkerOptions().position(marker))

            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("HHmmss.SSS")
            val formatted = current.format(formatter)

            val waypoint = Waypoint(it.latitude, it.longitude, formatted)

            mMarkers.add(waypoint)

            var i = 0
            var oldLat = 0.0
            var oldLong = 0.0
            var newLat: Double
            var newLong: Double
            if (mMarkers.size > 1) {

                mMarkers.forEach {

                    newLat = it.latitude
                    newLong = it.longitude

                    if (i >= 1) {
                        mMap.addPolyline(
                            PolylineOptions()
                                .add(
                                    LatLng(oldLat, oldLong),
                                    LatLng(newLat, newLong)
                                )
                        )
                    }

                    oldLat = newLat
                    oldLong = newLong

                    i += 1

                }
            }
        }
    }

    private fun clicMarker(){

        mMap.setOnMarkerClickListener {
            mMap.clear()
            mMarkers.clear()

            true
        }

    }

    fun isExternalStorageWritable(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }

    fun isExternalStorageReadable(): Boolean {
        return Environment.getExternalStorageState() in
                setOf(Environment.MEDIA_MOUNTED, Environment.MEDIA_MOUNTED_READ_ONLY)
    }

}