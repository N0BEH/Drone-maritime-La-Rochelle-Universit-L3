package com.example.projetsmartphone

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions


class MapFragment : Fragment(), MessageListener{

    lateinit var mMap: GoogleMap
    var maposition: LatLng = LatLng(46.14645953235613, -1.1581339314579964)
    lateinit var mapFragment: SupportMapFragment
    var vitesseNoeuds : Double = Double.NaN
    var latitude : Double = Double.NaN
    var longitude : Double = Double.NaN
    var h : Handler = Handler()
    lateinit var mRunnable: Runnable

    var mark: Marker? = null
    var mo: MarkerOptions = MarkerOptions()


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

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(maposition, 12f))
            mMap.animateCamera(CameraUpdateFactory.zoomTo( 12f ))


            mo
                .position(maposition)
                .title("Ma position")
                .snippet("N: ${vitesseNoeuds}")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.bateau))

            mark = mMap.addMarker(mo)


            mRunnable = Runnable {

                mo
                    .position(maposition)
                    .snippet("N: ${vitesseNoeuds}")

                mark?.position = mo.position
                mark?.snippet = mo.snippet
            }

            h.postDelayed( mRunnable, 1000)

        }

        launchClient()



        return mapFragmentView
    }

    override fun onMessage(text: String?) {

        val wp = NMEAConverter.trameToWaypoint(text)
        vitesseNoeuds = wp.vitesseNoeud
        latitude = wp.latitude/100
        longitude = wp.longitude/-100
        maposition = LatLng(latitude, longitude)

        println(latitude)
        println(longitude)
        println(wp.heure)
        println(vitesseNoeuds)
        println(wp.vitesseKmh)
        println("\n\n")
        h.postDelayed( mRunnable, 1000)

    }

    //On lance la connexion au websocket.
    override fun launchClient()
    {
        WebSocketManager.init("http://192.168.1.181:9000", this)
        WebSocketManager.connect()
    }

}