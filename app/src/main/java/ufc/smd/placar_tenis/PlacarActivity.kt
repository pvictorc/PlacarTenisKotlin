package ufc.smd.placar_tenis

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator

import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.core.content.getSystemService
import data.Placar
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.nio.charset.StandardCharsets

class PlacarActivity : AppCompatActivity() {
    lateinit var placar:Placar
    lateinit var tvResultadoJogo: TextView
    val listaPontos = arrayOf("00","15","30","40","AD","WI")
    var pontos1 = 0
    var pontos2 = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_placar)
        var pontos1 = 0
        var pontos2 = 0
        placar= getIntent().getExtras()?.getSerializable("placar") as Placar

        Log.v("PDM23","PlacarActivity placar.localizacao "+placar.localizacao)
        //Mudar o nome da partida
        val tvNomePartida=findViewById(R.id.tvNomePartida) as TextView
        tvNomePartida.text=placar.nome_partida

        ultimoJogos()
    }

    fun alteraSets1 (v:View){
        var tvSetsJoga1 = findViewById<TextView>(R.id.tvSetsJogador1)
        var setsJogador1 = tvSetsJoga1.text.toString().toInt()
        var tvSetsJoga2 = findViewById<TextView>(R.id.tvSetsJogador2)
        var setsJogador2 = tvSetsJoga2.text.toString().toInt()
        setsJogador1++

        vibrar(v)
        tvSetsJoga1.text = setsJogador1.toString()

        placar.resultado = ""+setsJogador1+ " x " + setsJogador2
    }

    fun alteraSets2 (v:View){
        var tvSetsJoga1 = findViewById<TextView>(R.id.tvSetsJogador1)
        var setsJogador1 = tvSetsJoga1.text.toString().toInt()
        var tvSetsJoga2 = findViewById<TextView>(R.id.tvSetsJogador2)
        var setsJogador2 = tvSetsJoga2.text.toString().toInt()
        setsJogador2++

        vibrar(v)
        tvSetsJoga2.text = setsJogador2.toString()

        placar.resultado = ""+setsJogador1+ " vs " + setsJogador2
    }

    fun alteraPlacar1 (v:View){
//        val listaPontos = arrayOf("00","15","30","40","AD","WI")
        var tvPontosJogador1 = findViewById<TextView>(R.id.tvPontuacaoJogador1)
        var pontosJogador1 = tvPontosJogador1.text.toString()
        var tvPontosJogador2 = findViewById<TextView>(R.id.tvPontuacaoJogador2)
        var pontosJogador2 = tvPontosJogador2.text.toString()

        if(pontos1<=3) {
            pontos1++
        }
        else if( (pontos1>=3) and (pontos2==3) ){
            pontos1++
        }
        else if( (pontos1>=3) and (pontos2==4) ){
            pontos2--
        }
        if( (pontos1>=4) and ((pontos1-pontos2)>=2) ) { // caso vença o set, incrementa o set pro vencedor e zera tudo
            alteraSets1(v)
            zeraPontos()
        }
        //atualiza pontos
        tvPontosJogador1.text = listaPontos[pontos1].toString()
        tvPontosJogador2.text = listaPontos[pontos2].toString()

        vibrar(v)

    }

    fun alteraPlacar2 (v:View){
        var tvPontosJogador1 = findViewById<TextView>(R.id.tvPontuacaoJogador1)
        var pontosJogador1 = tvPontosJogador1.text.toString()
        var tvPontosJogador2 = findViewById<TextView>(R.id.tvPontuacaoJogador2)
        var pontosJogador2 = tvPontosJogador2.text.toString()

        if(pontos2<=3) {
            pontos2++
        }
        else if( (pontos2>=3) and (pontos1==3) ){
            pontos2++
        }
        else if( (pontos2>=3) and (pontos1==4) ){
            pontos1--
        }
        if( (pontos2>=4) and ((pontos2-pontos1)>=2) ) { // caso vença o set, incrementa o set pro vencedor e zera tudo
            alteraSets2(v)
            zeraPontos()
        }

        tvPontosJogador1.text = listaPontos[pontos1].toString()
        tvPontosJogador2.text = listaPontos[pontos2].toString()

        vibrar(v)

    }

    fun zeraPontos(){
        pontos1 = 0
        pontos2 = 0
    }
    fun incrementaPontos(a:String): CharSequence? {
        var p=a
        if (a=="00")
            p="15"
        else if(a=="15")
            p="40"
        else if(a=="40")
            p="VA"
        Log.v("PDM23",p.toString())
        return (p.toString())
    }

    fun vibrar (v:View){
        val buzzer = this.getSystemService<Vibrator>()
         val pattern = longArrayOf(0, 200, 100, 300)
         buzzer?.let {
             if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                 buzzer.vibrate(VibrationEffect.createWaveform(pattern, -1))
             } else {
                 //deprecated in API 26
                 buzzer.vibrate(pattern, -1)
             }
         }

    }


    fun saveGame(v: View) {

        val sharedFilename = "PreviousGames"
        val sp: SharedPreferences = getSharedPreferences(sharedFilename, Context.MODE_PRIVATE)
        var edShared = sp.edit()
        //Salvar o número de jogos já armazenados
        var numMatches= sp.getInt("numberMatch",0) + 1
        edShared.putInt("numberMatch", numMatches)

        //Escrita em Bytes de Um objeto Serializável
        var dt= ByteArrayOutputStream()
        var oos = ObjectOutputStream(dt);
        oos.writeObject(placar);

        //Salvar como "match"
        edShared.putString("match"+numMatches, dt.toString(StandardCharsets.ISO_8859_1.name()))
        edShared.commit() //Não esqueçam de comitar!!!

    }

    fun lerUltimosJogos(v: View){
        val sharedFilename = "PreviousGames"
        val sp: SharedPreferences = getSharedPreferences(sharedFilename, Context.MODE_PRIVATE)

        var meuObjString:String= sp.getString("match1","").toString()
        if (meuObjString.length >=1) {
            var dis = ByteArrayInputStream(meuObjString.toByteArray(Charsets.ISO_8859_1))
            var oos = ObjectInputStream(dis)
            var placarAntigo:Placar=oos.readObject() as Placar
            Log.v("PDM23",placar.resultado)
        }

        openPreviusGames(v)
    }

    fun ultimoJogos () {
        val sharedFilename = "PreviousGames"
        val sp:SharedPreferences = getSharedPreferences(sharedFilename,Context.MODE_PRIVATE)
        var matchStr:String=sp.getString("match1","").toString()
       // Log.v("PDM22", matchStr)
        if (matchStr.length >=1){
            var dis = ByteArrayInputStream(matchStr.toByteArray(Charsets.ISO_8859_1))
            var oos = ObjectInputStream(dis)
            var prevPlacar:Placar = oos.readObject() as Placar
            Log.v("PDM23", "Jogo Salvo:"+ prevPlacar.resultado)
        }
    }

    fun openPreviusGames(v: View) {
        val intent = Intent(this, PreviousGamesActivity::class.java).apply {

        }
        startActivity(intent)

    }
}