package com.example.projetsmartphone

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.projetsmartphone.databinding.ActivityMapsBinding
import com.google.android.gms.maps.model.PolylineOptions
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import java.io.File
import android.widget.Toast
import java.io.FileInputStream
import java.io.FileOutputStream
import java.lang.Exception

//token : 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

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val mMarkers = arrayListOf<Waypoint>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val minime = LatLng(46.1403, -1.1649)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(minime))
        mMap.animateCamera(CameraUpdateFactory.zoomTo( 14f ))

        clicMap()
        clicMarker()
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
                val textToWrite = "${waypoint.longitude},${waypoint.longitude},${waypoint.heure}"
                val fileOutputStream: FileOutputStream
                val fileInputStream: FileInputStream
                var text = ""

                if (isExternalStorageWritable() && isExternalStorageReadable()) {

                    try {
                        fileInputStream = openFileInput(fileName)
                        fileInputStream.use {
                            text =  it.bufferedReader().use {
                                it.readText()
                            }
                            Log.d("TAG", "LOADED: $text")
                        }

                        fileOutputStream = openFileOutput(fileName, Context.MODE_PRIVATE)
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
                var text = ""

                if (isExternalStorageWritable() && isExternalStorageReadable()) {

                    try {

                        fileOutputStream = openFileOutput(fileName, Context.MODE_PRIVATE)
                        fileOutputStream.write("".toByteArray())

                        Log.d("TAG", "dir: $filesDir.")

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