package com.example.almacenharry

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private lateinit var dbHelper: DBHelper //para manter abierata la base de datos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        dbHelper = DBHelper(this)  //para manter abierata la base de datos
        dbHelper.openDatabase()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<Button>(R.id.btnProductos).setOnClickListener {
            startActivity(Intent(this, ProductosListActivity::class.java))
        }

        findViewById<Button>(R.id.btnCategorias).setOnClickListener {
            startActivity(Intent(this, CategoriaViewActivity::class.java))
        }



    }

    override fun onDestroy() {
        super.onDestroy()
        dbHelper.closeDatabase()
    }
}