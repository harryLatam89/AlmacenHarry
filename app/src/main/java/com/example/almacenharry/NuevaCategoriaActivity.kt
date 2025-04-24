package com.example.almacenharry
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class NuevaCategoriaActivity : AppCompatActivity() {
    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.nueva_categoria)

        dbHelper = DBHelper(this)

        findViewById<Button>(R.id.btnAgregarCategoria).setOnClickListener {
            val nombre = findViewById<EditText>(R.id.editTextNombreCategoria).text.toString()
            if (nombre.isNotEmpty()) {
                dbHelper.addCategoria(nombre)
                Toast.makeText(this, "Categoría agregada", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Por favor ingrese un nombre", Toast.LENGTH_SHORT).show()
            }
        }
    }
}