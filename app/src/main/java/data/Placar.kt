package data

import java.io.Serializable

data class Placar(var nome_partida:String, var resultado:String, var resultadoLongo:String,
                  var has_timer:Boolean, var localizacao:String) : Serializable {
                    companion object {
                        private const val serialVersionUID: Long = 202312032157
                    }
                  }
