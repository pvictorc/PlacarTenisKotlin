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
import data.Placar
import java.io.ByteArrayInputStream
import java.io.ObjectInputStream
import java.util.*


class MapActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        //
        val mapUrl = intent.getStringExtra("mapUrl")
//        textViewReceivedData.text = value
        Log.v("PDM23","mapa passado: "+mapUrl)

    }
//    fun exibeMapa(v: View){
//
//        var localiza = findViewById<TextView>(R.id.txtLocal)
////        val localiza = findViewById<TextView>(R.id.tvNomePartida).text
//
//        //display toast with position of cardview in recyclerview list upon click
//        Toast.makeText(this, localiza.text, Toast.LENGTH_SHORT).show()
//        Log.v("PDM23", "exibe mapa: "+localiza.text)
//    }
}
