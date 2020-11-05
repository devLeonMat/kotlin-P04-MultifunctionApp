package com.rleon.multifuncionapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.support.annotation.RequiresApi
import android.support.v4.app.ActivityCompat
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_third.*
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick

class ThirdActivity : AppCompatActivity() {

    private val PHONE_CODE = 4000 //

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third)

        //flecha para volver atras
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        // boton para llamada
        imageButtonPhone!!.setOnClickListener(object : View.OnClickListener {  // capturamos el evento onclick del boton
            @RequiresApi(Build.VERSION_CODES.M)
            override fun onClick(v: View?) {
                val phoneNumber = editTextPhone!!.text.toString() // obtenemos el numero ingresado
                if (!phoneNumber.isEmpty()) {
                    //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // comparamos la version actual vs mashMalow o superior
                    doFromSdk(Build.VERSION_CODES.LOLLIPOP){
                        // comprobar el permiso
                        if (ChecarPermiso(Manifest.permission.CALL_PHONE)) {
                            // si el permiso esta aceptado creamos el acctioncall con e numero
                            //val intentAceptado = Intent(Intent.ACTION_CALL, Uri.parse("tel:$phoneNumber"))
                            if (ActivityCompat.checkSelfPermission(this@ThirdActivity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                return // nos aseguramos explicitaente que el permiso este en el manifiesto
                            }
                            //startActivity(intentAceptado)
                            makeCall(phoneNumber)
                        }else{
                            // le preguntamos por el permiso
                            // le mostramos un popup para validar
                            if(!shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)){
                                requestPermissions(arrayOf(Manifest.permission.CALL_PHONE),PHONE_CODE)
                            }else{
                                /*
                                si ya denego el permiso lo dirigimos a los ajustes para que habilite el permiso
                                 */
                                longToast("Por favor habilita el permiso correspondiente para continuar")
                                val intentSetting = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                intentSetting.addCategory(Intent.CATEGORY_DEFAULT)
                                intentSetting.data = Uri.parse("package:$packageName")
                                intentSetting.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                intentSetting.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                                intentSetting.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
                                startActivity(intentSetting)
                            }
                        }
                    }
                    doIfSdk(Build.VERSION_CODES.LOLLIPOP){
                        versionAntigua(phoneNumber)
                    }
                }else{
                    longToast("Debes Marcar un numero, Intenta nuevamente")
                }

            }
            fun versionAntigua(phoneNumber: String){
               // val intentCall = Intent(Intent.ACTION_CALL, Uri.parse("tel:$phoneNumber"))
                if(ChecarPermiso(Manifest.permission.CALL_PHONE)){
                    if(ActivityCompat.checkSelfPermission(this@ThirdActivity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                        return
                    }
                    //startActivity(intentCall)
                    makeCall(phoneNumber)
                }
            }
        })

        // boton para abrir una web
        imageButtonWeb!!.setOnClickListener{
            val url = editTextWeb!!.text.toString()
            println("url--->$url")
            browse("http://$url")
        }

        // boton para enviar un correo
        buttonEmailMe!!.onClick{  // creamos el evento para enviar correo
            val email = "algunemail@gmail.com"   // creamos un valor correo del que saldra el correo

            email(email,"Titulo del Mail","Hola este es un anko email")

            /*
            val intentEmail = Intent(Intent.ACTION_SEND, Uri.parse(email))

            intentEmail.type="plain/text"
            intentEmail.putExtra(Intent.EXTRA_SUBJECT, "TITULO DE EMAIL")
            intentEmail.putExtra(Intent.EXTRA_TEXT, "Mensaje de prueba......")
            intentEmail.putExtra(Intent.EXTRA_EMAIL, arrayOf("leonmatias1991@gmail.com","trableon2017@gmail.com"))
            startActivity(Intent.createChooser(intentEmail, "elije cliente de correo"))  // creamos un chooser para que elija el correo*/
        }

        // boton para llamada sin permisos

        buttonContactPhone!!.onClick{// con la libreria de anko solo se pone onclick
            makeCall("995084713")
            /*
            val intentCall = Intent(Intent.ACTION_CALL, Uri.parse("tel:995084713"))
            startActivity(intentCall)*/
        }

        // BOTON PARA LA CAMARA

        imageButtonCamera!!.onClick{
            val intentCamera = Intent("android.media.action.IMAGE_CAPTURE")
            startActivity(intentCamera)
        }
    }

    // le decimos a nuestra app donde se encuentra el menu
    override fun onCreateOptionsMenu(menu: Menu):Boolean{
        menuInflater.inflate(R.menu.menu, menu) // hacemos la llamada
        return true
    }

    // creamos la funcion que realizara la opcion del menu
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when(item!!.itemId){
            R.id.menuContactos->{ // aqui llamamos a cada opcion con su respectiva funcion
                val intentContacts = Intent(Intent.ACTION_VIEW, Uri.parse("content://contacts/people"))
                startActivity(intentContacts)
            }

            R.id.menuCompartir->{share("Anko esta genial","Anko for Android Kotlin")}
            R.id.menuMensaje->{sendSMS("995084713","Esto es un Mensaje de Prueba Panza de Conejo")}


        }
        return super.onOptionsItemSelected(item)
    }

    // metodo asincrono para comprobar permisos
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        when (requestCode) {
        // caso de uso
            PHONE_CODE -> {
                val permisos = permissions[0]
                val resultado = grantResults[0]

                if (permisos == Manifest.permission.CALL_PHONE) {
                    // comprobar si ha sido aceptado o denegado la peticion del permiso
                    if (resultado == PackageManager.PERMISSION_GRANTED) {
                        // se concedio el permiso
                        val phoneNumber = editTextPhone.text.toString() // recibimos el numero ingresado
                        val intentCall = Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber)) // recibimos el intent
                        // debemos verificar que exista el permiso en el manifest explicitamente
                        // ya que el usuario puede rechazar esta peticion de permiso
                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            return
                        }
                        startActivity(intentCall)
                    } else {
                        // denego el permiso
                        Toast.makeText(this, "Has denegado el permiso", Toast.LENGTH_LONG).show()
                    }

                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    // verificamos que el permiso este aceptado
    fun ChecarPermiso(permission: String): Boolean {

        val result = this.checkCallingOrSelfPermission(permission)
        println("result-->" + result)
        return result == PackageManager.PERMISSION_GRANTED
        println("PackageManager.PERMISSION_GRANTED==>" + PackageManager.PERMISSION_GRANTED)

    }
}
