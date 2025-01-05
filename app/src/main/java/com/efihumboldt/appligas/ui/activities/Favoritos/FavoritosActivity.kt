package com.efihumboldt.appligas.ui.activities.Favoritos

import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.efihumboldt.appligas.entidades.EquipoSimple
import com.efihumboldt.appligas.implementaciones.ApiServiceImpl
import com.efihumboldt.appligas.implementaciones.EquipoSimpleDAOImpl
import com.efihumboldt.appligas.interfaces.ApiService
import com.efihumboldt.appligas.interfaces.EquipoSimpleDAO
import com.efihumboldt.appligas.R
import com.efihumboldt.appligas.services.EquipoSimpleService
import com.efihumboldt.appligas.ui.activities.DetalleEquipo.DetalleEquipoActivity
import com.efihumboldt.appligas.Varios.SharedDataHolder.selectedTournament
import com.efihumboldt.appligas.Varios.SharedPreferencesManager
import com.efihumboldt.appligas.Varios.UpdateManager
import com.efihumboldt.appligas.databinding.ActivityFavoritosBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class FavoritosActivity : AppCompatActivity() {

    private lateinit var recyclerViewEquipos: RecyclerView
    private lateinit var viewModel: FavoritosViewModel
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private var listaEquipos: List<EquipoSimple> = emptyList()
    private lateinit var favoritosAdapter : FavoritosAdapter
    private lateinit var editTextSearch: EditText
    private var cargaDatosJob: Job? = null
    private lateinit var binding: ActivityFavoritosBinding
    private lateinit var customToolbar : Toolbar



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val updateManager = UpdateManager()
        updateManager.checkAndForceUpdate(this)

        binding = ActivityFavoritosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        editTextSearch = findViewById(R.id.editTextSearch)
        editTextSearch.isEnabled = false

        recyclerViewEquipos = findViewById(R.id.recyclerViewEquipos)
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayoutFragmentEquipos)

        val layoutManager = GridLayoutManager(this, 3)
        recyclerViewEquipos.layoutManager = layoutManager

        viewModel = ViewModelProvider(this)[FavoritosViewModel::class.java]
        val selectedTournament = viewModel.selectedTournament
        val bd = viewModel.bd
        val selectedLeague = viewModel.selectedLeague

        val screenWidth = resources.displayMetrics.widthPixels
        val margin = resources.getDimensionPixelSize(R.dimen.card_equipo_margin)
        val marginExterno = resources.getDimensionPixelSize(R.dimen.recyler_margin)
        val itemWidth = (screenWidth - (layoutManager.spanCount - 1) * margin - marginExterno * 4) / layoutManager.spanCount

        cargarDatos(itemWidth, bd!!)

        editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                val filteredLigas = listaEquipos.filter { equipo ->
                    equipo.nombrecompleto.contains(s.toString(), ignoreCase = true)
                }

                favoritosAdapter.submitList(filteredLigas)
            }
        })

        swipeRefreshLayout.setOnRefreshListener {

            binding.progressBar.visibility = View.VISIBLE
            cargarDatos(itemWidth, bd!!)

        }

        val searchIconDrawable = editTextSearch.compoundDrawablesRelative[2] // El índice 2 representa el drawableEnd
        searchIconDrawable.setColorFilter(ContextCompat.getColor(this, R.color.lupa), PorterDuff.Mode.SRC_IN)
        editTextSearch.background.setColorFilter(Color.parseColor(selectedTournament?.color), PorterDuff.Mode.SRC_IN)

        try {
            val colorString: String = selectedTournament!!.color
            val colorInt = Color.parseColor(colorString)

            val window: Window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = colorInt
            supportActionBar?.setBackgroundDrawable(ColorDrawable(colorInt))

            val customActionBar = layoutInflater.inflate(R.layout.custom_action_bar_layout, null)
            customToolbar = customActionBar.findViewById(R.id.custom_toolbar)
            val customIcon: ImageView = customActionBar.findViewById(R.id.customActionBarIcon)
            val customTitle: TextView = customActionBar.findViewById(R.id.customActionBarTitle)
            val starFav : ImageView = customActionBar.findViewById(R.id.imgFinal)
            starFav.visibility = View.INVISIBLE
            customToolbar.setBackgroundDrawable(ColorDrawable(colorInt))

            Glide.with(customIcon.context).load("${selectedLeague!!.link}/${selectedLeague.logo}")
                .into(customIcon)
            customTitle.text = selectedTournament.nombreTorneoDivision


            supportActionBar?.apply {
                displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
                customView = customActionBar
                setCustomView(customActionBar)
                val customTitle = customActionBar.findViewById<TextView>(R.id.customActionBarTitle)
            }

        } catch (e: IllegalArgumentException) {

        }

    }

    private fun abrirActividadConItem(selectedTeam: EquipoSimple) {
        val intent = Intent(this, DetalleEquipoActivity::class.java)
        selectedTeam.let { viewModel.setSelectedTeam(it) }
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        cargaDatosJob?.cancel()
    }
    private fun cargarDatos(itemWidth : Int, bd : String) {

        val sharedPreferencesManager = SharedPreferencesManager(this)
        val listaIdsEquiposFavoritos = sharedPreferencesManager.getFavorites()

        val apiEquipoService: ApiService = ApiServiceImpl(this, bd)
        val equipoDAO: EquipoSimpleDAO = EquipoSimpleDAOImpl(apiEquipoService.equipoSimpleApiService, bd, selectedTournament!!.divisionID, this)
        val equipoSimpleService = EquipoSimpleService(equipoDAO)

        CoroutineScope(Dispatchers.Main).launch {
            val listaEquiposFavoritos = mutableListOf<EquipoSimple>()
            for (equipoId in listaIdsEquiposFavoritos) {
                val equipo = equipoSimpleService.getTeamByID(equipoId.toInt())
                if (equipo.isNotEmpty()) {
                    equipo.let { listaEquiposFavoritos.add(it[0]) }

                }
            }

            favoritosAdapter = FavoritosAdapter(listaEquiposFavoritos, bd, this@FavoritosActivity)
            recyclerViewEquipos.adapter = favoritosAdapter

            favoritosAdapter.setOnItemClickListener(object : FavoritosAdapter.OnItemClickListener {
                override fun onItemClick(equipo: EquipoSimple) {
                    abrirActividadConItem(equipo)
                }
            })

            //favoritosAdapter.setItemWidth(itemWidth)
            binding.progressBar.visibility = View.GONE
            editTextSearch.isEnabled = true
            swipeRefreshLayout.isRefreshing = false

        }
    }
}
