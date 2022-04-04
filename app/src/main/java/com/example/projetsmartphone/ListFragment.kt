package com.example.projetsmartphone

import android.content.Context
import android.content.Context.SENSOR_SERVICE
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class ListFragment : Fragment() , SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private lateinit var mMap: GoogleMap
    private lateinit var mMarkers : Waypoint

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val listFragmentView = inflater.inflate(R.layout.fragment_list, container, false)
        val mapFragment = childFragmentManager.findFragmentById(R.id.acceleromterFragment) as SupportMapFragment
        println("act $activity")

        mapFragment.getMapAsync {
                googleMap -> mMap = googleMap
            mMap.clear()

            val minime = LatLng(46.14645953235613, -1.1581339314579964)
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(minime, 12f))
            mMap.animateCamera(CameraUpdateFactory.zoomTo( 12f ))

            setUpSensorStuff()
            setStartingPoint()
        }


        return listFragmentView;
    }

    private fun setStartingPoint(){

        mMap.setOnMapClickListener { it ->

            mMap.clear()
            println("lat $it.latitude")
            println("long $it.longitude")
            val marker = LatLng(it.latitude, it.longitude)
            mMap.addMarker(MarkerOptions().position(marker))

            mMarkers = Waypoint(it.latitude, it.longitude)


        }
    }

    private fun setUpSensorStuff() {
        // Create the sensor manager

        sensorManager = getActivity()?.getSystemService(SENSOR_SERVICE) as SensorManager

        // Specify the sensor you want to listen to
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also { accelerometer ->
            sensorManager.registerListener(
                this,
                accelerometer,
                SensorManager.SENSOR_DELAY_FASTEST,
                SensorManager.SENSOR_DELAY_FASTEST
            )
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        // Checks for the sensor we have registered
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            //Log.d("Main", "onSensorChanged: sides ${event.values[0]} front/back ${event.values[1]} ")

            // Sides = Tilting phone left(10) and right(-10)
            val sides = event.values[0]

            // Up/Down = Tilting phone up(10), flat (0), upside-down(-10)
            val upDown = event.values[1]

            println("up $upDown")
            println("sides $sides")

        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        return
    }

}