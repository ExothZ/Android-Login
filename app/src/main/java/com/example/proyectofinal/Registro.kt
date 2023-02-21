package com.example.proyectofinal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Registro : AppCompatActivity() {

	/*
	 * Copyright 2023.
	 * Ver Copyright.txt para más detalles de permisos
	 */
	
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        val btnRegistrar = findViewById<Button>(R.id.btnregistrar)
        val btnRegresar = findViewById<Button>(R.id.btnregresar)
        val Usuario = findViewById<EditText>(R.id.usuarioRegister)
        val Contraseña = findViewById<EditText>(R.id.contraseñaRegister)

        btnRegresar.setOnClickListener {
            val intentoRegresar = Intent(this, MainActivity::class.java)
            startActivity(intentoRegresar)
        }

        btnRegistrar.setOnClickListener {
            if (Usuario.text.isNotEmpty() && Contraseña.text.isNotEmpty()) {
                FirebaseAuth.getInstance().
                    createUserWithEmailAndPassword(
                        Usuario.text.toString(),
                        Contraseña.text.toString()
                ).addOnCompleteListener {

                    if (it.isSuccessful) {
                        enlaceMain(it.result?.user?.email?: "", ProviderType.BASIC)
                    } else {
                        alerta()
                    }
                }
            }

        }
    }

    fun alerta() {
        val config = AlertDialog.Builder(this)
        config.setTitle("Ha ocurrido un error")
        config.setMessage("Se ha producido un error")
        config.setPositiveButton("Aceptar", null)
        val msg: AlertDialog = config.create()
        msg.show()
    }



    private fun enlaceMain(email: String, provider: ProviderType) {
        val intentoMain = Intent(this, MainActivity::class.java).apply {
            putExtra("email", email)
            putExtra("provider", provider.name)
        }
        Toast.makeText(this, "Se ha registrado Correctamente", Toast.LENGTH_SHORT).show()
        startActivity(intentoMain)
    }





}