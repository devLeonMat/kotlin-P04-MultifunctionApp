package com.rleon.multifuncionapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_second.*
import org.jetbrains.anko.startActivity

class SecondActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        //flecha para volver atras
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        // recibimos el extra que se envio con key edad y lo guardamos en una variable edad
        // y lo mostramos en el textview
        val bundle = intent.extras
        val edad = bundle.getInt("edad") // le pasamos el key

        textViewIntent.text = edad.toString()

        btnToThirdActivity.setOnClickListener{
            startActivity<ThirdActivity>()
        }
    }
}
