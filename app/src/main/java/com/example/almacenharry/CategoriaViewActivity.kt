package com.example.almacenharry
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class CategoriaViewActivity : AppCompatActivity() {
    private lateinit var dbHelper: DBHelper
    private lateinit var listViewCategorias: ListView
    private lateinit var adapter: CategoriaAdapter
    private val ACTUALIZAR_CATEGORIA_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.categoria_view)

        dbHelper = DBHelper(this)
        listViewCategorias = findViewById(R.id.listViewCategorias)

        findViewById<Button>(R.id.btnNuevaCategoria).setOnClickListener {
            startActivity(Intent(this, NuevaCategoriaActivity::class.java))
        }

        loadCategorias()
    }

    private fun loadCategorias() {
        val categorias = dbHelper.getAllCategorias()

//        adapter = CategoriaAdapter(this, categorias) { categoria ->  // Este es el onDeleteClickListener
//
//            eliminarCategoria(categoria)
//        }
        adapter = CategoriaAdapter(
            this,
            categorias,
            { categoria -> eliminarCategoria(categoria) },
            { categoria -> abrirActualizarCategoria(categoria) }
        )
        listViewCategorias.adapter = adapter
    }

    private fun eliminarCategoria(categoria: Categoria) {
        // Implementa la lógica para eliminar la categoría de la base de datos
        dbHelper.deleteCategoria(categoria.id)
        // Actualiza la lista
        adapter.updateCategorias(dbHelper.getAllCategorias())
    }

    private fun abrirActualizarCategoria(categoria: Categoria) {
        val intent = Intent(this, ActualizarCategoriaActivity::class.java).apply {
            putExtra("CATEGORIA_ID", categoria.id)
            putExtra("CATEGORIA_NOMBRE", categoria.nombre)
        }
        startActivityForResult(intent, ACTUALIZAR_CATEGORIA_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ACTUALIZAR_CATEGORIA_REQUEST && resultCode == Activity.RESULT_OK) {
            loadCategorias()
        }
    }

    override fun onResume() {
        super.onResume()
        loadCategorias()
    }
}