package com.example.almacenharry
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class NuevoProductoActivity : AppCompatActivity() {
    private lateinit var dbHelper: DBHelper
    private lateinit var spinnerCategoria: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.nuevo_producto)

        dbHelper = DBHelper(this)
        spinnerCategoria = findViewById(R.id.spinnerCategoria)

        loadCategorias()

        findViewById<Button>(R.id.btnAgregarProducto).setOnClickListener {
            val nombre = findViewById<EditText>(R.id.editTextNombreProducto).text.toString()
            val cantidad =
                findViewById<EditText>(R.id.editTextCantidadProducto).text.toString().toIntOrNull()
            val precio =
                findViewById<EditText>(R.id.editTextPrecioProducto).text.toString().toDoubleOrNull()
            val categoriaId = (spinnerCategoria.selectedItem as? Categoria)?.id

            if (nombre.isNotEmpty() && cantidad != null && precio != null && categoriaId != null) {
                dbHelper.addProducto(nombre, precio, cantidad, categoriaId)
                Toast.makeText(this, "Producto agregado", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun loadCategorias() {
        val categorias = dbHelper.getAllCategorias()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categorias)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategoria.adapter = adapter
    }

}