package com.efihumboldt.appligas.ui.activities.DetallePartido

import android.content.Intent
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.efihumboldt.appligas.entidades.Banner
import com.efihumboldt.appligas.entidades.Liga
import com.efihumboldt.appligas.entidades.Partido
import com.efihumboldt.appligas.entidades.Torneo
import com.efihumboldt.appligas.implementaciones.ApiServiceImpl
import com.efihumboldt.appligas.implementaciones.BannerDAOImpl
import com.efihumboldt.appligas.implementaciones.EquipoSimpleDAOImpl
import com.efihumboldt.appligas.implementaciones.PartidoDAOImpl
import com.efihumboldt.appligas.interfaces.ApiService
import com.efihumboldt.appligas.interfaces.BannerDAO
import com.efihumboldt.appligas.interfaces.EquipoSimpleDAO
import com.efihumboldt.appligas.interfaces.PartidoDAO
import com.efihumboldt.appligas.R
import com.efihumboldt.appligas.services.BannerService
import com.efihumboldt.appligas.services.EquipoSimpleService
import com.efihumboldt.appligas.services.PartidoService
import com.efihumboldt.appligas.ui.activities.DetalleEquipo.DetalleEquipoActivity
import com.efihumboldt.appligas.Varios.SharedDataHolder
import com.efihumboldt.appligas.Varios.UpdateManager
import com.efihumboldt.appligas.databinding.ActivityDetallePartidoBinding
import com.efihumboldt.appligas.databinding.CustomHeaderMatchBinding
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class DetallePartidoActivity : AppCompatActivity() {

    private lateinit var viewModel: DetallePartidoViewModel
    private lateinit var customToolbar: Toolbar
    private lateinit var customHeader: CustomHeaderMatchBinding
    private lateinit var binding: ActivityDetallePartidoBinding
    private lateinit var selectedMatch: Partido
    private lateinit var recyclerViewPrimerTiempo: RecyclerView
    private lateinit var recyclerViewSegundoTiempo: RecyclerView
    private var latitud: Double = 0.0
    private var longitud: Double = 0.0
    private lateinit var swipeRefreshLayoutActivityPartido: SwipeRefreshLayout
    private var cargaDatosJob: Job? = null
    private lateinit var bannerList : List<Banner>

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_partido)

        val updateManager = UpdateManager()
        updateManager.checkAndForceUpdate(this)


        viewModel = ViewModelProvider(this)[DetallePartidoViewModel::class.java]

        selectedMatch = viewModel.selectedMatch!!
        val bd = viewModel.bd
        val selectedTounament = viewModel.selectedTournament!!
        val selectedLeague = viewModel.selectedLeague!!




        CoroutineScope(Dispatchers.Main).launch {
            cargarBanner(bd, selectedMatch.idLocal.toInt(), selectedMatch.idVisita.toInt())
            cargaInterfazGrafica(selectedMatch, selectedTounament, selectedLeague, bd!!, 0)
        }

        binding = ActivityDetallePartidoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val customActionBar = layoutInflater.inflate(R.layout.custom_action_bar_layout, null)
        customToolbar = customActionBar.findViewById(R.id.custom_toolbar)
        val customIcon: ImageView = customActionBar.findViewById(R.id.customActionBarIcon)
        val customTitle: TextView = customActionBar.findViewById(R.id.customActionBarTitle)
        customHeader = binding.customHeaderMatch

        swipeRefreshLayoutActivityPartido = binding.swipeRefreshLayoutActivityPartido


        swipeRefreshLayoutActivityPartido.setOnRefreshListener {

            cargaDatosJob?.cancel()

            val apiService: ApiService = ApiServiceImpl(this, bd)
            val partidoDAO: PartidoDAO = PartidoDAOImpl(apiService.partidoApiService, bd, this)
            val partidoService = PartidoService(partidoDAO)

            cargaDatosJob = CoroutineScope(Dispatchers.Main).launch {
                try {

                    if (!isFinishing && !isDestroyed) {
                        binding.progressBar.visibility = VISIBLE
                        cargarBanner(bd, selectedMatch.idLocal.toInt(), selectedMatch.idVisita.toInt())
                        selectedMatch = partidoService.getMatchByMatchID(selectedMatch.idPartido.toInt())
                        cargaInterfazGrafica(selectedMatch, selectedTounament, selectedLeague, bd!!, 2000)
                    }
                } catch (e: CancellationException) {
                    // Maneja la cancelación de la tarea
                    // Puedes ignorar esta excepción ya que la tarea fue cancelada
                }
            }
        }

        latitud = selectedMatch.latitudEstadio!!.toDouble()
        longitud = selectedMatch.longitudEstadio!!.toDouble()
        val imgMaps = binding.mapIcon
        val link = binding.tvLinkMap

        if (selectedMatch.estadioPartido == "A definir")
        {
            imgMaps.visibility = INVISIBLE
            link.visibility = INVISIBLE
        }
        else
        {
            imgMaps.visibility = VISIBLE
            link.visibility = VISIBLE
        }

        imgMaps.setOnClickListener {
            abrirGoogleMaps()
        }

        link.setOnClickListener {
            abrirGoogleMaps()
        }
    }


    private fun changeColors(statusColor: Int) {
        val window: Window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = statusColor


        val colorLocal = Color.parseColor(selectedMatch.colorLocal)
        val colorVisita = Color.parseColor(selectedMatch.colorVisita)

        // Obtener las dimensiones de la pantalla
        val displayMetrics = resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels.toFloat()

        // Calcular las posiciones de los colores para lograr el efecto deseado
        val colorPositions = floatArrayOf(0.25f, 0.5f, 0.75f)
        val gradientColors = intArrayOf(colorLocal, colorLocal, colorVisita)

        // Crear un LinearGradient
        val linearGradient = LinearGradient(
            0.05f * screenWidth, 0f,
            0.95f * screenWidth, 0f,
            gradientColors,
            colorPositions,
            Shader.TileMode.CLAMP
        )

        // Envolver el LinearGradient en un ShapeDrawable (SUBCLASE DE DRAWABLE)
        val shapeDrawable = ShapeDrawable(RectShape())
        shapeDrawable.paint.shader = linearGradient



        supportActionBar?.setBackgroundDrawable(ColorDrawable(statusColor))
        customHeader.customHeaderMatch.background = shapeDrawable
        customToolbar.setBackgroundColor(statusColor)

    }

    private fun abrirGoogleMaps(){

        val latitudDestino = selectedMatch.latitudEstadio?.toFloat()
        val longitudDestino = selectedMatch.longitudEstadio?.toFloat()

        val uri = "geo:0,0?q=$latitudDestino,$longitudDestino"
        println("URI generada: $uri")

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity")

        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            Toast.makeText(this, "Google Maps no está instalado", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        cargaDatosJob?.cancel()
    }

    private suspend fun abrirActividadEquipoConItem(teamID: Int) {

        val apiService: ApiService = ApiServiceImpl(this, SharedDataHolder.bd)
        val equipoSimpleDAO: EquipoSimpleDAO = EquipoSimpleDAOImpl(
            apiService.equipoSimpleApiService,
            SharedDataHolder.bd,
            SharedDataHolder.selectedTournament!!.divisionID,
            this
        )
        val equipoSimpleService = EquipoSimpleService(equipoSimpleDAO)


        try {
            val team =
                equipoSimpleService.getTeamByID(teamID)

            team[0].let { viewModel.setSelectedTeam(it) }

            val intent = Intent(this, DetalleEquipoActivity::class.java)
            startActivity(intent)

        } catch (e: HttpException) {
            //e.printStackTrace()
            e.response()?.code()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    /*
    private fun abrirGoogleMaps() {
        val gmmIntentUri: Uri = Uri.parse("geo:$latitud,$longitud?q=$latitud,$longitud")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)

        // Verifica si hay una aplicación de mapas disponible para manejar el intent
        if (mapIntent.resolveActivity(packageManager) != null) {
            // Si hay una aplicación de mapas instalada, la abre
            Log.e("AbrirGoogleMaps", "Abriendo Google Maps directamente")
            startActivity(mapIntent)
        } else {
            // Si no hay una aplicación de mapas instalada, abre el navegador con Google Maps
            val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/place/$latitud,$longitud"))
            if (webIntent.resolveActivity(packageManager) != null) {
                Log.e("AbrirGoogleMaps", "Abriendo Google Maps a través del navegador")
                startActivity(webIntent)
            } else {
                // Manejar el caso en el que no se puede abrir Google Maps
                Log.e("AbrirGoogleMaps", "No se pudo abrir Google Maps")
                // Puedes mostrar un mensaje al usuario o tomar otras acciones según tus necesidades.
            }
        }
    }

     */

    private suspend fun cargaInterfazGrafica(
        selectedMatch: Partido,
        selectedTounament: Torneo,
        selectedLeague: Liga,
        bd: String,
        delay: Long
    ) {
        coroutineScope {
            delay(delay)

            val customActionBar = layoutInflater.inflate(R.layout.custom_action_bar_layout, null)
            customToolbar = customActionBar.findViewById(R.id.custom_toolbar)
            val customIcon: ImageView = customActionBar.findViewById(R.id.customActionBarIcon)
            val customTitle: TextView = customActionBar.findViewById(R.id.customActionBarTitle)
            val fav : ImageView = customActionBar.findViewById(R.id.imgFinal)
            customHeader = binding.customHeaderMatch
            fav.visibility = INVISIBLE

            val fechaCalendario: TextView = binding.tvCalendario
            val hora: TextView = binding.tvHora
            val ubicacion: TextView = binding.tvUbicacion
            val inicioPrimerTiempo: TextView = binding.inicioPrimerTiempoTitle
            val finPrimerTiempo: TextView = binding.finPrimerTiempoTitle
            val inicioSegundoTiempo: TextView = binding.inicioSegundoTiempoTitle
            val finPartido: TextView = binding.finPartidoTitle
            val primerBarra: View = binding.verticalBarFirst
            val segundaBarra: View = binding.verticalBarSecond
            val nada: TextView = binding.tvNada

            if (selectedMatch.inicioPrimerTiempo != null) inicioPrimerTiempo.visibility =
                View.VISIBLE
            else {
                inicioPrimerTiempo.visibility = View.INVISIBLE
                primerBarra.visibility = View.INVISIBLE
                nada.visibility = View.VISIBLE
            }

            if (selectedMatch.finPrimerTiempo != null) finPrimerTiempo.visibility = View.VISIBLE
            else finPrimerTiempo.visibility = View.INVISIBLE

            if (selectedMatch.inicioSegundoTiempo != null) inicioSegundoTiempo.visibility =
                View.VISIBLE
            else {
                inicioSegundoTiempo.visibility = View.INVISIBLE
                segundaBarra.visibility = View.INVISIBLE
            }

            if (selectedMatch.finSegundoTiempo != null) finPartido.visibility = View.VISIBLE
            else finPartido.visibility = View.INVISIBLE

            fechaCalendario.text = selectedMatch.fecha
            hora.text = selectedMatch.hora
            ubicacion.text = selectedMatch.estadioPartido

            Glide.with(customIcon.context).load("${selectedLeague.link}/${selectedLeague.logo}")
                .into(customIcon)
            customTitle.text = selectedLeague.nombre

            supportActionBar?.apply {
                displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
                customView = customActionBar
            }

            try {
                val colorString: String = selectedTounament.color
                val colorInt = Color.parseColor(colorString)
                changeColors(colorInt)
            } catch (e: IllegalArgumentException) {
                changeColors(Color.parseColor("#FFFFFF"))
            }

            Glide.with(this@DetallePartidoActivity).load("${bd}/${selectedMatch.imgLocal}")
                .into(binding.customHeaderMatch.imagenEquipoLocal)
            Glide.with(this@DetallePartidoActivity).load("${bd}/${selectedMatch.imgVisita}")
                .into(binding.customHeaderMatch.imagenEquipoVisita)
            binding.customHeaderMatch.nombreEquipoLocal.text = selectedMatch.nombreLocal
            binding.customHeaderMatch.nombreEquipoVisita.text = selectedMatch.nombreVisita
            binding.customHeaderMatch.resultadoLocal.text = selectedMatch.resultadoLocal
            binding.customHeaderMatch.resultadoVisita.text = selectedMatch.resultadoVisita


            customHeader.imagenEquipoLocal.setOnClickListener {
                CoroutineScope(Dispatchers.Main).launch {
                    abrirActividadEquipoConItem(selectedMatch.idLocal.toInt())
                }
            }
            customHeader.imagenEquipoVisita.setOnClickListener {
                CoroutineScope(Dispatchers.Main).launch {
                    abrirActividadEquipoConItem(selectedMatch.idVisita.toInt())
                }
            }

            //para los goles del partido
            val apiService: ApiService = ApiServiceImpl(this@DetallePartidoActivity, bd)
            val partidoDAO: PartidoDAO = PartidoDAOImpl(apiService.partidoApiService, bd, this@DetallePartidoActivity)
            val partidoService = PartidoService(partidoDAO)

            recyclerViewPrimerTiempo = binding.recyclerViewGolesPrimerTiempo
            recyclerViewSegundoTiempo = binding.recyclerViewGolesSegundoTiempo

            try {
                val listaGoles = partidoService.getGoalsByMatchID(selectedMatch.idPartido.toInt())
                val listaPrimerTiempo = listaGoles.filter { it.tiempo == 1 }
                val listaSegundoTiempo = listaGoles.filter { it.tiempo == 2 }

                val golesPrimerTiempoAdapter = GolesAdapter(listaPrimerTiempo, bd)
                recyclerViewPrimerTiempo.layoutManager =
                    LinearLayoutManager(this@DetallePartidoActivity)
                recyclerViewPrimerTiempo.adapter = golesPrimerTiempoAdapter

                val golesSegundoTiempoAdapter = GolesAdapter(listaSegundoTiempo, bd)
                recyclerViewSegundoTiempo.layoutManager =
                    LinearLayoutManager(this@DetallePartidoActivity)
                recyclerViewSegundoTiempo.adapter = golesSegundoTiempoAdapter

            } catch (e: HttpException) {
                e.response()?.code()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            if (selectedMatch.finSegundoTiempo == null && selectedMatch.inicioPrimerTiempo != null)
            {
                binding.customHeaderMatch.estadoPartido.text = selectedMatch.infoPartido.replace("\n", " - ")

                binding.customHeaderMatch.estadoPartido.apply {
                    typeface = ResourcesCompat.getFont(context, R.font.montserrat_bold)
                    setTextColor(ContextCompat.getColor(context, R.color.verdeMatch))
                }
            }
            else
            {
                binding.customHeaderMatch.estadoPartido.text = selectedMatch.infoPartido.replace("\n", " ")

            }

            binding.progressBar.visibility = View.GONE
            swipeRefreshLayoutActivityPartido.isRefreshing = false
        }
    }

    private suspend fun cargarBanner(bd : String?, localID : Int, visitID: Int){

        val apiService: ApiService = ApiServiceImpl(this, bd)
        val bannerDAO: BannerDAO = BannerDAOImpl(apiService.bannerApiService, bd, SharedDataHolder.selectedTournament!!.divisionID, this)
        val bannerService = BannerService(bannerDAO)
        bannerList = bannerService.getBanners6ByDivisionID(SharedDataHolder.selectedTournament!!.divisionID, localID, visitID)

        val banner : ImageView = binding.banner6
        if (bannerList.isNotEmpty())
        {
            Glide.with(banner.context).load("${bd}/${bannerList[0].link}").into(banner)
        }
    }

}


