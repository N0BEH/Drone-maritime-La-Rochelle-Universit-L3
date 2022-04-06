package com.example.projetsmartphone

import android.content.Context.SENSOR_SERVICE
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlin.math.round


class AccelerometerFragment : Fragment(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private lateinit var mMap: GoogleMap
    private lateinit var mMarkers: Waypoint
    private var mMarkersNew: Waypoint = Waypoint()

    var mark: Marker? = null
    var mo: MarkerOptions = MarkerOptions()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()

        val listFragmentView = inflater.inflate(R.layout.fragment_accelerometer, container, false)
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.accelerometerFragment) as SupportMapFragment
        println("act $activity")

        mapFragment.getMapAsync { googleMap ->
            mMap = googleMap
            mMap.clear()

            setUpSensorStuff()
            setStartingPoint()

            val minime = LatLng(46.14645953235613, -1.1581339314579964)
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(minime, 12f))
            mMap.animateCamera(CameraUpdateFactory.zoomTo(12f))

        }


        return listFragmentView;
    }

    private fun setStartingPoint() {

        mMap.setOnMapClickListener { it ->

            mMap.clear()
            println("lat $it.latitude")
            println("long $it.longitude")
            val marker = LatLng(it.latitude, it.longitude)
            mMap.addMarker(
                MarkerOptions()
                        .position(marker)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.flag))
            )

            mMarkers = Waypoint(it.latitude, it.longitude)

            mo
                .position(LatLng(mMarkersNew.latitude, mMarkersNew.longitude))
                .title("Ma position")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.bateau))

            mark = mMap.addMarker(mo)

            refreshBoat()


        }
    }

    private fun setUpSensorStuff() {
        // Create the sensor manager

        sensorManager = getActivity()?.getSystemService(SENSOR_SERVICE) as SensorManager

        // Specify the sensor you want to listen to
        sensorManager.getDefaultSensor(
            Sensor.TYPE_ORIENTATION
        )?.also { accelerometer ->
            sensorManager.registerListener(
                this,
                accelerometer,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }


    }

    private fun refreshBoat() {

        mo
            .position(LatLng(mMarkersNew.latitude, mMarkersNew.longitude))

        mark?.position = mo.position
        mark?.snippet = mo.snippet
        mark?.let { CameraUpdateFactory.newLatLng(it.position) }
            ?.let { mMap.moveCamera(it) }

    }

    override fun onSensorChanged(event: SensorEvent?) {
        // Checks for the sensor we have registered

        if (this::mMarkers.isInitialized) {
            if (event?.sensor?.type == Sensor.TYPE_ORIENTATION) {
                //Log.d("Main", "onSensorChanged: sides ${event.values[0]} front/back ${event.values[1]} ")

                // Sides = Tilting phone left(10) and right(-10)
                val y = event.values[1]

                // Up/Down = Tilting phone up(10), flat (0), upside-down(-10)
                val x = event.values[0]

                println("x = $x")
                println("y = $y")
                var long = mMarkers.longitude
                var lat = mMarkers.latitude
                if (x < -0.1) {
                    lat += x/1000 * -1
                } else if (x > 0.1) {
                    lat += x/1000 * -1
                }

                if (y < -0.1) {
                    long += (y/5000)*2
                } else if (y > 0.1) {
                    long += (y/5000)*2
                }

                mMarkersNew = Waypoint(lat, long)

                mMap.addPolyline(
                    PolylineOptions()
                        .add(
                            LatLng(mMarkers.latitude, mMarkers.longitude),
                            LatLng(mMarkersNew.latitude, mMarkersNew.longitude)
                        )
                )

                mMarkers = Waypoint(mMarkersNew.latitude, mMarkersNew.longitude)

                refreshBoat()
            }
        }

    }


    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        return
    }

}