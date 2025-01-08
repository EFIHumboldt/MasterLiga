package com.efihumboldt.appligas.ui.activities.DetalleTorneo

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.efihumboldt.appligas.entidades.Liga
import com.efihumboldt.appligas.entidades.Torneo
import com.efihumboldt.appligas.R
import com.efihumboldt.appligas.Varios.SharedDataHolder
import com.efihumboldt.appligas.Varios.SharedPreferencesManager
import com.efihumboldt.appligas.Varios.UpdateManager
import com.efihumboldt.appligas.databinding.ActivityDetalleTorneoBinding
import com.efihumboldt.appligas.databinding.CustomActionBarBinding
import com.efihumboldt.appligas.entidades.EquipoSimple
import com.efihumboldt.appligas.implementaciones.ApiServiceImpl
import com.efihumboldt.appligas.implementaciones.EquipoSimpleDAOImpl
import com.efihumboldt.appligas.interfaces.ApiService
import com.efihumboldt.appligas.interfaces.EquipoSimpleDAO
import com.efihumboldt.appligas.services.EquipoSimpleService
import com.efihumboldt.appligas.ui.activities.DetalleEquipo.DetalleEquipoActivity
import com.efihumboldt.appligas.ui.activities.Favoritos.FavoritosAdapter
import com.efihumboldt.appligas.ui.activities.Favoritos.FavoritosViewModel
import com.efihumboldt.appligas.ui.menu_inferior_torneo.config.AppOptionsAdapter
import com.efihumboldt.appligas.ui.menu_inferior_torneo.config.LeagueOptionsAdapter
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.random.Random

class DetalleTorneoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetalleTorneoBinding
    private lateinit var customHeader: CustomActionBarBinding
    private lateinit var customToolbar : Toolbar
    private lateinit var viewModel: DetalleTorneoViewModel
    private var isTransactionInProgress = false
    private var listaEquiposFavs: List<EquipoSimple> = emptyList()
    private lateinit var favoritosAdapter : FavoritosAdapter
    private lateinit var recyclerViewEquiposFavs : RecyclerView
    private lateinit var progressBarFavs : ProgressBar
    private lateinit var swipeRefreshLayoutFavs : SwipeRefreshLayout


    private val tutorialLayouts = listOf(
        R.layout.layout_tutorial_ii,
        R.layout.layout_tutorial_iii,
        R.layout.layout_tutorial_iv,
        R.layout.layout_tutorial_v,
        R.layout.layout_tutorial_vi
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val updateManager = UpdateManager()
        updateManager.checkAndForceUpdate(this)

        val sharedPreferences = getSharedPreferences("MiAppPrefs", Context.MODE_PRIVATE)
        val isFirstRun = sharedPreferences.getBoolean("firstTimeInTournament", true)

        if (isFirstRun) {
            Handler(Looper.getMainLooper()).postDelayed({ showTutorial(0) }, 2000)
            sharedPreferences.edit().putBoolean("firstTimeInTournament", false).apply()
        }


        viewModel = ViewModelProvider(this)[DetalleTorneoViewModel::class.java]

        //var torneoSeleccionado = intent.getSerializableExtra("torneo") as Torneo
        //var ligaSeleccionada = intent.getSerializableExtra("liga") as Liga

        //CAMBIAR --> LO HARDCODEO PARA ESTAR DIRECTAMENTE EN LA PANTALLA DE DETALLE TORNEO

        var torneoSeleccionado = Torneo(1, 1, 1, "Torneo Hardcodeado", "null", "#000000")
        var ligaSeleccionada = Liga(1, "Liga Hardcodeada","null", "null", "null", "null", "#0000")

        //PARA LOS FRAGMENTS
        torneoSeleccionado.let { viewModel.torneoSeleccionado = it }
        ligaSeleccionada.let { viewModel.bd = it.link }

        //PARA EL RESTO DE LOS ACTIVITIES
        ligaSeleccionada.let { viewModel.setBD(it.link) }
        ligaSeleccionada.let { viewModel.setSelectedLeague(it) }
        torneoSeleccionado.let { viewModel.setSelectedTournament(it) }

        binding = ActivityDetalleTorneoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        customHeader = binding.customActionBar

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)

        val customActionBar = layoutInflater.inflate(R.layout.custom_action_bar_layout, null)
        customToolbar = customActionBar.findViewById(R.id.custom_toolbar)
        val customIcon: ImageView = customActionBar.findViewById(R.id.customActionBarIcon)
        val customTitle: TextView = customActionBar.findViewById(R.id.customActionBarTitle)
        val optionsIcon : ImageView = customActionBar.findViewById(R.id.imgFinal)
        val favsIcon : ImageView = customActionBar.findViewById(R.id.favs)

        optionsIcon.setOnClickListener { showOptions(ligaSeleccionada, torneoSeleccionado) }
        favsIcon.setOnClickListener { showFavs() }


        Glide.with(customIcon.context).load("${ligaSeleccionada.link}/${ligaSeleccionada.logo}").into(customIcon)
        customTitle.text = torneoSeleccionado.nombreTorneoDivision


        supportActionBar?.apply {
            displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
            customView = customActionBar
        }


        val btCambiaColor: View = findViewById(R.id.btCambiaColor)

        btCambiaColor.setOnClickListener {
            val color: Int = generateAleatoryColor()
            changeColors(color)
        }


        try {
            val colorString: String = torneoSeleccionado.color
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
        customHeader.customToolbar.setBackgroundColor(statusColor)
        customHeader.barraInferiorToolbar.setBackgroundColor(statusColor)

        val colorStateList = ColorStateList(
            arrayOf(
                intArrayOf(android.R.attr.state_checked),
                intArrayOf(-android.R.attr.state_checked)
            ),
            intArrayOf(statusColor, ContextCompat.getColor(this, R.color.colorNoSeleccionado))
        )

        binding.navView.itemIconTintList = colorStateList

        binding.navView.itemTextColor = colorStateList

        customToolbar.setBackgroundColor(statusColor)
    }


    private fun generateAleatoryColor(): Int {

        val random = Random
        val red = random.nextInt(256)
        val green = random.nextInt(256)
        val blue = random.nextInt(256)

        return Color.rgb(red, green, blue)
    }

    private fun showTutorial(index: Int) {
        if (index < tutorialLayouts.size) {
            val tutorialView = LayoutInflater.from(this).inflate(tutorialLayouts[index], null)
            val popupWindow = PopupWindow(
                tutorialView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                true
            )

            popupWindow.showAtLocation(tutorialView, Gravity.NO_GRAVITY, 0, 0)

            tutorialView.setOnClickListener {
                popupWindow.dismiss()
                showTutorial(index + 1)
            }
        }
    }

    private fun showFavs()
    {
        val favsView = LayoutInflater.from(this).inflate(R.layout.activity_favoritos_updated, null)
        val popupWindow = PopupWindow(
            favsView,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT,
            true
        )
        val background : ConstraintLayout = favsView.findViewById(R.id.background_transparente)

        background.setOnClickListener { popupWindow.dismiss()}

        val layoutTransicion: ConstraintLayout = favsView.findViewById(R.id.constraint_favs)

        val scaleX = ObjectAnimator.ofFloat(layoutTransicion, View.SCALE_X, 0f, 1f)
        val scaleY = ObjectAnimator.ofFloat(layoutTransicion, View.SCALE_Y, 0f, 1f)

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(scaleX, scaleY)
        animatorSet.duration = 500
        animatorSet.interpolator = AccelerateDecelerateInterpolator()

        popupWindow.showAtLocation(favsView, Gravity.NO_GRAVITY, 0, 0)
        animatorSet.start()

        recyclerViewEquiposFavs = favsView.findViewById(R.id.recyclerViewEquipos)
        swipeRefreshLayoutFavs = favsView.findViewById(R.id.swipeRefreshLayoutFragmentEquipos)
        progressBarFavs = favsView.findViewById(R.id.progressBar)

        val layoutManager = GridLayoutManager(this, 2)
        recyclerViewEquiposFavs.layoutManager = layoutManager

        val screenWidth = resources.displayMetrics.widthPixels + resources.getDimensionPixelSize(R.dimen.dimen_minus_100dp)
        val margin = resources.getDimensionPixelSize(R.dimen.card_equipo_margin)
        val marginExterno = resources.getDimensionPixelSize(R.dimen.card_equipo_margin)
        val itemWidth = (screenWidth - (layoutManager.spanCount - 1) * margin - marginExterno * 4) / layoutManager.spanCount

        val viewModel = ViewModelProvider(this)[FavoritosViewModel::class.java]
        val bd = viewModel.bd

        cargarDatosFavs(itemWidth, bd!!)


        swipeRefreshLayoutFavs.setOnRefreshListener {

            progressBarFavs.visibility = View.VISIBLE
            cargarDatosFavs(itemWidth, bd)

        }
    }


    private fun showOptions(ligaSeleccionada : Liga, torneoSeleccionado : Torneo)
    {

        val optionsView = LayoutInflater.from(this).inflate(R.layout.fragment_config, null)
        val popupWindow = PopupWindow(
            optionsView,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT,
            true
        )

        val background : ConstraintLayout = optionsView.findViewById(R.id.background_transparente)

        background.setOnClickListener { popupWindow.dismiss()}


        val layoutTransicion: ConstraintLayout = optionsView.findViewById(R.id.layout_transicion)


        val scaleX = ObjectAnimator.ofFloat(layoutTransicion, View.SCALE_X, 0f, 1f)
        val scaleY = ObjectAnimator.ofFloat(layoutTransicion, View.SCALE_Y, 0f, 1f)

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(scaleX, scaleY)
        animatorSet.duration = 500
        animatorSet.interpolator = AccelerateDecelerateInterpolator()

        popupWindow.showAtLocation(optionsView, Gravity.NO_GRAVITY, 0, 0)
        animatorSet.start()

        val headerOptions : LinearLayout = optionsView.findViewById(R.id.optionsHeader)
        headerOptions.setBackgroundColor(Color.parseColor(ligaSeleccionada.color))

        val escudoLiga : ImageView = headerOptions.findViewById(R.id.customActionBarIcon)
        val nombreLiga : TextView = headerOptions.findViewById(R.id.customActionBarTitle)
        val divisionLiga : TextView = headerOptions.findViewById(R.id.customActionBarSubtitle)

        Glide.with(escudoLiga.context).load("${ligaSeleccionada.link}/${ligaSeleccionada.logo}").into(escudoLiga)
        nombreLiga.text = ligaSeleccionada.nombre

        divisionLiga.text =  torneoSeleccionado.nombreTorneoDivision

        val recyclerView = optionsView.findViewById<RecyclerView>(R.id.recyclerOptions)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = LeagueOptionsAdapter(getData(), this)

        val recyclerView2 = optionsView.findViewById<RecyclerView>(R.id.recyclerOptionsApp)
        recyclerView2.layoutManager = LinearLayoutManager(this)
        recyclerView2.adapter = AppOptionsAdapter(getData2(), this)
    }

    private fun getData(): List<String> {
        return listOf("Tabla de clasificación", "Reglamento", "Historial de equipos")
    }

    private fun getData2(): List<String> {
        return listOf("Contactos", "Puntúanos", "Reportar Problema")
    }

    private fun cargarDatosFavs(itemWidth : Int, bd : String) {

        val sharedPreferencesManager = SharedPreferencesManager(this)
        val listaEquiposFavoritosJson = sharedPreferencesManager.getFavorites()

        val apiEquipoService: ApiService = ApiServiceImpl(this, bd)
        val equipoDAO: EquipoSimpleDAO = EquipoSimpleDAOImpl(apiEquipoService.equipoSimpleApiService, bd, SharedDataHolder.selectedTournament!!.divisionID, this)
        val equipoSimpleService = EquipoSimpleService(equipoDAO)

        CoroutineScope(Dispatchers.Main).launch {
            val listaEquiposFavoritos = mutableListOf<EquipoSimple>()
            val gson = Gson()

            val listCompleta = equipoSimpleService.getAllEquipos()

            for (equipoJson in listaEquiposFavoritosJson) {
                try {
                    val equipo = gson.fromJson(equipoJson, EquipoSimple::class.java)
                    if (listCompleta.contains(equipo))
                    {
                        listaEquiposFavoritos.add(equipo)
                    }
                } catch (e: JsonSyntaxException) {
                    Log.e("Error", "Error al deserializar equipo JSON: $equipoJson")
                }
            }


            favoritosAdapter = FavoritosAdapter(listaEquiposFavoritos, bd, this@DetalleTorneoActivity)
            recyclerViewEquiposFavs.adapter = favoritosAdapter
            favoritosAdapter.setItemWidth(itemWidth)

            favoritosAdapter.setOnItemClickListener(object : FavoritosAdapter.OnItemClickListener {
                override fun onItemClick(equipo: EquipoSimple) {
                    abrirActividadFavsConItem(equipo)
                }
            })

            progressBarFavs.visibility = View.GONE
            swipeRefreshLayoutFavs.isRefreshing = false

        }
    }

    private fun abrirActividadFavsConItem(selectedTeam: EquipoSimple) {
        val intent = Intent(this, DetalleEquipoActivity::class.java)
        selectedTeam.let { viewModel.setSelectedTeam(it) }
        startActivity(intent)
    }
}

/*
  supportActionBar?.apply {

            val inflater = LayoutInflater.from(this@DetalleTorneoActivity)
            val customView = inflater.inflate(R.layout.custom_action_bar, null)
            setCustomView(customView)

            // Puedes acceder a las vistas en el diseño personalizado
            val customTitle = customView.findViewById<TextView>(R.id.customActionBarTitle)
            //customTitle.text = "Nuevo Título"

            val linearLayout = customView.findViewById<LinearLayout>(R.id.custom_action_bar_layout)
            customToolbar = linearLayout.findViewById(R.id.custom_toolbar)
            displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        }

 */