package com.efihumboldt.appligas.ui.activities.Contactos

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.efihumboldt.appligas.Varios.SharedDataHolder
import com.efihumboldt.appligas.databinding.ActivityClasificationTableBinding
import com.efihumboldt.appligas.entidades.Banner
import com.efihumboldt.appligas.entidades.Posicion
import com.efihumboldt.appligas.implementaciones.ApiServiceImpl
import com.efihumboldt.appligas.implementaciones.BannerDAOImpl
import com.efihumboldt.appligas.implementaciones.PosicionDAOImpl
import com.efihumboldt.appligas.interfaces.ApiService
import com.efihumboldt.appligas.interfaces.BannerDAO
import com.efihumboldt.appligas.interfaces.PosicionDAO
import com.efihumboldt.appligas.services.BannerService
import com.efihumboldt.appligas.services.PosicionService
import com.efihumboldt.appligas.ui.menu_inferior_torneo.principal.DescripcionColoresAdapter
import com.efihumboldt.appligas.ui.menu_inferior_torneo.principal.PosicionesAdapter
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class ClasificationTableActivity : AppCompatActivity() {

    private lateinit var tablaPosiciones : List<Posicion>
    private lateinit var bd : String
    private lateinit var swipeRefreshLayoutFragmentPosiciones: SwipeRefreshLayout
    private lateinit var recyclerViewPosiciones: RecyclerView
    private lateinit var recyclerViewDescripcionColores: RecyclerView
    private var cargaDatosJob : Job? = null
    private var cargaDatosJob2 : Job? = null
    private lateinit var bannerList: List<Banner>
    private lateinit var binding : ActivityClasificationTableBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityClasificationTableBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.progressBar.visibility = View.VISIBLE


        val color = Color.parseColor(SharedDataHolder.selectedLeague!!.color)
        val window: Window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = color

        supportActionBar?.setBackgroundDrawable(ColorDrawable(color))
        val customActionBar = layoutInflater.inflate(com.efihumboldt.appligas.R.layout.custom_action_bar_layout_detalle_equipo, null)
        customActionBar.setBackgroundColor(color)
        val customTitle : TextView = customActionBar.findViewById(com.efihumboldt.appligas.R.id.customActionBarTitle)
        customTitle.text = "Tabla de clasificación"


        supportActionBar?.apply {
            displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
            setCustomView(customActionBar)
            setBackgroundDrawable(ColorDrawable(color))
        }



        recyclerViewPosiciones = binding.recyclerViewTablaPosiciones
        recyclerViewPosiciones.layoutManager = LinearLayoutManager(this)

        recyclerViewDescripcionColores = binding.recyclerViewDescripcionColores
        recyclerViewDescripcionColores.layoutManager = LinearLayoutManager(this)

        swipeRefreshLayoutFragmentPosiciones =  binding.swipeRefreshLayoutFragmentPosiciones


        val torneoSeleccionado = SharedDataHolder.selectedTournament
        bd = SharedDataHolder.bd!!

        val apiService: ApiService = ApiServiceImpl(this, bd)
        val posicionDAO: PosicionDAO = PosicionDAOImpl(apiService.posicionApiService, bd, torneoSeleccionado!!.divisionID, this)
        val posicionService = PosicionService(posicionDAO)

        cargaDatosJob2 = lifecycleScope.launch {
            coroutineScope {
                val bannerDAO: BannerDAO = BannerDAOImpl(
                    apiService.bannerApiService,
                    bd,
                    torneoSeleccionado.divisionID,
                    this@ClasificationTableActivity
                )
                val bannerService = BannerService(bannerDAO)
                bannerList = bannerService.getBanners7ByDivisionID(torneoSeleccionado.divisionID)
                cargarDatos(posicionService, 0, torneoSeleccionado.divisionID)
            }
        }

        swipeRefreshLayoutFragmentPosiciones.setOnRefreshListener {
            cargaDatosJob?.cancel()
            cargaDatosJob2?.cancel()

            cargaDatosJob =  lifecycleScope.launch {
                try {
                    coroutineScope{
                        binding.progressBar.visibility = View.VISIBLE
                        val bannerDAO: BannerDAO = BannerDAOImpl(apiService.bannerApiService, bd, torneoSeleccionado.divisionID, this@ClasificationTableActivity)
                        val bannerService = BannerService(bannerDAO)
                        bannerList = bannerService.getBanners7ByDivisionID(torneoSeleccionado.divisionID)
                        cargarDatos(posicionService, 2000, torneoSeleccionado.divisionID)
                        binding.progressBar.visibility = View.GONE
                    }
                } catch (e : CancellationException) {

                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cargaDatosJob?.cancel()
        cargaDatosJob2?.cancel()
    }

    private suspend fun cargarDatos(posicionService: PosicionService, delay: Long, divisionID : Int) {
        try {
            val listaDatos = posicionService.getAllPosicionClasificacion()
            withContext(Dispatchers.Main) {
                val posicionesAdapter = PosicionesAdapter(
                    listaDatos.posicionesPorZona,
                    bd,
                    null,
                    this@ClasificationTableActivity,
                    recyclerViewPosiciones,
                    bannerList
                )
                recyclerViewPosiciones.adapter = posicionesAdapter

                val colorsTitle: TextView = binding.tvDescripcionColores
                if (listaDatos.coloresDescripcion.isEmpty()) {
                    colorsTitle.visibility = View.INVISIBLE
                }

                val descripcionColoresAdapter = DescripcionColoresAdapter(
                    listaDatos.coloresDescripcion,
                    bd,
                    this@ClasificationTableActivity
                )
                recyclerViewDescripcionColores.adapter = descripcionColoresAdapter

                swipeRefreshLayoutFragmentPosiciones.isRefreshing = false
                //SharedDataHolder. = listaDatos.posicionesPorZona
                binding.progressBar.visibility = View.GONE
            }

        } catch (e: HttpException) {
            e.printStackTrace()
            swipeRefreshLayoutFragmentPosiciones.isRefreshing = false
        } catch (e: IOException) {
            e.printStackTrace()
            swipeRefreshLayoutFragmentPosiciones.isRefreshing = false
        }
    }

}