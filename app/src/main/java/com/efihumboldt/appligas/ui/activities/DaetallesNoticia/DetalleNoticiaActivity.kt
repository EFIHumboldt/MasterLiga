package com.efihumboldt.appligas.ui.activities.DaetallesNoticia

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.efihumboldt.appligas.R
import com.efihumboldt.appligas.Varios.UpdateManager
import com.efihumboldt.appligas.databinding.ActivityDetalleNoticiaBinding

class DetalleNoticiaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetalleNoticiaBinding
    private lateinit var viewModel: DetalleNoticiaViewModel
    private lateinit var customToolbar : Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_noticia)

        val updateManager = UpdateManager()
        updateManager.checkAndForceUpdate(this)

        viewModel = ViewModelProvider(this)[DetalleNoticiaViewModel::class.java]

        val noticia = viewModel.selectedNew
        val liga = viewModel.selectedLeague
        var imagen_noticia : ImageView = findViewById(R.id.imagen_detalle_noticia)
        var titulo_noticia :TextView = findViewById(R.id.titulo_detalle_noticia)
        var cuerpo_noticia : TextView = findViewById(R.id.cuerpo_detalle_noticia)

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val screenWidth = displayMetrics.widthPixels

        val layoutParams: ViewGroup.LayoutParams = imagen_noticia.layoutParams
        layoutParams.width = screenWidth
        layoutParams.height = (screenWidth * 0.7).toInt()
        imagen_noticia.layoutParams = layoutParams

        Glide.with(imagen_noticia.context).load("${viewModel.bd}/${noticia?.imagen}").into(imagen_noticia)
        titulo_noticia.text = noticia?.titulo
        cuerpo_noticia.text = noticia?.cuerpo

        val customActionBar = layoutInflater.inflate(R.layout.custom_action_bar_layout, null)
        customToolbar = customActionBar.findViewById(R.id.custom_toolbar)
        val customIcon : ImageView = customActionBar.findViewById(R.id.customActionBarIcon)
        val customTitle : TextView = customActionBar.findViewById(R.id.customActionBarTitle)

        Glide.with(customIcon.context).load("${liga?.link}/${liga?.logo}").into(customIcon)
        customTitle.text = liga?.nombre


        supportActionBar?.apply {
            displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
            customView = customActionBar
            setCustomView(customActionBar)
        }
        try {
            val colorString: String? = liga?.color
            val colorInt = Color.parseColor(colorString)
            changeColors(colorInt)
        } catch (e: IllegalArgumentException) {

            changeColors(Color.parseColor("#FFFFFF"))
        }
    }

    private fun changeColors(statusColor: Int) {
        val window: Window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = statusColor
        supportActionBar?.setBackgroundDrawable(ColorDrawable(statusColor))

        customToolbar.setBackgroundColor(statusColor)


    }
}