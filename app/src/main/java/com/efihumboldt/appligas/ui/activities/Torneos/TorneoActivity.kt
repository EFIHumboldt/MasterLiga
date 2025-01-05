package com.efihumboldt.appligas.ui.activities.Torneos

import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.efihumboldt.appligas.entidades.Liga
import com.efihumboldt.appligas.entidades.Torneo
import com.efihumboldt.appligas.implementaciones.ApiServiceImpl
import com.efihumboldt.appligas.implementaciones.TorneoDAOImpl
import com.efihumboldt.appligas.interfaces.ApiService
import com.efihumboldt.appligas.interfaces.TorneoDAO
import com.efihumboldt.appligas.R
import com.efihumboldt.appligas.Varios.UpdateManager
import com.efihumboldt.appligas.services.TorneoService
import com.efihumboldt.appligas.ui.activities.DetalleTorneo.DetalleTorneoActivity
import com.efihumboldt.appligas.databinding.ActivityTorneoBinding
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TorneoActivity : AppCompatActivity() {

    private lateinit var recyclerViewTorneos: RecyclerView
    private lateinit var binding: ActivityTorneoBinding
    private lateinit var customToolbar : Toolbar
    private lateinit var swipeRefreshLayoutActivityTorneo : SwipeRefreshLayout
    private lateinit var torneoAdapter : TorneoAdapter
    private var listaTorneos : List<Torneo> = emptyList()
    private var cargaDatosJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val updateManager = UpdateManager()
        updateManager.checkAndForceUpdate(this)

       val liga = intent.getSerializableExtra("liga") as Liga

        binding = ActivityTorneoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val editTextSearch: EditText = binding.editTextSearch
        recyclerViewTorneos = binding.recyclerViewTorneos
        recyclerViewTorneos.layoutManager = LinearLayoutManager(this)

        swipeRefreshLayoutActivityTorneo = binding.swipeRefreshLayoutActivityTorneo

        val apiService: ApiService = ApiServiceImpl(this, liga.link)
        val torneoDAO: TorneoDAO = TorneoDAOImpl(apiService.torneoApiService, liga.link, liga.ID.toString(), this)
        val torneoService = TorneoService(torneoDAO)

        binding.progressBar.visibility = View.VISIBLE

        cargarDatos(torneoService, liga, 0)


        val customActionBar = layoutInflater.inflate(R.layout.custom_action_bar_layout, null)
        customToolbar = customActionBar.findViewById(R.id.custom_toolbar)
        val customIcon : ImageView = customActionBar.findViewById(R.id.customActionBarIcon)
        val customTitle : TextView = customActionBar.findViewById(R.id.customActionBarTitle)

        val optionsIcon : ImageView = customActionBar.findViewById(R.id.imgFinal)
        val favsIcon : ImageView = customActionBar.findViewById(R.id.favs)
        optionsIcon.visibility = View.INVISIBLE
        favsIcon.visibility = View.INVISIBLE


        Glide.with(customIcon.context).load("${liga.link}/${liga.logo}").into(customIcon)
        customTitle.text = liga.nombre


        supportActionBar?.apply {
            displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
            customView = customActionBar
            setCustomView(customActionBar)
        }
        try {
            val colorString: String? = liga.color
            val colorInt = Color.parseColor(colorString)
            changeColors(colorInt)
        } catch (e: IllegalArgumentException) {

            changeColors(Color.parseColor("#FFFFFF"))
        }

        swipeRefreshLayoutActivityTorneo.setOnRefreshListener {

            cargaDatosJob?.cancel()


            cargaDatosJob = CoroutineScope(Dispatchers.Main).launch {
                try {

                    if (!isFinishing && !isDestroyed) {

                        cargarDatos(torneoService, liga, 2000)
                    }

                } catch (e: CancellationException) {
                    // Maneja la cancelación de la tarea
                    // Puedes ignorar esta excepción ya que la tarea fue cancelada
                }
            }
        }


        val searchIconDrawable = editTextSearch.compoundDrawablesRelative[2] // El índice 2 representa el drawableEnd
        searchIconDrawable.setColorFilter(ContextCompat.getColor(this, R.color.lupa), PorterDuff.Mode.SRC_IN)
        editTextSearch.background.setColorFilter(Color.parseColor(liga.color), PorterDuff.Mode.SRC_IN)


        editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // No es necesario implementar este método
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No es necesario implementar este método
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                try {
                    val filteredTorneos = listaTorneos.filter { torneo ->
                        torneo.nombreTorneoDivision.contains(s.toString(), ignoreCase = true)
                    }

                    // Actualiza el adaptador del RecyclerView con la lista filtrada
                    torneoAdapter.submitList(filteredTorneos)
                } catch (_: Exception)
                {}
            }
        })

    }

    private fun changeColors(statusColor: Int) {
        val window: Window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = statusColor
        supportActionBar?.setBackgroundDrawable(ColorDrawable(statusColor))

        customToolbar.setBackgroundColor(statusColor)


    }


    private fun abrirActividadConItem(torneoSeleccionado : Torneo, ligaseleccionada : Liga) {

        val intent = Intent(this, DetalleTorneoActivity::class.java)
        intent.putExtra("torneo", torneoSeleccionado)
        intent.putExtra("liga", ligaseleccionada)
        startActivity(intent)
    }

    private fun cargarDatos(torneoService: TorneoService, liga: Liga, delay: Long) {
        CoroutineScope(Dispatchers.Main).launch {
            delay(delay) // Esta línea reemplaza al Handler
            try {
                listaTorneos = torneoService.getAllTorneos()
                torneoAdapter = TorneoAdapter(listaTorneos, liga.logo, this@TorneoActivity)
                recyclerViewTorneos.adapter = torneoAdapter
                torneoAdapter.setOnItemClickListener(object : TorneoAdapter.OnItemClickListener {
                    override fun onItemClick(torneo : Torneo) {
                        abrirActividadConItem(torneo, liga)
                    }
                })

                binding.progressBar.visibility = View.GONE

                swipeRefreshLayoutActivityTorneo.isRefreshing = false
            } catch (e: Exception) {
                Log.e("Error", "Error al cargar los datos: ${e.message}")
                // Manejar el error adecuadamente
            }
        }
    }
}