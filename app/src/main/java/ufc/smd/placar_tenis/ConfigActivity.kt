package ufc.smd.placar_tenis

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import androidx.annotation.RequiresApi
import data.Placar
import ufc.smd.placar_tenis.R
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ConfigActivity : AppCompatActivity() {
    val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")
    val current = LocalDateTime.now().format(formatter)
    var placar: Placar= Placar("Jogo sem Config","0x0", current,false)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_config)
       // placar= getIntent().getExtras()?.getSerializable("placar") as Placar
        //Log.v("PDM22",placar.nome_partida)
        //Log.v("PDM22",placar.has_timer.toString())

//        val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")

        openConfig()
        initInterface()

    }
    fun saveConfig(){
        val sharedFilename = "configPlacar"
        val sp:SharedPreferences = getSharedPreferences(sharedFilename,Context.MODE_PRIVATE)
        var edShared = sp.edit()

        edShared.putString("matchname",placar.nome_partida)
        edShared.putBoolean("has_timer",placar.has_timer)
        edShared.commit()
    }
    fun openConfig() {
        val sharedFilename = "configPlacar"
        val sp:SharedPreferences = getSharedPreferences(sharedFilename,Context.MODE_PRIVATE)
        val nome_jogo = "Partida em "+LocalDateTime.now().toLocalDate().toString()

        placar.nome_partida=sp.getString("matchname","Jogo Padrão").toString()
        placar.has_timer=sp.getBoolean("has_timer",false)

        Log.v("PDM23","openConf: "+placar.nome_partida)
    }
    fun initInterface(){
        val tv= findViewById<EditText>(R.id.editTextGameName)
        val nome_jogo = "Partida em "+LocalDateTime.now().format(formatter).toString()
        var txtNomeJogo = findViewById<TextView>(R.id.editTextGameName)
        txtNomeJogo.text = nome_jogo
        placar.nome_partida = nome_jogo.toString()
        Log.v("PDM23",placar.nome_partida)
        tv.setText(placar.nome_partida)
        val sw= findViewById<Switch>(R.id.swTimer)
        sw.isChecked=placar.has_timer
    }

    fun updatePlacarConfig(){
        val tv= findViewById<EditText>(R.id.editTextGameName)
        val sw= findViewById<Switch>(R.id.swTimer)
        placar.nome_partida= tv.text.toString()
        placar.has_timer=sw.isChecked
    }

    fun openPlacar(v: View){ //Executa ao click do Iniciar Jogo
        updatePlacarConfig() //Pega da Interface e joga no placar
        saveConfig() //Salva no Shared preferences
        val intent = Intent(this, PlacarActivity::class.java).apply{
            putExtra("placar", placar)
        }
        startActivity(intent)
    }
}