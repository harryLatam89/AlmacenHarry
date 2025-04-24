package com.example.almacenharry

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class ProductosListActivity : AppCompatActivity() {
    private lateinit var dbHelper: DBHelper
    private lateinit var listViewProductos: ListView
    private lateinit var adapter: ProductoAdapter
    private val ACTUALIZAR_PRODUCTO_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.product_list_view)

        dbHelper = DBHelper(this)
        listViewProductos = findViewById(R.id.listViewProductos)

        findViewById<Button>(R.id.btnNuevoProducto).setOnClickListener {
            startActivity(Intent(this, NuevoProductoActivity::class.java))
        }

        loadProductos()
    }

    private fun loadProductos() {
        val productos = dbHelper.getAllProductos()

        adapter = ProductoAdapter(
            this,
            productos,
            { producto -> eliminarProducto(producto) },  // Este es el onDeleteClickListener
            { producto -> abrirActualizarProducto(producto) }
        )
        listViewProductos.adapter = adapter
    }


    private fun eliminarProducto(producto: Producto) {
        // Implementa la lógica para eliminar la categoría de la base de datos
        dbHelper.deleteProducto(producto.id)
        // Actualiza la lista
        adapter.updateProductos(dbHelper.getAllProductos())
    }

    private fun abrirActualizarProducto(producto: Producto) {
        val intent = Intent(this, ActualizarProductoActivity::class.java).apply {
            putExtra("PRODUCTO_ID", producto.id)
            putExtra("PRODUCTO_NOMBRE", producto.nombre)
        }
        startActivityForResult(intent, ACTUALIZAR_PRODUCTO_REQUEST)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ACTUALIZAR_PRODUCTO_REQUEST && resultCode == Activity.RESULT_OK) {
            loadProductos()
        }
    }

    override fun onResume() {
        super.onResume()
        loadProductos()
    }
}