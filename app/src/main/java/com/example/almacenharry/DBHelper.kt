package com.example.almacenharry
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.almacenharry.Categoria
import com.example.almacenharry.Producto

class DBHelper  (context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
// esto mantiene la base de datos abier
    private var database: SQLiteDatabase? = null

    fun openDatabase() {
        if (database == null || !database!!.isOpen) {
            database = this.writableDatabase
        }
    }

    fun closeDatabase() {
        database?.close()
        database = null
    }

    // fin de  esto mantiene la base de datos abieta
    companion object {
        private const val DATABASE_VERSION = 3
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

        // Crear triggers
        createTriggers(db)
    }

    private fun createTriggers(db: SQLiteDatabase) {
        // Trigger para actualización en cascada de categorías
        val TRIGGER_UPDATE_CATEGORIA = "CREATE TRIGGER IF NOT EXISTS trigger_update_categoria " +
                "AFTER UPDATE ON $TABLE_CATEGORIA " +
                "FOR EACH ROW " +
                "BEGIN " +
                "    UPDATE $TABLE_PRODUCTO " +
                "    SET $KEY_PRODUCTO_CATEGORIA_ID = NEW.$KEY_CATEGORIA_ID " +
                "    WHERE $KEY_PRODUCTO_CATEGORIA_ID = OLD.$KEY_CATEGORIA_ID; " +
                "END;"
        db.execSQL(TRIGGER_UPDATE_CATEGORIA)

        // Trigger para eliminar productos cuando se elimina una categoría
        val TRIGGER_DELETE_CATEGORIA = "CREATE TRIGGER IF NOT EXISTS trigger_delete_categoria " +
                "BEFORE DELETE ON $TABLE_CATEGORIA " +
                "FOR EACH ROW " +
                "BEGIN " +
                "    DELETE FROM $TABLE_PRODUCTO " +
                "    WHERE $KEY_PRODUCTO_CATEGORIA_ID = OLD.$KEY_CATEGORIA_ID; " +
                "END;"
        db.execSQL(TRIGGER_DELETE_CATEGORIA)

        // Trigger para verificar integridad antes de insertar un producto
        val TRIGGER_CHECK_CATEGORIA = "CREATE TRIGGER IF NOT EXISTS trigger_check_categoria " +
                "BEFORE INSERT ON $TABLE_PRODUCTO " +
                "FOR EACH ROW " +
                "BEGIN " +
                "    SELECT CASE " +
                "        WHEN ((SELECT $KEY_CATEGORIA_ID FROM $TABLE_CATEGORIA " +
                "              WHERE $KEY_CATEGORIA_ID = NEW.$KEY_PRODUCTO_CATEGORIA_ID) IS NULL) " +
                "        THEN RAISE(ABORT, 'La categoría no existe') " +
                "    END; " +
                "END;"
        db.execSQL(TRIGGER_CHECK_CATEGORIA)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Eliminar triggers existentes
        db.execSQL("DROP TRIGGER IF EXISTS trigger_update_categoria")
        db.execSQL("DROP TRIGGER IF EXISTS trigger_delete_categoria")
        db.execSQL("DROP TRIGGER IF EXISTS trigger_check_categoria")

        // Eliminar tablas existentes
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PRODUCTO")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CATEGORIA")

        // Recreate tables and triggers
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


