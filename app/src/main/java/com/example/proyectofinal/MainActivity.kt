package com.example.proyectofinal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.content.Intent
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.security.Provider

enum class ProviderType {
    BASIC
}

class MainActivity : AppCompatActivity() {


    private val db = FirebaseFirestore.getInstance()
    private val login = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val Usuario = findViewById<EditText>(R.id.usuario)
        val Contraseña = findViewById<EditText>(R.id.contraseña)
        val btnAcceder = findViewById<Button>(R.id.btnacceder)
        val btnUnete = findViewById<Button>(R.id.btnunete)

        btnUnete.setOnClickListener {
            val intentoRegistro = Intent(this, Registro::class.java)
            startActivity(intentoRegistro)
        }

        btnAcceder.setOnClickListener {
            if (Usuario.text.isNotEmpty() && Contraseña.text.isNotEmpty()) {
                FirebaseAuth.getInstance().
                signInWithEmailAndPassword(
                    Usuario.text.toString(),
                    Contraseña.text.toString()
                ).addOnCompleteListener {

                    if (it.isSuccessful) {
                        enlacePerfil(it.result?.user?.email?: "", ProviderType.BASIC)
                    } else {
                        alerta()
                    }
                }
            }
            Usuario.setText("")
            Contraseña.setText("")
        }
    }

        fun alerta() {
            val config = AlertDialog.Builder(this)
            config.setTitle("Ha ocurrido un error")
            config.setMessage("El correo/Contraseña no son correctos")
            config.setPositiveButton("Aceptar", null)
            val msg: AlertDialog = config.create()
            msg.show()
        }

        fun enlacePerfil(email: String, provider: ProviderType) {
            val intentoPerfil = Intent(this, perfil::class.java).apply {
                putExtra("email", email)
                putExtra("provider", provider.name)
            }
            Toast.makeText(this, "Ha iniciado sesion", Toast.LENGTH_LONG).show()
            startActivity(intentoPerfil)
        }

    }
