package ufc.smd.placar_tenis

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import data.Placar
import data.UIEducacionalPermissao
import ufc.smd.placar_tenis.R
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ConfigActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")
    @RequiresApi(Build.VERSION_CODES.O)
    val current = LocalDateTime.now().format(formatter)
    var placar :Placar = Placar("Jogo sem Config","0x0", "jogo em "+current,false,"")
    lateinit var requestLocalPermissionLauncher:androidx.activity.result.ActivityResultLauncher<String>
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_config)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        requestLocalPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                //Localização
                Log.v("PDM23","Acesso à localização concedido")
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location : Location? ->
                        Log.v("PDM23",location.toString())
                    }
            } else {
                Snackbar.make(
                    findViewById(R.id.layoutPermission),
                    R.string.semPerLoca,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

        Log.v("PDM23",placar.nome_partida)
        Log.v("PDM23",placar.has_timer.toString())


        openConfig()
        initInterface()
        getLocal()
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
        // Pega a localização
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

    fun getLocal(){
        when{
            //Primeiro Caso do When - A permissão já foi concedida
            ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED ->{
                Log.v("PDM23","Tem permissão de localização")
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location : Location? ->
                        if(location!=null){
                            Log.v("PDM23","Lat:"+location?.latitude)
//                            sendLocationWhatsApp(location?.latitude.toString(), location?.longitude.toString())
                            placar.localizacao = "http://maps.google.com/maps?@"+location?.latitude.toString()+location?.longitude.toString()
                            Log.v("PDM23",location.toString())
                            Log.v("PDM23","http://maps.google.com/maps?@"+location?.latitude.toString()+location?.longitude.toString())
                        }
//                        Snackbar.make(
//                            findViewById(R.id.layoutPermission),
//                            "Localização OK",
//                            Snackbar.LENGTH_SHORT
//                        ).show()
//                        return@addOnSuccessListener("http://maps.google.com/maps?@${location?.latitude.toString()}${location?.longitude.toString()}")
                    }
            }
            //Permissão foi negada, mas não para sempre
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION) ->{
                // Chamar a UI Educacional
                val mensagem =
                    "Nossa aplicação precisa acessar a localização"
                val titulo = "Permissão de acesso a localização"
                val codigo = 2//Código da requisição
                val mensagemPermissao = UIEducacionalPermissao(mensagem, titulo, codigo)
                mensagemPermissao.onAttach(this as Context)
                mensagemPermissao.show(supportFragmentManager, "primeiravez")
            }
            // Permissão negada ou não foi pedida
            else ->{
                requestLocalPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
            }
        }
    }
}