package com.example.projetsmartphone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment

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

            /*
            var texte : String = ""
            var ajout = "${waypoint.longitude},${waypoint.longitude},${waypoint.heure}"
            val inputStream = resources.openRawResource(R.raw.test)
            inputStream.bufferedReader().useLines {
                texte = readLine().toString()
                texte += ajout
            }



             */
            //Environment.getExternalStorageState()
            Toast.makeText(this, Environment.getExternalStorageState().toString(), Toast.LENGTH_SHORT).show()



            File("test.txt").readText()
            var texte : String = ""
            var ajout = "${waypoint.longitude},${waypoint.longitude},${waypoint.heure}"

            texte += ajout
            File("test.txt").writeText(texte)

            /*
            Toast.makeText(this, MapsActivity::class.java.getResource("/res/raw/test").path, Toast.LENGTH_SHORT).show()

            File(MapsActivity::class.java.getResource("/res/raw/test").path).bufferedWriter().use { out ->
                out.write(texte)
            }


             */





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

            /*
            val fichier = File("./src/main/res/raw/test.txt")
            try {
                fichier.writeText("cocou")

            } catch (e: Exception){
                Toast.makeText(this, "err", Toast.LENGTH_SHORT).show()
            }

             */

            true
        }

    }
}