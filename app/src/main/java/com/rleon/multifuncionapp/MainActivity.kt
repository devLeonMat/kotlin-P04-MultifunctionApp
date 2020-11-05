package com.rleon.multifuncionapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // forzar icono en el action bar
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setIcon(R.mipmap.ic_principal)

        //Toast.makeText(this,"Hola Simple Terricola", Toast.LENGTH_LONG).show()
        longToast(getString(R.string.main_longToast))


        btn_Calcular.setOnClickListener{
            val anio_nac = editText.text.toString().toInt()
            val anio_actual = Calendar.getInstance().get(Calendar.YEAR)
            val miEdad = anio_actual-anio_nac

            // intent explicito con anko le enviamos el key: edad y la variable
            startActivity<SecondActivity>("edad" to miEdad)

        }

        btnAnko.onClick {  // creamos la alerta
            alert(getString(R.string.main_alert_msj), getString(R.string.main_alert_titulo)){
                yesButton { longToast(getString(R.string.main_alert_btn_positivo)) } // boton ok
                noButton { longToast(getString(R.string.main_alert_btn_negativo)) } // boton cancelar
            }.show()
        }

        btnLista.onClick {  // creamos una lista
            val paises = listOf("MEX", "PER","ARG","URU","BOL","COL")
            selector(getString(R.string.main_preg_pais),paises) { dialogInterface, i ->
                longToast("Genial, entonces vives en ${paises[i]}, cierto?") }
        }

        btnProgreso.onClick { // creamos un progreso
            val dialog = progressDialog(message = "Por Favor espera un Momento..",title = "Cargando Datos")
        }


    }
}
