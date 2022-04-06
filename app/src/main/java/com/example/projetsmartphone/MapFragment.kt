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
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.round


class MapFragment : Fragment(), MessageListener{

    lateinit var mMap: GoogleMap
    var maposition: LatLng = LatLng(Double.NaN, Double.NaN)
    var mapositionold: LatLng = LatLng(Double.NaN, Double.NaN)
    lateinit var mapFragment: SupportMapFragment
    var vitesseNoeuds : Double = 0.0
    var latitude : Double = round(46.14645953235613 * 1000) / 1000
    var longitude : Double = round(-1.1581339314579964 * 1000) / 1000
    var h : Handler = Handler()
    lateinit var mRunnable: Runnable

    var mark: Marker? = null
    var mo: MarkerOptions = MarkerOptions()

    var polylines = ArrayList<PolylineOptions>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        // Inflate the layout for this fragment
        val mapFragmentView = inflater.inflate(R.layout.fragment_map, container, false)
        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

        // Permet de générer la map (Google Maps)
        mapFragment.getMapAsync {
                googleMap -> mMap = googleMap
            mMap.clear()

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(maposition, 12f))
            mMap.animateCamera(CameraUpdateFactory.zoomTo( 12f ))

            // Permet d'avoir les informations sur le marker
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

            for(line: PolylineOptions in polylines) {
                mMap.addPolyline(
                    line
                )
            }

            // Permet de relancer la réactualisation du bateau sur la map
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


                if(!(mapositionold.latitude.isNaN() || mapositionold.longitude.isNaN()))
                {
                    // Permet de générer le tracage du bateau
                    val polylineToAdd = PolylineOptions()
                        .add(
                            mapositionold,
                            maposition
                        )

                    mMap.addPolyline(
                        polylineToAdd
                    )

                    polylines.add(polylineToAdd)


                }
            }

            h.postDelayed( mRunnable, 1000)

        }
        // Lien avec le simulateur
        launchClient()

        return mapFragmentView
    }

    // Update des données (vitesse, position)
    override fun onMessage(text: String?) {

        //Last position
        mapositionold = maposition
        val wp = NMEAConverter.trameToWaypoint(text)

        vitesseNoeuds = wp.vitesseNoeud
        //New position
        latitude = wp.latitude
        longitude = wp.longitude
        maposition = LatLng(latitude, longitude)
        print(maposition)
        print(latitude)
        print(longitude)

        h.postDelayed( mRunnable, 1000)

    }

    //On lance la connexion au websocket.
    override fun launchClient()
    {
        WebSocketManager.init("http://192.168.0.24:9000", this)
        WebSocketManager.connect()
    }

}