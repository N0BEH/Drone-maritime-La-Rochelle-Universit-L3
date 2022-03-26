package com.example.projetsmartphone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.projetsmartphone.databinding.ActivityMapsBinding
import com.google.android.gms.maps.GoogleMap.OnMapClickListener
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.PolylineOptions


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val mMarkers = arrayListOf<Marker>()

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

    fun clicMap(){

        mMap.setOnMapClickListener(OnMapClickListener { it ->

            val marker = LatLng(it.latitude, it.longitude)
            mMap.addMarker(MarkerOptions().position(marker))?.let { it1 ->
                mMarkers.add(it1)
            }


            var i = 0;
            var oldLat = 0.0
            var oldLong = 0.0
            var newLat = 0.0
            var newLong = 0.0
            if (mMarkers.size > 1){

                    mMarkers.forEach {

                        newLat = it.position.latitude
                        newLong =it.position.longitude

                        if( i >= 1) {
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
        })
    }


    fun clicMarker(){

        mMap.setOnMarkerClickListener { marker ->
            mMap.clear()
            mMarkers.clear()
            true
        }

    }
}