package com.example.projetsmartphone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MapFragment : Fragment(), MessageListener{

    lateinit var mMap: GoogleMap
    var maposition: LatLng = LatLng(46.14645953235613, -1.1581339314579964)
    lateinit var mapFragment: SupportMapFragment
    var vitesseNoeuds : Double = Double.NaN
    var latitude : Double = Double.NaN
    var longitude : Double = Double.NaN


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val mapFragmentView = inflater.inflate(R.layout.fragment_map, container, false)
        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

        mapFragment.getMapAsync {
                googleMap -> mMap = googleMap
            mMap.clear()

            if (latitude.isNaN() || longitude.isNaN()) {
                maposition = LatLng(46.14645953235613, -1.1581339314579964)
            }
            else {
                maposition = LatLng(latitude, longitude)
            }

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(maposition, 12f))
            mMap.animateCamera(CameraUpdateFactory.zoomTo( 12f ))
            mMap.addMarker(
                MarkerOptions()
                    .position(maposition)
                    .title("Ma position")
                    .snippet("N: ${vitesseNoeuds}")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.bateau))
            )

        }

        launchClient()

        return mapFragmentView
    }

    //Call automatique dès reception de nouvelle trame NMEA.
    override fun onMessage(text: String?) {

        val wp = NMEAConverter.trameToWaypoint(text)
        vitesseNoeuds = wp.vitesseNoeud
        latitude = wp.latitude
        longitude = wp.longitude

        println(latitude)
        println(longitude)
        println(wp.heure)
        println(vitesseNoeuds)
        println(wp.vitesseKmh)
        println("\n\n")

    }

    //On lance la connexion au websocket.
    override fun launchClient()
    {
        WebSocketManager.init("http://192.168.0.24:9000", this)
        WebSocketManager.connect()
    }

}
