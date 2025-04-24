package com.example.almacenharry

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class ProductosListActivity : AppCompatActivity() {
    private lateinit var dbHelper: DBHelper
    private lateinit var listViewProductos: ListView

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
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, productos.map { it.nombre })
        listViewProductos.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        loadProductos()
    }
}