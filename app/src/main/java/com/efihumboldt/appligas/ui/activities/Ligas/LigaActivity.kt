package com.efihumboldt.appligas.ui.activities.Ligas

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.efihumboldt.appligas.entidades.Liga
import com.efihumboldt.appligas.implementaciones.ApiServiceBaseImpl
import com.efihumboldt.appligas.implementaciones.LigaDAOImpl
import com.efihumboldt.appligas.interfaces.ApiBaseService
import com.efihumboldt.appligas.interfaces.LigaDAO
import com.efihumboldt.appligas.R
import com.efihumboldt.appligas.Varios.UpdateManager
import com.efihumboldt.appligas.services.LigaService
import com.efihumboldt.appligas.ui.activities.Torneos.TorneoActivity
import com.efihumboldt.appligas.databinding.ActivityLigaBinding
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability

class LigaActivity : AppCompatActivity() {

    private lateinit var recyclerViewLigas: RecyclerView
    private lateinit var binding: ActivityLigaBinding
    private lateinit var viewModel : LigaViewModel
    private lateinit var customToolbar : Toolbar
    private lateinit var listaLigas : List<Liga>
    private lateinit var ligaAdapter : LigaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val updateManager = UpdateManager()
        updateManager.checkAndForceUpdate(this)

        binding = ActivityLigaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val editTextSearch: EditText = binding.editTextSearch

        recyclerViewLigas = binding.recyclerViewLigas

        recyclerViewLigas.layoutManager = LinearLayoutManager(this)

        val apiService: ApiBaseService = ApiServiceBaseImpl(this, null)
        val ligaDAO: LigaDAO = LigaDAOImpl(apiService.ligaApiService, this)
        val ligaService = LigaService(ligaDAO)


        listaLigas = ligaService.getAllLigas()
        ligaAdapter = LigaAdapter(listaLigas, this@LigaActivity)
        recyclerViewLigas.adapter = ligaAdapter



        val color = Color.parseColor("#0a369d")

        val window: Window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = color
        supportActionBar?.setBackgroundDrawable(ColorDrawable(color))
        val customActionBar = layoutInflater.inflate(R.layout.custom_action_bar_layout_detalle_equipo, null)
        customToolbar = customActionBar.findViewById(R.id.custom_toolbar_detalle_equipo)
        customToolbar.setBackgroundDrawable(ColorDrawable(color))
        val customTitle : TextView = customActionBar.findViewById(R.id.customActionBarTitle)
        customTitle.text = getString(R.string.app_name_mayus)


        supportActionBar?.apply {
            displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
            customView = customActionBar
            setCustomView(customActionBar)
        }

        val searchIconDrawable = editTextSearch.compoundDrawablesRelative[2] // El índice 2 representa el drawableEnd
        searchIconDrawable.setColorFilter(ContextCompat.getColor(this, R.color.lupa), PorterDuff.Mode.SRC_IN)
        editTextSearch.background.setColorFilter(Color.parseColor("#0a369d"), PorterDuff.Mode.SRC_IN)


        editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // No es necesario implementar este método
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No es necesario implementar este método
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                val filteredLigas = listaLigas.filter { torneo ->
                    torneo.nombre.contains(s.toString(), ignoreCase = true)
                }

                // Actualiza el adaptador del RecyclerView con la lista filtrada
                ligaAdapter.submitList(filteredLigas)
            }
        })



        viewModel = ViewModelProvider(this).get(LigaViewModel::class.java)

        ligaAdapter.setOnItemClickListener(object : LigaAdapter.OnItemClickListener {

            override fun onItemClick(ligaSeleccionada : Liga) {

                ligaSeleccionada?.let { viewModel.setLeague(it) }
                abrirActividadConItem(ligaSeleccionada)
            }
        })

    }

    private fun abrirActividadConItem(liga: Liga) {

        val intent = Intent(this, TorneoActivity::class.java)
        intent.putExtra("liga", liga)
        startActivity(intent)
    }

}