package com.efihumboldt.appligas.ui.activities.Contactos

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.PersistableBundle
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.efihumboldt.appligas.R
import com.efihumboldt.appligas.Varios.UpdateManager
import com.efihumboldt.appligas.databinding.ActivityContactosBinding
import com.efihumboldt.appligas.databinding.ActivityDetalleTorneoBinding
import com.efihumboldt.appligas.databinding.ActivityTorneoBinding

class ContactosActivity : AppCompatActivity() {
    private lateinit var binding : ActivityContactosBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val updateManager = UpdateManager()
        updateManager.checkAndForceUpdate(this)

        val color = Color.parseColor("#0a369d")

        val window: Window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = color

        supportActionBar?.setBackgroundDrawable(ColorDrawable(color))
        val customActionBar = layoutInflater.inflate(R.layout.custom_action_bar_layout_detalle_equipo, null)
        customActionBar.setBackgroundColor(color)
        val customTitle : TextView = customActionBar.findViewById(R.id.customActionBarTitle)
        customTitle.text = getString(R.string.app_name_mayus)
        binding.telefono.setColorFilter(color)


        supportActionBar?.apply {
            displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
            setCustomView(customActionBar)
            setBackgroundDrawable(ColorDrawable(color))
        }
    }
}