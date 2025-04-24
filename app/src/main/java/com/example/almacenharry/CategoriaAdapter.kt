package com.example.almacenharry

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView

class CategoriaAdapter(
    private val context: Context,
    private var categorias: List<Categoria>,
    private val onDeleteClickListener: (Categoria) -> Unit,
    private val onUpdateClickListener: (Categoria) -> Unit  // Nuevo listener para actualizar
) : BaseAdapter() {

    override fun getCount(): Int = categorias.size
    override fun getItem(position: Int): Any = categorias[position]
    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_categoria, parent, false)

        val categoria = getItem(position) as Categoria
        val txtNombreCategoria: TextView = view.findViewById(R.id.txtNombreCategoria)
        val btnEliminarCategoria: Button = view.findViewById(R.id.btnEliminarCategoria)
        val btnActualizarCategoria: Button = view.findViewById(R.id.btnActualizarCategoria)  // Nuevo botón

        txtNombreCategoria.text = categoria.nombre
        btnEliminarCategoria.setOnClickListener { onDeleteClickListener(categoria) }
        btnActualizarCategoria.setOnClickListener { onUpdateClickListener(categoria) }  // Listener para actualizar

        return view
    }

    fun updateCategorias(newCategorias: List<Categoria>) {
        categorias = newCategorias
        notifyDataSetChanged()
    }
}