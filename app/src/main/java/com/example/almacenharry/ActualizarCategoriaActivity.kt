package com.example.almacenharry

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ActualizarCategoriaActivity : AppCompatActivity() {
    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actualizar_categoria)

        dbHelper = DBHelper(this)

        val categoriaId = intent.getIntExtra("CATEGORIA_ID", -1)
        val categoriaNombre = intent.getStringExtra("CATEGORIA_NOMBRE") ?: ""

        findViewById<TextView>(R.id.tvCategoriaId).text = categoriaId.toString()
        findViewById<EditText>(R.id.etNombreCategoria).setText(categoriaNombre)

        findViewById<Button>(R.id.btnActualizarCategoria).setOnClickListener {
            val nuevoNombre = findViewById<EditText>(R.id.etNombreCategoria).text.toString()
            if (nuevoNombre.isNotEmpty()) {
                dbHelper.updateCategoria(categoriaId, nuevoNombre)
                setResult(Activity.RESULT_OK)
                finish()
            } else {
                Toast.makeText(this, "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show()
            }
        }
    }
}