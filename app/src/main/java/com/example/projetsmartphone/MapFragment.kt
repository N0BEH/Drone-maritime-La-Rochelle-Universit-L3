package com.example.projetsmartphone

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlin.math.round


class MapFragment : Fragment(), MessageListener{

    lateinit var mMap: GoogleMap
    var maposition: LatLng = LatLng(46.14645953235613, -1.1581339314579964)
    lateinit var mapFragment: SupportMapFragment
    var vitesseNoeuds : Double = 0.0
    var latitude : Double = round(46.14645953235613 * 1000) / 1000
    var longitude : Double = round(-1.1581339314579964 * 1000) / 1000
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

            mMap.setInfoWindowAdapter(object : InfoWindowAdapter {
                override fun getInfoWindow(arg0: Marker): View? {
                    return null
                }

                override fun getInfoContents(marker: Marker): View? {
                    val info = LinearLayout(mapFragmentView.context)
                    info.orientation = LinearLayout.VERTICAL
                    val title = TextView(mapFragmentView.context)
                    title.setTextColor(Color.BLACK)
                    title.gravity = Gravity.CENTER
                    title.setTypeface(null, Typeface.BOLD)
                    title.text = marker.title
                    val snippet = TextView(mapFragmentView.context)
                    snippet.setTextColor(Color.GRAY)
                    snippet.text = marker.snippet
                    info.addView(title)
                    info.addView(snippet)
                    return info
                }
            })

            mo
                .position(maposition)
                .title("Ma position")
                .snippet(
                    """
                    N: ${vitesseNoeuds}
                    Lat: ${round(latitude * 1000) / 1000}
                    Lon: ${round(longitude * 1000) / 1000}
                    """.trimIndent()
                )
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.bateau))

            mark = mMap.addMarker(mo)


            mRunnable = Runnable {

                mo
                    .position(maposition)
                    .snippet(
                        """
                        N: ${vitesseNoeuds}
                        Lat: ${round(latitude * 1000) / 1000}
                        Lon: ${round(longitude * 1000) / 1000}
                        """.trimIndent()
                    )

                mark?.position = mo.position
                mark?.snippet = mo.snippet
                mark?.let { CameraUpdateFactory.newLatLng(it.position) }
                    ?.let { mMap.moveCamera(it) }
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
        println(longitude)/*
        println(wp.heure)
        println(vitesseNoeuds)
        println(wp.vitesseKmh)
        println("\n\n")*/
        h.postDelayed( mRunnable, 1000)

    }

    //On lance la connexion au websocket.
    override fun launchClient()
    {
        WebSocketManager.init("http://192.168.0.24:9000", this)
        WebSocketManager.connect()
    }

}