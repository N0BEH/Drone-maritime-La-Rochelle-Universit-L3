package com.example.projetsmartphone

import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import java.io.FileInputStream
import java.io.FileOutputStream
import java.lang.Exception
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class CustomMapFragment : Fragment(){

    private lateinit var mMap: GoogleMap
    private val mMarkers = arrayListOf<Waypoint>()
    private var mapReady = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_custom_map, container, false)
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync {
            googleMap -> mMap = googleMap
            mapReady = true

            val minime = LatLng(46.14547556383779, -1.1685415729880333)
            mMap.moveCamera(CameraUpdateFactory.newLatLng(minime))
            mMap.animateCamera(CameraUpdateFactory.zoomTo( 12f ))

            clicMap()
            clicMarker()
        }
        return view
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

            val state = Environment.getExternalStorageState()
            if (state == Environment.MEDIA_MOUNTED) {
                // Available to read and write

                val fileName = "test.txt"
                val textToWrite = "${it.latitude},${it.longitude},${formatted}"
                val fileOutputStream: FileOutputStream
                val fileInputStream: FileInputStream
                var text = ""


                if (isExternalStorageWritable() && isExternalStorageReadable()) {

                    try {

                        fileInputStream = requireActivity().openFileInput(fileName)
                        fileInputStream.use {
                            text =  it.bufferedReader().use {
                                it.readText()
                            }
                            Log.d("TAG", "LOADED: $text")
                        }

                        fileOutputStream = requireActivity().openFileOutput(fileName, Context.MODE_PRIVATE)
                        fileOutputStream.write("$text\n$textToWrite".toByteArray())

                        fileOutputStream.close()
                        fileInputStream.close()


                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
            }


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

            val state = Environment.getExternalStorageState()
            if (state == Environment.MEDIA_MOUNTED) {
                // Available to read and write

                val fileName = "test.txt"
                val fileOutputStream: FileOutputStream

                if (isExternalStorageWritable() && isExternalStorageReadable()) {

                    try {

                        fileOutputStream = requireActivity().openFileOutput(fileName, Context.MODE_PRIVATE)
                        fileOutputStream.write("".toByteArray())

                        fileOutputStream.close()


                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }

            }

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