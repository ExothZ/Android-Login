package com.example.proyectofinal

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_perfil.*
import java.io.File


class perfil : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        val btnSalir = findViewById<Button>(R.id.btnsalir)
        val galeria = findViewById<ImageView>(R.id.imgEditar)
        val camara = findViewById<ImageView>(R.id.imgCamara)

        val bundle = intent.extras
        val email = bundle?.getString("email")
        mostrar(email ?: "")

        btnSalir.setOnClickListener {
            Toast.makeText(this, "Ha cerrado Sesion", Toast.LENGTH_LONG).show()
            FirebaseAuth.getInstance().signOut()
            onBackPressed()
        }

        camara.setOnClickListener {
            val takeIntentPhoto = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            fotoFile = getFile(FILE_NAME)

            val providerFile = FileProvider.getUriForFile(this, "com.example.proyectofinal.fileprovider", fotoFile)
            takeIntentPhoto.putExtra(MediaStore.EXTRA_OUTPUT, providerFile)
            if(takeIntentPhoto.resolveActivity(this.packageManager) != null) {
                startActivityForResult(takeIntentPhoto, REQUEST)
            } else {
                Toast.makeText(this, "La camara no esta disponible", Toast.LENGTH_LONG).show()
            }
        }

        galeria.setOnClickListener {
            val IntentGalery = Intent(Intent.ACTION_GET_CONTENT)
            IntentGalery.type = "image/*"
            starForActivityGallery.launch(IntentGalery)
        }

    }

    private fun mostrar(email: String) {
        val Correo = findViewById<TextView>(R.id.correo)
        Correo.text = "Correo: $email"
    }

    private val starForActivityGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data?.data
            fotoPerfil.setImageURI(data)
        }
    }

    private fun getFile(fileName: String): File {
        val directory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".jpg", directory)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST && resultCode == Activity.RESULT_OK) {
            val fotoTomada = BitmapFactory.decodeFile(fotoFile.absolutePath)
            fotoPerfil.setImageBitmap(fotoTomada)
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }


}

private const val REQUEST = 13
private lateinit var fotoFile: File
private const val FILE_NAME = "photo.jpg"