package ufc.smd.placar_tenis

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import ufc.smd.placar_tenis.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    fun openConfig(v: View){
        val intent = Intent(this, ConfigActivity::class.java).apply{}
        startActivity(intent)

    }

    fun openPermission(v: View){
        val intent = Intent(this, PermissionActivity::class.java).apply{}
        startActivity(intent)
    }
    fun openPreviusGames(v: View) {
        val intent = Intent(this, PreviousGamesActivity::class.java).apply {

        }
        startActivity(intent)

    }
}