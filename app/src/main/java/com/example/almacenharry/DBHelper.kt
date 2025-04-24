package com.example.almacenharry
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.almacenharry.Categoria
import com.example.almacenharry.Producto

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "ProductosDB"

        private const val TABLE_CATEGORIA = "categorias"
        private const val KEY_CATEGORIA_ID = "id"
        private const val KEY_CATEGORIA_NOMBRE = "nombre"

        private const val TABLE_PRODUCTO = "productos"
        private const val KEY_PRODUCTO_ID = "id"
        private const val KEY_PRODUCTO_NOMBRE = "nombre"
        private const val KEY_PRODUCTO_PRECIO = "precio"
        private const val KEY_PRODUCTO_CANTIDAD = "cantidad"
        private const val KEY_PRODUCTO_CATEGORIA_ID = "categoria_id"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_CATEGORIAS_TABLE = ("CREATE TABLE " + TABLE_CATEGORIA + "("
                + KEY_CATEGORIA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_CATEGORIA_NOMBRE + " TEXT)")
        db.execSQL(CREATE_CATEGORIAS_TABLE)

        val CREATE_PRODUCTOS_TABLE = ("CREATE TABLE " + TABLE_PRODUCTO + "("
                + KEY_PRODUCTO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_PRODUCTO_NOMBRE + " TEXT,"
                + KEY_PRODUCTO_PRECIO + " REAL,"
                + KEY_PRODUCTO_CANTIDAD + " INTEGER,"
                + KEY_PRODUCTO_CATEGORIA_ID + " INTEGER,"
                + "FOREIGN KEY(" + KEY_PRODUCTO_CATEGORIA_ID + ") REFERENCES " + TABLE_CATEGORIA + "(" + KEY_CATEGORIA_ID + "))")
        db.execSQL(CREATE_PRODUCTOS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CATEGORIA")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PRODUCTO")
        onCreate(db)
    }

    // Métodos para Categorías
    fun addCategoria(nombre: String): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(KEY_CATEGORIA_NOMBRE, nombre)
        return db.insert(TABLE_CATEGORIA, null, values)
    }
    fun deleteCategoria(id: Int): Int {
        val db = this.writableDatabase
        return db.delete(TABLE_CATEGORIA, "$KEY_CATEGORIA_ID = ?", arrayOf(id.toString()))
    }

    fun updateCategoria(id: Int, nuevoNombre: String): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(KEY_CATEGORIA_NOMBRE, nuevoNombre)
        }
        return db.update(TABLE_CATEGORIA, values, "$KEY_CATEGORIA_ID = ?", arrayOf(id.toString()))
    }

    fun getAllCategorias(): List<Categoria> {
        val categoriasList = ArrayList<Categoria>()
        val selectQuery = "SELECT * FROM $TABLE_CATEGORIA"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        cursor.use {
            if (it.moveToFirst()) {
                val idIndex = it.getColumnIndexOrThrow(KEY_CATEGORIA_ID)
                val nombreIndex = it.getColumnIndexOrThrow(KEY_CATEGORIA_NOMBRE)

                do {
                    val categoria = Categoria(
                        it.getInt(idIndex),
                        it.getString(nombreIndex)
                    )
                    categoriasList.add(categoria)
                } while (it.moveToNext())
            }
        }
        return categoriasList
    }

    // Métodos para Productos
    fun addProducto(nombre: String, precio: Double, cantidad: Int, categoriaId: Int): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(KEY_PRODUCTO_NOMBRE, nombre)
        values.put(KEY_PRODUCTO_PRECIO, precio)
        values.put(KEY_PRODUCTO_CANTIDAD, cantidad)
        values.put(KEY_PRODUCTO_CATEGORIA_ID, categoriaId)
        return db.insert(TABLE_PRODUCTO, null, values)
    }

    fun deleteProducto(id: Int): Int {
        val db = this.writableDatabase
        return db.delete(TABLE_PRODUCTO, "$KEY_PRODUCTO_ID = ?", arrayOf(id.toString()))
    }
    fun updateProducto(id: Int, nuevoNombre: String): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(KEY_PRODUCTO_NOMBRE, nuevoNombre)
        }
        return db.update(TABLE_PRODUCTO, values, "$KEY_PRODUCTO_ID = ?", arrayOf(id.toString()))
    }


    fun getAllProductos(): List<Producto> {
        val productosList = ArrayList<Producto>()
        val selectQuery = "SELECT * FROM $TABLE_PRODUCTO"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        cursor.use {
            if (it.moveToFirst()) {
                // Obtener índices fuera del bucle
                val idIndex = it.getColumnIndexOrThrow(KEY_PRODUCTO_ID)
                val nombreIndex = it.getColumnIndexOrThrow(KEY_PRODUCTO_NOMBRE)
                val precioIndex = it.getColumnIndexOrThrow(KEY_PRODUCTO_PRECIO)
                val cantidadIndex = it.getColumnIndexOrThrow(KEY_PRODUCTO_CANTIDAD)
                val categoriaIdIndex = it.getColumnIndexOrThrow(KEY_PRODUCTO_CATEGORIA_ID)

                do {
                    val producto = Producto(
                        it.getInt(idIndex),
                        it.getString(nombreIndex),
                        it.getDouble(precioIndex),
                        it.getInt(cantidadIndex),
                        it.getInt(categoriaIdIndex)
                    )
                    productosList.add(producto)
                } while (it.moveToNext())
            }
        }
        return productosList
    }
}


