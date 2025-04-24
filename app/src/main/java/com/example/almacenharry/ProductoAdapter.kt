package com.example.almacenharry

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView

class ProductoAdapter(
    private val context: Context,
    private var productos: List<Producto>,
    private val onDeleteClickListener: (Producto) -> Unit,
    private val onUpdateClickListener: (Producto) -> Unit  // Nuevo listener para actualizar
) : BaseAdapter() {

    override fun getCount(): Int = productos.size
    override fun getItem(position: Int): Any = productos[position]
    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_producto, parent, false)

        val producto = getItem(position) as Producto
        val txtNombreProducto: TextView = view.findViewById(R.id.txtNombreProducto)
        val btnEliminarProducto: Button = view.findViewById(R.id.btnEliminarProducto)
        val btnActualizarProducto: Button = view.findViewById(R.id.btnActualizarProducto)  // Nuevo botón

        txtNombreProducto.text = producto.nombre
        btnEliminarProducto.setOnClickListener { onDeleteClickListener(producto) }
        btnActualizarProducto.setOnClickListener { onUpdateClickListener(producto) }  // Listener para actualizar

        return view
    }

    fun updateProductos(newProductos: List<Producto>) {
        productos = newProductos
        notifyDataSetChanged()
    }
}