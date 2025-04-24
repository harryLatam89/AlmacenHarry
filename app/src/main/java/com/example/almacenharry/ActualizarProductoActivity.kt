package com.example.almacenharry

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ActualizarProductoActivity : AppCompatActivity() {
    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actualizar_producto)

        dbHelper = DBHelper(this)

        val productoId = intent.getIntExtra("PRODUCTO_ID", -1)
        val productoNombre = intent.getStringExtra("PRODUCTO_NOMBRE") ?: ""

        findViewById<TextView>(R.id.tvProductoId).text = productoId.toString()
        findViewById<EditText>(R.id.etNombreProducto).setText(productoNombre)

        findViewById<Button>(R.id.btnActualizarProducto).setOnClickListener {
            val nuevoNombre = findViewById<EditText>(R.id.etNombreProducto).text.toString()
            if (nuevoNombre.isNotEmpty()) {
                dbHelper.updateProducto(productoId, nuevoNombre)
                setResult(Activity.RESULT_OK)
                finish()
            } else {
                Toast.makeText(this, "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show()
            }
        }
    }
}