package ufc.smd.placar_tenis

import adapters.CustomAdapter
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import data.Placar
import java.io.ByteArrayInputStream
import java.io.ObjectInputStream
import java.util.*


class MapActivity : AppCompatActivity(), OnMapReadyCallback {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        //
        val mapUrl = intent.getStringExtra("mapUrl")
//        textViewReceivedData.text = value

        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
        Log.v("PDM23","mapa passado: "+mapUrl)

    }

    override fun onMapReady(map: GoogleMap) {
        val googleMap = map

        // Add a marker and move the camera to a specific location
        val location = LatLng(37.7749, -122.4194) // San Francisco coordinates
        googleMap.addMarker(
            MarkerOptions()
            .position(location)
            .title("partida"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 12.0f))
    }
}

